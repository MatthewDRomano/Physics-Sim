public class Vector {
    public double X, Y;
    
    public Vector(double xComp, double yComp) {
        X = xComp;
        Y = yComp;
    }

    public static Vector vecSum(Vector v1, Vector v2) { // returns sum of 2 vectors
        return new Vector(v1.X + v2.X, v1.Y + v2.Y);
    }
    public static double dotProduct(Vector v1, Vector v2) {
        double xProd = v1.X * v2.X;
        double yProd = v1.Y * v2.Y;

        return xProd + yProd;
    }
}
