package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;

public class EpsilonEqualStates implements StateComparator {

    private double E;

    EpsilonEqualStates(double eps){
        this.E = eps;
    }

    @Override
    public boolean equal(JSONObject s1, JSONObject s2) {
        boolean equals = true;
        if(s1.get("time").equals(s2.get("time"))) {
            JSONObject bodies1 = s1.getJSONObject("bodies");
            JSONObject bodies2 = s2.getJSONObject("bodies");
            if (bodies1.similar(bodies2)) {
                int i = 0, j = 0;
                while (i < bodies1.length() && equals) {
                    JSONObject id1 = bodies1.getJSONObject("id");
                    double m1 = bodies1.getDouble("m");
                    JSONArray p1 = bodies1.getJSONArray("p");
//                    JSONObject p1_x = p1.getJSONObject(0);
                    JSONArray v1 = bodies1.getJSONArray("v");
                    JSONArray f1 = bodies1.getJSONArray("f");
                    while (j < bodies2.length() && equals) {
                        JSONObject id2 = bodies1.getJSONObject("id");
                        double m2 = bodies2.getDouble("m");
                        JSONObject p2 = bodies1.getJSONObject("p");
                        JSONObject v2 = bodies1.getJSONObject("v");
                        JSONObject f2 = bodies1.getJSONObject("f");
                        if (Math.abs(m1 - m2) <= E || !id1.equals(id2) || !p1.equals(p2) || !v1.equals(v2) || !f1.equals(f2))
                            equals = false;
                        j++;
                    }
                    i++;

                }
            }
            else equals = false;
        }
        return equals;
    }
}
