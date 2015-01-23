package mysd;

import mysd.writer.*;
import mysd.builder.SpinBuilder;
import java.io.PrintStream;
import java.io.IOException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.List;
import java.util.ArrayList;

public class ConcurrentSimulationManager extends SimulationManager{

    private List<SpinSystem<?>> subSystems;
    private List<Thread> threads;
    private final int nthreads;
    private final CyclicBarrier barrier;

    public ConcurrentSimulationManager(int nthreads){
        this.subSystems = new ArrayList<SpinSystem<?>>();
        this.nthreads = nthreads;
        this.barrier = new CyclicBarrier(nthreads+1);
        this.threads = new ArrayList<Thread>(nthreads);
    }

    public void setThreads(){
        int subsize = super.system.size()/this.nthreads;
        int cnt = 0;
        for ( int n = 0; n < this.nthreads; n++){
            if ( n == this.nthreads - 1 ){
                SpinSystem<?> subSystem = super.system.makePartialSystem
                                          (this.barrier, cnt, super.system.size());
                Thread t = new Thread(subSystem);
                subSystems.add(subSystem);
                threads.add(t);
            }
            else{
                SpinSystem<?> subSystem = super.system.makePartialSystem
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

    @Override
    public void run(){
        for ( Thread t : this.threads ){
            t.start();
        }
    }


    private void runControl(){
        try{
            for ( int t = 0; t < param.ntstep; t++){
                barrier.await();
                if ( t%10 == 0) reportProgress();
                if ( param.nstout != 0 && t%param.nstout == 0 ) writeToFile();
                this.system.pushTimeStep();
                if ( t == param.ntstep - 1 ){
                    for ( SpinSystem<?> subSystem : subSystems){
                        subSystem.setStop();
                    }
                }
                barrier.await();
            }
            writeToFile(confFileName);
            close();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        catch(BrokenBarrierException e){
            e.printStackTrace();
        }
    }

    @Override 
    public void close(){
        this.writer.close();
    }


}
