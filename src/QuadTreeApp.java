import processing.core.PApplet;

import java.util.Random;

public class QuadTreeApp extends PApplet {
    ParticleSystem ps;
    Random r = new Random();
    int num = 1000;
    int frames = 480;

    public void settings() {
        size(600, 600);
    }

    public void setup() {
        background(0);
        Rectangle b = new Rectangle(this, 0, 0, width, height);
        ps = new ParticleSystem(this, width, height);
        for (int i = 0; i < num; i++) {
            float x = map((float) r.nextDouble(), 0, 1, 0, width);
            float y = map((float) r.nextDouble(), 0, 1, 0, height);
            Particle point = new Particle(this, x, y, 10, 5);
//            float ang = map((float) r.nextDouble(), 0, 1, 0, TWO_PI);
//            float mag = map((float) r.nextDouble(), 0, 1, 0, width / 2);
//            PVector point = new PVector(mag * cos(ang) + width / 2, mag * sin(ang) + height / 2);
            ps.add(point);
            fill(240);
            noStroke();
            circle(point.position.x, point.position.y, 4);
        }
    }

    public void draw() {
        float start = millis();
        background(0);
//        qt.show();
        ps.update();
        ps.show();
        System.out.println("Frame time: " + (millis() - start));
    }

    public static void main(String[] args) {
        PApplet.main("QuadTreeApp");
    }
}
