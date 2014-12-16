package mysd.lattice;

import mysd.vector.Vector3D;

public class LatticeSite{

    private final Vector3D basePosition;
    
    public LatticeSite(Vector3D basePosition){
        this.basePosition = basePosition;
    }

    public Vector3D getBasePosition(){
        return this.basePosition;
    }
}
