package mysd;

import mysd.vector.*;

public class Hamiltonian{

    private final double[][] matrix;
    
    public Hamiltonian(double[][] matrix){
        this.matrix = matrix;
    }
    public Vector3D getForce(FullSpinSite sj){
        Vector3D spin = sj.getSpinVector();
        return product(spin);
    }
    public double getEnergy(FullSpinSite si, FullSpinSite sj){
        Vector3D spini = si.getSpinVector();
        Vector3D spinj = sj.getSpinVector();
        return Vector3D.dot(product(spini), spinj); 
    }

    private Vector3D product(Vector3D v){
        double vx = v.getX();
        double vy = v.getY();
        double vz = v.getZ();
        double x = matrix[0][0]*vx + matrix[0][1]*vy + matrix[0][2]*vz;
        double y = matrix[1][0]*vx + matrix[1][1]*vy + matrix[1][2]*vz;
        double z = matrix[2][0]*vx + matrix[2][1]*vy + matrix[2][2]*vz;
        return new Vector3D(x, y, z);
    }

}
