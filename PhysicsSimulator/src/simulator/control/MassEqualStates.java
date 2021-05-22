package simulator.control;
import org.json.JSONArray;
import org.json.JSONObject;

public class MassEqualStates implements StateComparator{

    public MassEqualStates(){
    }

    @Override
    public boolean equal(JSONObject s1, JSONObject s2) {
        boolean equals = true;
        if(s1.getInt("time") == s2.getInt("time")) {
            JSONArray bodies1 = s1.getJSONArray("bodies");
            JSONArray bodies2 = s2.getJSONArray("bodies");
            if(bodies1.length() == bodies2.length()) {
                int i = 0;
                while (i < bodies1.length() && equals) {
                    double m1 = bodies1.getJSONObject(i).getDouble("m");
                    String id1 = bodies1.getJSONObject(i).getString("id");
                    double m2 = bodies2.getJSONObject(i).getDouble("m");
                    String id2 = bodies2.getJSONObject(i).getString("id");
                    if (m1 != m2 || !id1.equals(id2)) equals = false;
                    i++;
                }
            }
            else return false;
        }
        else return equals = false;
        return equals;
    }
}