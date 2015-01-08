package mysd;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import mysd.vector.*;
import mysd.integrator.Integrator;
import java.lang.UnsupportedOperationException;

public class SigmaSpinSystem implements SpinSystem<SigmaSpinSite>{

    private final List<SigmaSpinSite> sites;
    private final Integrator<SigmaSpinSite> integrator;
    private final double alpha;
    private int t; // current time step(simulation time)
    private final CyclicBarrier barrier;
    private boolean stop = false;

    public SigmaSpinSystem(Builder builder){
        this.sites = builder.sites;
        this.integrator = builder.integrator;
        this.alpha = builder.alpha;
        this.t = 0;
        this.barrier = builder.barrier;
    }


    /**
     * subsystem creator for concurrent simulations.
     * @param barrier CyclibBarrier instance to be used to synchronize concurrent
     *                algorithms. The other subsystems must have the same barrier
     * @param start index(inclusive) of the first site in this object to be
     *              included in this subsystem
     * @param last index(exclusive) of the last site in this object to be
     *              included in this subsystem
     */
    public SigmaSpinSystem makePartialSystem(CyclicBarrier barrier, int start, int last){
        List<SigmaSpinSite> subsites = new ArrayList<SigmaSpinSite>();
        for ( int i = start; i < last; i++){
            subsites.add( this.getSite(i) );
        }
        return new Builder().copySites(subsites).integrator(this.integrator).alpha(this.alpha)
                            .barrier(barrier).build();
    }

    public void run(){
        while ( !this.stop ){
            try{
                this.barrier.await();
                updateForce();
                forward();
                this.barrier.await();
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
                return;
            }
            catch (BrokenBarrierException e){
                e.printStackTrace();
            }
        }
    }

    public void setStop(){
        this.stop = true;
    }

    public void pushTimeStep(){
        this.t++;
    }

    public void forward(){
        integrator.forward(this);
        this.t++;
    }
    public double getTime(){
        return getDt()*this.t;
    }
    public int getCurrentTimeStep(){
        return this.t;
    }   
    public Iterator<SigmaSpinSite> iterator(){
        return this.sites.iterator();
    }
    public SigmaSpinSite getSite(int i){
        return this.sites.get(i);
    }
    public int size(){
        return this.sites.size();
    }
    public double getDt(){
        return this.integrator.getDt();
    }
    public double getAlpha(){
        return this.alpha;
    }

    public void perturbSite(int index, double amount){
        SigmaSpinSite si = this.sites.get(index);
        double[] sigma0 = {amount, 0.0, 0.0};
        si.updateSpinVector(sigma0);
    }

    public void updateForce(){
        for ( SigmaSpinSite si : this.sites ){
            si.updateForce();
            for ( Neighbor<SigmaSpinSite> nj : si.getNeighbors() ){
                SigmaSpinSite sj = nj.getSite();
                Interaction<SigmaSpinSite> hamiltonian = nj.getHamiltonian();
                Vector3D bareForce = hamiltonian.getForce(si, sj);
                si.addForce(bareForce);
            }
        }
    }

    public double getEnergy(){
        // energy computation is meaning-less for linearized simulation 
        throw new UnsupportedOperationException
                  ("Energy cannot be computed for linearized simulation. "+
                   "This method is not supposed to be invoked.");
    }

    public static class Builder{

        private List<SigmaSpinSite> sites;
        private Integrator<SigmaSpinSite> integrator;
        private double alpha;
        private CyclicBarrier barrier;

        public Builder copySites(List<SigmaSpinSite> sites){
            this.sites = sites;
            return this;
        }

        public <T extends Site<?>> Builder sites(List<T> sites){
            this.sites = new ArrayList<SigmaSpinSite>();
            for ( T s : sites ){
                this.sites.add(new SigmaSpinSite(s));
            }
            // construct neighbor lists
            for (T s : sites ){
                int index_i = s.getIndex();
                SigmaSpinSite si = this.sites.get(index_i);
                assert index_i == si.getIndex();
                for ( int i = 0; i < s.getNeighbors().size(); i++){
                    // s is guaranteed to have Neighbor<T> objects. Type safety is ensured
                    @SuppressWarnings("unchecked")
                    Neighbor<T> nj = (Neighbor<T>)s.getNeighbors().get(i);
                    int index_j = nj.getSite().getIndex();
                    SigmaSpinSite sj = this.sites.get(index_j);
                    SigmaHamiltonian h = new CachedSigmaHamiltonian(nj.getHamiltonian());
                    assert index_j == sj.getIndex();
                    si.addNeighbor(new Neighbor<SigmaSpinSite>(sj, h)); 
                }
            }
            return this;
        }
        public Builder integrator(Integrator<SigmaSpinSite> integrator){
            this.integrator = integrator;
            return this;
        }
        public Builder alpha(double alpha){
            this.alpha = alpha;
            return this;
        }
        public Builder barrier(CyclicBarrier barrier){
            this.barrier = barrier;
            return this;
        }
        public SigmaSpinSystem build(){
            return new SigmaSpinSystem(this);
        }
    }


}
