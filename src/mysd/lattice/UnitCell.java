package mysd.lattice;

import mysd.vector.Vector3D;
import java.util.List;

public class UnitCell{
    
    private final Vector3D ax, ay, az;
    private final List<LatticeSite> basis;

    public UnitCell(Vector3D ax, Vector3D ay, Vector3D az, List<LatticeSite> basis){
        this.ax = ax;
        this.ay = ay;
        this.az = az;
        this.basis = basis;
    }

}
