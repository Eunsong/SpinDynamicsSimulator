package mysd;

import mysd.writer.*;
import mysd.builder.SpinBuilder;
import java.io.File;
import java.io.PrintStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SimulationManager{

    protected SpinSystem<?> system;
    protected Writer writer; // trajectory writer
    protected Writer energyWriter; // energy writer
    protected RunParameter param;
    protected String infoFileName = "unnamed.info";
    protected String confFileName = "unnamed.cnf";

    public void setInfoFileName(String name){
        this.infoFileName = name;
    }

    public void setConfFileName(String name){
        this.confFileName = name;
    }

    public void addSystem(SpinSystem<?> system){
        this.system = system;
    }
    public void addWriter(Writer writer){
        this.writer = writer;
    }
    public void addEnergyWriter(Writer writer){
        this.energyWriter = writer;
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


    public void writeSystemInfo(String outFileName){

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String header = "# mysd simulator system information file\n" +
                        "# created at : " + dateFormat.format(date) + "\n" +
                        "# check out https://github.com/Eunsong/SpinDynamicsSimulator" +
                        " for most recent updates\n\n\n";

        String runparams = "[ simulation parameters ]\n" +
                           "system_name       = " + param.title + "\n" +
                           "ntstep            = " + param.ntstep + "\n" +
                           "dt                = " + param.dt + "\n" +
                           "alpha             = " + param.alpha + "\n" +
                           "nstout            = " + param.nstout + "\n" +
                           "perturbation_size = " + param.perturbation_size + "\n\n\n";

        String sites =  "[ lattice sites ]\n";
        for ( Site<?> site : system ){
            int index = site.getIndex();
            int baseType = site.getBaseType();
            double x = site.getLocation().getX();
            double y = site.getLocation().getY();
            double z = site.getLocation().getZ();
            String siteRep = String.format("%8d %5d    %10.5f %10.5f %10.5f\n",
                                            index, baseType, x, y, z);
            sites += siteRep;
        }

        PrintStream ps = null;
        try {
            ps = new PrintStream(outFileName);
            ps.print(header);
            ps.print(runparams);
            ps.print(sites);
        }
        catch(IOException ex){
            System.out.println("Cannot create output file " + outFileName +"!");
            System.exit(99);
        }
        finally{
            ps.close();
        }
    }

    public void writeEnergyToScreen(){
        if ( param.runtype == RunParameter.SimulationType.NONLINEAR ){
            double t = system.getTime();
            double energy = system.getEnergy();
            String message = String.format("%f     %e\n", t, energy);
            writer.writeMessageToScreen(message);
        }
    }

    public void writeEnergyToFile(){
        if ( param.runtype == RunParameter.SimulationType.NONLINEAR ){
            double t = system.getTime();
            double energy = system.getEnergy();
            String message = String.format("%f     %e\n", t, energy);
            energyWriter.writeMessageToFile(message);
        }
    }

    public void writeToFile(){
        this.writer.writeToFile();
    }

    public void writeToFile(String filename){
        this.writer.writeToFile(filename);
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

    public void run(){
        for ( int t = 0; t < param.ntstep; t++){
            reportProgress();
            if ( param.nstout != 0 && t%param.nstout == 0 ){
                writeToFile();
            }
            if ( param.nstenergy != 0 && t%param.nstenergy == 0 ) writeEnergyToFile();
            updateForce();
            forward();
        }
        writeToFile(confFileName);
        close();
    }

    public void close(){
        this.writer.close();
        if ( this.energyWriter != null ) this.energyWriter.close();
    } 


}
