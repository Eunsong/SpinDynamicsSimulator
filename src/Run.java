import java.util.*;
import java.io.*;

import mysd.*;
import mysd.lattice.*;
import mysd.vector.*;
import mysd.integrator.*;
import mysd.writer.*;
import mysd.builder.Builder;

public class Run{
    public static void main(String[] args){

        String topFile = "topol.top";
        String sdpFile = "sdrun.sdp";
        String outTraj = "traj.dat";
        File top = new File(topFile);
        File sdp = new File(sdpFile);
        List<FullSpinSite> sites = Builder.buildSites(top);        
        RunParameter param = Builder.importRunParam(sdp);

        NonlinearIntegrator integrator = new NonlinearIntegrator(param.dt);
        SpinSystem system = new SpinSystem.Builder().sites(sites).
                                integrator(integrator).alpha(param.alpha).build();
        mysd.writer.Writer writer = new BasicWriter(system, outTraj);
        for ( int t = 0; t < param.ntstep; t++){
            if ( t%param.nstout == 0 ){
                writer.writeToFile();
            }
            system.updateForce();
            system.forward();
        }
        

    }
}
