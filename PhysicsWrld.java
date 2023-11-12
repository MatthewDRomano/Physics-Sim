import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;

public class PhysicsWrld {
    static JFrame gui;
    static JPanel screen;
    static ArrayList<Ball> balls;
    static final double GRAVITY = 0.5; //locked
    static final double COEFFICIENT_OF_RESTITUTION = 0.725; //locked
    static final double COEFFICIENT_OF_GROUND_FRICTION = 0.1; //0.10-0.15
    //static final double AIR_RESISTENCE_CONSTANT = 0.00005; //FIX
    public static void main(String[] args) {
        instantiateSim();

        balls.add(new Ball(5, 450,250, 20, 0));
        balls.add(new Ball(30, 400,350, 30, -20));
        //balls.add(new Ball(140, 10,280, 10000, 10000));
        screen.add(balls.get(0));
        screen.add(balls.get(1));
        //screen.add(balls.get(2));

        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                for (Ball b : balls) {

                    b.move();
                    checkWallCollisions(b);
                    checkGroundFriction(b);
                    b.repaint();

                    System.out.println("Vx: " + b.velocity.X);
                    System.out.println("Vy: " + b.velocity.Y);
                    System.out.println("X: " + b.getLocation());

                }
            }
        };
        
        Timer timer = new Timer(10, taskPerformer);
        timer.start();
    }
    public static void checkWallCollisions(Ball b) {
        if (b.position.getX() <= 0) //checks collision on left wall
            b.velocity.X = Math.abs(b.velocity.X) * (COEFFICIENT_OF_RESTITUTION);

        else if (b.position.getX() >= (screen.getWidth() - b.getWidth())) //checks collision on right wall
            b.velocity.X = Math.abs(b.velocity.X) * -(COEFFICIENT_OF_RESTITUTION);


           
        if (b.position.getY() <= 0) //checks collision on top wall
            b.velocity.Y = Math.abs(b.velocity.Y) * (COEFFICIENT_OF_RESTITUTION);

        else if (b.position.getY() >= (screen.getHeight() - b.getHeight())) //checks collision on bottom wall
            b.velocity.Y = Math.abs(b.velocity.Y) * -(COEFFICIENT_OF_RESTITUTION);
        }
           
    public static void checkGroundFriction(Ball b) {
        //if vert speed is negligable, and ball is on ground, ball stops

        // [0.02*b.getMass()] Accomodates larger balls bouncing more and ignoring default 0.1 benchmark (5 mass standard (reference))
        if (Math.abs(b.velocity.Y) < 0.02*b.getMass() && b.getLocation().getY() > screen.getHeight() - b.getHeight() - 5) {
            b.velocity.Y = 0;
            b.acceleration.Y = 0;
            b.setLocation(b.getX(), screen.getHeight() - b.getHeight() - 1);

            if (Math.abs(b.velocity.X) < 0.1) {
                b.velocity.X = 0;
                b.acceleration.X = 0;
                return;
            }

            double frictionForce = b.getMass() * GRAVITY * COEFFICIENT_OF_GROUND_FRICTION;
            b.acceleration.X = (b.velocity.X > 0) ? -(frictionForce/b.getMass()) : frictionForce/b.getMass(); //may need to update to incorporate air resistence
            //make friction AND OTHER FORCES incorporate mass 
        }
    }
    public static void instantiateSim() {
        gui = new JFrame() {
            {
                setPreferredSize(new Dimension(800, 800));
                setTitle("Physics Wrld");
                setResizable(false);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }        
        };
        screen = new JPanel(){
            {
                setBackground(Color.black);
                setLayout(null);
            }
        };
        gui.setContentPane(screen);
        gui.pack();
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);

        balls = new ArrayList<>();
    }       
}