package mysd.writer;

import java.io.*;
import java.util.*;
import mysd.*;
import mysd.vector.*;

public class BasicWriter<T extends Site<?>> implements Writer{

    protected PrintWriter pw;
    protected final SpinSystem<T> sys;
    protected final double dt;

    /** 
     * print format in the order of index, Sx, Sy, Sz
     */
    private final String format = "%8d %10.5f %10.5f %10.5f\n";


    /**
     * default constructor
     *
     * @param sys a SpinSystem<T> object where data will be extracted from
     * @param outFileName a String object for the name of the output file
     */
    public BasicWriter(SpinSystem<T> sys, String outFileName){
        this.pw = null;
        File file = null; 
        try{
            file = new File(outFileName);
            this.pw = new PrintWriter(file);
        }
        catch(IOException ex){
            System.out.println("Cannot create output file " + outFileName +"!");
            System.exit(1);
        }
        this.sys = sys;
        this.dt = sys.getDt();
    }


    public void writeToScreen(){
        System.out.print( getStringRep() );    
    }


    public void writeMessageToScreen(String msg){
        System.out.print(msg);
    }

    /**
     * writes current spin configurations to the specified file.
     *
     * @param pw a PrintWriter object where output will be recorded
     */
    public void writeToFile(){
        writeToFile(this.pw);
    }

    /**
     * writes current spin configurations to the specified file.
     */
    public void writeToFile(PrintWriter pw){

        pw.print( getStringRep() );
    }


    /**
     * writes spin configurations to the specified
     * output file path single time
     *
     * @param filename String object representing a desired file name of the output
     */
    public void writeToFile(String filename){

        File file = null;
        PrintWriter tmpPW = null;
        try{
            file = new File(filename);
            tmpPW = new PrintWriter(file);
            writeToFile(tmpPW);
        }
        catch(IOException ex){
            System.out.println("Cannot create output file " + filename +"!");
            System.exit(1);
        }
        finally{
            tmpPW.close();
        }
    }

    public String getStringRep(){

        StringBuffer buff = new StringBuffer();
        buff.append(String.format("# time = %14.4f\n", this.sys.getTime()));
        for ( T s : sys ){
            Vector3D sV = s.getSpinVector();
            double sx = sV.getX();
            double sy = sV.getY();
            double sz = sV.getZ();
            buff.append(String.format(format, s.getIndex(), sx, sy, sz)); 
        }
        buff.append("\n");
        return buff.toString();
    }

    public void writeMessageToFile(String msg){
        pw.print(msg);
    }

    /**
     * closes necessary fields (e.g. PrintWriter)
     * This method must be called at the end of the code where 
     * the Writer object is used. 
     */
    public void close(){
        this.pw.close();
    }    

}
