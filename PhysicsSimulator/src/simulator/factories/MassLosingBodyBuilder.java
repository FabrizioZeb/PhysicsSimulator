package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MassLossingBody;

public class MassLosingBodyBuilder extends Builder<Body>{

    String id;
    double[] p;
    double[] v;
    double m;
    double freq;
    double factor;

    public MassLosingBodyBuilder() {
        super("mlb", "mass losing body");
    }

    @Override
    public Body createT(JSONObject o) {
        id = o.getString("id");
        this.id = id;
        double[] p = getVector(o.getJSONArray("p"));
        this.p = p;
        double[] v = getVector(o.getJSONArray("v"));
        this.v = v;
        double m = o.getDouble("m");
        this.m = m;
        double freq = o.getDouble("freq");
        this.freq = freq;
        double factor = o.getDouble("factor");
        this.factor = factor;

        return new MassLossingBody(id,new Vector2D(v[0],v[1]),new Vector2D(p[0],p[1]),m,factor,freq);

    }

    @Override
    public JSONObject createData(){
        JSONObject data = new JSONObject();
        data.put("id", id);
        data.put("p",p);
        data.put("v",v);
        data.put("m",m);
        data.put("freq", freq);
        data.put("factor",factor);
        return data;
    }
}
