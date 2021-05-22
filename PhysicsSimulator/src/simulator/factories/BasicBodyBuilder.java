package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body>{

    String id;
    double[] p;
    double[] v;
    double m;

    public BasicBodyBuilder() {
        super("basic", "Basic body");
    }

    @Override
    public Body createT(JSONObject o) {
        String id = o.getString("id");
        this.id = id;
        double[] p = getVector(o.getJSONArray("p"));
        this.p = p;
        double[] v = getVector(o.getJSONArray("v"));
        this.v = v;
        double m = o.getDouble("m");
        this.m = m;
        return new Body(id, new Vector2D(v[0],v[1]), new Vector2D(p[0],p[1]), m);
    }

    @Override
    public JSONObject createData(){
        JSONObject data = new JSONObject();
        data.put("id",id);
        data.put("p", p);
        data.put("v", v);
        data.put("m",m);
        return data;
    }
}
