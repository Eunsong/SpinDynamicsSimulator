package mysd;

import mysd.vector.*;

public class SigmaHamiltonian extends Interaction<SigmaSpinSite>{

    private final double[][] matrix;

    public SigmaHamiltonian(Interaction<?> h){
        super(h.type, h.matrix);
        this.matrix = super.matrix;
    }
    public SigmaHamiltonian(String type, double[][] matrix){
        super(type, matrix);
        this.matrix = super.matrix;
    }

    public Vector3D getForce(SigmaSpinSite sj){
        throw new UnsupportedOperationException("getForce(Site<T>) method is not"+
                                                "supported in SigmaHamiltonian.");
    }

    public Vector3D getForce(SigmaSpinSite si, SigmaSpinSite sj){

        Vector3D sigmai = si.getSpinVector();
        Vector3D sigmaj = sj.getSpinVector();

        Vector3D F_sxi = (getSigmaXiCoeff(si, sj)).times(sigmai.getX());
        Vector3D F_sxj = (getSigmaXjCoeff(si, sj)).times(sigmaj.getX());
        Vector3D F_syi = (getSigmaYiCoeff(si, sj)).times(sigmai.getY());
        Vector3D F_syj = (getSigmaYjCoeff(si, sj)).times(sigmaj.getY());
        Vector3D F_tot = F_sxi.add(F_sxj).add(F_syi).add(F_syj);
    
        return F_tot; 
    }

    public double getEnergy(SigmaSpinSite si, SigmaSpinSite sj){
        // implmenet this
        return 0.0;
    }


    protected Vector3D getSigmaXiCoeff(SigmaSpinSite si, SigmaSpinSite sj){

        Vector3D xi = si.getLocalX();
        Vector3D yi = si.getLocalY();
        Vector3D zi = si.getLocalZ();
        Vector3D xj = sj.getLocalX();
        Vector3D yj = sj.getLocalY();
        Vector3D zj = sj.getLocalZ();
        double xai = xi.getX();
        double xbi = xi.getY();
        double xci = xi.getZ();
        double yai = yi.getX();
        double ybi = yi.getY();
        double yci = yi.getZ();
        double zai = zi.getX();
        double zbi = zi.getY();
        double zci = zi.getZ();
        double xaj = xj.getX();
        double xbj = xj.getY();
        double xcj = xj.getZ();
        double yaj = yj.getX();
        double ybj = yj.getY();
        double ycj = yj.getZ();
        double zaj = zj.getX();
        double zbj = zj.getY();
        double zcj = zj.getZ();

        double Fx = (((matrix[1][0]*zaj + matrix[1][1]*zbj +
                    matrix[1][2]*zcj)*xai - (matrix[0][0]*zaj + matrix[0][1]*zbj +
                    matrix[0][2]*zcj)*xbi)*(-(ybi*zai) + yai*zbi) + ((-(matrix[2][0]*zaj) -
                    matrix[2][1]*zbj - matrix[2][2]*zcj)*xai + (matrix[0][0]*zaj + matrix[0][1]*zbj
                    + matrix[0][2]*zcj)*xci)*(yci*zai - yai*zci) + ((matrix[2][0]*zaj +
                    matrix[2][1]*zbj + matrix[2][2]*zcj)*xbi - (matrix[1][0]*zaj + matrix[1][1]*zbj
                    + matrix[1][2]*zcj)*xci)*(-(yci*zbi) + ybi*zci))/(xci*(-(ybi*zai) + yai*zbi) +
                    xbi*(yci*zai - yai*zci) + xai*(-(yci*zbi) + ybi*zci));
        
        double Fy = (((matrix[1][0]*zaj + matrix[1][1]*zbj +
                    matrix[1][2]*zcj)*xai - (matrix[0][0]*zaj + matrix[0][1]*zbj +
                    matrix[0][2]*zcj)*xbi)*(xbi*zai - xai*zbi) + ((-(matrix[2][0]*zaj) -
                    matrix[2][1]*zbj - matrix[2][2]*zcj)*xai + (matrix[0][0]*zaj + matrix[0][1]*zbj
                    + matrix[0][2]*zcj)*xci)*(-(xci*zai) + xai*zci) + ((matrix[2][0]*zaj +
                    matrix[2][1]*zbj + matrix[2][2]*zcj)*xbi - (matrix[1][0]*zaj + matrix[1][1]*zbj
                    + matrix[1][2]*zcj)*xci)*(xci*zbi - xbi*zci))/(xci*(-(ybi*zai) + yai*zbi) +
                    xbi*(yci*zai - yai*zci) + xai*(-(yci*zbi) + ybi*zci));
        
        double Fz = 0.0;
        return new Vector3D(Fx, Fy, Fz);
    }

    protected Vector3D getSigmaXjCoeff(SigmaSpinSite si, SigmaSpinSite sj){

        Vector3D xi = si.getLocalX();
        Vector3D yi = si.getLocalY();
        Vector3D zi = si.getLocalZ();
        Vector3D xj = sj.getLocalX();
        Vector3D yj = sj.getLocalY();
        Vector3D zj = sj.getLocalZ();
        double xai = xi.getX();
        double xbi = xi.getY();
        double xci = xi.getZ();
        double yai = yi.getX();
        double ybi = yi.getY();
        double yci = yi.getZ();
        double zai = zi.getX();
        double zbi = zi.getY();
        double zci = zi.getZ();
        double xaj = xj.getX();
        double xbj = xj.getY();
        double xcj = xj.getZ();
        double yaj = yj.getX();
        double ybj = yj.getY();
        double ycj = yj.getZ();
        double zaj = zj.getX();
        double zbj = zj.getY();
        double zcj = zj.getZ();

        double Fx = ((-(zbi*(matrix[0][0]*xaj + matrix[0][1]*xbj +
                    matrix[0][2]*xcj)) + zai*(matrix[1][0]*xaj + matrix[1][1]*xbj +
                    matrix[1][2]*xcj))*(-(ybi*zai) + yai*zbi) + (zci*(matrix[0][0]*xaj +
                    matrix[0][1]*xbj + matrix[0][2]*xcj) - zai*(matrix[2][0]*xaj + matrix[2][1]*xbj
                    + matrix[2][2]*xcj))*(yci*zai - yai*zci) + (-(zci*(matrix[1][0]*xaj +
                    matrix[1][1]*xbj + matrix[1][2]*xcj)) + zbi*(matrix[2][0]*xaj + matrix[2][1]*xbj
                    + matrix[2][2]*xcj))*(-(yci*zbi) + ybi*zci))/(xci*(-(ybi*zai) + yai*zbi) +
                    xbi*(yci*zai - yai*zci) + xai*(-(yci*zbi) + ybi*zci));
        
        double Fy = ((-(zbi*(matrix[0][0]*xaj + matrix[0][1]*xbj +
                    matrix[0][2]*xcj)) + zai*(matrix[1][0]*xaj + matrix[1][1]*xbj +
                    matrix[1][2]*xcj))*(xbi*zai - xai*zbi) + (-(zci*(matrix[0][0]*xaj +
                    matrix[0][1]*xbj + matrix[0][2]*xcj)) + zai*(matrix[2][0]*xaj + matrix[2][1]*xbj
                    + matrix[2][2]*xcj))*(xci*zai - xai*zci) + (-(zci*(matrix[1][0]*xaj +
                    matrix[1][1]*xbj + matrix[1][2]*xcj)) + zbi*(matrix[2][0]*xaj + matrix[2][1]*xbj
                    + matrix[2][2]*xcj))*(xci*zbi - xbi*zci))/(xci*(-(ybi*zai) + yai*zbi) +
                    xbi*(yci*zai - yai*zci) + xai*(-(yci*zbi) + ybi*zci));
        
        double Fz = 0.0;
        return new Vector3D(Fx, Fy, Fz);
    }


    protected Vector3D getSigmaYiCoeff(SigmaSpinSite si, SigmaSpinSite sj){

        Vector3D xi = si.getLocalX();
        Vector3D yi = si.getLocalY();
        Vector3D zi = si.getLocalZ();
        Vector3D xj = sj.getLocalX();
        Vector3D yj = sj.getLocalY();
        Vector3D zj = sj.getLocalZ();
        double xai = xi.getX();
        double xbi = xi.getY();
        double xci = xi.getZ();
        double yai = yi.getX();
        double ybi = yi.getY();
        double yci = yi.getZ();
        double zai = zi.getX();
        double zbi = zi.getY();
        double zci = zi.getZ();
        double xaj = xj.getX();
        double xbj = xj.getY();
        double xcj = xj.getZ();
        double yaj = yj.getX();
        double ybj = yj.getY();
        double ycj = yj.getZ();
        double zaj = zj.getX();
        double zbj = zj.getY();
        double zcj = zj.getZ();

        double Fx = (((matrix[1][0]*zaj + matrix[1][1]*zbj +
                    matrix[1][2]*zcj)*yai - (matrix[0][0]*zaj + matrix[0][1]*zbj +
                    matrix[0][2]*zcj)*ybi)*(-(ybi*zai) + yai*zbi) + ((matrix[2][0]*zaj +
                    matrix[2][1]*zbj + matrix[2][2]*zcj)*yai - (matrix[0][0]*zaj + matrix[0][1]*zbj
                    + matrix[0][2]*zcj)*yci)*(-(yci*zai) + yai*zci) + ((matrix[2][0]*zaj +
                    matrix[2][1]*zbj + matrix[2][2]*zcj)*ybi - (matrix[1][0]*zaj + matrix[1][1]*zbj
                    + matrix[1][2]*zcj)*yci)*(-(yci*zbi) + ybi*zci))/(xci*(-(ybi*zai) + yai*zbi) +
                    xbi*(yci*zai - yai*zci) + xai*(-(yci*zbi) + ybi*zci));
        
        double Fy = (((matrix[1][0]*zaj + matrix[1][1]*zbj +
                    matrix[1][2]*zcj)*yai - (matrix[0][0]*zaj + matrix[0][1]*zbj +
                    matrix[0][2]*zcj)*ybi)*(xbi*zai - xai*zbi) + ((matrix[2][0]*zaj +
                    matrix[2][1]*zbj + matrix[2][2]*zcj)*yai - (matrix[0][0]*zaj + matrix[0][1]*zbj
                    + matrix[0][2]*zcj)*yci)*(xci*zai - xai*zci) + ((matrix[2][0]*zaj +
                    matrix[2][1]*zbj + matrix[2][2]*zcj)*ybi - (matrix[1][0]*zaj + matrix[1][1]*zbj
                    + matrix[1][2]*zcj)*yci)*(xci*zbi - xbi*zci))/(xci*(-(ybi*zai) + yai*zbi) +
                    xbi*(yci*zai - yai*zci) + xai*(-(yci*zbi) + ybi*zci));
        
        double Fz = 0.0;
        return new Vector3D(Fx, Fy, Fz);

    }

    protected Vector3D getSigmaYjCoeff(SigmaSpinSite si, SigmaSpinSite sj){

        Vector3D xi = si.getLocalX();
        Vector3D yi = si.getLocalY();
        Vector3D zi = si.getLocalZ();
        Vector3D xj = sj.getLocalX();
        Vector3D yj = sj.getLocalY();
        Vector3D zj = sj.getLocalZ();
        double xai = xi.getX();
        double xbi = xi.getY();
        double xci = xi.getZ();
        double yai = yi.getX();
        double ybi = yi.getY();
        double yci = yi.getZ();
        double zai = zi.getX();
        double zbi = zi.getY();
        double zci = zi.getZ();
        double xaj = xj.getX();
        double xbj = xj.getY();
        double xcj = xj.getZ();
        double yaj = yj.getX();
        double ybj = yj.getY();
        double ycj = yj.getZ();
        double zaj = zj.getX();
        double zbj = zj.getY();
        double zcj = zj.getZ();

        double Fx = ((-(zbi*(matrix[0][0]*yaj + matrix[0][1]*ybj +
                    matrix[0][2]*ycj)) + zai*(matrix[1][0]*yaj + matrix[1][1]*ybj +
                    matrix[1][2]*ycj))*(-(ybi*zai) + yai*zbi) + (zci*(matrix[0][0]*yaj +
                    matrix[0][1]*ybj + matrix[0][2]*ycj) - zai*(matrix[2][0]*yaj + matrix[2][1]*ybj
                    + matrix[2][2]*ycj))*(yci*zai - yai*zci) + (-(zci*(matrix[1][0]*yaj +
                    matrix[1][1]*ybj + matrix[1][2]*ycj)) + zbi*(matrix[2][0]*yaj + matrix[2][1]*ybj
                    + matrix[2][2]*ycj))*(-(yci*zbi) + ybi*zci))/(xci*(-(ybi*zai) + yai*zbi) +
                    xbi*(yci*zai - yai*zci) + xai*(-(yci*zbi) + ybi*zci));
    
        double Fy = (zai*((-(xbi*(matrix[1][0]*yaj + matrix[1][1]*ybj +
                    matrix[1][2]*ycj)) - xci*(matrix[2][0]*yaj + matrix[2][1]*ybj +
                    matrix[2][2]*ycj))*zai + xai*(matrix[1][0]*yaj + matrix[1][1]*ybj +
                    matrix[1][2]*ycj)*zbi + xai*(matrix[2][0]*yaj + matrix[2][1]*ybj +
                    matrix[2][2]*ycj)*zci) + zci*(xci*((matrix[0][0]*yaj + matrix[0][1]*ybj +
                    matrix[0][2]*ycj)*zai + (matrix[1][0]*yaj + matrix[1][1]*ybj +
                    matrix[1][2]*ycj)*zbi) - (xai*(matrix[0][0]*yaj + matrix[0][1]*ybj +
                    matrix[0][2]*ycj) + xbi*(matrix[1][0]*yaj + matrix[1][1]*ybj +
                    matrix[1][2]*ycj))*zci) + zbi*((-(xai*(matrix[0][0]*yaj + matrix[0][1]*ybj +
                    matrix[0][2]*ycj)) - xci*(matrix[2][0]*yaj + matrix[2][1]*ybj +
                    matrix[2][2]*ycj))*zbi + xbi*((matrix[0][0]*yaj + matrix[0][1]*ybj +
                    matrix[0][2]*ycj)*zai + (matrix[2][0]*yaj + matrix[2][1]*ybj +
                    matrix[2][2]*ycj)*zci)))/(xci*(ybi*zai - yai*zbi) + xbi*(-(yci*zai) + yai*zci) +
                    xai*(yci*zbi - ybi*zci));

        double Fz = 0.0;    
        return new Vector3D(Fx, Fy, Fz);

    }

}
