package mysd.builder;

import java.io.*;
import java.util.*;
import java.lang.NumberFormatException;
import java.lang.ArrayIndexOutOfBoundsException;

import mysd.*;
import mysd.vector.*;

public class SpinBuilder{

    /**
     * overloads spinVector components of each site in system from confFile.
     * 
     * @param sites List object containing to be updated sites
     * @param confFile File object containing spin configurations(*.cnf file)
     * @param normalize boolean variable, true if each spinVector object needs to be 
     *                  normalized, false otherwise. 
     */ 
    public static <T extends Site<?>> void overloadSpins(List<T> sites, File confFile, boolean normalize){

        if ( !confFile.exists() || !confFile.canRead() ){
            System.err.println("Error: Cannot access cnf file");
            System.exit(99);
        }
        try{
            Scanner sc = new Scanner(confFile);
            while ( sc.hasNext() ){
                String line = Builder.removeComment(sc.nextLine()).trim();
                if ( !line.trim().matches("[^\\s]+.*") ){
                    // empty line
                }
                else{ 
                    String[] tokens = line.split("\\s+");
                    int index = Integer.parseInt(tokens[0]);
                    double sx = Double.parseDouble(tokens[1]);
                    double sy = Double.parseDouble(tokens[2]);
                    double sz = Double.parseDouble(tokens[3]); 
                    T s = sites.get(index);
                    assert index == s.getIndex();
                    Vector3D v = new Vector3D(sx, sy, sz);
                    if ( normalize ) v.normalize();
                    s.updateSpinVector(v);
                }
            }
        }
        catch (IOException ex1){
            System.err.println("ERROR! Cannot access cnf file!");
            System.exit(99);
        }
        catch (NumberFormatException ex2){
            System.err.println("ERROR! invalid line found in cnf file!");
            System.exit(99);
        } 
        catch (ArrayIndexOutOfBoundsException ex){
            System.err.println("ERROR! invalid line found in cnf file!");
            System.exit(99);
        }
               
    }

}
