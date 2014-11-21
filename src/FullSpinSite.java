public class FullSpinSite extends Site{

    private Vector3D spin;

    public FullSpinSite(int baseType, Vector3D location){
        super(baseType, location);
        this.spin = new Vector3D();
    }


}
