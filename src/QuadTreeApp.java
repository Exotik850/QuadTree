import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Random;



public class QuadTreeApp extends PApplet {
    QuadTree qt;
    ArrayList<Particle> bs = new ArrayList<Particle>();
    Random r = new Random();
    int num = 1000;
    int frames = 480;

    public void settings() {
        size(800, 800);
    }

    public void setup(){
        background(0);
        Rectangle b = new Rectangle(this,0, 0, width, height);
        qt = new QuadTree(this, b, 8);
//        m = new Rectangle(this, mouseX, mouseY, 50, 50);
        for(int i = 0; i < num; i++){
            float x = map((float) r.nextDouble(), 0, 1, 0, width);
            float y = map((float) r.nextDouble(), 0, 1, 0, height);
            Particle point = new Particle(this, qt, x, y, 10, 5);
//            float ang = map((float) r.nextDouble(), 0, 1, 0, TWO_PI);
//            float mag = map((float) r.nextDouble(), 0, 1, 0, width / 2);
//            PVector point = new PVector(mag * cos(ang) + width / 2, mag * sin(ang) + height / 2);
            bs.add(point);
            qt.insert(point);
            fill(240);
            noStroke();
            circle(point.position.x, point.position.y, 4);
        }
//        qt.show();
//        qt.print(0);
//        qt.serialize("quadtree.txt");
    }

    public void draw(){
        float start = millis();
        background(0);
//        qt.show();
        qt.update_points();
        qt.show_points();
        System.out.println(millis() - start);
    }

    public static void main(String[] args) {
        PApplet.main("QuadTreeApp");
    }


    float normalize(float value, float min, float max) {
        return (value - min) / (max - min);
    }
}
