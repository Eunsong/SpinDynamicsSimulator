package mysd;

import mysd.vector.*;

public class CachedSigmaHamiltonian extends SigmaHamiltonian{

    private final Vector3D sigmaXiCoeff, sigmaXjCoeff, sigmaYiCoeff, sigmaYjCoeff;

    public CachedSigmaHamiltonian(Interaction<?> h,
                                  SigmaSpinSite si, SigmaSpinSite sj){
        super(h.type, h.matrix);
        super.xi = si.getLocalX();
        super.yi = si.getLocalY();
        super.zi = si.getLocalZ();
        super.xj = sj.getLocalX();
        super.yj = sj.getLocalY();
        super.zj = sj.getLocalZ();
        super.xai = super.xi.getX();
        super.xbi = super.xi.getY();
        super.xci = super.xi.getZ();
        super.yai = super.yi.getX();
        super.ybi = super.yi.getY();
        super.yci = super.yi.getZ();
        super.zai = super.zi.getX();
        super.zbi = super.zi.getY();
        super.zci = super.zi.getZ();
        super.xaj = super.xj.getX();
        super.xbj = super.xj.getY();
        super.xcj = super.xj.getZ();
        super.yaj = super.yj.getX();
        super.ybj = super.yj.getY();
        super.ycj = super.yj.getZ();
        super.zaj = super.zj.getX();
        super.zbj = super.zj.getY();
        super.zcj = super.zj.getZ();

        this.sigmaXiCoeff = super.getSigmaXiCoeff();
        this.sigmaXjCoeff = super.getSigmaXjCoeff();
        this.sigmaYiCoeff = super.getSigmaYiCoeff();
        this.sigmaYjCoeff = super.getSigmaYjCoeff();
    }

    /**
     * this method overrides getForce method in SigmaHamiltonian class
     * note that since CachedSigmaHamiltonian class is using cached coefficients
     * instead of computing them every time the method is invoked,
     * SigmaSpinSite arguments si and sj do not play any role.
     * These arguments are not necessary but left as is
     * to be fully compatible with the original SigmaHamiltonian class.
     */
    @Override
    public Vector3D getForce(SigmaSpinSite si, SigmaSpinSite sj){

        Vector3D sigmai = si.getSpinVector();
        Vector3D sigmaj = sj.getSpinVector();

        Vector3D F_sxi = Vector3D.times(this.sigmaXiCoeff, sigmai.getX());
        Vector3D F_sxj = Vector3D.times(this.sigmaXjCoeff, sigmaj.getX());
        Vector3D F_syi = Vector3D.times(this.sigmaYiCoeff, sigmai.getY());
        Vector3D F_syj = Vector3D.times(this.sigmaYjCoeff, sigmaj.getY());
        Vector3D F_tot = F_sxi.add(F_sxj).add(F_syi).add(F_syj);

        return F_tot;
 
    }


}
