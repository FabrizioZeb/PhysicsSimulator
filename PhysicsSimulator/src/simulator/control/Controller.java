package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.PhysicsSimulator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class Controller {

    private PhysicsSimulator Sim;
    private Factory<Body> bodyFactory;

    public Controller(PhysicsSimulator Sim, Factory<Body> bodyFactory) {
        this.Sim = Sim;
        this.bodyFactory = bodyFactory;
    }

    public void loadBodies(InputStream in) {
        JSONObject jsonInput = new JSONObject(new JSONTokener(in));
        JSONArray bodies = jsonInput.getJSONArray("bodies");
        for (int i = 0; i < bodies.length(); i++) {
            Sim.addBody(bodyFactory.createInstance(bodies.getJSONObject(i)));
        }
    }

    public void run(int n, OutputStream out, InputStream expOut, StateComparator cmp) throws IOException,ExceptionState {
    	boolean notEq = false;
    	String st = null, str = null;
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
        		st = "Estados diferentes: ";
        		st += Arrayjo.getJSONObject(0).toString() + " y " + Sim.toString() + " y ";
        		st += "Paso: 0\n";
        	}
        }
        p.print("\n,");
        for (int i = 0; i < n; i++) {
            Sim.advance();
            p.print(Sim.toString());

            if(cmp != null && expOut != null) {
                
                if(!cmp.equal(Arrayjo.getJSONObject(i) ,Sim.getState())) {
                	notEq = true;
                    str = "Estados diferentes: ";
                    str += Arrayjo.getJSONObject(i).toString() + " y " + Sim.toString() + " y ";
                    str += "Paso: " + i + "\n";
                    System.out.println(str);
                }
            }

            if (i != n - 1) p.print("\n,");
        }
        p.print("\n]");
        p.print("\n}");
        p.close();

        if (notEq) {
        	st += str;
        	throw new ExceptionState(st);
        }


    }


}
