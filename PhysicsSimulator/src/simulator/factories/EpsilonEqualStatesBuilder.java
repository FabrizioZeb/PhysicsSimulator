package simulator.factories;

import org.json.JSONObject;
import simulator.control.EpsilonEqualStates;
import simulator.control.StateComparator;

public class EpsilonEqualStatesBuilder extends Builder<StateComparator> {

    public EpsilonEqualStatesBuilder(){
        super("epseq", " ");
    }

    @Override
    public StateComparator createT(JSONObject o) {
        return new EpsilonEqualStates(o.getDouble("eps"));
    }
}
