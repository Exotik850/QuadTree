import processing.core.PApplet;
import processing.core.PVector;

import static processing.core.PConstants.CENTER;

public class Rectangle {
    PVector position;
    PVector size;
    PApplet pap;
    Rectangle(PApplet sketch, float x_, float y_, float w_, float h_) {
        position = new PVector(x_, y_);
        size = new PVector(w_, h_);
        pap = sketch;
    }
    boolean contains(Particle p) {
        return (((p.position.x < position.x + size.x) && (p.position.x > position.x - size.x)) && ((p.position.y < position.y + size.y) && (p.position.y > position.y - size.y)));
    }

    boolean intersects(Rectangle b) {
        return ((this.position.x - this.size.x < b.position.x + b.size.x) &&
                (this.position.x + this.size.x > b.position.x - b.size.x) &&
                (this.position.y - this.size.y < b.position.y + b.size.y) &&
                (this.position.y + this.size.y > b.position.y - b.size.y));
    }

    void show() {
        pap.push();
        pap.noFill();
        pap.stroke(240);
        pap.strokeWeight(2);
        pap.rectMode(CENTER);
        pap.rect(position.x, position.y, size.x * 2, size.y * 2);
        pap.pop();
    }
}