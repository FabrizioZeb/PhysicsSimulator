package simulator.model;

import simulator.misc.Vector2D;

import java.util.List;

public class NewtonUniversalGravitation implements ForceLaws{


    final static double G = 6.67E-11;

    public NewtonUniversalGravitation() {
        super();
    }

    @Override
    public void apply(List<Body> bs) {

        Vector2D Force = new Vector2D(bs.get(1).getPos());

        for(int i = 0; i < bs.size(); i++){
            for(int j = 0; j < bs.size(); j++){
                if(i != j){
                    Force = Force.plus(force(bs.get(i), bs.get(j)));
                    if(bs.get(i).getMass() == 0){

                    }

                }
            }
        }
    }

    private Vector2D force(Body i, Body j){ // F = G *(m1*m2)/|p2-p1|^2:
        double m = i.getMass() * j.getMass(); // Producto de las masas
        double R = i.getPos().distanceTo(j.getPos()); //Distancia de un cuerpo a otro
        Math.pow(R,2);

        double fij;
        fij = G * m/R; //Obtenemos la fuerza en escalar
        Vector2D dij = j.getPos().direction().minus(i.getPos().direction()); //Obtenemos la dirección de la fuerza
        Vector2D Fij = dij.scale(fij); // F*dir = F vector
        return Fij;
    }


}
