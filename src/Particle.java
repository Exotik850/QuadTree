import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Particle {
    PVector position;
    PVector velocity;
    private final PVector acceleration;
    private final float mass;
    float radius;
    PApplet parent;

    public Particle(PApplet sketch, float x, float y, float mass, float radius) {
        this.position = new PVector(x, y);
        float velocity = (float) (Math.random() * 2 - 1);
        this.velocity = new PVector(velocity, velocity);
        this.acceleration = new PVector(0, 0);
        this.mass = mass;
//        this.radius = radius;
        this.radius = (float) (Math.random() * radius);
        this.parent = sketch;
    }

    public boolean does_collide(Particle other) {
        float distance = PVector.dist(this.position, other.position);
        return distance < this.radius + other.radius;
    }

    public float time_to_collision(Particle other) {
        PVector relative_position = PVector.sub(this.position, other.position);
        PVector relative_velocity = PVector.sub(this.velocity, other.velocity);
        float distance = relative_position.dot(relative_position);
        if (distance < 0) {
            return 0;
        }
        float b = relative_velocity.dot(relative_position);
        if (b >= 0) {
            return -1.0f;
        }
        float a = relative_velocity.dot(relative_velocity);
        float d = b * b - a * distance;
        if (d < 0) {
            return -1.0f;
        }
        return (-b - (float) Math.sqrt(d)) / a;
    }

    public boolean does_collide_in_timestep(Particle other, float timestep) {
        float time_to_collision = this.time_to_collision(other);
        if (time_to_collision < 0) {
            return false;
        }
        return !(time_to_collision > timestep);
    }

    public void update() {
        acceleration.mult(1 / mass);
        velocity.add(acceleration);
        // Optional: limit the velocity to a maximum speed
        velocity.limit(5);
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

    }

    public void check_colliding(ArrayList<Particle> particles, float deltaT) {
        for(Particle p : particles) {
            if(p != this) {
                if(does_collide_in_timestep(p, deltaT)) {
                    PVector collisionNormal = PVector.sub(position, p.position);
                    collisionNormal.normalize();
                    float v1_normal = PVector.dot(collisionNormal, velocity);
                    float v2_normal = PVector.dot(collisionNormal, p.velocity);
                    float p_value = (2 * (v1_normal - v2_normal)) / (mass + p.mass);
                    PVector final_v1 = PVector.sub(velocity, PVector.mult(collisionNormal, p_value * mass));
                    PVector final_v2 = PVector.add(p.velocity, PVector.mult(collisionNormal, p_value * p.mass));
                    velocity = final_v1;
                    p.velocity = final_v2;
                    parent.fill(0, 255, 0);
//                    position.add(PVector.mult(collisionNormal, radius));
//                    p.position.add(PVector.mult(collisionNormal, p.radius));
                }
            }
        }
    }

    public PVector get_momentum() {
        return PVector.mult(velocity, mass);
    }



    public void show() {
        parent.circle(position.x, position.y, radius);
        parent.fill(255, 0, 0);
    }
}
