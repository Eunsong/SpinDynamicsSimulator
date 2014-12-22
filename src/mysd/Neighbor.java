package mysd;

public class Neighbor<T extends Site<?>>{

    private final T site;
    private final Hamiltonian<T> hamiltonian;

    public Neighbor(T s, Hamiltonian<T> h){
        this.site = s;
        this.hamiltonian = h;
    }
    public T getSite(){
        return this.site;
    }
    public Hamiltonian<T> getHamiltonian(){
        return this.hamiltonian;
    }

}
