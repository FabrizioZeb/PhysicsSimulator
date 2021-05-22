package simulator.model;

import simulator.misc.Vector2D;

import java.util.List;

public class MovingTowardsFixedPoint implements ForceLaws {

    public static final double _g = -9.81;
    public static final Vector2D _c = new Vector2D(0,0);

    public MovingTowardsFixedPoint() {
        super();
    }

    @Override
    public void apply(List<Body> bs) {
        for (Body b : bs) {
            Vector2D a = new Vector2D();
            if(b.getMass() == 0) a = new Vector2D();
            else {
                a = b.getPos().direction();
                a = a.scale(_g);
                a = a.scale(b.getMass());
                b.resetForce();
                b.addForce(a);
            }
        }
    }

    public String toString() {
        return "Moving towards " + _c + " with constant acceleration " + _g;
    }


}
