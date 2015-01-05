package mysd;

import java.util.*;
import mysd.vector.*;

public class CachedSigmaHamiltonian extends SigmaHamiltonian{

    private HashMap<SigmaSpinSite, HashMap<SigmaSpinSite, Coefficients>> 
            cachedCoefficients;

    public CachedSigmaHamiltonian(Interaction<?> h){
        super(h.type, h.matrix);
        cachedCoefficients = new HashMap
                            <SigmaSpinSite, HashMap<SigmaSpinSite, Coefficients>>();
    }


    /**
     * this method overrides getForce method in SigmaHamiltonian class
     * CachedSigmaHamiltonian class uses cached coefficients
     * instead of computing them each time the method is invoked.
     */
    @Override
    public Vector3D getForce(SigmaSpinSite si, SigmaSpinSite sj){

        HashMap<SigmaSpinSite, Coefficients> subMap = cachedCoefficients.get(si);
        if ( subMap == null ){
            subMap = new HashMap<SigmaSpinSite, Coefficients>();
            cachedCoefficients.put(si, subMap);
        }
        Coefficients coeffs = subMap.get(sj);
        if ( coeffs == null ){
            coeffs = new Coefficients();
            coeffs.sigmaXiCoeff = super.getSigmaXiCoeff(si, sj);
            coeffs.sigmaXjCoeff = super.getSigmaXjCoeff(si, sj);
            coeffs.sigmaYiCoeff = super.getSigmaYiCoeff(si, sj);
            coeffs.sigmaYjCoeff = super.getSigmaYjCoeff(si, sj);
            cachedCoefficients.get(si).put(sj, coeffs);
        }

        Vector3D sigmai = si.getSpinVector();
        Vector3D sigmaj = sj.getSpinVector();

        Vector3D F_sxi = Vector3D.times(coeffs.sigmaXiCoeff, sigmai.getX());
        Vector3D F_sxj = Vector3D.times(coeffs.sigmaXjCoeff, sigmaj.getX());
        Vector3D F_syi = Vector3D.times(coeffs.sigmaYiCoeff, sigmai.getY());
        Vector3D F_syj = Vector3D.times(coeffs.sigmaYjCoeff, sigmaj.getY());
        Vector3D F_tot = F_sxi.add(F_sxj).add(F_syi).add(F_syj);

        return F_tot;
    }


    private class Coefficients{
        private Vector3D sigmaXiCoeff, sigmaXjCoeff, sigmaYiCoeff, sigmaYjCoeff;
    }

}
            
