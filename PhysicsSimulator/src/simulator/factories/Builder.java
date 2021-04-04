package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Builder<T>{

    protected String type;
    protected String desc;

    public Builder(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public double [] getVector(JSONArray a){
        if(a.length() == 2) {
            double[] v = new double[a.length()];
            for (int i = 0; i < a.length(); i++) {
                v[i] = a.getDouble(i);
            }
            return v;
        }
        else return null;
    }

    public T createInstance(JSONObject info){
        T a = null;
        if(type != null && type.equals(info.getString("type")))
            a = createT(info.getJSONObject("data"));
        return a;
    }

    public JSONObject getBuilderInfo(){
        JSONObject info = new JSONObject();
        info.put("type", type);
        info.put("desc", desc);
        info.put("data",createData());
        return info;
    }

    protected JSONObject createData(){
        return new JSONObject();
    }

    public abstract T createT(JSONObject o);
}
