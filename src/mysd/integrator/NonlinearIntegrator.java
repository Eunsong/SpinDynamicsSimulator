package mysd.integrator;

import java.util.List;
import mysd.*;
import mysd.vector.*;


public class NonlinearIntegrator implements Integrator{

    private final double dt;

    public NonlinearIntegrator(double dt){
        this.dt = dt;
    }

    public void forward(SpinSystem system){
        List<FullSpinSite> sites = system.getSites();
        for ( FullSpinSite s : sites ){
            forward(s);
        }
    }

    public void forward(FullSpinSite si){
        double[][] A = getA(si);
        double[][] invA = getInverse(A);
        double[] B = getB(si);
        double[] newSpin = matrixProduct(invA, B);
        si.updateSpinVector(newSpin);        
    }

    public double[][] getA(FullSpinSite si){
        Vector3D heffOld = si.getForceOld();
        Vector3D heff = si.getForce();
        double[][] a = new double[3][3];
        a[0][0] = 1.0;
        a[1][1] = 1.0;
        a[2][2] = 1.0;
        a[0][1] = -dt/4.0*(3.0*heff.getZ() - heffOld.getZ());
        a[0][2] = -dt/4.0*(-3.0*heff.getY() + heffOld.getY());
        a[1][0] = -dt/4.0*(-3.0*heff.getZ() + heffOld.getZ());
        a[1][2] = -dt/4.0*(3.0*heff.getX() - heffOld.getX());
        a[2][0] = -dt/4.0*(3.0*heff.getY() - heffOld.getY());
        a[2][1] = -dt/4.0*(-3.0*heff.getX() + heffOld.getX());
        return a;
    }

    public double[] getB(FullSpinSite si){
        Vector3D vi = si.getSpinVector();
        Vector3D heffOld = si.getForceOld();
        Vector3D heff = si.getForce();
        double[] b = new double[3];
        b[0] = vi.getX() - dt/4.0*((heffOld.getZ() - 3.0*heff.getZ())*vi.getY() 
                                - (heffOld.getY() - 3.0*heff.getY())*vi.getZ()); 

        b[1] = vi.getY() - dt/4.0*((heffOld.getX() - 3.0*heff.getX())*vi.getZ() 
                                - (heffOld.getZ() - 3.0*heff.getZ())*vi.getX()); 

        b[2] = vi.getZ() - dt/4.0*((heffOld.getY() - 3.0*heff.getY())*vi.getX() 
                                - (heffOld.getX() - 3.0*heff.getX())*vi.getY()); 
        return b;
    }
    
    public static double[][] getInverse(double[][]a){
        double[][] invA = new double[3][3];
        double det = getDet(a);
        invA[0][0] = (a[1][1]*a[2][2] - a[1][2]*a[2][1])*det;
        invA[0][1] = (a[0][2]*a[2][1] - a[0][1]*a[2][2])*det;                   
        invA[0][2] = (a[0][1]*a[1][2] - a[0][2]*a[1][1])*det;
        invA[1][0] = (a[1][2]*a[2][0] - a[1][0]*a[2][2])*det;
        invA[1][1] = (a[0][0]*a[2][2] - a[0][2]*a[2][0])*det;                   
        invA[1][2] = (a[0][2]*a[1][0] - a[0][0]*a[1][2])*det;
        invA[2][0] = (a[1][0]*a[2][1] - a[1][1]*a[2][0])*det;
        invA[2][1] = (a[0][1]*a[2][0] - a[0][0]*a[2][1])*det;                   
        invA[2][2] = (a[0][0]*a[1][1] - a[0][1]*a[1][0])*det;
        return invA;
    }
    

    public static double getDet(double[][] a){
        return a[0][2]*a[1][1]*a[2][0] - a[0][1]*a[1][2]*a[2][0] 
               - a[0][2]*a[1][0]*a[2][1] + a[0][0]*a[1][2]*a[2][1] 
               + a[0][1]*a[1][0]*a[2][2] - a[0][0]*a[1][1]*a[2][2];
    }
   
    public static double[] matrixProduct(double[][] a, double[] b){
        double[] result = new double[3];
        result[0] = b[0]*a[0][0] + b[1]*a[0][1] + b[2]*a[0][2];
        result[1] = b[0]*a[1][0] + b[1]*a[1][1] + b[2]*a[1][2];
        result[2] = b[0]*a[2][0] + b[1]*a[2][1] + b[2]*a[2][2];
        return result;
    }

}
