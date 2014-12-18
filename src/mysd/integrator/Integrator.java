package mysd.integrator;

import mysd.*;

public interface Integrator{

    /**
     * returns time step size for this integrator object
     */
    public double getDt();

    /**
     * @param system SpinSystem instance containing Site objects
     */ 
    public void forward(SpinSystem system);


}
