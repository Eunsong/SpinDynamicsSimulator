import java.util.List;

public abstract class Site{

//    private final int index;
    private final int baseType; 
    protected final Vector3D location;
    protected List<Site> neighbors; 

    public Site(int baseType, Vector3D location){
        this.baseType = baseType;
        this.location = location;
    }

//    public void addNeighbor

}
