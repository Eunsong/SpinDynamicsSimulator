package mysd;

import java.util.List;
import mysd.vector.*;

public abstract class Site{

//    private final int index;
    private final int baseType; 
    protected final Vector3D location;

    public Site(int baseType, Vector3D location){
        this.baseType = baseType;
        this.location = location;
    }

}
