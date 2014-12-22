import java.util.*;
import java.io.*;

import mysd.*;
import mysd.lattice.*;
import mysd.vector.*;
import mysd.integrator.*;
import mysd.writer.*;
import mysd.builder.Builder;
import mysd.builder.SpinBuilder;

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
        if (cnfFile != null){
            cnf = new File(cnfFile);
        }
        List<FullSpinSite> sites = Builder.buildSites(top);        
        RunParameter param = Builder.importRunParam(sdp);

        NonlinearIntegrator integrator = new NonlinearIntegrator(param.dt);
        FullSpinSystem system = new FullSpinSystem.Builder().sites(sites).
                                integrator(integrator).alpha(param.alpha).build();
        if ( cnf != null ){
            SpinBuilder.overloadSpins(system, cnf, false);
        }
        mysd.writer.Writer writer = new BasicWriter<FullSpinSite>(system, outTraj);
        for ( int t = 0; t < param.ntstep; t++){
            if ( param.nstout != 0 && t%param.nstout == 0 ){
                writer.writeToFile();
            }
            system.updateForce();
            system.forward();
        }
        writer.writeToFile();
        writer.close();
    }
}
