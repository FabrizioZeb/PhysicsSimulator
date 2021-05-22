package simulator.viewer;

import simulator.control.Controller;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class BodiesTable extends JPanel {

    BodiesTable(Controller ctrl){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.black,2),
                "Bodies",
                TitledBorder.LEFT,TitledBorder.TOP));
        BodiesTableModel bodiesTableModel = new BodiesTableModel(ctrl);
        this.add(new JScrollPane(new JTable(bodiesTableModel)));
    }

}
