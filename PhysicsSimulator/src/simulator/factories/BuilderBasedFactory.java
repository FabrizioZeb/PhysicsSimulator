package simulator.factories;

import org.json.JSONObject;
import simulator.model.Body;

import java.util.ArrayList;
import java.util.List;

public class BuilderBasedFactory<T> implements Factory<T>{

    List<Builder<T>> builders;
    List<JSONObject> fElems;

    public BuilderBasedFactory(List<Builder<T>> builders){
        this.builders = new ArrayList<Builder<T>>(builders);
        this.fElems = new ArrayList<JSONObject>();
        for(int i = 0; i < this.builders.size(); i++){
            this.fElems.add(i,builders.get(i).getBuilderInfo());
        }

    }

    @Override
    public T createInstance(JSONObject info) {
        for (Builder<T> builder : builders) {
            T a = builder.createInstance(info);
            if (a != null) return a;
        }
        return null;
    }

    @Override
    public List<JSONObject> getInfo() {
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (Builder<T> builder : builders) {
            JSONObject o = builder.getBuilderInfo();
            list.add(o);
        }
        return list;
    }

    public static void main(String[] args) {
        ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
        bodyBuilders.add(new BasicBodyBuilder());
        bodyBuilders.add(new MassLosingBodyBuilder());
        Factory<Body> bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);
        System.out.println("FIN");
    }
}
