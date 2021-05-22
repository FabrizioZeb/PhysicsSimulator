package simulator.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;


public class PhysicsSimulator {

    private double dt;
    private double time;
    private ForceLaws forceLaws;
    private ArrayList<Body> bodyList;
    private ArrayList<SimulatorObserver> observerList;

    public PhysicsSimulator(ForceLaws forceLaws, double dt) {
        this.time = 0.0; //Tiempo de la simulación
        this.forceLaws = forceLaws;
        this.bodyList = new ArrayList<Body>();
        this.dt = dt;
        this.observerList = new ArrayList<SimulatorObserver>();

    }

    public void advance() throws IllegalArgumentException {
        forceLaws.apply(bodyList);
        for (Body b : bodyList){
//            b.resetForce();
            if(dt >= 0)
                b.move(dt);
            else throw new IllegalArgumentException();
        }
        this.time += this.dt;
        for(int i = 0; i < observerList.size(); i++){
            observerList.get(i).onAdvance(bodyList,time);
        }
    }

    public void addBody(Body b) throws IllegalArgumentException {
        if(!bodyList.contains(b)) {
            bodyList.add(b);
            for (SimulatorObserver o : this.observerList){
                o.onBodyAdded(bodyList,b);
            }
        }
        else throw new IllegalArgumentException("Already contains that body: " + b);
    }

    public void reset(){
        bodyList.clear();
        this.time = 0;
        for(int i = 0; i < observerList.size(); i++){
            observerList.get(i).onReset(bodyList,time,dt,forceLaws.toString());
        }
    }

    public void setDeltaTime(double dt){
        if(dt > 0)
            this.dt = dt;
        else throw new IllegalArgumentException("Delta Time: " + dt + ", no válido");
        for (int i = 0; i < bodyList.size(); i++){
            observerList.get(i).onDeltaTimeChanged(dt);
        }
    }

    public void setForceLaws(ForceLaws forceLaws){
        if(forceLaws != null)
            this.forceLaws = forceLaws;
        else throw new IllegalArgumentException("ForceLaws tiene valor null");
        for(int i = 0; i < bodyList.size(); i++){
            observerList.get(i).onForceLawsChanged(forceLaws.toString());
        }
    }

    public void addObserver(SimulatorObserver o){
        if (!this.observerList.contains(o)) {
            this.observerList.add(o);
            o.onRegistrer(this.bodyList, this.time, this.dt, this.forceLaws.toString());
        }
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
