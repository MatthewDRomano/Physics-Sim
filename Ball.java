import javax.swing.*;
import java.awt.*;

public class Ball extends JPanel {
    private final int MASS;
    private final int RADIUS;

    public Vector acceleration;
    public Vector velocity;
    public Point position;
    public Point[] boundaryPoints = new Point[16]; // points on perimeter of ball

    public Ball() {
        int x = PhysicsWrld.gui.getWidth();
        int y = PhysicsWrld.gui.getHeight();

        MASS = 5;
        RADIUS = 2*MASS;

        acceleration = new Vector(0, PhysicsWrld.GRAVITY);
        velocity = new Vector((int)(Math.random()*100), (int)(Math.random()*100));
        position = new Point((int)(Math.random()*x-100), (int)(Math.random()*y-100));

        initializeBoundaryPoints();
        setOpaque(false);
        setBounds((int)(Math.random()*x), (int)(Math.random()*y), RADIUS*2, RADIUS*2);
    }
    public Ball(int m, int xPos, int yPos, int xV, int yV) { //scale forces to mass (friction air res)
        MASS = m;
        RADIUS = 2*MASS;//fix (better scale logarithmic)
 
        acceleration = new Vector(0, PhysicsWrld.GRAVITY);
        velocity = new Vector(xV, yV);
        position = new Point(xPos, yPos);

        initializeBoundaryPoints();
        setOpaque(false);
        setBounds(xPos, yPos, RADIUS*2, RADIUS*2);
    }
    public void initializeBoundaryPoints() {
        double incriment = (2*Math.PI)/boundaryPoints.length;
        for (int i = 0; i < boundaryPoints.length; i++)
            boundaryPoints[i] = new Point((int)Math.cos(i*incriment), (int)Math.sin(i*incriment));
    }
    public Point[] getBoundaryPoints() { return boundaryPoints; }
    public int getRadius() { return RADIUS; }
    public int getMass() { return MASS; }

    public void move() { // projectile motion
        //Vector airRes = new Vector(-(PhysicsWrld.AIR_RESISTENCE_CONSTANT*velocity.X)/MASS, -(PhysicsWrld.AIR_RESISTENCE_CONSTANT*velocity.Y)/MASS);
        //if (Math.abs(airRes.X) > Math.abs(acceleration.X)) airRes.X = -(acceleration.X); //terminal V
        //if (Math.abs(airRes.Y) > Math.abs(acceleration.Y)) airRes.Y = -(acceleration.Y); //terminal V
        //acceleration = Vector.vecSum(acceleration, airRes);
        velocity = Vector.vecSum(velocity, acceleration);
        position = (new Point((int)(position.getX() + velocity.X), (int)(position.getY() + velocity.Y)));

        setLocation(position);
    }
    public static void checkBallCollision(Ball b1, Ball b2) {
        //collide(b1, b2);
        for (Point p : b1.getBoundaryPoints()) {
            if (p.x > b2.getLocation().getX() && p.x < b2.getLocation().getX() + 2*b2.getRadius()
            && p.y > b2.getLocation().getY() && p.y < b2.getLocation().getY() + 2*b2.getRadius())
                collide(b1, b2, p);
        }
    }
    private static void collide(Ball b1, Ball b2, Point collisionSpot) //calculate point where balls hit
    {
        //j = impulse
        double jScalar =  (b1.getMass() * b2.getMass() / (b1.getMass() + b2.getMass())) * (1 + PhysicsWrld.COEFFICIENT_OF_RESTITUTION);
        Vector temp = Vector.vecSum(b2.velocity, (new Vector(b1.velocity.X *-1, b1.velocity.Y *-1)));
        Vector jVector = new Vector(temp.X * jScalar, temp.Y * jScalar);

        double xCollsionDistance = collisionSpot.x - (b1.getLocation().x + b1.RADIUS);
        double yCollsionDistance = -(collisionSpot.y - (b1.getLocation().y + b1.RADIUS));
        double angle = Math.atan2(yCollsionDistance, xCollsionDistance);
        
        Vector normalVect = new Vector(Math.cos(angle), Math.sin(angle));
        //angle perpendicular to the angle which the balls hit eachother at

        double Jn = Vector.dotProduct(jVector, normalVect);

        Vector DeltaVf1 = new Vector(normalVect.X * (Jn/b1.getMass()), normalVect.Y * (Jn/b1.getMass()));
        Vector DeltaVf2 = new Vector(normalVect.X * (-(Jn)/b2.getMass()), normalVect.Y * (-(Jn)/b2.getMass()));

        Vector v1F = Vector.vecSum(DeltaVf1, b1.velocity);
        Vector v2F = Vector.vecSum(DeltaVf2, b2.velocity);

        b1.velocity = v1F;
        b2.velocity = v2F;
        //deltaV + b.V = vf
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.cyan);

        g2.fillOval(0, 0, g2.getClipBounds().width, g2.getClipBounds().height);
    }
}