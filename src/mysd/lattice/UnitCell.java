package lattice;

import mysd.vector.Vector3D;
import java.util.List;

public class UnitCell{
    
    private final Vector3D latticeVector;
    private final List<LatticeSite> basis;

    public UnitCell(Vector3D latticeVector, List<LatticeSite> basis){
        this.lattiecVector = latticeVector;
        this.basis = basis;
    }

}
