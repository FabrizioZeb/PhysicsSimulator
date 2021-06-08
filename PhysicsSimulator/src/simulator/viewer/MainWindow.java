package simulator.viewer;

import simulator.control.Controller;
import simulator.viewer.ControlPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;


public class MainWindow extends JFrame {

    private Controller _ctlr;
    private ControlPanel controlPanel;
    private Viewer viewer;
    private BodiesTable bodiesTable;
    private StatusBar statusBar;

    public MainWindow(Controller ctrl){
        super("Physics Simulator");
        _ctlr = ctrl;
        initGUI();
    }

    private JPanel MainPanel(){
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
        this.setLocation(20,20);
        this.setMinimumSize(new Dimension(700,700));
        return mainPanel;
    }

    private void addControlPanel(JPanel mainPanel) {
        this.controlPanel = new ControlPanel(_ctlr);
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(controlPanel, BorderLayout.PAGE_START);
    }

    private void initGUI() {

        JPanel mainPanel = this.MainPanel();
        setContentPane(mainPanel);

        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                controlPanel.exit();
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });


        this.addControlPanel(mainPanel);

        JPanel center = new JPanel();
        center.setLayout(new GridLayout(2,1));
        //Grid layout me parece mejor opción que Box Layout, así no tenemos que asignar un tamaño concreto, y que varíe de pantalla a pantalla.
//        mainPanel.add(center,BorderLayout.CENTER);

        this.bodiesTable = new BodiesTable(_ctlr);
        JPanel top = new JPanel();
        top.setAlignmentX(Component.CENTER_ALIGNMENT);
        top.setLayout(new GridLayout(1,1));
        top.setBackground(new Color(255, 255, 255));
        top.add(bodiesTable);
        center.add(top);

        this.viewer = new Viewer(_ctlr);
        JPanel bottom = new JPanel();
        bottom.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottom.setLayout(new GridLayout(1,1));
        //bottom.setBackground(new Color(255, 255, 255));
        bottom.add(viewer);
        center.add(bottom);

        mainPanel.add(center, BorderLayout.CENTER);

        statusBar = new StatusBar(_ctlr);
        statusBar.setBackground(new Color(255, 255, 255));
        mainPanel.add(statusBar, BorderLayout.PAGE_END);

        //statusBar.repaint();

        this.setVisible(true);
    }
}
