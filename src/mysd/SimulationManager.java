package mysd;

import mysd.writer.*;
import mysd.builder.SpinBuilder;
import java.io.File;

public class SimulationManager{

    private SpinSystem<?> system;
    private Writer writer;

    public void addSystem(SpinSystem<?> system){
        this.system = system;
    }
    public void addWriter(Writer writer){
        this.writer = writer;
    }
    public void overLoadSpins(File cnf, boolean normalize){
        SpinBuilder.overloadSpins(this.system, cnf, normalize);
    }
    public SpinSystem<?> getSystem(){
        return this.system;
    }

    public void updateForce(){
        system.updateForce();
    }    
   
    public void forward(){
        system.forward();
    }

    public void writeToFile(){
        this.writer.writeToFile();
    }
    public void writeMessageToFile(String msg){
        this.writer.writeMessageToFile(msg);
    }
    public void close(){
        this.writer.close();
    } 


}
