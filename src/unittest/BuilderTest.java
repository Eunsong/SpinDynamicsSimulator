import org.junit.* ;
import static org.junit.Assert.* ;
import mysd.*;
import mysd.builder.*;
import mysd.exceptions.*;
import java.util.*;
import java.io.*;
import java.lang.Double;
import mysd.RunParameter.SimulationType;

public class BuilderTest{

    @Test
    public void test_buildSites_test1(){
        
        System.out.println("Test if buildSites() works properly...");      
        System.out.println("testing test1.top...");
        File top = new File("test1.top");
        List<FullSpinSite> sites = null;
        try{
            sites = Builder.buildSites(top);
        }
        catch( InvalidTopFileException ex){
            assertTrue(false);
        }
        assertTrue(sites.size() == 10*200*4 );
        assertTrue(sites.get(2).getLocation().getY() == -1.0);
        assertTrue((new Double(sites.get(2).getSpinVector().getX())).equals(0.0001));
        int index = 0;
        for ( FullSpinSite s : sites ){
            assertTrue( s.getBaseType() == index%4 );
            assertTrue( s.getIndex() == index++ );
        }
    }


    @Test
    public void test_buildSites_test2(){
        
        System.out.println("testing test2.top...");
        File top = new File("test2.top");
        List<FullSpinSite> sites = null;
        boolean caughtException = false;
        try{
            sites = Builder.buildSites(top);
        }
        catch( InvalidTopFileException ex){
            caughtException = true;
        }
        assertTrue(caughtException);
    }

    @Test
    public void test_buildSites_test3(){
        
        System.out.println("testing test3.top...");
        File top = new File("test3.top");
        List<FullSpinSite> sites = null;
        boolean caughtException = false;
        try{
            sites = Builder.buildSites(top);
        }
        catch( InvalidTopFileException ex){
            caughtException = true;
        }
        assertTrue(caughtException);
    }


    @Test
    public void test_buildSites_test4(){
        
        System.out.println("testing test4.top...");
        File top = new File("test4.top");
        List<FullSpinSite> sites = null;
        boolean caughtException = false;
        try{
            sites = Builder.buildSites(top);
        }
        catch( InvalidTopFileException ex){
            caughtException = true;
        }
        assertTrue(caughtException);
    }



    @Test
    public void test_importRunParam_test1(){
        
        System.out.println("Test if importRunParam() works properly...");      
        System.out.println("testing test1.sdp...");
        File sdp = new File("test1.sdp");
        RunParameter param = null;
        try{
            param = Builder.importRunParam(sdp);
        }
        catch( InvalidSdpFileException ex){
            assertTrue(false);
        }
        assertTrue( param.runtype == SimulationType.LINEAR );
        assertTrue( param.dt == 0.02 );
        assertTrue( param.ntstep == 1000000 );
        assertTrue( param.alpha == 0.02 );
        assertTrue( param.nstout == 5 );
        assertTrue( param.nstenergy == 0 );
        assertTrue( param.perturb_site == true );
        assertTrue( param.perturbing_site_index == 21 );
        assertTrue( param.perturbation_size == 1.0 );
    }


    @Test
    public void test_importRunParam_test2(){
        
        System.out.println("testing test2.sdp...");
        File sdp = new File("test2.sdp");
        RunParameter param = null;
        boolean caughtException = false;
        try{
            param = Builder.importRunParam(sdp);
        }
        catch( InvalidSdpFileException ex){
            caughtException = true;
        }
        assertTrue(caughtException);
    }

}
