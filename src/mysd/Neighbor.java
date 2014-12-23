package mysd;

public class Neighbor<T extends Site<?>>{

    private final T site;
    private final Interaction<T> hamiltonian;

    public Neighbor(T s, Interaction<T> h){
        this.site = s;
        this.hamiltonian = h;
    }
    public T getSite(){
        return this.site;
    }
    public Interaction<T> getHamiltonian(){
        return this.hamiltonian;
    }

}
