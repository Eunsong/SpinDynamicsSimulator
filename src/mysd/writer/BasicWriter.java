package mysd.writer;

import java.io.*;
import java.util.*;
import mysd.*;
import mysd.vector.*;

public class BasicWriter implements Writer{

    private PrintStream ps;
    private final SpinSystem sys;
    private final double dt;

    /** 
     * print format in the order of index, Sx, Sy, Sz
     */
    private final String format = "%5d %8.5f %8.5f %8.5f";


    /**
     * default constructor
     *
     * @param sys a SpinSystem object where data will be extracted from
     * @param outFileName a String object for the name of the output file
     */
    public BasicWriter(SpinSystem sys, String outFileName){
        this.ps = null;
        try{
            this.ps = new PrintStream(outFileName);
        }
        catch(IOException ex){
            System.out.println("Cannot create output file " + outFileName +"!");
            System.exit(1);
        }
        this.sys = sys;
        this.dt = sys.getDt();
    }


    public void writeToScreen(){
        
        System.out.println(String.format("# time step : %6.3f", this.sys.getTime()));
        int index = 1;
        for ( FullSpinSite s : sys.getSites() ){
            Vector3D sV = s.getSpinVector();
            double sx = sV.getX();
            double sy = sV.getY();
            double sz = sV.getZ();
            if (s.getIndex() == -1){
                System.out.println(String.format(format, index++, sx, sy, sz));
            }
            else{
                System.out.println(String.format(format, s.getIndex(), sx, sy, sz)); 
                index++;
            }
        }
        System.out.println("\n");
    }


    /**
     * writes current spin configurations to the specified file.
     *
     * @param ps a PrintStream object where output will be recorded
     */
    public void writeToFile(){
        writeToFile(this.ps);
    }

    /**
     * writes current spin configurations to the specified file.
     */
    public void writeToFile(PrintStream ps){

        ps.println(String.format("# time step : %6.3f", this.sys.getTime()));
        int index = 1;
        for ( FullSpinSite s : sys.getSites() ){
            Vector3D sV = s.getSpinVector();
            double sx = sV.getX();
            double sy = sV.getY();
            double sz = sV.getZ();
            if (s.getIndex() == -1){
                ps.println(String.format(format, index++, sx, sy, sz));
            }
            else{
                ps.println(String.format(format, s.getIndex(), sx, sy, sz)); 
                index++;
            }
        }
        ps.println("\n");
    }

    /**
     * closes necessary fields (e.g. PrintStream)
     * This method must be called at the end of the code where 
     * the Writer object is used. 
     */
    public void close(){
        this.ps.close();
    }    

}
