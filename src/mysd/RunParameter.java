package mysd;

import java.lang.RuntimeException;
import mysd.exceptions.*;

public class RunParameter{

    public final String title;
    public final SimulationType runtype;
    public final double dt;
    public final int ntstep;
    public final double alpha;
    public final int nstout;
    public final int nstbuff;
    public final int nstenergy;
    public final boolean perturb_site;
    public final int perturbing_site_index;
    public final double perturbation_size;

    public enum SimulationType{
        LINEAR, NONLINEAR
    }

    private RunParameter(Builder builder){
         
        this.title = builder.title;
        this.runtype = builder.runtype;
        this.dt = builder.dt;
        this.ntstep = builder.ntstep;
        this.alpha = builder.alpha;
        this.nstout = builder.nstout;
        this.nstbuff = builder.nstbuff;
        this.nstenergy = builder.nstenergy;
        this.perturb_site = builder.perturb_site;
        this.perturbing_site_index = builder.perturbing_site_index;
        this.perturbation_size = builder.perturbation_size;
    }

    public static class Builder{
        private String title;
        private SimulationType runtype;
        private double dt;
        private int ntstep;
        private double alpha;
        private int nstout;
        private int nstbuff;
        private int nstenergy;
        private boolean perturb_site;
        private int perturbing_site_index;
        private double perturbation_size;


        public Builder title(String title){
            this.title = title;
            return this;
        }
        public Builder runtype(String runtype) throws InvalidSdpFileException{
            if ( runtype.toLowerCase().equals("linear") ){
                this.runtype = SimulationType.LINEAR;
            }
            else if ( runtype.toLowerCase().equals("nonlinear") ){
                this.runtype = SimulationType.NONLINEAR;
            }
            else {
                throw new InvalidSdpFileException("unknown runtype given. "+
                                       "Should be either linear or nonlinear");
            }
            return this;
        }
        public Builder dt(double dt){
            this.dt = dt;
            return this;
        }
        public Builder ntstep(int ntstep){
            this.ntstep = ntstep;
            return this;
        }
        public Builder alpha(double alpha){
            this.alpha = alpha;
            return this;
        }
        public Builder nstout(int nstout){
            this.nstout = nstout;
            return this;
        }
        public Builder nstbuff(int nstbuff){
            this.nstbuff = nstbuff;
            return this;
        }
        public Builder nstenergy(int nstenergy){
            this.nstenergy = nstenergy;
            return this;
        }   
        public Builder perturb_site(boolean perturb_site){
            this.perturb_site = perturb_site;
            return this;
        }    
        public Builder perturbing_site_index(int perturbing_site_index){
            this.perturbing_site_index = perturbing_site_index;
            return this;
        }    
        public Builder perturbation_size(double size){
            this.perturbation_size = size;
            return this;
        }    
        public RunParameter build(){
            return new RunParameter(this);
        }

    
    }


}
