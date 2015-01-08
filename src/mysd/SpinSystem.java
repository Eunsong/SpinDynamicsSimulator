package mysd;

import java.util.*;
import java.util.concurrent.CyclicBarrier;

public interface SpinSystem<T extends Site<?>> extends Iterable<T>, Runnable{

    public void forward();

    public double getTime();

    public Iterator<T> iterator();

    public T getSite(int i);

    public int size();

    public SpinSystem<T> makePartialSystem(CyclicBarrier barrier, int start, int end);
    
    public void pushTimeStep();

    public void setStop();

    public double getDt();

    public int getCurrentTimeStep();

    public double getAlpha();

    public void updateForce();

    public void perturbSite(int index, double amount);

    public double getEnergy();
}
