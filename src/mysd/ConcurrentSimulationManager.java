package mysd;

import mysd.writer.*;
import mysd.builder.SpinBuilder;
import java.io.File;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.List;
import java.util.ArrayList;

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
