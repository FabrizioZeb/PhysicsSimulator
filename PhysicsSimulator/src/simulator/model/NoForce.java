package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NoForce implements ForceLaws{

    @Override
    public void apply(List<Body> bs) {
    }

    @Override
    public void setConstants(ArrayList<String> parameters) {
    }

    public String toString(){
        return "No Force";
    }
}
