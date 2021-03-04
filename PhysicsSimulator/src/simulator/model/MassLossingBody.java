package simulator.model;

import simulator.misc.Vector2D;

public class MassLossingBody extends Body{

    private double lossFactor;
    private double lossFrequency;

    public MassLossingBody(String id, Vector2D velocity, Vector2D pos, double mass, double lossFactor, double lossFrequency) {
        super(id, velocity, pos, mass);
        this.lossFactor = lossFactor;
        this.lossFrequency = lossFrequency;
    }

    public void move(double t){
        if(t >= lossFrequency){
            mass = mass * (1-lossFactor);
        }
    }

}
