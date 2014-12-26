package mysd.integrator;

import mysd.*;

public interface Integrator<T extends Site<?>>{

    /**
     * returns time step size for this integrator object
     */
    public double getDt();

    /**
     * @param system SpinSystem instance containing Site objects
     */ 
    public void forward(SpinSystem<T> system);


}
