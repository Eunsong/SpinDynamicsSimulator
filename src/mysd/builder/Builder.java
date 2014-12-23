package mysd.builder;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import mysd.*;
import mysd.lattice.*;
import mysd.vector.*;
import java.lang.RuntimeException;

public class Builder{

    private static final String patternCmt = "([^#]*).*";
    private static final String patternSec = "\\[\\s*([\\S]+)\\s*\\]";
    private static final String patternBondOffset = "<([ijk][\\+-]\\d)>";
    private static final String patternSdp = "([^\\s]+)\\s*=\\s*([^\\s]+)";
    private static final Pattern cmt = Pattern.compile(patternCmt);
    private static final Pattern sec = Pattern.compile(patternSec);
    private static final Pattern offset = Pattern.compile(patternBondOffset);
    private static final Pattern sdp = Pattern.compile(patternSdp);


    public static RunParameter importRunParam(File sdpFile){
        
        if ( !sdpFile.exists() || !sdpFile.canRead() ){
            System.err.println("Error: Cannot access sdp file");
            System.exit(99);
        }
        HashMap<String, String> buffer = new HashMap<String, String>();
        try{
            Scanner sc = new Scanner(sdpFile);
            String field = "";
            String value = "";
            while ( sc.hasNext() ){
                String line = removeComment(sc.nextLine()).trim().toLowerCase();
                Matcher m = sdp.matcher(line);
                if ( m.find() ){
                    field = m.group(1).toLowerCase();
                    value = m.group(2);
                    buffer.put(field, value);
                }
            }
        }
        catch ( IOException ex){
            System.err.println("Error: Cannot read sdp file");
            System.exit(99);
        }

        RunParameter.Builder builder = new RunParameter.Builder();
        String[] fields = {"title", "runtype", "dt", "ntstep", "alpha", "nstout", "nstbuff",
                           "perturb_site"};
        for ( String field : fields ){
            if ( !buffer.containsKey(field) ){
                System.err.println("Error: missing essential field: " + field +
                                    " , in sdp file.");
                System.exit(99); 
            }
        }
        double dt = Double.parseDouble(buffer.get("dt"));
        String runtype = buffer.get("runtype");
        int ntstep = Integer.parseInt(buffer.get("ntstep"));
        double alpha = Double.parseDouble(buffer.get("alpha"));
        int nstout = Integer.parseInt(buffer.get("nstout"));
        int nstbuff = Integer.parseInt(buffer.get("nstbuff"));
        boolean pt_site = new Boolean(buffer.get("perturb_site"));
        int pt_site_index = -1;
        double pt_size = 0.0;
        if ( pt_site ){
            String[] pt_fields = {"perturbing_site_index", "perturbation_size"};
            for ( String field : pt_fields ){
                if ( !buffer.containsKey(field) ){
                    System.err.println("Error: perturbation option is turned on, "+
                                       "but no " + field + " is defined in sdp file");
                    System.exit(99);
                }
            }
            pt_site_index = Integer.parseInt
                                   (buffer.get("perturbing_site_index"));
            pt_size = Double.parseDouble
                                   (buffer.get("perturbation_size"));
        }
        builder.title( buffer.get("title")).runtype(runtype).dt(dt).ntstep(ntstep)
               .alpha(alpha).nstout(nstout).nstbuff(nstbuff).perturb_site(pt_site)
               .perturbing_site_index(pt_site_index).perturbation_size(pt_size);
        return builder.build();
    }


    public static List<FullSpinSite> buildSites(File topfile){

        HashMap<String, List<String>> inputs = parse(topfile);      
        List<LatticeSite> basis = getBasis(inputs);
        HashMap<Integer, Vector3D> spins = getSpins(inputs);
        Vector3D[] a = getLatticeVectors(inputs);
        HashMap<String, Hamiltonian> hamiltonians = getHamiltonians(inputs);
        List<Bond> bonds = getBonds(inputs);
        int[] unitcellSize = getNumberOfUnitCells(inputs);    
        int nx = unitcellSize[0];
        int ny = unitcellSize[1];
        int nz = unitcellSize[2];
        int m = basis.size();
        int numSites = m*nx*ny*nz;

        FullSpinSite[][][][] sitesArray = new FullSpinSite[nx][ny][nz][m];

        for ( int i = 0; i < nx; i++){
            for ( int j = 0; j < ny; j++){
                for ( int k = 0; k < nz; k++){
                    for ( int l = 0; l < m; l++){ 
                        int index = i*ny*nz*m + j*nz*m + k*m + l;
                        Vector3D location = new Vector3D(basis.get(l).getBasePosition());
                        location.add( Vector3D.times( a[0], i ) );
                        location.add( Vector3D.times( a[1], j ) );
                        location.add( Vector3D.times( a[2], k ) );
                        FullSpinSite site = new FullSpinSite(index, l, location);
                        // load spin configurations if specified in [ basis ]
                        if ( spins.containsKey(l) ){
                            site.updateSpinVector( spins.get(l) );
                        }
                        sitesArray[i][j][k][l] = site;
                    }
                }
            }
        }

        /**
         * construct bonds
         */
        for ( int i = 0; i < nx; i++){
            for ( int j = 0; j < ny; j++){
                for ( int k = 0; k < nz; k++){
                    for ( int l = 0; l < m; l++){ 
                        for ( Bond bond : bonds ){
                            if ( bond.i == l ){
                                int xoff = bond.offset.get("i");
                                int yoff = bond.offset.get("j");
                                int zoff = bond.offset.get("k");
                                int i_nb = (i + xoff + nx)%nx;
                                int j_nb = (j + yoff + ny)%ny;
                                int k_nb = (k + zoff + nz)%nz;
                                String type = bond.type;
                                Hamiltonian h = hamiltonians.get(type);
                                if ( h == null ){
                                    System.err.println("Error! undefined "+
                                                       "Hamiltonian type : "+ type);
                                    System.exit(99);
                                } 
                                Neighbor<FullSpinSite> nj = 
                                    new Neighbor<FullSpinSite>
                                        (sitesArray[i_nb][j_nb][k_nb][bond.j], h);
                                sitesArray[i][j][k][l].addNeighbor(nj);
                                // add current site as nb's Neighbor as well
                                Neighbor<FullSpinSite> ni = 
                                    new Neighbor<FullSpinSite>
                                        (sitesArray[i][j][k][l], h);
                                sitesArray[i_nb][j_nb][k_nb][bond.j].addNeighbor(ni);
                            }
                        }
                    }
                }
            }
        }

        List<FullSpinSite> sites = new ArrayList<FullSpinSite>();
        for ( int i = 0; i < nx; i++){
            for ( int j = 0; j < ny; j++){
                for ( int k = 0; k < nz; k++){
                    for ( int l = 0; l < m; l++){ 
                        sites.add(sitesArray[i][j][k][l]);
                    }
                }
            }   
        }
        return sites;
    }


    private static int[] getNumberOfUnitCells(HashMap<String, List<String>> inputs){
        int[] numbers = new int[3];
        int cnt = 0;
        try {
            for ( String line : inputs.get("unit_cells") ){
                String[] tokens = line.trim().split("\\s+");
                if ( !line.trim().matches("[^\\s]+.*") ){
                    // empty line
                }
                else if ( line.trim().matches(
                          "\\d+\\s+\\d+\\s+\\d+")){
                    if ( cnt > 0 ) {
                        System.err.println("Warning! more than one line is "+
                                           "found in [ unit_cells ]. "+
                                           "will use the last line.");
                    }
                    numbers[0] = Integer.parseInt(tokens[0]);
                    numbers[1] = Integer.parseInt(tokens[1]);
                    numbers[2] = Integer.parseInt(tokens[2]);
                    cnt++;
                }
                else{
                    System.err.println("Unrecognizable line found in [ unit_cells ]. "+
                                       "Will ignore this.");
                }
            }
        }
        catch ( RuntimeException ex){
            System.err.println("a line in [ unit_cells ] cannot be properly read. "+
                               "Check the format!"); 
            System.exit(99);
        }
        return numbers;
    }


    private static List<Bond> getBonds(HashMap<String, List<String>> inputs){

        List<Bond> bonds = new LinkedList<Bond>();        
        try {
            for ( String line : inputs.get("bonds") ){

                // pre-process unitcell offset descriptors e.g. <i+1><j-1>
                HashMap<String, Integer> offsets = new HashMap<String, Integer>();
                Matcher m = offset.matcher(line);
                while ( m.find() ){
                    String raw = m.group(1);
                    String s = raw.substring(0,1);
                    int offset;
                    if ( raw.contains("+") ){
                        offset = Integer.parseInt( raw.substring(2,3));
                    }
                    else{
                        offset = Integer.parseInt( raw.substring(1,3));
                    }
                    offsets.put( s, offset );
                }
                line = m.replaceAll("");

                String[] tokens = line.trim().split("\\s+");
                if ( !line.trim().matches("[^\\s]+.*") ){
                    // empty line
                }
                else if ( line.trim().matches(
                          "\\d+\\s+\\d+\\s+\\S+\\s*")){
                    Bond bond = new Bond();
                    bond.i = Integer.parseInt(tokens[0]);
                    bond.j = Integer.parseInt(tokens[1]);
                    bond.type = tokens[2];
                    String[] ijk = {"i", "j", "k"}; 
                    for ( String s: ijk ){
                        if ( offsets.containsKey(s)){
                            bond.offset.put(s, offsets.get(s) );
                        }
                    } 
                    bonds.add(bond);
                }
                else{
                    System.err.println("Unrecognizable line found in [ bonds ]. "+
                                       "Will ignore this.");
                }
            }
        }
        catch ( RuntimeException ex){
            System.err.println("a line in [ bonds ] cannot be properly read. "+
                               "Check the format!"); 
            System.exit(99);
        }
        return bonds;
    }


    private static HashMap<String, Hamiltonian> getHamiltonians
                                                (HashMap<String, List<String>> inputs){
        HashMap<String, Hamiltonian> hamiltonians = 
                                        new HashMap<String, Hamiltonian>();

        try {
            for ( String line : inputs.get("hamiltonian") ){
                String[] tokens = line.trim().split("\\s+");
                if ( !line.trim().matches("[^\\s]+.*") ){
                    // empty line
                }
                else if ( tokens.length == 10 ){
                    String type = tokens[0];
                    double[][] mat = new double[3][3];
                    for ( int i = 0; i < 3; i++){
                        for ( int j = 0; j < 3; j++){
                            mat[i][j] = Double.parseDouble(tokens[i*3+j+1]);
                        }
                    }
                    Hamiltonian h = new Hamiltonian(mat);
                    h.setType(type);
                    hamiltonians.put(type, h);
                }
                else{
                    System.err.println("Unrecognizable line found in [ Hamiltonian ]. "+
                                       "Will ignore this.");
                }
            }
        }
        catch ( RuntimeException ex){
            System.err.println("a line in [ Hamiltonian ] cannot be properly read. "+
                               "Check the format!"); 
            System.exit(99);
        }
        return hamiltonians;
    }




    private static Vector3D[] getLatticeVectors(HashMap<String, List<String>> inputs){
  
        int cnt = 0;
        Vector3D[] a = new Vector3D[3]; 
        try {
            for ( String line : inputs.get("lattice_vector") ){
                String[] tokens = line.trim().split("\\s+");
                if ( !line.trim().matches("[^\\s]+.*")){
                    // empty line
                }
                else if (line.matches("\\s*\\d+\\.?\\d*\\s+\\d+\\.?\\d*\\s+\\d+\\.?\\d*")){
                    double x = Double.parseDouble(tokens[0]);
                    double y = Double.parseDouble(tokens[1]);
                    double z = Double.parseDouble(tokens[2]);
                    a[cnt] = new Vector3D(x,y,z);
                    cnt++; 
                }
                else {
                    System.err.println("Unrecognizable line found in [ lattice_vector ]. "+
                                       "Will ignore this.");
                }
            }
        }
        catch ( RuntimeException ex){
            System.err.println("a line in [ lattice_vector ] cannot be properly read. "+
                               "Check the format!"); 
            System.exit(99);
        }
        return a;
    } 


    private static HashMap<Integer, Vector3D>
                            getSpins(HashMap<String, List<String>> inputs){
        HashMap<Integer, Vector3D> spins = new HashMap<Integer, Vector3D>();
        try {
            for ( String line : inputs.get("basis") ){
                String[] tokens = line.trim().split("\\s+");
                if ( !line.trim().matches("[^\\s]+.*") ){
                    // empty line
                }
                else if ( line.trim().matches(
                          "\\d+\\s+-?\\d+\\.?\\d*\\s+-?\\d+\\.?\\d*\\s+-?\\d+\\.?\\d*\\s+"+
                          "-?\\d+\\.?\\d*\\s+-?\\d+\\.?\\d*\\s+-?\\d+\\.?\\d*")){
                    int index = Integer.parseInt(tokens[0]);
                    double sx = Double.parseDouble(tokens[4]);
                    double sy = Double.parseDouble(tokens[5]);
                    double sz = Double.parseDouble(tokens[6]);
                    spins.put(index, new Vector3D(sx, sy, sz));
                }
                else{
                    // no default spins specified
                }
            }
        }
        catch ( RuntimeException ex){
            System.err.println("a line in [ basis ] cannot be properly read. "+
                               "Check the format!"); 
            System.exit(99);
        }
        return spins; 
    }



    private static List<LatticeSite> getBasis(HashMap<String, List<String>> inputs){

        List<LatticeSite> basis = new LinkedList<LatticeSite>();
        try {
            for ( String line : inputs.get("basis") ){
                String[] tokens = line.trim().split("\\s+");
                if ( !line.trim().matches("[^\\s]+.*") ){
                    // empty line
                }
                else if ( line.trim().matches(
                          "\\d+\\s+-?\\d+\\.?\\d*\\s+-?\\d+\\.?\\d*\\s+-?\\d+\\.?\\d*.*")){
                    int index = Integer.parseInt(tokens[0]);
                    double x = Double.parseDouble(tokens[1]);
                    double y = Double.parseDouble(tokens[2]);
                    double z = Double.parseDouble(tokens[3]);
                    LatticeSite site = new LatticeSite(index, new Vector3D(x,y,z));
                    basis.add(site);
                }
                else{
                    System.err.println("Unrecognizable line found in [ basis ]. "+
                                       "Will ignore this.");
                }
            }
        }
        catch ( RuntimeException ex){
            System.err.println("a line in [ basis ] cannot be properly read. "+
                               "Check the format!"); 
            System.exit(99);
        }
        return basis;
    }

    private static HashMap<String, List<String>> parse(File topfile){

        HashMap<String, List<String>> inputs = new HashMap<String, List<String>>();

        if ( !topfile.exists() || !topfile.canRead() ){
            System.err.println("Error: Cannot access top file");
            System.exit(99);
        }
        try{
            Scanner sc = new Scanner(topfile);
            String section = "";
            while ( sc.hasNext() ){
                String line = removeComment(sc.nextLine()).trim();
                Matcher m = sec.matcher(line);
                if ( m.find() ){
                    section = m.group(1).toLowerCase();
                }
                else{
                    if ( inputs.get(section) == null ){
                        List<String> lines = new LinkedList<String>();
                        inputs.put(section, lines);
                    }
                    List<String> lines = inputs.get(section);
                    lines.add(line);
                } 
            }
        }
        catch (IOException ex){
            System.err.println("Error: Cannot read top file");
            System.exit(99);
        }     
        return inputs; 
    }


    public static class Bond{
        private int i;
        private int j;
        private String type;
        private HashMap<String, Integer> offset = new HashMap<String, Integer>();
        public Bond(){
            offset.put("i", 0);
            offset.put("j", 0);
            offset.put("k", 0);
        }
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
