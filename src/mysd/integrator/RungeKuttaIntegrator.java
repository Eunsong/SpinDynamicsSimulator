package mysd.integrator;

import mysd.*;
import mysd.vector.*;

public class RungeKuttaIntegrator implements Integrator<SigmaSpinSite>{

    private final double dt;
        
    public RungeKuttaIntegrator(double dt){
        this.dt = dt;
    }

    public double getDt(){
        return this.dt;
    }

    public void forward(SpinSystem<SigmaSpinSite> system){
        double alpha = system.getAlpha();
        for ( SigmaSpinSite s: system){
            forward(alpha, s);
        }
    } 

    public void forward(double alpha, SigmaSpinSite si){

        Vector3D sigmaOrg = new Vector3D(si.getSpinVector());

        // k1s 
        Vector3D F = si.getForce();
        double Fx = F.getX();
        double Fy = F.getY();
        double k1x = dt/(1.0+alpha*alpha)*(Fx - alpha*Fy);
        double k1y = dt/(1.0+alpha*alpha)*(alpha*Fx + Fy);
        double[] newS = new double[3];
        newS[0] = sigmaOrg.getX() + 0.5*k1x;
        newS[1] = sigmaOrg.getY() + 0.5*k1y;
        newS[2] = 0.0;
        si.updateSpinVector( newS );
        si.updateForce();
        //update force using new spins
        for ( Neighbor<SigmaSpinSite> nj : si.getNeighbors() ){
            SigmaSpinSite sj = nj.getSite();
            Interaction<SigmaSpinSite> hamiltonian = nj.getHamiltonian();
            Vector3D force = hamiltonian.getForce(si, sj);
            si.addForce(force); 
        }

        // k2s
        F = si.getForce();
        Fx = F.getX(); 
        Fy = F.getY();
        double k2x = dt/(1.0+alpha*alpha)*(Fx - alpha*Fy);
        double k2y = dt/(1.0+alpha*alpha)*(alpha*Fx + Fy);
        newS[0] = sigmaOrg.getX() + 0.5*k2x;
        newS[1] = sigmaOrg.getY() + 0.5*k2y;
        si.updateSpinVector( newS );
        si.updateForce();
        //update force using new spins
        for ( Neighbor<SigmaSpinSite> nj : si.getNeighbors() ){
            SigmaSpinSite sj = nj.getSite();
            Interaction<SigmaSpinSite> hamiltonian = nj.getHamiltonian();
            Vector3D force = hamiltonian.getForce(si, sj);
            si.addForce(force); 
        }

        // k3s
        F = si.getForce();
        Fx = F.getX(); 
        Fy = F.getY();
        double k3x = dt/(1.0+alpha*alpha)*(Fx - alpha*Fy);
        double k3y = dt/(1.0+alpha*alpha)*(alpha*Fx + Fy);
        newS[0] = sigmaOrg.getX() + k3x;
        newS[1] = sigmaOrg.getY() + k3y;
        si.updateSpinVector( newS );
        si.updateForce();
        //update force using new spins
        for ( Neighbor<SigmaSpinSite> nj : si.getNeighbors() ){
            SigmaSpinSite sj = nj.getSite();
            Interaction<SigmaSpinSite> hamiltonian = nj.getHamiltonian();
            Vector3D force = hamiltonian.getForce(si, sj);
            si.addForce(force); 
        }

        // k4s
        F = si.getForce();
        Fx = F.getX(); 
        Fy = F.getY();
        double k4x = dt/(1.0+alpha*alpha)*(Fx - alpha*Fy);
        double k4y = dt/(1.0+alpha*alpha)*(alpha*Fx + Fy);

        newS[0] = sigmaOrg.getX() + k1x/6.0 + k2x/3.0 + k3x/3.0 + k4x/6.0;
        newS[1] = sigmaOrg.getY() + k1y/6.0 + k2y/3.0 + k3y/3.0 + k4y/6.0;
        si.updateSpinVector( newS );        

    }


}
