package mysd;

import mysd.writer.*;
import mysd.builder.SpinBuilder;
import java.io.File;

public class SimulationManager{

    private SpinSystem<?> system;
    private Writer writer;
    private RunParameter param;

    public void addSystem(SpinSystem<?> system){
        this.system = system;
    }
    public void addWriter(Writer writer){
        this.writer = writer;
    }
    public void addParam(RunParameter param){
        this.param = param;
    }
    public SpinSystem<?> getSystem(){
        return this.system;
    }

    public void perturbSite(){
        if ( this.param.perturb_site ){
            int index = this.param.perturbing_site_index;
            double amount = this.param.perturbation_size;
            this.system.perturbSite( index, amount );
        }
    }

    public void updateForce(){
        system.updateForce();
    }    
   
    public void forward(){
        system.forward();
    }

    public void writeEnergyToScreen(){
        if ( param.runtype == RunParameter.SimulationType.NONLINEAR ){
            writer.writeMessageToScreen(String.valueOf(system.getEnergy()));
        }
    }

    public void writeToFile(){
        this.writer.writeToFile();
    }
    public void writeMessageToFile(String msg){
        this.writer.writeMessageToFile(msg);
    }

    public void reportProgress(){
        int ntstep = this.param.ntstep;
        int currentStep = this.system.getCurrentTimeStep();
        double progress = (100.0*currentStep)/ntstep;
        String msg = String.format("\r%6.2f%% completed...", progress);
        this.writer.writeMessageToScreen(msg);
    }


    public void close(){
        this.writer.close();
    } 


}
