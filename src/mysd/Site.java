package mysd;

import java.util.ArrayList;
import java.util.List;
import mysd.vector.*;

public abstract class Site<T extends Site<?>>{

    private final int index;
    private final int baseType; 
    protected final Vector3D location;
    protected List<Neighbor<T>> neighbors;

    public Site(int index, int baseType, Vector3D location){
        this.index = index;
        this.baseType = baseType;
        this.location = location;
        this.neighbors = new ArrayList<Neighbor<T>>();
    }

    public Site(int index, Vector3D location){
        this.index = index;
        this.baseType = -1; // baseType not used
        this.location = location;
        this.neighbors = new ArrayList<Neighbor<T>>();
    }
    
    public Site(Vector3D location){
        this.index = -1; // index not used
        this.baseType = -1; // baseType not used
        this.location = location;
        this.neighbors = new ArrayList<Neighbor<T>>();
    }

    public int getIndex(){
        return this.index;
    }
    public int getBaseType(){
        return this.baseType;
    }

    public Vector3D getLocation(){
        return this.location;
    }

    public abstract Vector3D getSpinVector();

    public abstract void updateSpinVector(Vector3D s);

    public abstract void updateSpinVector(double[] s);

    public abstract void addForce(Vector3D force);

    public abstract Vector3D getForcePrev();

    public abstract Vector3D getForce();

    public abstract void updateForce();

    public abstract void updateForce(Vector3D force);

    public abstract void addNeighbor(Neighbor<T> neighbor);

    public List<Neighbor<T>> getNeighbors(){
        return this.neighbors;
    }

}
