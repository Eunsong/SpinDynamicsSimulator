package mysd;

import java.util.*;

public interface SpinSystem<T extends Site<?>> extends Iterable<T>{

    public void forward();

    public double getTime();

    public Iterator<T> iterator();

    public T getSite(int i);

    public int size();

    public double getDt();

    public int getCurrentTimeStep();

    public double getAlpha();

    public void updateForce();

    public void perturbSite(int index, double amount);

    public double getEnergy();
}
