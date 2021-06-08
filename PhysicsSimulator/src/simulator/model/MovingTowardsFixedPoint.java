package simulator.model;

import simulator.misc.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class MovingTowardsFixedPoint implements ForceLaws {

    public static double _g = -9.81;
    public static Vector2D _c = new Vector2D(0, 0);

    public MovingTowardsFixedPoint() {
        super();
    }

    @Override
    public void apply(List<Body> bs) {
        for (Body b : bs) {
            Vector2D a = new Vector2D();
            if (b.getMass() == 0) a = new Vector2D();
            else {
                a = b.getPos().minus(_c).direction();
                a = a.scale(_g);
                a = a.scale(b.getMass()); //a*m
                b.resetForce();
                b.addForce(a);
            }
        }
    }

    @Override
    public void setConstants(ArrayList<String> parameters) {
        if (!parameters.isEmpty()) {
            if (parameters.size() > 1) {
                if (parameters.get(0).matches("\\[[^\\[]*\\]")) {
                    String vector = parameters.get(0);
                    int comma = vector.indexOf(',');
                    double x = Double.parseDouble(vector.substring(1, comma));
                    double y = Double.parseDouble(vector.substring(comma + 1, vector.length() - 1));
                    _c = new Vector2D(x, y);
                    _g = Double.parseDouble(parameters.get(1));
                } else {
                    String vector = parameters.get(1);
                    int comma = vector.indexOf(',');
                    double x = Double.parseDouble(vector.substring(1, comma));
                    double y = Double.parseDouble(vector.substring(comma + 1, vector.length() - 1));
                    _c = new Vector2D(x, y);
                    _g = Double.parseDouble(parameters.get(0));
                }

            } else {
                if (parameters.get(0).matches("\\[[^\\[]*\\]")) {
                    String vector = parameters.get(0);
                    int comma = vector.indexOf(',');
                    double x = Double.parseDouble(vector.substring(1, comma));
                    double y = Double.parseDouble(vector.substring(comma + 1, vector.length() - 1));
                    _c = new Vector2D(x, y);
                } else _g = Double.parseDouble(parameters.get(0));
            }
        } else {
            _c = new Vector2D(0,0);
            _g = -9.81;
        }

    }


    public String toString() {
        return "Moving towards " + _c + " with constant acceleration " + _g;
    }


}
