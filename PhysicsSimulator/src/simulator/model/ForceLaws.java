package simulator.model;

import java.util.ArrayList;
import java.util.List;

public interface ForceLaws {
	public void apply(List<Body> bs);

	public void setConstants(ArrayList<String> parameters);
}
