package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;

public class EpsilonEqualStates implements StateComparator {

    private double E;

    public EpsilonEqualStates(double eps){
        this.E = eps;
    }

    @Override
    public boolean equal(JSONObject s1, JSONObject s2) {
        boolean equals = true;
        if(s1.get("time").equals(s2.get("time"))) {
            JSONArray bodies1 = s1.getJSONArray("bodies");
            JSONArray bodies2 = s2.getJSONArray("bodies");
            if (bodies1.similar(bodies2)) {
                int i = 0, j = 0;
                while (i < bodies1.length()) {

                    String id1 = bodies1.getJSONObject(i).getString("id");

                    double m1 = bodies1.getJSONObject(i).getDouble("m");
                    JSONArray p1 = bodies1.getJSONObject(i).getJSONArray("p");
                    double p1_x = p1.getDouble(0);
                    double p1_y = p1.getDouble(1);
                    Vector2D _p1 = new Vector2D(p1_x,p1_y);
                    JSONArray v1 = bodies1.getJSONObject(i).getJSONArray("v");
                    double v1_x = v1.getDouble(0);
                    double v1_y = v1.getDouble(1);
                    Vector2D _v1 = new Vector2D(v1_x,v1_y);
                    JSONArray f1 = bodies1.getJSONObject(i).getJSONArray("f");
                    double f1_x = f1.getDouble(0);
                    double f1_y = f1.getDouble(1);
                    Vector2D _f1 = new Vector2D(f1_x,f1_y);
                    while (j < bodies2.length()) {
                    	equals = true;
                        String id2 = bodies2.getJSONObject(j).getString("id");
                        if(id1.equals(id2)) {
	                        double m2 = bodies2.getJSONObject(i).getDouble("m");
	                        JSONArray p2 = bodies2.getJSONObject(j).getJSONArray("p");
	                        double p2_x = p2.getDouble(0);
	                        double p2_y = p2.getDouble(1);
	                        Vector2D _p2 = new Vector2D(p2_x,p1_y);
	                        JSONArray v2 = bodies2.getJSONObject(j).getJSONArray("v");
	                        double v2_x = v2.getDouble(0);
	                        double v2_y = v2.getDouble(1);
	                        Vector2D _v2 = new Vector2D(v2_x,v2_y);
	                        JSONArray f2 = bodies2.getJSONObject(j).getJSONArray("f");
	                        double f2_x = f2.getDouble(0);
	                        double f2_y = f2.getDouble(1);
	                        Vector2D _f2 = new Vector2D(f2_x,f2_y);
	                        if (Math.abs(m1 - m2) > E || _p1.distanceTo(_p2) > E || _v1.distanceTo(_v2) > E || _f1.distanceTo(_f2) > E)
	                            equals = false;
                        }
                        j++;
                    }
                    i++;

                }
            }
            else equals = false;
        }
        else equals = false;
        return equals;
    }
}
