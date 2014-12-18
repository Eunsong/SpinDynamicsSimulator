package mysd;

import java.util.List;
import java.util.ArrayList;
import mysd.vector.*;

public class FullSpinSite extends Site{

    private Vector3D spin;
    private Vector3D forcePrev, force; // effective field including damping term
    private List<Neighbor<FullSpinSite>> neighbors;

    public FullSpinSite(int baseType, Vector3D location){
        super(baseType, location);
        this.spin = new Vector3D();
        this.neighbors = new ArrayList<Neighbor<FullSpinSite>>();
        this.force = new Vector3D();
        this.forcePrev = new Vector3D();
    }

    public Vector3D getLocation(){
        return super.location;
    }

    public Vector3D getSpinVector(){
         return this.spin;
    }

    public void updateSpinVector(Vector3D s){
        this.spin = new Vector3D(s);
    }

    public void updateSpinVector(double[] s){
        this.spin.setX(s[0]);
        this.spin.setY(s[1]);
        this.spin.setZ(s[2]);
    }

    public Vector3D getForcePrev(){
        return this.forcePrev;
    }
    public Vector3D getForce(){
        return this.force;
    }
    public void addForce(Vector3D force){
        this.force.addSet(force);
    }
    public void updateForce(){
        this.forcePrev = this.force;
        this.force = new Vector3D();
    }
    public void updateForce(Vector3D force){
        this.forcePrev = this.force;
        this.force = force;
    }

    public void addNeighbor(Neighbor<FullSpinSite> neighbor){
        this.neighbors.add(neighbor);
    }
    public List<Neighbor<FullSpinSite>> getNeighbors(){
        return this.neighbors;
    }

}
