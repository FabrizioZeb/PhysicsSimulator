package simulator.factories;

import org.json.JSONObject;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {

    public MovingTowardsFixedPointBuilder() {
        super("mtcp", "");
    }

    @Override
    public ForceLaws createT(JSONObject o) {
        return new MovingTowardsFixedPoint();
    }
}
