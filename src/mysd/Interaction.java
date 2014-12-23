package mysd;

import mysd.vector.*;
import java.lang.RuntimeException;

public abstract class Interaction<T extends Site<?>>{

    protected String type;
    protected int index;
    protected final double[][] matrix;

    public Interaction(String type, double[][] matrix){
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


    public abstract Vector3D getForce(T sj);

    public abstract Vector3D getForce(T si, T sj);

    public abstract double getEnergy(T si, T sj);



}
