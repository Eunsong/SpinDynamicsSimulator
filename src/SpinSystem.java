import java.util.List;

public class SpinSystem{

    private final List<Site> sites; 
    private final Integrator integrator;
    
    protected SpinSystem(Builder builder){
        this.sites = builder.sites;
        this.integrator = builder.integrator;
    }

    public void forward(){
        integrator.forward(this);
    }

    public static class Builder{

        private List<Site> sites;
        private Integrator integrator;

        public Builder sites(List<Site> sites){
            this.sites = sites;
            return this;
        }
        public Builder integrator(Integrator integrator){
            this.integrator = integrator;
            return this;
        }

        public SpinSystem build(){
            return new SpinSystem(this);
        }
    }   

}
