package mysd;

import mysd.vector.*;

public class SigmaHamiltonian extends Hamiltonian<SigmaSpinSite>{

    private Vector3D xi, yi, zi, xj, yj, zj;
    private double xai, xbi, xci, yai, ybi, yci, zai, zbi, zci,
                   xaj, xbj, xcj, yaj, ybj, ycj, zaj, zbj, zcj;

    public SigmaHamiltonian(Hamiltonian<?> h){
        super(h.type, h.matrix);
    }
    public SigmaHamiltonian(String type, double[][] matrix){
        super(type, matrix);
    }

    @Override
    public Vector3D getForce(SigmaSpinSite si, SigmaSpinSite sj){

        xi = si.getLocalX();
        yi = si.getLocalY();
        zi = si.getLocalZ();
        xj = sj.getLocalX();
        yj = sj.getLocalY();
        zj = sj.getLocalZ();
        xai = xi.getX();
        xbi = xi.getY();
        xci = xi.getZ();
        yai = yi.getX();
        ybi = yi.getY();
        yci = yi.getZ();
        zai = zi.getX();
        zbi = zi.getY();
        zci = zi.getZ();
        xaj = xj.getX();
        xbj = xj.getY();
        xcj = xj.getZ();
        yaj = yj.getX();
        ybj = yj.getY();
        ycj = yj.getZ();
        zaj = zj.getX();
        zbj = zj.getY();
        zcj = zj.getZ();

        Vector3D sigmai = si.getSpinVector();
        Vector3D sigmaj = sj.getSpinVector();

        Vector3D F_sxi = (getSigmaXiCoeff()).times(sigmai.getX());
        Vector3D F_sxj = (getSigmaXjCoeff()).times(sigmaj.getX());
        Vector3D F_syi = (getSigmaYiCoeff()).times(sigmai.getY());
        Vector3D F_syj = (getSigmaYjCoeff()).times(sigmaj.getY());
        Vector3D F_tot = F_sxi.add(F_sxj).add(F_syi).add(F_syj);
    
        return F_tot; 
    }

    public double getEnergy(SigmaSpinSite si, SigmaSpinSite sj){
        // implmenet this
        return 0.0;
    }


    private Vector3D getSigmaXiCoeff(){

        double Fx = (((matrix[1][0]*zaj + matrix[1][1]*zbj + matrix[1][2]*zcj)*xai - (matrix[0][0]*zaj + matrix[0][1]*zbj + matrix[0][2]*zcj)*xbi)*(-(ybi*zai) + yai*zbi) + ((-(matrix[2][0]*zaj) - matrix[2][1]*zbj - matrix[2][2]*zcj)*xai + (matrix[0][0]*zaj + matrix[0][1]*zbj + matrix[0][2]*zcj)*xci)*(yci*zai - yai*zci) + ((matrix[2][0]*zaj + matrix[2][1]*zbj + matrix[2][2]*zcj)*xbi - (matrix[1][0]*zaj + matrix[1][1]*zbj + matrix[1][2]*zcj)*xci)*(-(yci*zbi) + ybi*zci))/(xci*(-(ybi*zai) + yai*zbi) + xbi*(yci*zai - yai*zci) + xai*(-(yci*zbi) + ybi*zci));
        
        double Fy = (((matrix[1][0]*zaj + matrix[1][1]*zbj + matrix[1][2]*zcj)*xai - (matrix[0][0]*zaj + matrix[0][1]*zbj + matrix[0][2]*zcj)*xbi)*(xbi*zai - xai*zbi) + ((-(matrix[2][0]*zaj) - matrix[2][1]*zbj - matrix[2][2]*zcj)*xai + (matrix[0][0]*zaj + matrix[0][1]*zbj + matrix[0][2]*zcj)*xci)*(-(xci*zai) + xai*zci) + ((matrix[2][0]*zaj + matrix[2][1]*zbj + matrix[2][2]*zcj)*xbi - (matrix[1][0]*zaj + matrix[1][1]*zbj + matrix[1][2]*zcj)*xci)*(xci*zbi - xbi*zci))/(xci*(-(ybi*zai) + yai*zbi) + xbi*(yci*zai - yai*zci) + xai*(-(yci*zbi) + ybi*zci));
        
        double Fz = 0.0;
        return new Vector3D(Fx, Fy, Fz);
    }

    private Vector3D getSigmaXjCoeff(){

        double Fx = ((-(zbi*(matrix[0][0]*xaj + matrix[0][1]*xbj + matrix[0][2]*xcj)) + zai*(matrix[1][0]*xaj + matrix[1][1]*xbj + matrix[1][2]*xcj))*(-(ybi*zai) + yai*zbi) + (zci*(matrix[0][0]*xaj + matrix[0][1]*xbj + matrix[0][2]*xcj) - zai*(matrix[2][0]*xaj + matrix[2][1]*xbj + matrix[2][2]*xcj))*(yci*zai - yai*zci) + (-(zci*(matrix[1][0]*xaj + matrix[1][1]*xbj + matrix[1][2]*xcj)) + zbi*(matrix[2][0]*xaj + matrix[2][1]*xbj + matrix[2][2]*xcj))*(-(yci*zbi) + ybi*zci))/(xci*(-(ybi*zai) + yai*zbi) + xbi*(yci*zai - yai*zci) + xai*(-(yci*zbi) + ybi*zci));
        
        double Fy = ((-(zbi*(matrix[0][0]*xaj + matrix[0][1]*xbj + matrix[0][2]*xcj)) + zai*(matrix[1][0]*xaj + matrix[1][1]*xbj + matrix[1][2]*xcj))*(xbi*zai - xai*zbi) + (-(zci*(matrix[0][0]*xaj + matrix[0][1]*xbj + matrix[0][2]*xcj)) + zai*(matrix[2][0]*xaj + matrix[2][1]*xbj + matrix[2][2]*xcj))*(xci*zai - xai*zci) + (-(zci*(matrix[1][0]*xaj + matrix[1][1]*xbj + matrix[1][2]*xcj)) + zbi*(matrix[2][0]*xaj + matrix[2][1]*xbj + matrix[2][2]*xcj))*(xci*zbi - xbi*zci))/(xci*(-(ybi*zai) + yai*zbi) + xbi*(yci*zai - yai*zci) + xai*(-(yci*zbi) + ybi*zci));
        
        double Fz = 0.0;
        return new Vector3D(Fx, Fy, Fz);
    }


    private Vector3D getSigmaYiCoeff(){

        double Fx = (((matrix[1][0]*zaj + matrix[1][1]*zbj + matrix[1][2]*zcj)*yai - (matrix[0][0]*zaj + matrix[0][1]*zbj + matrix[0][2]*zcj)*ybi)*(-(ybi*zai) + yai*zbi) + ((matrix[2][0]*zaj + matrix[2][1]*zbj + matrix[2][2]*zcj)*yai - (matrix[0][0]*zaj + matrix[0][1]*zbj + matrix[0][2]*zcj)*yci)*(-(yci*zai) + yai*zci) + ((matrix[2][0]*zaj + matrix[2][1]*zbj + matrix[2][2]*zcj)*ybi - (matrix[1][0]*zaj + matrix[1][1]*zbj + matrix[1][2]*zcj)*yci)*(-(yci*zbi) + ybi*zci))/(xci*(-(ybi*zai) + yai*zbi) + xbi*(yci*zai - yai*zci) + xai*(-(yci*zbi) + ybi*zci));
        
        double Fy = (((matrix[1][0]*zaj + matrix[1][1]*zbj + matrix[1][2]*zcj)*yai - (matrix[0][0]*zaj + matrix[0][1]*zbj + matrix[0][2]*zcj)*ybi)*(xbi*zai - xai*zbi) + ((matrix[2][0]*zaj + matrix[2][1]*zbj + matrix[2][2]*zcj)*yai - (matrix[0][0]*zaj + matrix[0][1]*zbj + matrix[0][2]*zcj)*yci)*(xci*zai - xai*zci) + ((matrix[2][0]*zaj + matrix[2][1]*zbj + matrix[2][2]*zcj)*ybi - (matrix[1][0]*zaj + matrix[1][1]*zbj + matrix[1][2]*zcj)*yci)*(xci*zbi - xbi*zci))/(xci*(-(ybi*zai) + yai*zbi) + xbi*(yci*zai - yai*zci) + xai*(-(yci*zbi) + ybi*zci));
        
        double Fz = 0.0;
        return new Vector3D(Fx, Fy, Fz);

    }

    private Vector3D getSigmaYjCoeff(){

    double Fx = ((-(zbi*(matrix[0][0]*yaj + matrix[0][1]*ybj + matrix[0][2]*ycj)) + zai*(matrix[1][0]*yaj + matrix[1][1]*ybj + matrix[1][2]*ycj))*(-(ybi*zai) + yai*zbi) + (zci*(matrix[0][0]*yaj + matrix[0][1]*ybj + matrix[0][2]*ycj) - zai*(matrix[2][0]*yaj + matrix[2][1]*ybj + matrix[2][2]*ycj))*(yci*zai - yai*zci) + (-(zci*(matrix[1][0]*yaj + matrix[1][1]*ybj + matrix[1][2]*ycj)) + zbi*(matrix[2][0]*yaj + matrix[2][1]*ybj + matrix[2][2]*ycj))*(-(yci*zbi) + ybi*zci))/(xci*(-(ybi*zai) + yai*zbi) + xbi*(yci*zai - yai*zci) + xai*(-(yci*zbi) + ybi*zci));
    
    double Fy = (zai*((-(xbi*(matrix[1][0]*yaj + matrix[1][1]*ybj + matrix[1][2]*ycj)) - xci*(matrix[2][0]*yaj + matrix[2][1]*ybj + matrix[2][2]*ycj))*zai + xai*(matrix[1][0]*yaj + matrix[1][1]*ybj + matrix[1][2]*ycj)*zbi + xai*(matrix[2][0]*yaj + matrix[2][1]*ybj + matrix[2][2]*ycj)*zci) + zci*(xci*((matrix[0][0]*yaj + matrix[0][1]*ybj + matrix[0][2]*ycj)*zai + (matrix[1][0]*yaj + matrix[1][1]*ybj + matrix[1][2]*ycj)*zbi) - (xai*(matrix[0][0]*yaj + matrix[0][1]*ybj + matrix[0][2]*ycj) + xbi*(matrix[1][0]*yaj + matrix[1][1]*ybj + matrix[1][2]*ycj))*zci) + zbi*((-(xai*(matrix[0][0]*yaj + matrix[0][1]*ybj + matrix[0][2]*ycj)) - xci*(matrix[2][0]*yaj + matrix[2][1]*ybj + matrix[2][2]*ycj))*zbi + xbi*((matrix[0][0]*yaj + matrix[0][1]*ybj + matrix[0][2]*ycj)*zai + (matrix[2][0]*yaj + matrix[2][1]*ybj + matrix[2][2]*ycj)*zci)))/(xci*(ybi*zai - yai*zbi) + xbi*(-(yci*zai) + yai*zci) + xai*(yci*zbi - ybi*zci));

    double Fz = 0.0;    
    return new Vector3D(Fx, Fy, Fz);

    }

}
