package mysd;

import java.util.HashMap;
import java.lang.ArrayIndexOutOfBoundsException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class ArgumentParser{

    public static HashMap<String, String> parse(String[] args){

        HashMap<String, String> messages = new HashMap<String, String>();
        for ( int i = 0; i < args.length - 1; i++){
            if (args[i].substring(0,1).equals("-")) {
                messages.put( args[i].substring(1,2), args[i+1] ); 
            }
        }
        return messages;   
    }

}
