package mysd;

import java.util.*;

public class NeighborList<T extends Site> implements Iterable<T>{

    private List<T> sites;
    private final int rank; // 1 if nearest neighbors, 2 if second nearest neighbors, etc 
    
    public NeighborList(){
        this.sites = new ArrayList<T>();
        this.rank = -1; // rank not used
    }   
    
    public Iterator<T> iterator(){
        return this.sites.iterator();
    }
 

}
