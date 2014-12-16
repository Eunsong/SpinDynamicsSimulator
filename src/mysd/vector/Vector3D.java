package mysd.vector;

public class Vector3D{

    private double x,y,z;

    public static Vector3D create(){
        return new Vector3D();
    }
    public static Vector3D create(Vector3D v){
        return new Vector3D(v);
    }

    public Vector3D() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(Vector3D vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public double getZ(){
        return this.z;
    }

    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }
    public void setZ(double z){
        this.z = z;
    }


    public double norm() {
        return Math.sqrt( this.x*this.x + this.y*this.y + this.z*this.z );
    }

    public double normsq() {
        return this.x*this.x + this.y*this.y + this.z*this.z;
    }

    public void normalize(){
        double factor = 1.0/norm();
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
    }

    public void copySet(Vector3D vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }
    public void copySet(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void subSet(Vector3D vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
    }

    public void addSet(double x, double y, double z){
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void addSet(Vector3D vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
    }

    public void addSet(Vector3D vec1, Vector3D vec2){
        this.x += (vec1.x + vec2.x );
        this.y += (vec1.y + vec2.y );
        this.z += (vec1.z + vec2.z );
    }

    public void addSet(Vector3D vec1, Vector3D vec2, Vector3D vec3){
        this.x += (vec1.x + vec2.x + vec3.x );
        this.y += (vec1.y + vec2.y + vec3.y );
        this.z += (vec1.z + vec2.z + vec3.z );
    }

    public void crossSet(Vector3D vec){
        double newx = this.y*vec.z - this.z*vec.y;
        double newy = -this.x*vec.z + this.z*vec.x;
        double newz = this.x*vec.y - this.y*vec.x;
        this.x = newx;
        this.y = newy;
        this.z = newz;
    }


    public Vector3D copy(Vector3D vec) {
        copySet(vec);
        return this;
    }
    public Vector3D copy(double x, double y, double z){
        copySet(x, y, z);
        return this;
    }

    public Vector3D sub(Vector3D vec) {
        subSet(vec);
        return this;
    }

    public Vector3D add(Vector3D vec) {
        addSet(vec);
        return this;
    }

    public Vector3D add(Vector3D vec1, Vector3D vec2){
        addSet(vec1, vec2);
        return this;
    }

    public Vector3D add(Vector3D vec1, Vector3D vec2, Vector3D vec3){
        addSet(vec1, vec2, vec3);
        return this;
    }

    public Vector3D cross(Vector3D vec){
        crossSet(vec);
        return this;
    }



    public void timesSet(double factor){
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
    }

    public void timesSet(int factor){
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
    }


    public Vector3D times(double factor){
        timesSet(factor);
        return this;
    }

    public Vector3D times(int factor){
        timesSet(factor);
        return this;
    }

    public void reset(){
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    /**
     * done method is used to finalize successive operations 
     * e.g. vector1.add(someVector).sub(anotherVcetor).done()
     */
    public void done(){
        // do nothing   
    }   

    public void minImage(Vector3D box){
        if ( this.x > box.x/2.0 ) this.x -= box.x;
        else if ( this.x < -box.x/2.0 ) this.x += box.x;

        if ( this.y > box.y/2.0 ) this.y -= box.y;
        else if ( this.y < -box.y/2.0 ) this.y += box.y;

        if ( this.z > box.z/2.0 ) this.z -= box.z;
        else if ( this.z < -box.z/2.0 ) this.z += box.z;
    }

    public void print(){
        System.out.println(this.x + ", " + this.y + ", " + this.z);
    }

    /******** Static methods ********/

    public static double norm(Vector3D vec) {
        return Math.sqrt( vec.x*vec.x + vec.y*vec.y + vec.z*vec.z );
    }

    public static Vector3D diff(Vector3D v1, Vector3D v2) {
        return new Vector3D( v1.x - v2.x, v1.y - v2.y, v1.z - v2.z );
    }

    public static Vector3D sum(Vector3D v1, Vector3D v2) {
        return new Vector3D( v1.x + v2.x, v1.y + v2.y, v1.z + v2.z );
    }

    // three vector addition
    public static Vector3D sum(Vector3D v1, Vector3D v2, Vector3D v3) {
        return new Vector3D( v1.x + v2.x + v3.x, v1.y + v2.y + v3.y, v1.z + v2.z + v3.z);

    }

    public static double dot(Vector3D v1, Vector3D v2) {
        return (v1.x*v2.x + v1.y*v2.y + v1.z*v2.z);
    }

    public static Vector3D cross(Vector3D v1, Vector3D v2) {
        return new Vector3D( v1.y*v2.z - v1.z*v2.y, -v1.x*v2.z + v1.z*v2.x, v1.x*v2.y - v1.y*v2.x );
    }

    public static Vector3D times(double factor, Vector3D v) {
        Vector3D tmp = new Vector3D( v.x*factor, v.y*factor, v.z*factor );
        return tmp;
    }
    public static Vector3D times(Vector3D v, double factor) {
        Vector3D tmp = new Vector3D( v.x*factor, v.y*factor, v.z*factor );
        return tmp;
    }

    public static Vector3D times(int factor, Vector3D v) {
        Vector3D tmp = new Vector3D( v.x*factor, v.y*factor, v.z*factor );
        return tmp;
    }
    public static Vector3D times(Vector3D v, int factor){
        Vector3D tmp = new Vector3D( v.x*factor, v.y*factor, v.z*factor );
        return tmp;
    }




}
