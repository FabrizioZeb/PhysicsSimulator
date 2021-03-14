package simulator.model;

import simulator.misc.Vector2D;

import java.util.List;

public class MovingTowardsFixedPoint implements ForceLaws {

    public MovingTowardsFixedPoint() {
        super();
    }

    @Override
    public void apply(List<Body> bs) {
        for (Body b : bs) {
            Vector2D a;
            if(b.getMass() == 0) a = new Vector2D();
            else {
                a = b.getPos().direction().scale(-9.81);
                a = a.scale(b.getMass());
                b.addForce(a);
            }
        }
    }

    public String toString() {
        return "";
    }


}
