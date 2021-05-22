package simulator.factories;

import org.json.JSONObject;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {

    public MovingTowardsFixedPointBuilder() {
        super("mtfp", "c: the point towards wich bodies move\n" +
                "(a json list of 2 numbers, e.g., [100.0, 50.0])\n" +
                "g: the length of the acceleration vector (a number)" ,"Moving towards a fixed point");
    }

    @Override
    public ForceLaws createT(JSONObject o) {
        return new MovingTowardsFixedPoint();
    }
}
