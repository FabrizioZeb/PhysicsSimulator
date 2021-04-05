package simulator.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhysicsSimulator {

    private double dt;
    private double time;
    private ForceLaws forceLaws;
    private ArrayList<Body> bodyList;

    public PhysicsSimulator(ForceLaws forceLaws, double dt) {
        this.time = 0.0; //Tiempo de la simulación
        this.forceLaws = forceLaws;
        this.bodyList = new ArrayList<Body>();
        this.dt = dt;
    }

    public void advance() throws IllegalArgumentException {
        for (Body b : bodyList){
            b.resetForce();
            forceLaws.apply(bodyList);
            if(dt >= 0)
                b.move(dt);
            else throw new IllegalArgumentException();
        }
        this.time += this.dt;
    }

    public void addBody(Body b) throws IllegalArgumentException {
        if(!bodyList.contains(b)) bodyList.add(b);
        else throw new IllegalArgumentException();
    }

    public JSONObject getState(){
        JSONObject a = new JSONObject();
        JSONArray ar = new JSONArray();
        for(Body b: bodyList){
            ar.put(b.getState());
        }
        a.put("bodies", ar);
        a.put("time", time);
        return a;
    }

    public String toString(){
        return getState().toString();
    }

}
