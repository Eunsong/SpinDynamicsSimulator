import java.util.List;
import java.util.ArrayList;

public class FullSpinSite extends Site{

    private Vector3D spin;
    private Vector3D forceOld, force; // effectice field including damping term
    private List<Neighbor<FullSpinSite>> neighbors;

    public FullSpinSite(int baseType, Vector3D location){
        super(baseType, location);
        this.spin = new Vector3D();
        this.neighbors = new ArrayList<Neighbor<FullSpinSite>>();
    }

    public Vector3D getSpinVector(){
         return this.spin;
    }
    public void updateSpinVector(double[] s){
        this.spin.setX(s[0]);
        this.spin.setY(s[1]);
        this.spin.setZ(s[2]);
    }

    public Vector3D getForceOld(){
        return this.forceOld;
    }
    public Vector3D getForce(){
        return this.force;
    }
    public void addForce(Vector3D force){
        this.force.addSet(force);
    }
    public void updateForce(){
        this.forceOld = this.force;
        this.force = new Vector3D();
    }
    public void updateForce(Vector3D force){
        this.forceOld = this.force;
        this.force = force;
    }

    public void addNeighbor(Neighbor<FullSpinSite> neighbor){
        this.neighbors.add(neighbor);
    }
    public List<Neighbor<FullSpinSite>> getNeighbors(){
        return this.neighbors;
    }

}
