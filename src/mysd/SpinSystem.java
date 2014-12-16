package mysd;

import java.util.List;
import mysd.vector.*;
import mysd.integrator.*;

public class SpinSystem{

    private final List<FullSpinSite> sites; 
    private final Integrator integrator;
    private final double alpha;
    
    protected SpinSystem(Builder builder){
        this.sites = builder.sites;
        this.integrator = builder.integrator;
        this.alpha = builder.alpha;
    }

    public void forward(){
        integrator.forward(this);
    }

    public List<FullSpinSite> getSites(){
        return this.sites;
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

        public SpinSystem build(){
            return new SpinSystem(this);
        }
    }   

}
