package simulator.viewer;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;


import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StatusBar extends JPanel implements SimulatorObserver {

    private JLabel _currTime;
    private JLabel _currLaws;
    private JLabel _numOfBodies;

    StatusBar(Controller ctrl) {
        _currTime = new JLabel("Time: " + 0);
        _numOfBodies = new JLabel("Bodies: " + 0);
        _currLaws = new JLabel("Flaw: " + " ");

        initGui();
        ctrl.addObserver(this);
    }

    private void initGui() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setLayout(new GridLayout(1, 3));
        this.setBorder(BorderFactory.createBevelBorder(1));

        this.add(_currTime);
        this.add(Box.createHorizontalStrut(150));
        this.add(_numOfBodies);
        this.add(Box.createHorizontalStrut(150));
        this.add(_currLaws);

    }


    @Override
    public void onRegistrer(List<Body> bodies, double time, double dt, String fLawsDesc) {
        _numOfBodies = new JLabel("Bodies: " + String.valueOf(bodies.size()));
        _currLaws = new JLabel("Flaw: " + fLawsDesc);
        _currTime = new JLabel("Time: " + String.valueOf(time));
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
        _numOfBodies = new JLabel("Bodies: " + String.valueOf(bodies.size()));
        _currLaws = new JLabel("Flaw: " + fLawsDesc);
        _currTime = new JLabel("Time: " + String.valueOf(time));
    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {
        _numOfBodies = new JLabel("Bodies: " + String.valueOf(bodies.size()));
    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
        _currTime = new JLabel("Time: " + String.valueOf(time));
    }

    @Override
    public void onDeltaTimeChanged(double dt) {
       // _currTime = new JLabel("Time: " + String.valueOf(dt + Integer.parseInt(_currTime.getText())));
    }

    @Override
    public void onForceLawsChanged(String fLawsDesc) {
        _currLaws = new JLabel("Flaw: " + fLawsDesc);

    }
}