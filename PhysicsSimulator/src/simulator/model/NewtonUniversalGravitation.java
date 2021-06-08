package simulator.model;

import simulator.misc.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class NewtonUniversalGravitation implements ForceLaws{


    double G = 6.67E-11;

    public NewtonUniversalGravitation() {
        super();
    }

    @Override
    public void apply(List<Body> bs) {
        for(int i = 0; i < bs.size(); i++){
            Vector2D Force = new Vector2D();
            Vector2D a = new Vector2D();
            for(int j = 0; j < bs.size(); j++){
                if(i != j){
                    Force = Force.plus(force(bs.get(i), bs.get(j)));
                    if(bs.get(i).getMass() == 0){
                        bs.get(i).velocity = new Vector2D();
                        bs.get(i).resetForce();
                    }
                    else {
                        a = a.plus(Force.scale(1/bs.get(i).getMass()));
                    }

                }
            }
            bs.get(i).resetForce();
            bs.get(i).addForce(a.scale(bs.get(i).getMass()));
        }
    }

    @Override
    public void setConstants(ArrayList<String> parameters) {
        if(!parameters.isEmpty())
            this.G = Double.parseDouble(parameters.get(0));
        else G = 6.67E-11;
    }

    private Vector2D force(Body i, Body j){ // F = G *(m1*m2)/|p2-p1|^2:
        double m = i.getMass() * j.getMass(); // Producto de las masas
        double R = i.getPos().distanceTo(j.getPos()); //Distancia de un cuerpo a otro
        R = Math.pow(R,2);

        double fij;
        fij = G * m/R; //Obtenemos la fuerza en escalar
        Vector2D dij = j.getPos().direction().minus(i.getPos().direction());//Obtenemos la dirección de la fuerza
        dij = dij.direction();
        Vector2D Fij = dij.scale(fij); // F*dir = F vector
        return Fij;
    }

    public String toString(){
        return "Newton's Universal Gravitation with G = " + G;
    }

    public void setG(double G){
        this.G = G;
    }

}
