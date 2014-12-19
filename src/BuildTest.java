import java.io.*;
import mysd.builder.*;
import java.util.*;

public class BuildTest{
    public static void main(String[] args){

        File file = new File("topol.top");
        Builder.build(file);
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
