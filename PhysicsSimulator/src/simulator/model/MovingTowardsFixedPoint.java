package simulator.model;
import simulator.misc.Vector2D;
import java.util.List;
public class MovingTowardsFixedPoint implements ForceLaws{

	@Override
	public void apply(List<Body> bs) {
		for(int i = 0; i < bs.size(); i++)
		{
			Vector2D a;
	        if(bs.get(i).getMass() == 0) a = new Vector2D();
	        else {
	            a=bs.get(i).getPos().direction().scale(-9.81); // a=-9.8*direccion del cuerpo
	            a=a.scale(bs.get(i).getMass());//ahora multiplicamos la masa por la aceleracion
	            bs.get(i).addForce(a);//añadimos la fuerza al cuerpo
	        }	
		}
	}

}
