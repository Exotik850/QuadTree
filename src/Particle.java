import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Particle {
    PVector position;
    private PVector velocity;
    private PVector acceleration;
    private float mass;
    private float radius;
    PApplet parent;
    QuadTree quadTree;

    public Particle(PApplet sketch, QuadTree qt, float x, float y, float mass, float radius) {
        this.position = new PVector(x, y);
        float velocity = (float) (Math.random() * 2 - 1);
        this.velocity = new PVector(velocity, velocity);
        this.acceleration = new PVector(0, 0);
        this.mass = mass;
//        this.radius = radius;
        this.radius = (float) (Math.random() * radius);
        this.parent = sketch;
        this.quadTree = qt;
    }

    public boolean does_collide(Particle other) {
        PVector distance = PVector.sub(position, other.position);
        float distanceMagnitude = distance.mag();
        if (distanceMagnitude < radius + other.radius) {
            return true;
        }
        PVector relativeVelocity = PVector.sub(velocity, other.velocity);
        float a = PVector.dot(relativeVelocity, relativeVelocity);
        float b = PVector.dot(distance, relativeVelocity);
        if (b > 0) {
            return false;
        }
        float c = distanceMagnitude * distanceMagnitude - (radius + other.radius) * (radius + other.radius);
        if (c < 0) {
            return false;
        }
        return true;
    }

    public ArrayList does_collide(ArrayList<Particle> particles) {
        ArrayList<Particle> colliding = new ArrayList<Particle>();
        for (Particle other : particles) {
            if (does_collide(other)) {
                colliding.add(other);
            }
        }
        return colliding;
    }

    public void update() {
        acceleration.mult(1 / mass);
        velocity.add(acceleration);
        position.add(velocity);
        acceleration.mult(0);
        // Make sure the particle stays in the window
        if (position.x < 1) {
            position.x = 1;
            velocity.x *= -1;
        }
        if (position.x > parent.width - 1) {
            position.x = parent.width - 1;
            velocity.x *= -1;
        }
        if (position.y < 1) {
            position.y = 1;
            velocity.y *= -1;
        }
        if (position.y > parent.height - 1) {
            position.y = parent.height - 1;
            velocity.y *= -1;
        }
        // Collision detection and response
        Circle q = new Circle(parent, position.x, position.y, radius);
        for(Particle p : quadTree.query(q)) {
            if(p != this) {
                if(does_collide(p)) {
                    PVector collisionNormal = PVector.sub(position, p.position);
                    collisionNormal.normalize();
                    PVector relativeVelocity = PVector.sub(velocity, p.velocity);
                    float j = -(1 + 0.5f) * PVector.dot(relativeVelocity, collisionNormal);
                    j /= (1 / mass + 1 / p.mass);
                    PVector impulse = PVector.mult(collisionNormal, j);
                    acceleration.add(PVector.mult(impulse, 1 / mass));
                    p.acceleration.add(PVector.mult(impulse, 1 / p.mass));
                }
            }
        }
    }


    public void show() {
        parent.fill(255, 0, 0);
        parent.circle(position.x, position.y, radius);
    }
}
