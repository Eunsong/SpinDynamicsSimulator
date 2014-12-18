package mysd.writer;

import java.util.*;
import java.io.PrintStream;

public interface Writer{

    /**
     * writes current spin configurations to stdout.
     **/
    public void writeToScreen();

    /**
     * writes current spin configurations to the specified file.
     *
     * @param ps a PrintStream object where output will be recorded 
     */
    public void writeToFile(PrintStream ps);

    /**
     * writes current spin configurations to a default file.
     */
    public void writeToFile();

    /**
     * closes necessary fields (e.g. PrintStream)
     * This method must be called at the end of the code where
     * the Writer object is used.
     */
    public void close();

}
