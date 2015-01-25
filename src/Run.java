import java.util.*;
import java.io.*;

import mysd.*;
import mysd.lattice.*;
import mysd.vector.*;
import mysd.integrator.*;
import mysd.writer.*;
import mysd.builder.Builder;
import mysd.builder.SpinBuilder;
import mysd.RunParameter.SimulationType;

public class Run{
    public static void main(String[] args){

        String help =
            "#####################################################################\n"+
            "#                                                                   #\n"+
            "#  Run.java is an executable back-bone code of mysd package         #\n"+
            "# simulating classical spin dynamics on a periodic latice system.   #\n"+
            "# checkout the following github repository for most recent updates: #\n"+
            "# https://github.com/Eunsong/SpinDynamicsSimulator.git              #\n"+
            "#                                                                   #\n"+
            "# The code requires following inputs to run :                       #\n"+
            "#          1. -t   .top file defining system topology               #\n"+
            "#          2. -s   .sdp file listing simulation specific parameters #\n"+
            "#          3. -c   (optional) .cnf file for overloading initial     #\n"+
            "#                  spin configurations.                             #\n"+
            "#          4. -o   desired common file name for outputs             #\n"+
            "#                  (e.g. -o out will create out.info, out.trj,      #\n"+
            "#                   ,out.cnf, and out.eng)                          #\n"+ 
            "#          5. -nt  (optional) number of threads to be used for      #\n"+
            "#                  the simulation. If not specified, number of      #\n"+
            "#                  currently available processors will be used.     #\n"+
            "#                                                                   #\n"+
            "# Usage example : java Run -t topology.top -s sdrun.sdp -c conf.cnf #\n"+
            "#                -nt 8 -o out                                       #\n"+
            "#                                                                   #\n"+
            "#####################################################################\n";
        for ( String arg : args ){
            if ( arg.equals("-h") || arg.equals("--help") ){
                System.out.println(help);
                System.exit(0);
            }
        }

        int nthreads = Runtime.getRuntime().availableProcessors();

        HashMap<String, String> messages = ArgumentParser.parse(args);
        String topFile = messages.get("t");
        String sdpFile = messages.get("s");
        String cnfFile = messages.get("c");
        String outFile = messages.get("o");
        String outTraj = outFile + ".trj";
        String outInfo = outFile + ".info";
        String outConf = outFile + ".cnf";
        String outEner = outFile + ".eng";
        if ( messages.get("nt") != null){
            nthreads = Integer.parseInt(messages.get("nt"));
        }

        File top = new File(topFile);
        File sdp = new File(sdpFile);
        File cnf = null;
        List<FullSpinSite> sites = Builder.buildSites(top);
        if (cnfFile != null){
            cnf = new File(cnfFile);
            SpinBuilder.overloadSpins(sites, cnf, false);
        }
        RunParameter param = Builder.importRunParam(sdp);
        ConcurrentSimulationManager manager = 
            new ConcurrentSimulationManager(nthreads);
        manager.addParam(param);
        manager.setConfFileName(outConf);
        manager.setInfoFileName(outInfo);

        switch ( param.runtype ){

            case NONLINEAR:{
                Integrator<FullSpinSite> integrator = 
                    new NonlinearIntegrator(param.dt);
                FullSpinSystem system = 
                    new FullSpinSystem.Builder().sites(sites).
                        integrator(integrator).alpha(param.alpha).build();

                mysd.writer.Writer writer = 
                    new BasicWriter<FullSpinSite>(system, outTraj);
                mysd.writer.Writer energyWriter =
                    new BasicWriter<FullSpinSite>(system, outEner);
                manager.addSystem(system);
                manager.addWriter(writer);
                manager.addEnergyWriter(energyWriter);
            }
                break;

            case LINEAR:{
                Integrator<SigmaSpinSite> integrator = 
                    new RungeKuttaIntegrator(param.dt);
                SigmaSpinSystem system = 
                    new SigmaSpinSystem.Builder().sites(sites).
                        integrator(integrator).alpha(param.alpha).build();
                mysd.writer.Writer writer = 
                    new BasicWriter<SigmaSpinSite>(system, outTraj);
                manager.addSystem(system);
                manager.addWriter(writer);
            }
                break;
        }
        manager.writeSystemInfo(outInfo);
        manager.setThreads();
        manager.perturbSite();
        manager.run();
    }
}
