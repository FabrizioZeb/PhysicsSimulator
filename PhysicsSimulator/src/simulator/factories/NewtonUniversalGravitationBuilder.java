package simulator.factories;

import org.json.JSONObject;
import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws> {
    public NewtonUniversalGravitationBuilder() {
        super("nlug", "");
    }

    @Override
    public ForceLaws createT(JSONObject o) {
        return new NewtonUniversalGravitation();
    }
}
