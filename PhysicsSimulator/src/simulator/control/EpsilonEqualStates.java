package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;

public class EpsilonEqualStates implements StateComparator {

    private double E;

    public EpsilonEqualStates(double eps) {
        this.E = eps;
    }

    @Override
    public boolean equal(JSONObject s1, JSONObject s2) {
        boolean equals = true;
        if (s1.getInt("time") == s2.getInt("time")) {
            JSONArray bodies1 = s1.getJSONArray("bodies");
            JSONArray bodies2 = s2.getJSONArray("bodies");
            if (bodies1.length() == bodies2.length()) {
                int i = 0;
                while (i < bodies1.length()) {
                    //Body 1
                    String id1 = bodies1.getJSONObject(i).getString("id");
                    double m1 = bodies1.getJSONObject(i).getDouble("m");
                    JSONArray p1 = bodies1.getJSONObject(i).getJSONArray("p");
                    Vector2D _p1 = new Vector2D(p1.getDouble(0), p1.getDouble(1));
                    JSONArray v1 = bodies1.getJSONObject(i).getJSONArray("v");
                    Vector2D _v1 = new Vector2D(v1.getDouble(0), v1.getDouble(1));
                    JSONArray f1 = bodies1.getJSONObject(i).getJSONArray("f");
                    Vector2D _f1 = new Vector2D(f1.getDouble(0), f1.getDouble(1));
                    //Body 2
                    String id2 = bodies2.getJSONObject(i).getString("id");
                    if (id1.equals(id2)) {
                        double m2 = bodies2.getJSONObject(i).getDouble("m");
                        JSONArray p2 = bodies2.getJSONObject(i).getJSONArray("p");
                        Vector2D _p2 = new Vector2D(p2.getDouble(0), p2.getDouble(1));
                        JSONArray v2 = bodies2.getJSONObject(i).getJSONArray("v");
                        Vector2D _v2 = new Vector2D(v2.getDouble(0), v2.getDouble(1));
                        JSONArray f2 = bodies2.getJSONObject(i).getJSONArray("f");
                        Vector2D _f2 = new Vector2D(f2.getDouble(0), f2.getDouble(1));
                        if (Math.abs(m1 - m2) > E || _p1.distanceTo(_p2) > E || _v1.distanceTo(_v2) > E || _f1.distanceTo(_f2) > E)
                            equals = false;
                    } else return false;
                    i++;
                }
            } else equals = false;
        } else equals = false;
        return equals;
    }
}