import javax.swing.*;
import java.awt.*;

public class Ball extends JPanel {
    private final int MASS;
    private final int RADIUS;

    public Vector acceleration;
    public Vector velocity;
    public Point position;

    public Ball(int m, int xPos, int yPos, int xV, int yV) { //scale forces to mass (friction air res)
        setOpaque(false);
        MASS = m;
        RADIUS = 2*MASS;//fix (better scale logarithmic)

        
        acceleration = new Vector(0, PhysicsWrld.GRAVITY);
        velocity = new Vector(xV, yV);
        position = new Point(xPos, yPos);

        setBounds(xPos, yPos, RADIUS*2, RADIUS*2);
    }
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

    public static void collide(Ball b1, Ball b2) //account for mass
    {
        //m1v1 + m2v2 = m1v1` + m2v2`
        double P1 =  b1.velocity.X * b1.getMass();
        double P2 = b2.velocity.X * b2.getMass();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.cyan);

        g2.fillOval(0, 0, g2.getClipBounds().width, g2.getClipBounds().height);
    }
}