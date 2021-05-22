package simulator.viewer;

import org.json.JSONObject;
import simulator.control.Controller;
import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

public class ControlPanel extends JPanel implements SimulatorObserver {

    private Controller _ctrl;
    private boolean _stopped;

    JButton openButton;
    JButton physicsButton;
    JButton exitButton;
    JButton runButton;
    JButton stopButton;
    JSpinner steps;
    JTextField time;
    JTable table;
    DefaultTableModel dtm;

    private JFileChooser fileChooser;

    public ControlPanel(Controller ctrl) {
        _ctrl = ctrl;
        _stopped = true;
        _ctrl.addObserver(this);

        openButton = new JButton();
        physicsButton = new JButton();
        exitButton = new JButton();
        runButton = new JButton();
        stopButton = new JButton();
        initGUI();

    }

    private void initGUI() {
        JToolBar toolBar = new JToolBar();
        Color mycolor = new Color(212, 223, 255);
        toolBar.setBackground(mycolor);
        toolBar.setFloatable(false);

        //Open button
        openButton.setToolTipText("Load an event file"); // Label text that shows when mouse is over the button
        openButton.setIcon(new ImageIcon(loadImg("resources/icons/open.png"))); // Url of the icon
        openButton.setPreferredSize(new Dimension(40, 40));
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Carga el fichero.
                fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        InputStream inputStream = new FileInputStream(file);
                        _ctrl.reset();
                        _ctrl.loadBodies(inputStream);
                    } catch (Exception exception) {
                        e.toString();
                    }
                }
            }
        });

        //Physics Button
        physicsButton.setToolTipText("Choose a Force Law");
        physicsButton.setIcon(new ImageIcon(loadImg("resources/icons/physics.png")));
        physicsButton.setPreferredSize(new Dimension(40, 40));
        physicsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forceLawsDialog();
            }
        });

        //Button exit
        exitButton.setToolTipText("Exit");
        exitButton.setIcon(new ImageIcon(loadImg("resources/icons/exit.png")));
        exitButton.setPreferredSize(new Dimension(40,40));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int res = JOptionPane.showConfirmDialog(new JFrame(),"Are you sure?","Quit",JOptionPane.YES_NO_OPTION);
                if(res == 0)
                    System.exit(0);

            }
        });

        //Stop Button
        stopButton.setToolTipText("Stop the execution");
        stopButton.setIcon(new ImageIcon(loadImg("resources/icons/stop.png")));
        stopButton.setPreferredSize(new Dimension(40,40));
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _stopped = true;
            }
        });

        steps = new JSpinner(new SpinnerNumberModel(0,null,null,100));
        steps.setToolTipText("Steps to execute (1-1000)");
        steps.setFont(Font.getFont(Font.SANS_SERIF));
        steps.setPreferredSize(new Dimension(70,35));
        steps.setValue(0);

        time = new JTextField("0",5);
        time.setToolTipText("Time between steps");
        time.setFont(Font.getFont(Font.SANS_SERIF));
        time.setPreferredSize(new Dimension(70,35));

        time.setEditable(true);

        runButton.setToolTipText("Disable the buttons, except 'Stop'");
        runButton.setIcon(new ImageIcon(loadImg("resources/icons/run.png")));
        runButton.setPreferredSize(new Dimension(40,40));
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double t = Double.parseDouble(time.getText());
                    int n_steps;
                    if(t > 0){
                        _ctrl.setDeltaTime(t);
                        n_steps = Integer.parseInt(steps.getValue().toString());
                        openButton.setEnabled(false);
                        physicsButton.setEnabled(false);
                        exitButton.setEnabled(false);
                        _stopped = false;
                        run_sim(n_steps);
                    }
                }catch (Exception exception){
                    JOptionPane.showMessageDialog(null,exception.getMessage());
                }
            }
        });

        toolBar.add(openButton);
        toolBar.add(physicsButton);
        toolBar.add(runButton);
        toolBar.add(stopButton);
        toolBar.addSeparator();
        toolBar.add(new JLabel("Steps:  "));
        toolBar.add(steps);
        toolBar.addSeparator();
        toolBar.add(new JLabel("Delta-Time:  "));
        toolBar.add(time);
        toolBar.addSeparator(new Dimension(200,0));
        toolBar.add(exitButton);
        toolBar.setFloatable(true);
        this.add(toolBar);
    }


    private void forceLawsDialog() {
        List<JSONObject> list = _ctrl.getForceLawsInfo();
        String[] forceLaws = new String[list.size()];
        // option -> [nlug, mtfp, nf]
        JSONObject[] option = new JSONObject[list.size()];
        int fLaw = 0;

        for (int i = 0; i < list.size(); i++) {
            forceLaws[i] = list.get(i).getString("desc");
            option[i] = list.get(i);
        }

        JFrame frame = new JFrame("Force Law selector");
        frame.setSize(new Dimension(600,400));
        JPanel pnPane = new JPanel();
        JButton button1 = new JButton("OK");
        JButton button2 = new JButton("Cancel");
        JComboBox<String> Flaw_ComboBox = new JComboBox<String>(forceLaws);
        JLabel label = new JLabel("Force Law: ");
        pnPane.setLayout(new GridLayout(4,1));


        String info = (String) Flaw_ComboBox.getSelectedItem();
        fLaw = selectedItem(info);

        _ctrl.setForceLaws(option[fLaw]);

        onForceLawsChanged(forceLaws[fLaw]);


        String[] columnNames = {"Key", "Value", "Description"};
        dtm = new DefaultTableModel(){
            String[] columnNames = {"Key", "Value", "Description"};
            @Override
            public int getColumnCount() {
                return columnNames.length;
            }
            @Override
            public String getColumnName(int index){
                return columnNames[index];
            }
        };
        table = new JTable(dtm);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.blue,2)));

        for(int i = 0; i < dtm.getColumnCount();i++){
            TableColumn col = table.getTableHeader().getColumnModel().getColumn(i);
            col.setHeaderValue(columnNames[i].toString());
        }
        changeTable(fLaw);



        double constant;
        Vector2D c;

        final int[] finalFLaw = new int[1];

        Flaw_ComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sel = (String) Flaw_ComboBox.getSelectedItem();
                finalFLaw[0] = selectedItem(sel);
                changeTable(finalFLaw[0]);
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _ctrl.setForceLaws(option[finalFLaw[0]]);
                onForceLawsChanged(forceLaws[finalFLaw[0]]);
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Flaw_ComboBox.setSelectedItem(forceLaws[0]);
                _ctrl.setForceLaws(option[0]);
                onForceLawsChanged(forceLaws[0]);
                changeTable(0);
            }
        });
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1,2));
        pnPane.add(new JLabel("Select a force law and provide values for the parameters " +
                "in the Value Column ( default values are used for parameters with no values"));
        pnPane.add(table);
        pnPane.add(Flaw_ComboBox);
        buttons.add(button1);
        buttons.add(button2);
        pnPane.add(buttons);

        frame.getContentPane().add(BorderLayout.CENTER,pnPane);
        frame.setVisible(true);

        _ctrl.getForceLawsInfo();
    }

    private void changeTable(int data){

        dtm.setRowCount(0);
        if(data == 0){
            dtm.addRow(new Object[]{"G",null,"the gravitational constant (a number)"});
        }
        if(data == 1){

            dtm.addRow(new Object[]{"c",null,"the point towards which bodies move"});
            dtm.addRow(new Object[]{"g",null,"the lenght of the acceleration"});
        }
        else{ /*No data*/};
    }

    private double getConst(){
        double constant = 0;
        for(int i = 0; i < dtm.getRowCount(); i++){
            if(dtm.getValueAt(i,1) != " ")
                try {
                    constant = (double) dtm.getValueAt(i, 1);
                } catch (Exception e){
                    e.getMessage().toString();
                }
            ;
        }
        return constant;
    }

    private int selectedItem(String info){
        int fLaw;
        if (info == null) {
            //info = forceLaws[0];
            fLaw = 0;
        }
        fLaw = switch (info) {
            case "Newton's law of universal gravitation" -> 0;
            case "Moving towards a fixed point" -> 1;
            case "No force" -> 2;
            default -> 0;
        };
        return fLaw;
    }

    public Image loadImg(String dir) {
        return Toolkit.getDefaultToolkit().createImage(dir); // Oracle Documentation: https://docs.oracle.com/javase/7/docs/api/java/awt/Toolkit.html#createImage(java.lang.String)
    }

    private void run_sim(int n) {
        if (n > 0 && !_stopped) {
            try {
                _ctrl.run(1);
            } catch (Exception e) {
                // TODO show the error in a dialog box
                JOptionPane.showMessageDialog(null,e.getMessage());
                // TODO enable all buttons
                openButton.setEnabled(true);
                physicsButton.setEnabled(true);
                exitButton.setEnabled(true);
                stopButton.setEnabled(true);
                runButton.setEnabled(true);
                _stopped = true;
                return;
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    run_sim(n - 1);
                }
            });
        } else {
            _stopped = true;
            // TODO enable all buttons
            openButton.setEnabled(true);
            physicsButton.setEnabled(true);
            exitButton.setEnabled(true);
            stopButton.setEnabled(true);
            runButton.setEnabled(true);
        }
    }
    // SimulatorObserver methods
    // ...

    @Override
    public void onRegistrer(List<Body> bodies, double time, double dt, String fLawsDesc) {
        _ctrl.setDeltaTime(dt);

    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {

    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {

    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {

    }

    @Override
    public void onDeltaTimeChanged(double dt) {

    }

    @Override
    public void onForceLawsChanged(String fLawsDesc) {
        if(dtm != null) {
            dtm.fireTableStructureChanged();
        }
    }

    public void exit() {
    }
}
