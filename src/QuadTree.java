import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class QuadTree {
    Rectangle bound;
    int capacity;
    ArrayList<Particle> points;

    QuadTree tl, tr, bl, br;
    boolean div;
    PApplet pap;

    QuadTree(PApplet sketch, Rectangle boun, int capac) {
        this.bound = boun;
        this.capacity = capac;
        this.points = new ArrayList<>();
        this.div = false;
        this.pap = sketch;
    }

    void insert(Particle p) {
        if (!bound.contains(p)) {
            return;
        }
        if (points.size() < capacity) {
            points.add(p);
        } else {
            if (!div) {
                divide();
            }
            tl.insert(p);
            tr.insert(p);
            bl.insert(p);
            br.insert(p);
        }

    }

    void insert(ArrayList<Particle> tp) {
        if (!this.div && this.points.size() + tp.size() < this.capacity) {
            this.points.addAll(tp);
        } else {
            if (!this.div)
                this.divide();
            for (Particle t : tp) {
                this.tl.insert(t);
                this.tr.insert(t);
                this.bl.insert(t);
                this.br.insert(t);
            }
        }


    }

    void divide() {
        Rectangle rtl, rtr, rbl, rbr;
        rtl = new Rectangle(pap, bound.position.x - bound.size.x / 2, bound.position.y - bound.size.y / 2, bound.size.x / 2, bound.size.y / 2);
        rtr = new Rectangle(pap, bound.position.x + bound.size.x / 2, bound.position.y - bound.size.y / 2, bound.size.x / 2, bound.size.y / 2);
        rbl = new Rectangle(pap, bound.position.x - bound.size.x / 2, bound.position.y + bound.size.y / 2, bound.size.x / 2, bound.size.y / 2);
        rbr = new Rectangle(pap, bound.position.x + bound.size.x / 2, bound.position.y + bound.size.y / 2, bound.size.x / 2, bound.size.y / 2);
        this.tl = new QuadTree(pap, rtl, this.capacity);
        this.tr = new QuadTree(pap, rtr, this.capacity);
        this.bl = new QuadTree(pap, rbl, this.capacity);
        this.br = new QuadTree(pap, rbr, this.capacity);
        this.div = true;
    }

    ArrayList<Particle> query(Rectangle inter) {
        ArrayList<Particle> found = new ArrayList<>();

        if (this.bound.intersects(inter)) {
            if (!div) {
                for (Particle p : points) {
                    if (inter.contains(p)) {
                        found.add(p);
                    } else {
                        pap.push();
                        pap.fill(255, 0, 0);
                        pap.circle(p.position.x, p.position.y, 4);
                        pap.pop();
                    }
                }
            } else {
                found.addAll(tl.query(inter));
                found.addAll(tr.query(inter));
                found.addAll(bl.query(inter));
                found.addAll(br.query(inter));
            }
        }
        return found;
    }

    ArrayList<Particle> query(Circle inter) {
        ArrayList<Particle> found = new ArrayList<>();

        if (inter.intersects(this.bound)) {
            if (!div) {
                for (Particle p : points) {
                    if (inter.contains(p)) {
                        pap.push();
                        pap.fill(0, 255, 0);
                        pap.circle(p.position.x, p.position.y, 4);
                        pap.pop();
                        found.add(p);
                    } else {
                        pap.push();
                        pap.fill(255, 0, 0);
                        pap.circle(p.position.x, p.position.y, 4);
                        pap.pop();
                    }
                }
            } else {
                found.addAll(tl.query(inter));
                found.addAll(tr.query(inter));
                found.addAll(bl.query(inter));
                found.addAll(br.query(inter));
            }
        }
        return found;
    }

    void show() {

        bound.show();
        if (this.div) {
            this.tl.show();
            this.tr.show();
            this.bl.show();
            this.br.show();
        }
    }

    void clear() {
        points.clear();
        if (this.div) {
            this.tl.clear();
            this.tr.clear();
            this.bl.clear();
            this.br.clear();
        }
    }


    void getPoints(ArrayList<Particle> points) {
        if (!div) {
            points.addAll(this.points);
        } else {
            this.tl.getPoints(points);
            this.tr.getPoints(points);
            this.bl.getPoints(points);
            this.br.getPoints(points);
        }
    }

    void print(float depth) {

        System.out.println("QuadTree depth: " + depth);
        System.out.println("Capacity: " + this.capacity);
        System.out.println("Bound: " + this.bound);
        if (this.div) {
            this.tl.print(depth + 1);
            this.tr.print(depth + 1);
            this.bl.print(depth + 1);
            this.br.print(depth + 1);
        }
    }


    public void show_points() {
        if (!div) {
            for (Particle p : points) {
                p.show();
            }
        } else {
            this.tl.show_points();
            this.tr.show_points();
            this.bl.show_points();
            this.br.show_points();
        }
    }
}