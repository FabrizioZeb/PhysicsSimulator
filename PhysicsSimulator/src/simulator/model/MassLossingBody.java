package simulator.model;

import simulator.misc.Vector2D;

public class MassLossingBody extends Body{

    private double lossFactor;
    private double lossFrequency;
    private double c = 0;

    public MassLossingBody(String id, Vector2D velocity, Vector2D pos, double mass, double lossFactor, double lossFrequency) {
        super(id, velocity, pos, mass);
        this.lossFactor = lossFactor;
        this.lossFrequency = lossFrequency;
    }


    @Override
    public void move(double t){
        super.move(t);
        if(c >= lossFrequency){
            mass = mass * (1-lossFactor);
            c = 0;
        }
        else c += t;
    }

}
