package mysd;

public class NeighborList{

    private List<Site> sites;
    private final int rank; // 1 if nearest neighbors, 2 if second nearest neighbors, etc 
    
    public NeighborList(int rank){
        this.rank = rank;
    }

}
