package mysd.writer;

import mysd.*;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;


public class BufferedBasicWriter<T extends Site<?>> extends BasicWriter<T>{

    private int count;
    private final int nstbuff;

    /**
     * default constructor using buffered PrintWriter.
     *
     * @param sys a SpinSystem<T> object where data will be extracted from
     * @param outFileName a String object for the name of the output file
     * @param nstbuff size of buffer before flushing the output
     */
    public BufferedBasicWriter(SpinSystem<T> sys, String outFileName, int nstbuff){
        super(sys, outFileName);
        pw.close();
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(outFileName)));
        }
        catch ( IOException ex){
            System.err.println("Error! cannot open output file " + outFileName);
            System.exit(99);
        }
        this.count = 0;
        this.nstbuff = nstbuff;
    }

    @Override
    public void writeToFile(){
        super.writeToFile();
        this.count++;
        if ( this.count > this.nstbuff ){
            pw.flush();
            this.count = 0;
        }
    }

    @Override
    public void close(){
        pw.flush();
        super.close();
    }

}
