package mysd;

import mysd.writer.*;
import mysd.builder.SpinBuilder;
import java.io.PrintStream;
import java.io.IOException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.List;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConcurrentSimulationManager{

    private SpinSystem<?> system;
    private List<SpinSystem<?>> subSystems;
    private List<Thread> threads;
    private Writer writer;
    private RunParameter param;
    private final int nthreads;
    private final CyclicBarrier barrier;

    public ConcurrentSimulationManager(int nthreads){
        this.subSystems = new ArrayList<SpinSystem<?>>();
        this.nthreads = nthreads;
        this.barrier = new CyclicBarrier(nthreads+1);
        this.threads = new ArrayList<Thread>(nthreads);
    }

    public void setThreads(){
        int subsize = this.system.size()/this.nthreads;
        int cnt = 0;
        for ( int n = 0; n < this.nthreads; n++){
            if ( n == this.nthreads - 1 ){
                SpinSystem<?> subSystem = this.system.makePartialSystem
                                          (this.barrier, cnt, this.system.size());
                Thread t = new Thread(subSystem);
                subSystems.add(subSystem);
                threads.add(t);
            }
            else{
                SpinSystem<?> subSystem = this.system.makePartialSystem
                                          (this.barrier, cnt, cnt + subsize);
                Thread t = new Thread(subSystem);
                cnt += subsize;
                subSystems.add(subSystem);
                threads.add(t);
            }
        }
        // add controller thread
        Thread controllerThread = new Thread( 
            new Runnable(){
                @Override
                public void run() {
                    runControl();
                }
            }
        );
        threads.add(controllerThread); 
    }

    public void addSystem(SpinSystem<?> system){
        this.system = system;
    }
    public void addWriter(Writer writer){
        this.writer = writer;
    }
    public void addParam(RunParameter param){
        this.param = param;
    }

    public void perturbSite(){
        if ( this.param.perturb_site ){
            int index = this.param.perturbing_site_index;
            double amount = this.param.perturbation_size;
            this.system.perturbSite( index, amount );
        }
    }


    public void writeSystemInfo(String outFileName){

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String header = "# mysd simulator system information file\n" +
                        "# created at : " + dateFormat.format(date) + "\n" + 
                        "# check out https://github.com/Eunsong/SpinDynamicsSimulator" + 
                        " for most recent updates\n\n\n";

        String runparams = "[ simulation parameters ]\n" +
                           "system_name       = " + param.title + "\n" +
                           "ntstep            = " + param.ntstep + "\n" +
                           "dt                = " + param.dt + "\n" +
                           "alpha             = " + param.alpha + "\n" +
                           "nstout            = " + param.nstout + "\n" +
                           "perturbation_size = " + param.perturbation_size + "\n\n\n";
        
        String sites =  "[ lattice sites ]\n";
        for ( Site<?> site : system ){
            int index = site.getIndex();
            int baseType = site.getBaseType();
            double x = site.getLocation().getX();
            double y = site.getLocation().getY();
            double z = site.getLocation().getZ();
            String siteRep = String.format("%8d %5d    %10.5f %10.5f %10.5f\n", 
                                            index, baseType, x, y, z);
            sites += siteRep;
        }
        
        PrintStream ps = null;
        try {
            ps = new PrintStream(outFileName);
            ps.print(header);
            ps.print(runparams);
            ps.print(sites);
        }
        catch(IOException ex){
            System.out.println("Cannot create output file " + outFileName +"!");
            System.exit(99);
        }
        finally{
            ps.close();
        }
    }

    public void writeEnergyToScreen(){
        if ( param.runtype == RunParameter.SimulationType.NONLINEAR ){
            writer.writeMessageToScreen(String.valueOf(system.getEnergy()));
        }
    }

    public void writeToFile(){
        this.writer.writeToFile();
    }
    public void writeMessageToFile(String msg){
        this.writer.writeMessageToFile(msg);
    }

    public void reportProgress(){
        int ntstep = this.param.ntstep;
        int currentStep = this.system.getCurrentTimeStep();
        double progress = (100.0*currentStep)/ntstep;
        String msg = String.format("\r%6.2f%% completed...", progress);
        this.writer.writeMessageToScreen(msg);
    }

    public void run(){
        for ( Thread t : this.threads ){
            t.start();
        }
    }


    private void runControl(){
        try{
            for ( int t = 0; t < param.ntstep; t++){
                if ( t%10 == 0) reportProgress();
                if ( param.nstout != 0 && t%param.nstout == 0 ) writeToFile();
                barrier.await();
                this.system.pushTimeStep();
                if ( t == param.ntstep - 1 ){
                    for ( SpinSystem<?> subSystem : subSystems){
                        subSystem.setStop();
                    }
                }
                barrier.await();
            }
            if ( param.nstout == 0 ) writeToFile();
            close();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        catch(BrokenBarrierException e){
            e.printStackTrace();
        }
    }

    public void close(){
        this.writer.close();
    }


}
