import java.util.*;
import java.io.*;

import mysd.*;
import mysd.vector.*;

public class ComputeSpinWave{
    public static void main(String[] args){

        HashMap<String, String> messages = ArgumentParser.parse(args);
        String topFile = messages.get("t");
        String kxRaw = messages.get("kx");
        String kyRaw = messages.get("ky");
        String kzRaw = messages.get("kz");
        String input = messages.get("i"); // file name without file extension
                                          // need both input.trj and input.info
        String infoPath = input + ".info";
        String trjPath = input + ".trj";
        String outFile = messages.get("o");
        String nkRaw = messages.get("nk");
        String dwRaw = messages.get("dw");
        String nwRaw = messages.get("nw");
        int nthreads = Runtime.getRuntime().availableProcessors();
        if ( messages.get("nt") != null){
            nthreads = Integer.parseInt(messages.get("nt"));
        }
        Topology top = new Topology(topFile);
        List<FullSpinSite> sites = buildSites(top, infoPath);

        HashMap<String, String> runParams = getRunParams(infoPath);
        int ntstep = Integer.parseInt(runParams.get("ntstep"));
        int nstout = Integer.parseInt(runParams.get("nstout"));
        double dt = Double.parseDouble(runParams.get("dt"));

        if ( nstout == 0 ){
            System.err.println("nstout must be greater than 0 in order to compute spin-waves!");
            System.exit(99);
        }
        int nt = (int)(ntstep/nstout);
        int numSites = top.getNumSites();
        int nk = Integer.parseInt(nkRaw);
        double kx = Double.parseDouble(kxRaw);
        double ky = Double.parseDouble(kyRaw);
        double kz = Double.parseDouble(kzRaw);
        double[][][] spinkt = kFourierTransform(nk, kx, ky, kz, nt, trjPath, sites, top); 
        int nw = Integer.parseInt(nwRaw);
        double dw = Double.parseDouble(dwRaw);

        double[][][] spinkw = wFourierTransform(spinkt, nw, dw, nt, dt, nstout);
        writeToFile(spinkw, outFile, dw);


    }


    public static void writeToFile(double[][][] spinkw, String outputFilePath, double dw){

        PrintStream ps = null;
        try{    
            ps = new PrintStream(outputFilePath);   
         
            int m = spinkw.length;
            int nk = spinkw[0].length - 1; 
            int nw = spinkw[0][0].length - 1;
            for ( int ik = 0; ik < nk + 1; ik++){
                for ( int iw = 0; iw < nw + 1; iw++){
                    double k = (double)ik/nk;
                    double w = iw*dw;
                    double value = 0.0;
                    for ( int im = 0; im < m; im++){
                        value += Math.abs(spinkw[im][ik][iw]);
                    } 
                    String line = String.format("%8.4f  %8.4f  %5.3e", k, w, value);
                    ps.println(line);            
                }
                ps.print("\n");            
            }
        }
        catch ( IOException ex){
            System.err.println("Error! cannot write to file " + outputFilePath);
            System.exit(99);
        }
        finally{
            ps.close();
        }
    }

    public static Vector3D[] getReciprocalLatticeVectors(Topology top){

        Vector3D ax = top.getAx();
        Vector3D ay = top.getAy();
        Vector3D az = top.getAz();

        Vector3D axay = Vector3D.cross( ax, ay);
        Vector3D ayaz = Vector3D.cross( ay, az);
        Vector3D azax = Vector3D.cross( az, ax);
        Vector3D bx = Vector3D.times( ayaz,  2.0*Math.PI/Vector3D.dot( ax, ayaz ) );
        Vector3D by = Vector3D.times( azax,  2.0*Math.PI/Vector3D.dot( ay, azax ) );
        Vector3D bz = Vector3D.times( axay,  2.0*Math.PI/Vector3D.dot( az, axay ) );
        Vector3D[] recLatticeVectors = {bx, by, bz};
        return recLatticeVectors;
    }


    public static double[][][] wFourierTransform(double[][][] spinkt, 
                    int nw, double dw, int nt, double dt, int nstout){

        int m = spinkt.length;
        int nk = spinkt[0].length;

        double[][][] spinkw = new double[m][nk+1][nw+1];
        for ( double[][] each_k : spinkw ) {
            for ( double[] each_w : each_k) Arrays.fill(each_w, 0.0);
        }

        for ( int im = 0; im < m; im++){
            for ( int ik = 0; ik < nk; ik++){
                for ( int iw = 0; iw < nw; iw++){
                    double w = dw*iw;
                    for ( int it = 0; it < nt; it++){
                        double t = dt*it*nstout;
                        spinkw[im][ik][iw] += spinkt[im][ik][it]*Math.cos( w*t ); 
                    }
                }        
            }
        }
        return spinkw;
    }


    public static double[][][] kFourierTransform(int nk, double kx, double ky, double kz, int nt, 
                                  String pathToTrjFile, List<FullSpinSite> sites, Topology top){

        int m = top.getBasis().size();
        double[][][] spinkt = new double[m][nk+1][nt+1];
        for ( double[][] each_k : spinkt ) {
            for ( double[] each_t : each_k) Arrays.fill(each_t, 0.0);
        }
        Vector3D[] b = getReciprocalLatticeVectors(top);
        Vector3D bx = b[0];
        Vector3D by = b[1];
        Vector3D bz = b[2];
        Scanner sc = null;
        try{
            File trjfile = new File(pathToTrjFile);
            sc = new Scanner(trjfile);       
            int t = -1;
            Vector3D spin = new Vector3D();
            while ( sc.hasNext() ){
                String line = sc.nextLine().trim();
                if ( !line.equals("") ){
                    String[] tokens = line.split("\\s+"); 
                    if ( tokens[0].equals("#") ) t++;
                    else{
                        int index = Integer.parseInt(tokens[0]);
                        int baseType = sites.get(index).getBaseType();
                        double sx = Double.parseDouble(tokens[1]);
                        double sy = Double.parseDouble(tokens[2]);
                        spin.copy(sx, sy, 0.0); 
                        for ( int ik = 0; ik < nk + 1; ik++){
                            double coeff = ((double)ik)/nk;
                            Vector3D k = new Vector3D();
                            k.add( Vector3D.times(bx, coeff*kx) ).add( Vector3D.times(by, coeff*ky) ).
                              add( Vector3D.times(bz, coeff*kz));
                            Vector3D r = sites.get(index).getLocation();
                            spinkt[baseType][ik][t] += spin.getX()*Math.cos( Vector3D.dot( k, r ) );
                        } 
                    }
                }
            }
            assert t == nt + 1; 
        }
        catch (IOException ex){

        }
        finally{
            sc.close();
        }
        return spinkt;
    }


    public static HashMap<String, String> getRunParams(String infoFilePath){

        HashMap<String, String> runParams = new HashMap<String, String>();
        
        Scanner sc = null;
        try{
            File file = new File(infoFilePath);
            sc = new Scanner(file);
            while ( sc.hasNext() ){
                String line = sc.nextLine().trim();
                if ( line.equals("[ simulation parameters ]")){
                    while ( !line.equals("[ lattice sites ]") ){
                        line = sc.nextLine().trim();
                        String[] tokens = line.split("=");
                        if ( tokens.length == 2 ){
                            runParams.put( tokens[0].trim(), tokens[1].trim());
                        }
                    }
                }
            }
        }
        catch ( ArrayIndexOutOfBoundsException ex){
            System.err.println("Error! invalid info file!");
            System.exit(99);
        }
        catch ( IOException ex){
            System.err.println("Error! cannot read info file");
            System.exit(99);
        }
        finally{
            sc.close();
        } 
        return runParams;
    }


    public static List<FullSpinSite> buildSites(Topology top, String infoFilePath){

        List<FullSpinSite> sites = new ArrayList<FullSpinSite>();
        int numSites = top.getNumSites();
        Scanner sc = null;
        try{
            File file = new File(infoFilePath);
            sc = new Scanner(file);
            while ( sc.hasNext() ){
                String line = sc.nextLine().trim();
                if ( line.equals("[ lattice sites ]")){
                    for ( int n = 0; n < numSites; n++){
                        line = sc.nextLine().trim();
                        String[] tokens = line.split("\\s+");
                        int index = Integer.parseInt(tokens[0]);
                        int baseType = Integer.parseInt(tokens[1]);
                        double x = Double.parseDouble(tokens[2]);
                        double y = Double.parseDouble(tokens[3]);
                        double z = Double.parseDouble(tokens[4]);
                        FullSpinSite site = new FullSpinSite(index, baseType, new Vector3D(x, y, z));
                        sites.add(site); 
                    } 

                    if ( sc.hasNext() && !sc.nextLine().trim().equals("") ){
                        System.err.println("top file and info file do not match!");
                        System.exit(99);
                    }
                }
            }
        }
        catch ( ArrayIndexOutOfBoundsException ex){
            System.err.println("Error! invalid info file!");
            System.exit(99);
        }
        catch ( IOException ex){
            System.err.println("Error! cannot read info file");
            System.exit(99);
        }
        finally{
            sc.close();
        } 
        return sites;  
    }

}
