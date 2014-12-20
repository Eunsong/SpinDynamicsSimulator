package mysd;

public class RunParameter{

    public final String title;
    public final double dt;
    public final int ntstep;
    public final double alpha;
    public final int nstout;
    public final int nstbuff;

    private RunParameter(Builder builder){
         
          this.title = builder.title;
          this.dt = builder.dt;
          this.ntstep = builder.ntstep;
          this.alpha = builder.alpha;
          this.nstout = builder.nstout;
          this.nstbuff = builder.nstbuff;
    }

    public static class Builder{
        private String title;
        private double dt;
        private int ntstep;
        private double alpha;
        private int nstout;
        private int nstbuff;

        public Builder title(String title){
            this.title = title;
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
