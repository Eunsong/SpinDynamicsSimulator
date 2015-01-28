import org.junit.* ;
import static org.junit.Assert.* ;
import mysd.*;
import mysd.builder.*;
import java.util.*;
import java.io.*;
import java.lang.Double;

public class BuilderTest{

    @Test
    public void test_buildSites(){
        
        System.out.println("Test if buildSites() works properly...");      
        System.out.println("testing sample1,top...");
        File top = new File("sample1.top");
        List<FullSpinSite> sites = Builder.buildSites(top);
        assertTrue(sites.size() == 10*200*4 );
        assertTrue(sites.get(2).getLocation().getY() == -1.0);
        assertTrue((new Double(sites.get(2).getSpinVector().getX())).equals(0.0001));
        int index = 0;
        for ( FullSpinSite s : sites ){
            assertTrue( s.getBaseType() == index%4 );
            assertTrue( s.getIndex() == index++ );
        }
    }


}
