package simulator.viewer;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {

    private List<Body> _bodies;
    private String[] columns = {"Id", "Position", "Velocity", "Force", "Mass"};
    private String[] col;

    BodiesTableModel(Controller ctrl){
        _bodies = new ArrayList<>();
        ctrl.addObserver(this);
        this.col = new String[columns.length];
    }

    public void setBodiesList(List<Body> bodies){
        _bodies = bodies;
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return _bodies.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column){
        return this.columns[column].toString();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        this.col[0] = _bodies.get(rowIndex).getId().toString();
        this.col[1] = _bodies.get(rowIndex).getPos().toString();
        this.col[2] = _bodies.get(rowIndex).getVelocity().toString();
        this.col[3] = _bodies.get(rowIndex).getForce().toString();
        this.col[4] = String.valueOf(_bodies.get(rowIndex).getMass());

        return this.col[columnIndex];
    }

    @Override
    public void onRegistrer(List<Body> bodies, double time, double dt, String fLawsDesc) {
        _bodies = bodies;
        fireTableStructureChanged();
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
        this._bodies = bodies;
        fireTableStructureChanged();
    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {
        this._bodies = bodies;
        fireTableStructureChanged();

    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
        _bodies = bodies;
        fireTableStructureChanged();
    }

    @Override
    public void onDeltaTimeChanged(double dt) {

    }

    @Override
    public void onForceLawsChanged(String fLawsDesc) {

    }
}
