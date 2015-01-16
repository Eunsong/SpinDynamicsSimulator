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


        HashMap<String, String> messages = ArgumentParser.parse(args);
        String topFile = messages.get("t");
        String sdpFile = messages.get("s");
        String outTraj = messages.get("o");
        String cnfFile = messages.get("c");

        File top = new File(topFile);
        File sdp = new File(sdpFile);
        File cnf = null;
        List<FullSpinSite> sites = Builder.buildSites(top);        
        if (cnfFile != null){
            cnf = new File(cnfFile);
            SpinBuilder.overloadSpins(sites, cnf, false);
        }
        RunParameter param = Builder.importRunParam(sdp);
        SimulationManager manager = new SimulationManager();
        manager.addParam(param);

        switch ( param.runtype ){
    
            case NONLINEAR:{
                Integrator<FullSpinSite> integrator = 
                    new NonlinearIntegrator(param.dt);
                FullSpinSystem system = 
                    new FullSpinSystem.Builder().sites(sites).
                        integrator(integrator).alpha(param.alpha).build();

                mysd.writer.Writer writer = 
                    new BasicWriter<FullSpinSite>(system, outTraj);
                manager.addSystem(system);
                manager.addWriter(writer);
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

        manager.perturbSite();
        for ( int t = 0; t < param.ntstep; t++){
            manager.reportProgress();
//            manager.writeEnergyToScreen();
            if ( param.nstout != 0 && t%param.nstout == 0 ){
                manager.writeToFile();
            }
            manager.updateForce();
            manager.forward();
        }
        if ( param.nstout == 0 ) manager.writeToFile();
        manager.close();
    }
}
