package mysd;

import java.lang.RuntimeException;

public class RunParameter{

    public final String title;
    public final SimulationType runtype;
    public final double dt;
    public final int ntstep;
    public final double alpha;
    public final int nstout;
    public final int nstbuff;

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
    }

    public static class Builder{
        private String title;
        private SimulationType runtype;
        private double dt;
        private int ntstep;
        private double alpha;
        private int nstout;
        private int nstbuff;

        public Builder title(String title){
            this.title = title;
            return this;
        }
        public Builder runtype(String runtype){
            if ( runtype.toLowerCase().equals("linear") ){
                this.runtype = SimulationType.LINEAR;
            }
            else if ( runtype.toLowerCase().equals("nonlinear") ){
                this.runtype = SimulationType.NONLINEAR;
            }
            else {
                throw new RuntimeException("unknown runtype given. "+
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
        public RunParameter build(){
            return new RunParameter(this);
        }
    }


}
