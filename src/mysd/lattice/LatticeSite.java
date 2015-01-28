package mysd.lattice;

import mysd.vector.Vector3D;

public class LatticeSite{

    private final Vector3D basePosition;
    private final int baseIndex;
   
    public LatticeSite(int index, Vector3D basePosition){
        this.basePosition = basePosition;
        this.baseIndex = index;
    }
 
    public LatticeSite(Vector3D basePosition){
        this.basePosition = basePosition;
        this.baseIndex = -1; // baseIndex not used
    }

    public Vector3D getBasePosition(){
        return this.basePosition;
    }

    public int getBaseIndex(){
        return this.baseIndex;
    }
}
