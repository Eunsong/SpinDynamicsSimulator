package mysd;

import mysd.lattice.LatticeSite;
import mysd.vector.*;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;


public class Topology{

    private static final String patternCmt = "([^#]*).*";
    private static final String patternSec = "\\[\\s*([\\S]+)\\s*\\]"; 
    private static final Pattern cmt = Pattern.compile(patternCmt);
    private static final Pattern sec = Pattern.compile(patternSec);

    private final UnitCells unitCells;
    private final LatticeVector latticeVector;
    private final List<LatticeSite> basis;

    public Topology(String topFileName){

        basis = new ArrayList<LatticeSite>();
        latticeVector = new LatticeVector();
        unitCells = new UnitCells();
        HashMap<String, List<String>> buffer = new HashMap<String, List<String>>();
        String[] fields = {"basis", "lattice_vector", "unit_cells"};
        for ( String field : fields ){
            buffer.put(field, new ArrayList<String>());
        }
        Scanner sc = null;
        try{
            File file = new File(topFileName);
            sc = new Scanner(file);
            String field = "none";
            while ( sc.hasNext() ){
                String line = removeComment(sc.nextLine()).trim().toLowerCase();
                Matcher m = sec.matcher(line);
                if ( m.find() ){
                    field = m.group(1).toLowerCase();
                }
                else if ( line.length() != 0 && buffer.get(field) != null  ) {
                    buffer.get(field).add(line);
                }
            }

            for ( String each_line : buffer.get("basis") ){
                String[] tokens = each_line.trim().split("\\s+");
                int index = Integer.parseInt(tokens[0]);
                double x = Double.parseDouble(tokens[1]);
                double y = Double.parseDouble(tokens[2]);
                double z = Double.parseDouble(tokens[3]);
                basis.add( new LatticeSite(index, new Vector3D(x, y, z)));
            }

            int cnt = 0;
            for ( String each_line : buffer.get("lattice_vector") ){
                String[] tokens = each_line.trim().split("\\s+");
                double x = Double.parseDouble(tokens[0]);
                double y = Double.parseDouble(tokens[1]);
                double z = Double.parseDouble(tokens[2]);
                Vector3D v = new Vector3D(x, y, z); 
                if ( cnt == 0 ){
                    latticeVector.ax = v; 
                    cnt++;
                }
                else if ( cnt == 1 ){
                    latticeVector.ay = v;
                    cnt++;
                }
                else if ( cnt == 2 ){
                    latticeVector.az = v;
                    cnt++;
                }
                else{
                    System.err.println("More than three lattice vectors are defined! Aborting the program");
                    System.exit(99);
                }
            }

            String[] tokens = buffer.get("unit_cells").get(0).trim().split("\\s+");
            int nx = Integer.parseInt(tokens[0]);
            int ny = Integer.parseInt(tokens[1]);
            int nz = Integer.parseInt(tokens[2]);
            unitCells.nx = nx;
            unitCells.ny = ny;
            unitCells.nz = nz;            

        }
        catch (IOException ex){
            System.err.println("Error! Cannot open top file!");
            System.exit(99);
        }
//        catch (RuntimeException ex){
//            System.err.println("There is an error in top file. Check the format and try it again!");
//            System.exit(99);
//        }
        finally {
            sc.close();        
        }
    }


    public int getNx(){
        return this.unitCells.nx;
    }

    public int getNy(){
        return this.unitCells.ny;
    }

    public int getNz(){
        return this.unitCells.nz;
    }

    public Vector3D getAx(){
        return this.latticeVector.ax;
    }

    public Vector3D getAy(){
        return this.latticeVector.ay;
    }

    public Vector3D getAz(){
        return this.latticeVector.az;
    }

    public List<LatticeSite> getBasis(){
        return this.basis;
    }

    public int getNumSites(){
        return getNx()*getNy()*getNz()*basis.size();
    }

    public static class UnitCells{
        private int nx, ny, nz;
    }


    public static class LatticeVector{
        private Vector3D ax, ay, az;
    }



    public static String removeComment(String s){
        if (!s.contains("#")){
            return s;
        }
        Matcher m = cmt.matcher(s);
        m.find();
        return m.group(1);
    }


}
