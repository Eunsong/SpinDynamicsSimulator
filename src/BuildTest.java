import java.io.*;
import mysd.builder.*;
import java.util.*;

import mysd.*;

public class BuildTest{
    public static void main(String[] args){

        File file = new File("topol.top");
        List<FullSpinSite> sites = Builder.buildSites(file);

/*
        HashMap<String, List<String>> t = Builder.load(file);


        for ( String section : t.keySet() ){
            System.out.println(section);
            for ( String each_line : t.get(section) ){
                System.out.println(each_line);
            }
        }
*/
    }
}
