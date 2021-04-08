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
            if(bodies1.similar(s2)) {
                int i = 0, j = 0;
                while (i < bodies1.length() && equals) {
                    JSONObject m1 = bodies1.getJSONObject(i).getJSONObject("m");
                    JSONObject id1 = bodies1.getJSONObject(i).getJSONObject("id");
                    while (j < bodies2.length() && equals) {
                        JSONObject m2 = bodies2.getJSONObject(j).getJSONObject("m");
                        JSONObject id2 = bodies2.getJSONObject(j).getJSONObject("id");
                        if (!m1.equals(m2) || !id1.equals(id2)) equals = false;
                        j++;
                    }
                    i++;
                }
            }
            else equals = false;
        }
        else return equals = false;
		return equals;
    }
}
