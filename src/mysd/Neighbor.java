package mysd;

public class Neighbor<T extends Site>{

    private final T site;
    private final Hamiltonian hamiltonian;

    public Neighbor(T s, Hamiltonian h){
        this.site = s;
        this.hamiltonian = h;
    }
    public T getSite(){
        return this.site;
    }
    public Hamiltonian getHamiltonian(){
        return this.hamiltonian;
    }

}
