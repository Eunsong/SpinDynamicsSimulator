package mysd;

import java.util.Iterator;
import java.util.List;
import mysd.vector.*;
import mysd.integrator.Integrator;

public class FullSpinSystem implements SpinSystem<FullSpinSite>{

    private final List<FullSpinSite> sites; 
    private final Integrator integrator;
    private final double alpha;
    private int t; // current time step(simulation time, not wall time)
    
    protected FullSpinSystem(Builder builder){
        this.sites = builder.sites;
        this.integrator = builder.integrator;
        this.alpha = builder.alpha;
        this.t = 0;
    }

    public void forward(){
        integrator.forward(this);
        this.t++;
    }
    public double getTime(){
        return getDt()*this.t;
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

    public void updateForce(){
        for ( FullSpinSite si : this.sites ){
            Vector3D vi = si.getSpinVector();
            si.updateForce(); // prepare to load new force 
            for ( Neighbor<FullSpinSite> nj : si.getNeighbors() ){
                FullSpinSite sj = nj.getSite(); 
                Hamiltonian hamiltonian = nj.getHamiltonian();
                Vector3D bareForce = hamiltonian.getForce(sj);
                Vector3D dampingTerm = Vector3D.cross(vi, bareForce).times(alpha);
                si.addForce(bareForce);
                si.addForce(dampingTerm);
            }
        }
    }

    public static class Builder{

        private List<FullSpinSite> sites;
        private Integrator integrator;
        private double alpha;

        public Builder sites(List<FullSpinSite> sites){
            this.sites = sites;
            return this;
        }
        public Builder integrator(Integrator integrator){
            this.integrator = integrator;
            return this;
        }
        public Builder alpha(double alpha){
            this.alpha = alpha;
            return this;
        }

        public FullSpinSystem build(){
            return new FullSpinSystem(this);
        }
    }   

}
