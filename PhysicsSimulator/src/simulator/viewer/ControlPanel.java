package simulator.viewer;

import org.json.JSONObject;
import simulator.control.Controller;

import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ControlPanel extends JPanel implements SimulatorObserver {

    private final Controller _ctrl;
    private boolean _stopped;
    private int fLaw = 0;

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
        Color mycolor = new Color(255, 255, 255);
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

        steps = new JSpinner(new SpinnerNumberModel(0,0,null,100));
        steps.setToolTipText("Steps to execute (1-1000)");
        steps.setFont(Font.getFont(Font.SANS_SERIF));
        steps.setPreferredSize(new Dimension(70,35));
        steps.setValue(0);

        time = new JTextField("0",5);
        time.setToolTipText("Time between steps");
        time.setFont(Font.getFont(Font.SANS_SERIF));
        time.setPreferredSize(new Dimension(70,35));

        time.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String valor = time.getText();
                int l = valor.length();
                time.setEditable(e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || e.getKeyChar() == KeyEvent.VK_BACK_SPACE || e.getKeyChar() == KeyEvent.VK_ESCAPE);
            }
        });

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
        pnPane.setLayout(new BorderLayout());

        Flaw_ComboBox.setSelectedItem(forceLaws[fLaw]);


        String[] columnNames = {"Key", "Value", "Description"};
        dtm = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row,int column){
                return column == 1;
            }
        };

        table = new JTable(dtm);


        JScrollPane scrollPane = new JScrollPane(table);
        table.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(124, 158, 217),2,true)));

        //Ancho de las columnas:
        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        table.getColumnModel().getColumn(1).setPreferredWidth(20);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);

        for(int i = 0; i < table.getColumnCount();i++){
            table.getColumnModel().getColumn(i).setHeaderValue(columnNames[i]);
            table.getTableHeader().repaint();

        }

        changeTable(fLaw);


        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                ArrayList<String> parameters = getConst();
                _ctrl.setConstants(parameters);
            }
        });

        final int[] finalFLaw = new int[1];
        Flaw_ComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sel = (String) Flaw_ComboBox.getSelectedItem();
                finalFLaw[0] = selectedItem(sel);
                changeTable(finalFLaw[0]);
                _ctrl.setForceLaws(option[finalFLaw[0]]);
                onForceLawsChanged(forceLaws[finalFLaw[0]]);

            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _ctrl.setForceLaws(option[finalFLaw[0]]);
                onForceLawsChanged(forceLaws[finalFLaw[0]]);
                fLaw = finalFLaw[0];
                //frame.dispose();
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Flaw_ComboBox.setSelectedItem(forceLaws[0]);
                _ctrl.setForceLaws(option[0]);
                onForceLawsChanged(forceLaws[0]);
                changeTable(0);
               //frame.dispose();
            }
        });

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        //Panel de texto de arriba (no editable)
        JTextPane DialogText = new JTextPane();
        DialogText.setEditable(false);
        DialogText.setPreferredSize(new Dimension(100,50));
        DialogText.setLayout(new FlowLayout());
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setBold(attributeSet,true);
        DialogText.setCharacterAttributes(attributeSet,true);
        DialogText.setText("Select a force law and provide values for the parameters in the Value Column (default values are used for parameters with no values)");
        pnPane.add(DialogText,BorderLayout.PAGE_START);
        //---------------------

        JPanel Combo_tabla = new JPanel();
        Combo_tabla.setLayout(new BoxLayout(Combo_tabla,BoxLayout.Y_AXIS));
        table.setPreferredSize(new Dimension(700,300));
        //Tabla
        Combo_tabla.add(new JScrollPane(table));
        //Panel: Label y Combo-Box
        JPanel Combo = new JPanel();
        Combo.setLayout(new FlowLayout());
        Combo.add(label);
        Combo.add(Flaw_ComboBox);
        Combo_tabla.add(Combo);
        pnPane.add(Combo_tabla, BorderLayout.CENTER);
        buttons.add(button1);
        buttons.add(button2);
        pnPane.add(buttons,BorderLayout.PAGE_END);

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
            dtm.addRow(new Object[]{"g",null,"the length of the acceleration"});
        }
        else{ /*No data*/}
    }

    private ArrayList<String> getConst(){
        ArrayList<String> constant = new ArrayList<>();
        for(int i = 0; i < dtm.getRowCount(); i++){
            if(dtm.getValueAt(i,1) != null && dtm.getValueAt(i,1) != "" && dtm.getValueAt(i,1) != " ") {
                try {
                    constant.add((String) dtm.getValueAt(i, 1));
                } catch (Exception e){
                    e.getMessage();
                }
            }
        }
        return constant;
    }




    private int selectedItem(String info){
        int fLaw;
/*        if (info == null) {
            //info = forceLaws[0];
        }*/
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
                JOptionPane.showMessageDialog(null,e.getMessage());
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
            openButton.setEnabled(true);
            physicsButton.setEnabled(true);
            exitButton.setEnabled(true);
            stopButton.setEnabled(true);
            runButton.setEnabled(true);
        }
    }
    // SimulatorObserver methods

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
