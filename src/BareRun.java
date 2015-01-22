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

public class BareRun{
    public static void main(String[] args){

        HashMap<String, String> messages = ArgumentParser.parse(args);
        String topFile = messages.get("t");
        String sdpFile = messages.get("s");
        String outFile = messages.get("o");
        String outTraj = outFile + ".trj";
        String outInfo = outFile + ".info";
        String outConf = outFile + ".cnf";
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

        manager.writeSystemInfo(outInfo);
        manager.perturbSite();
        manager.run();

    }
}
