package mysd.writer;

import java.util.*;
import java.io.PrintStream;

public interface Writer{

    /**
     * writes current spin configurations to stdout.
     **/
    public void writeToScreen();

    /**
     * writes specified String message to stdout.
     */
    public void writeMessageToScreen(String msg);

    /**
     * writes current spin configurations to the specified file.
     *
     * @param ps a PrintStream object where output will be recorded 
     */
    public void writeToFile(PrintStream ps);

    /**
     * writes current spin configurations to the default file.
     */
    public void writeToFile();


    /**
     * writes specified String message to the defulat file.
     */
    public void writeMessageToFile(String msg);

    /**
     * closes necessary fields (e.g. PrintStream)
     * This method must be called at the end of the code where
     * the Writer object is used.
     */
    public void close();

}
