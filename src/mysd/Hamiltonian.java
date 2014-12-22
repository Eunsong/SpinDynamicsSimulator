package mysd;

import mysd.vector.*;
import java.lang.RuntimeException;

public class Hamiltonian<T extends Site<?>>{

    protected String type;
    protected int index;
    protected final double[][] matrix;
    
    public Hamiltonian(double[][] matrix){
        this.matrix = matrix;
        this.type = null;
        this.index = -1;
    }

    public Hamiltonian(String type, double[][] matrix){
        this.matrix = matrix;
        this.type = type;
        this.index = -1;
    }

    public void setType(String type){
        if ( this.type == null){
            this.type = type;
        }
        else{
            throw new RuntimeException
                      ("type of the Hamiltonian object cannot be reassigned!");
        }
    }

    public void setIndex(int index){
        if ( index == -1){
            this.index = index;
        }
        else{
            throw new RuntimeException
                      ("index of the Hamiltonian object cannot be reassigned!");
        }
    }

    public String getType(){
        return this.type;
    }
    public int getIndex(){
        return this.index;
    }
    
    public Vector3D getForce(Site<T> sj){
        Vector3D spin = sj.getSpinVector();
        return product(spin);
    }
    public Vector3D getForce(Site<T> si, Site<T> sj){
        throw new UnsupportedOperationException("getForce(Site<T>, Site<T>) method "+
                  "is not supported in default Hamiltonian class.");
    }

    public double getEnergy(Site<T> si, Site<T> sj){
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
