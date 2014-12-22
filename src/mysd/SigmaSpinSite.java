package mysd;

import java.util.List;
import java.util.ArrayList;
import mysd.vector.*;

public class SigmaSpinSite extends FullSpinSite{

    private Vector3D sigma;
    private List<Neighbor<SigmaSpinSite>> neighbors;
    private final Vector3D xi, yi, zi; // local coordinates expressed in lab coordinates
    private boolean groundStateUpdated = false; /* true if ground state 
                                                   spin(super.spin) is updated */

    public SigmaSpinSite(int index, int baseType, Vector3D location){
        super(index, baseType, location);
        super.spin = null;
        super.neighbors = null;
        this.xi = new Vector3D();
        this.yi = new Vector3D();
        this.zi = new Vector3D();
        this.neighbors = new ArrayList<Neighbor<SigmaSpinSite>>();
        this.sigma = new Vector3D();
    }
    @Override
    public void updateSpinVector(Vector3D s){
        if (!groundStateUpdated) updateLocalCoordinates(s);
        else this.sigma.copySet(s);
    }
    @Override
    public void updateSpinVector(double[] s){
        if (!groundStateUpdated) updateLocalCoordinates(new Vector3D(s[0],s[1],s[2]));
        else this.sigma.copySet(s[0], s[1], s[2]);
    }
    private void updateLocalCoordinates(Vector3D zi){
        this.zi.copySet(zi);
        Vector3D trialAxis = new Vector3D(1.0, 0.0, 0.0);
        this.xi.copySet( Vector3D.cross( zi, trialAxis));
        if (xi.normsq() == 0.0 ){
            trialAxis = new Vector3D(0.0, 1.0, 0.0);
            this.xi.copySet( Vector3D.cross( zi, trialAxis));
        }
        this.yi.copySet( Vector3D.cross(this.zi, this.xi));
        // ensure normalization
        this.zi.normalize();
        this.xi.normalize();
        this.yi.normalize();
        assert ( this.zi.normsq() == 1.0 && this.xi.normsq() == 1.0 && 
                 this.yi.normsq() == 1.0);
        assert ( Vector3D.cross(this.xi, this.yi).normsq() == 0.0 && 
                 Vector3D.cross(this.xi, this.zi).normsq() == 0.0 &&
                 Vector3D.cross(this.yi, this.zi).normsq() == 0.0);
    }
    

    @Override
    public Vector3D getSpinVector(){
        return this.sigma;
    }

    public Vector3D getGroundStateSpinVector(){
        return this.zi;
    }
    @Override
    public void updateForce(){

    }
    
    public void addNeighbor(Neighbor<SigmaSpinSite> neighbor){
        this.neighbors.add(neighbor);
    }
    public List<Neighbor<SigmaSpinSite>> getNeighbors(){
        return this.neighbors;
    }


}
