package mysd;

import java.util.List;
import mysd.vector.*;

public abstract class Site{

    private final int index;
    private final int baseType; 
    protected final Vector3D location;

    public Site(int index, int baseType, Vector3D location){
        this.index = index;
        this.baseType = baseType;
        this.location = location;
    }

    public Site(int index, Vector3D location){
        this.index = index;
        this.baseType = -1; // baseType not used
        this.location = location;
    }
    
    public Site(Vector3D location){
        this.index = -1; // index not used
        this.baseType = -1; // baseType not used
        this.location = location;
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

    public abstract void updateSpinVector(double[] s);

}
