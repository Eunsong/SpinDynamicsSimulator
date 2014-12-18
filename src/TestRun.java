import java.util.*;
import java.io.PrintStream;

import mysd.*;
import mysd.lattice.*;
import mysd.vector.*;
import mysd.integrator.*;
import mysd.writer.BasicWriter;

public class TestRun{


    public static void main(String[] args){

        LatticeSite base0 = new LatticeSite(new Vector3D(0.0, 0.0, 0.0));
        List<LatticeSite> basis = new ArrayList<LatticeSite>();
        basis.add(base0);
        Vector3D ax = new Vector3D(1.0, 0.0, 0.0);
        Vector3D ay = new Vector3D(0.0, 1.0, 0.0);
        Vector3D az = new Vector3D(0.0, 0.0, 1.0);
        UnitCell unitCell = new UnitCell(ax, ay, az, basis);

    
        double[][] mat = new double[3][3];
        for ( double[] array : mat ){
            Arrays.fill(array, 0.0);
        }
        mat[0][0] = -1.0;
        mat[1][1] = -1.0;
        mat[2][2] = -1.0;
 
        Hamiltonian h = new Hamiltonian(mat);
   
        FullSpinSite[][] sitesArray = new FullSpinSite[20][20];
 
        for ( int i = 0; i < 20; i++){
            for ( int j = 0; j < 20; j++){

                Vector3D location = Vector3D.times( ax, i);
                location.add( Vector3D.times(ay, j) );
                FullSpinSite site = new FullSpinSite(location);
                sitesArray[i][j] = site;
                
            }
        }

        for ( int i = 0; i < 20; i++){
            for ( int j = 0; j < 20; j++){
       
                Neighbor<FullSpinSite> n1 = new Neighbor<FullSpinSite>( sitesArray[ (i+20-1)%20 ][j], h);
                
                Neighbor<FullSpinSite> n2 = new Neighbor<FullSpinSite>( sitesArray[ (i+20+1)%20 ][j], h);
                Neighbor<FullSpinSite> n3 = new Neighbor<FullSpinSite>( sitesArray[i][ (j+20-1)%20 ], h);
                Neighbor<FullSpinSite> n4 = new Neighbor<FullSpinSite>( sitesArray[i][ (j+20+1)%20 ], h);

                sitesArray[i][j].addNeighbor(n1);
                sitesArray[i][j].addNeighbor(n2);
                sitesArray[i][j].addNeighbor(n3);
                sitesArray[i][j].addNeighbor(n4);

            }
        }


        List<FullSpinSite> sites = new ArrayList<FullSpinSite>(400);

        for ( int i = 0; i < 20; i++){
            for ( int j = 0; j < 20; j++){
                sites.add(sitesArray[i][j]);
            }
        }

        NonlinearIntegrator integrator = new NonlinearIntegrator(0.1);
        
        SpinSystem system = new SpinSystem.Builder().sites(sites).integrator(integrator).
                                                     alpha(0.01).build();

        for ( FullSpinSite s : system.getSites() ){
            s.updateSpinVector(new Vector3D(0.0, 0.0, 1.0));
        }
        system.getSites().get(0).updateSpinVector(new Vector3D(1.0, 0.0, 0.0));


        BasicWriter writer = new BasicWriter(system, "test.dat");

        for ( int t = 0; t < 100; t++){
            if (t%10 == 0){
                writer.writeToFile();
            }
            system.updateForce();
            system.forward();
        }


    }


}
