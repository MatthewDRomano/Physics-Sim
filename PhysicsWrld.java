import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;

public class PhysicsWrld {
    static DragListener drag;
    static JFrame gui;
    static JPanel screen;
    static ArrayList<Ball> balls;
    static final double GRAVITY = 0.5; //locked
    static final double COEFFICIENT_OF_RESTITUTION = 0.725; //locked
    static final double COEFFICIENT_OF_GROUND_FRICTION = 0.1; //0.10-0.15
    //static final double AIR_RESISTENCE_CONSTANT = 0.00005; //FIX
    public static void main(String[] args) {
        instantiateSim();
        drag = new DragListener();

        //balls.add(new Ball(5, 450,250, 40, 20));
        //balls.add(new Ball(30, 400,350, 30, -20));

        //balls.get(1).addMouseListener(drag);//
        //balls.get(1).addMouseMotionListener(drag);//
        //balls.add(new Ball(140, 10,280, 10000, 10000));
        //screen.add(balls.get(0));
        //screen.add(balls.get(1));
        for (int i = 0; i < 30; i ++) {
            balls.add(new Ball());
            screen.add(balls.get(i));
        }

        //screen.add(balls.get(2));

        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                for (Ball b : balls) {

                    b.move();
                    checkWallCollisions(b);
                    checkGroundFriction(b);
                    Ball.checkBallCollision(balls.get(0), balls.get(1));
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
        if (Math.abs(b.velocity.Y) < 0.02*b.getMass() && b.getLocation().getY() > 730) {
            b.acceleration.Y = 0;
            b.velocity.Y = 0;
            b.position = new Point(b.getX(), screen.getHeight() - b.getHeight() - 1);
            b.setLocation(b.position);

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
    
    
    static class DragListener extends MouseInputAdapter  {
        Point location;
        MouseEvent pressed, released;

        public void mousePressed(MouseEvent me)
        {
            Ball ball = (Ball)me.getComponent();
            location = ball.getLocation(location);
            pressed = me;

            location = ball.getLocation(location);
        }
        public void mouseReleased(MouseEvent me)
        {       
            Ball ball = (Ball)me.getComponent(); 
            ball.acceleration = new Vector(0, GRAVITY);
        }

        public void mouseDragged(MouseEvent me)
        {
            if (!SwingUtilities.isLeftMouseButton(me)) return; 
            Ball ball = (Ball)me.getComponent();
            (ball.getParent()).setComponentZOrder(ball, 0);
            location = ball.getLocation(location);
            int x = location.x - pressed.getX() + me.getX();
            int y = (location.y - pressed.getY() + me.getY());   
            // DeltaX/DeltaY = mouse Final pos - init.
            // V = dX / dT (timer for t)

            // F for drag is cont ---> a is f/m
            // V = integral a dT (timer for t)
            
            ball.position = new Point(x, y); 
            ball.velocity = new Vector(0, 0);
            ball.acceleration = new Vector(0, 0);
            ball.setLocation(x, y);
        }
    }
}