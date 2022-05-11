import processing.core.PApplet;
import processing.core.PVector;

import static java.lang.Math.*;

public class Circle {
    PVector position;
    float r;
    PApplet pap;

    Circle(PApplet sketch, float x_, float y_, float r_) {
        position = new PVector(x_, y_);
        r = r_;
        pap = sketch;
    }

    boolean contains(Particle p) {
        return (PVector.dist(p.position, new PVector(position.x, position.y)) < r);
    }

    boolean intersects(Rectangle b) {
        float dx = position.x - max(b.position.x - b.size.x, min(position.x, b.position.x + b.size.x));
        float dy = position.y - max(b.position.y - b.size.y, min(position.y, b.position.y + b.size.y));
        return (sqrt(dx * dx + dy * dy) < r);
    }

    void show() {
        pap.push();
        pap.noFill();
        pap.stroke(0, 255, 0);
        pap.circle(position.x, position.y, r * 2);
        pap.pop();
    }
}