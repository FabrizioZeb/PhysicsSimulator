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
        JSONObject jo = null;
        if(expOut != null) {
            jo = new JSONObject(new JSONTokener(expOut));
        }
        out.write("{\n".getBytes());
        out.write("\"states\": [\n".getBytes());
        out.write(Sim.toString().getBytes());
        out.write("\n,".getBytes());
        for (int i = 0; i < n; i++) {
            Sim.advance();
            out.write(Sim.toString().getBytes());

            if(cmp != null) {
                JSONObject object = new JSONObject(new JSONTokener(String.valueOf(out)));
                if(!cmp.equal(object,jo)) {
                    String str = "Estados diferentes: %s";
                    String.format(str,i);
                    str += "Paso: %s";
                    String.format(str,n);
                    throw new ExceptionState(str);
                }
            }

            if (i != n - 1) out.write("\n,".getBytes());
        }
        out.write("]".getBytes());
        out.write("}".getBytes());
        out.close();




    }


}
