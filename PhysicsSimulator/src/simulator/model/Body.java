package simulator.model;

import org.json.JSONObject;
import simulator.misc.Vector2D;

public class Body {

    protected String id;
    protected Vector2D velocity;
    protected Vector2D force;
    protected Vector2D pos;
    protected double mass;

    public Body(String id, Vector2D velocity, Vector2D pos, double mass) {
        this.id = id;
        this.velocity = velocity;
        this.force = new Vector2D();
        this.pos = pos;
        this.mass = mass;
    }

    public String getId() {
        return id;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public Vector2D getForce() {
        return force;
    }

    public Vector2D getPos() {
        return pos;
    }

    public double getMass() {
        return mass;
    }

    void addForce(Vector2D f) {
        double x = force.getX() + f.getX();
        double y = force.getY() + f.getY();
        force = new Vector2D(x,y);
        //this.force.plus(f);
    }

    void resetForce() {
        this.force = new Vector2D();
    }

    void move(double t) {
        Vector2D a;
        if (mass != 0) {
            a = force.scale(1 / mass); // a = f/m
            pos = pos.plus(velocity.scale(t));
            pos = pos.plus(a.scale((t * t) / 2)); // p = p + v*t + 1/2*a*t^2
            velocity = velocity.plus(a.scale(t)); // v = v + a*t
        } else a = new Vector2D(0, 0);
    }

    public boolean equals(Object o) {
        return this == o;
    }

    //Ver que hace JSON y como leer datos desde el.
    public JSONObject getState() {
        JSONObject a = new JSONObject();
        a.put("id", id);
        a.put("p", pos);
        a.put("v", velocity);
        a.put("f",force);
        a.put("m", mass);
        return a;
    }

    public String toString() {
        return getState().toString();
    }
}
