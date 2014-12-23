package mysd;

import java.util.List;
import java.util.ArrayList;
import mysd.vector.*;
import java.util.Random;

public class SigmaSpinSite extends Site<SigmaSpinSite>{

    private Vector3D sigma; // a devation vector measured in local coordinates
    private final Vector3D xi, yi, zi; // local coordinates expressed in lab coordinates
    private Vector3D force;

    public <T extends Site<?>> SigmaSpinSite(T site){
        super(site.getIndex(), site.getBaseType(), site.getLocation());
        this.zi = new Vector3D();
        this.xi = new Vector3D();
        this.yi = new Vector3D();
        updateLocalCoordinates(site.getSpinVector()); 
        this.sigma = new Vector3D();
        this.force = new Vector3D();
    }

    public void updateSpinVector(Vector3D s){
        this.sigma.copySet(s);
    }
    public void updateSpinVector(double[] s){
        this.sigma.copySet(s[0], s[1], s[2]);
    }
    private void updateLocalCoordinates(Vector3D zi){
        this.zi.copySet(zi);
        Vector3D trialAxis = getRandomVector();
        this.xi.copySet( Vector3D.cross( zi, trialAxis));
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

    private Vector3D getRandomVector(){
        Random rand = new Random();
        double x = 2.0*rand.nextDouble() - 1.0;
        double y = 2.0*rand.nextDouble() - 1.0;
        double z = 2.0*rand.nextDouble() - 1.0;
        Vector3D v = new Vector3D(x, y, z);
        v.normalize();
        return v;
    }

    public Vector3D getLocalX(){
        return this.xi;
    }
    public Vector3D getLocalY(){    
        return this.yi;
    }
    public Vector3D getLocalZ(){
        return this.zi;
    }

    public Vector3D getSpinVector(){
        return this.sigma;
    }

    public Vector3D getGroundStateSpinVector(){
        return this.zi;
    }
    public void updateForce(){
        this.force.reset();
    }
    public void updateForce(Vector3D force){
        this.force.copySet(force);
    }
    public void addForce(Vector3D force){
        this.force.addSet(force);
    }
    public Vector3D getForce(){
        return this.force;
    }
    public Vector3D getForcePrev(){
        // this method is not used!
        throw new UnsupportedOperationException
                  ("getForcePrev() method is not supported for SigmaSpinSite objects");
    }    

    public void addNeighbor(Neighbor<SigmaSpinSite> neighbor){
        super.neighbors.add(neighbor);
    }
    public List<Neighbor<SigmaSpinSite>> getNeighbors(){
        return super.neighbors;
    }


}
