package mysd;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import mysd.vector.*;
import mysd.integrator.Integrator;

public class FullSpinSystem implements SpinSystem<FullSpinSite>{

    private final List<FullSpinSite> sites; 
    private final Integrator<FullSpinSite> integrator;
    private final double alpha;
    private int t; // current time step(simulation time, not wall time)
    private final CyclicBarrier barrier;  
    private boolean stop = false;
 
    protected FullSpinSystem(Builder builder){
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
    public FullSpinSystem makePartialSystem(CyclicBarrier barrier, int start, int last){
        List<FullSpinSite> subsites = new ArrayList<FullSpinSite>();
        for ( int i = start; i < last; i++){
            subsites.add( this.getSite(i) );
        }
        return new Builder().sites(subsites).integrator(this.integrator).alpha(this.alpha)
                            .barrier(barrier).build();
    }

    public void run(){
        while ( !this.stop ){
            try{
                this.barrier.await();
                updateForce();
                this.barrier.await();
                forward();
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
    public Iterator<FullSpinSite> iterator(){
        return this.sites.iterator();
    }
    public FullSpinSite getSite(int i){
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
        FullSpinSite si = this.sites.get(index);
        Vector3D vi = si.getSpinVector();
        double rads = amount*Math.PI/180.0;
        rotate(vi, rads); 
    }

    public void updateForce(){
        for ( FullSpinSite si : this.sites ){
            Vector3D vi = si.getSpinVector();
            si.updateForce(); // prepare to load new force 
            for ( Neighbor<FullSpinSite> nj : si.getNeighbors() ){
                FullSpinSite sj = nj.getSite(); 
                Interaction<FullSpinSite> hamiltonian = nj.getHamiltonian();
                Vector3D bareForce = hamiltonian.getForce(sj);
                Vector3D dampingTerm = Vector3D.cross(vi, bareForce).times(alpha);
                si.addForce(bareForce);
                si.addForce(dampingTerm);
            }
        }
    }


    /**
     * roated a given Vector3D v w.r.t a random axis that is perpendicular to v.
     * roateted vector will be overwritten to v instead of returning a new object.
     */
    private void rotate(Vector3D v, double rads){
        Vector3D trialAxis = new Vector3D(1.0, 0.0, 0.0);
        Vector3D rotAxis = Vector3D.cross( v, trialAxis );
        if ( rotAxis.normsq() == 0.0 ){
            trialAxis = new Vector3D(0.0, 1.0, 0.0);
            rotAxis = Vector3D.cross(v, trialAxis);
        } 
        rotAxis.normalize();
        v.times(Math.cos(rads)).add( rotAxis.times(Math.sin(rads)) );
        v.normalize();
    }


    public double getEnergy(){
        double pot = 0.0;
        for ( FullSpinSite si : sites ){
            for ( Neighbor<FullSpinSite> nj : si.getNeighbors() ){
                FullSpinSite sj = nj.getSite();
                Interaction<FullSpinSite> h = nj.getHamiltonian();
                pot += 0.5*h.getEnergy(si, sj);
            }
        }
        return pot;
    }

    public static class Builder{

        private List<FullSpinSite> sites;
        private Integrator<FullSpinSite> integrator;
        private double alpha;
        private CyclicBarrier barrier;

        public Builder sites(List<FullSpinSite> sites){
            this.sites = sites;
            return this;
        }
        public Builder integrator(Integrator<FullSpinSite> integrator){
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
        public FullSpinSystem build(){
            return new FullSpinSystem(this);
        }
    }   

}
