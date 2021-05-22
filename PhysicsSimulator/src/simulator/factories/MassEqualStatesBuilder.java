package simulator.factories;

import org.json.JSONObject;
import simulator.control.MassEqualStates;
import simulator.control.StateComparator;

public class MassEqualStatesBuilder extends Builder<StateComparator> {

    public MassEqualStatesBuilder(){
        super("masseq", " ");
    }

    @Override
    public StateComparator createT(JSONObject o) {
        return new MassEqualStates();
    }
}
