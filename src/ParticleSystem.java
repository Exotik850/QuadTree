import processing.core.PApplet;

import java.util.ArrayList;

public class ParticleSystem {
    ArrayList<Particle> particles;
    QuadTree quadTree;
    PApplet parent;
    float width;
    float height;
    float deltaT;

    public ParticleSystem(PApplet parent, float width, float height) {
        this.parent = parent;
        this.width = width;
        this.height = height;
        deltaT = 1.0f / parent.frameRate;
        particles = new ArrayList<>();
        quadTree = new QuadTree(parent, new Rectangle(parent, 0, 0, width, height), 8);
    }

    public void add(Particle p) {
        particles.add(p);
        quadTree.insert(p);
    }

    public void update() {
        for (Particle p : particles) {
            p.update();
        }
        quadTree.insert(particles);
        for (Particle p : particles) {
//            p.check_colliding(get_colliding(p));
            p.check_colliding(get_colliding(p), deltaT);
        }
        quadTree.clear();
    }

    public void show() {
        for (Particle p : particles) {
            p.show();
        }
    }

    public ArrayList<Particle> get_colliding(Particle p) {
        ArrayList<Particle> colliding;
        int signx = (p.velocity.x < 0) ? 1 : -1;
        int signy = (p.velocity.y < 0) ? 1 : -1;
        Rectangle r = new Rectangle(parent, p.position.x - p.radius * signx, p.position.y - p.radius * signy, p.velocity.y + p.radius * signx, p.velocity.x + p.radius * signy);
        r.show();
        colliding = quadTree.query(r);
        return colliding;
    }
}
