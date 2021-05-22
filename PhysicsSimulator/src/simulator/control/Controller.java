package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public class Controller {

    private PhysicsSimulator Sim;
    private Factory<Body> bodyFactory;
    private Factory<ForceLaws> forceLawsFactory;

    public Controller(PhysicsSimulator Sim, Factory<Body> bodyFactory, Factory<ForceLaws> forceLawsFactory) {
        this.Sim = Sim;
        this.bodyFactory = bodyFactory;
        this.forceLawsFactory = forceLawsFactory;
    }

    public void loadBodies(InputStream in) {
        JSONObject jsonInput = new JSONObject(new JSONTokener(in));
        JSONArray bodies = jsonInput.getJSONArray("bodies");
        for (int i = 0; i < bodies.length(); i++) {
            Sim.addBody(bodyFactory.createInstance(bodies.getJSONObject(i)));
        }
    }

    public void reset(){
        Sim.reset();
    }

    public void setDeltaTime(double dt){
        Sim.setDeltaTime(dt);
    }

    public void addObserver(SimulatorObserver o){
        Sim.addObserver(o);
    }

    public void run(int n) throws IOException, ExceptionState{
        for(int i = 0; i < n; i++)
            Sim.advance();
/*        OutputStream o = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        };
        _run(n,o, null,null);*/
    }

    public void _run(int n, OutputStream out, InputStream expOut, StateComparator cmp) throws IOException,ExceptionState {
        boolean notEq = false;
        PrintStream p = new PrintStream(out);
        JSONObject jo = null;
        JSONArray Arrayjo = null;
        if(expOut != null) {
            jo = new JSONObject(new JSONTokener(expOut));
            Arrayjo = jo.getJSONArray("states");

        }
        //Si n es 0 al menos recorre el estado 0.
        p.print("{\n");
        p.print("\"states\": [\n");
        p.print(Sim.toString());
        if(cmp != null && expOut != null) {
            if(!cmp.equal(Arrayjo.getJSONObject(0), Sim.getState())) {
                notEq=true;
                String st = "\nEstados diferentes:\n ";
                st += Arrayjo.getJSONObject(0).toString() + " y \n" + Sim.toString() + "\n";
                st += "Step: 0\n";
                throw new ExceptionState(st);
            }
        }
        p.print("\n,");
        for (int i = 1; i <= n; i++) {
            Sim.advance();
            p.print(Sim.toString());
            if(cmp != null && expOut != null) {

                if(!cmp.equal(Arrayjo.getJSONObject(i) ,Sim.getState())) {
                    notEq = true;
                    String str = "\nEstados diferentes:\n";
                    str += Arrayjo.getJSONObject(i).toString() + " y \n" + Sim.toString() + "\n";
                    str += "Step: " + i + "\n";
                    throw new ExceptionState(str);
                }
            }
            if (i != n) p.print("\n,");
        }
        p.print("\n]");
        p.print("\n}");
        p.close();


    }

    public List<JSONObject> getForceLawsInfo(){
        return forceLawsFactory.getInfo();
    }

    public void setForceLaws(JSONObject info){
        ForceLaws forceLaws = forceLawsFactory.createInstance(info);
        Sim.setForceLaws(forceLaws);
    }

}