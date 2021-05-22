package simulator.viewer;

import simulator.control.Controller;
import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class Viewer extends JComponent implements SimulatorObserver {
    // ...
    private int _centerX;
    private int _centerY;
    private double _scale;
    private List<Body> _bodies;
    private boolean _showHelp;
    private boolean _showVectors;

    Viewer(Controller ctrl) {
        initGUI();
        ctrl.addObserver(this);
    }

    private void initGUI() {

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Viewer",
                TitledBorder.LEFT,TitledBorder.TOP));
        _bodies = new ArrayList<>();
        _scale = 1.0;
        _showHelp = true;
        _showVectors = true;
        addKeyListener(new KeyListener() {

            // ...
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case '-':
                        _scale = _scale * 1.1;
                        repaint();
                        break;
                    case '+':
                        _scale = Math.max(1000.0, _scale / 1.1);
                        repaint();
                        break;
                    case '=':
                        autoScale();
                        repaint();
                        break;
                    case 'h':
                        _showHelp = !_showHelp;
                        repaint();
                        break;
                    case 'v':
                        _showVectors = !_showVectors;
                        repaint();
                        break;
                    default:
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            // ...
            @Override
            public void mouseEntered(MouseEvent e) {
                requestFocus();
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // use ’gr’ to draw not ’g’ --- it gives nicer results
        Graphics2D gr = (Graphics2D) g;
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // calculate the center
        _centerX = getWidth() / 2;
        _centerY = getHeight() / 2;
        // TODO draw a cross at center
        gr.setColor(Color.red);
        gr.drawString("+",_centerX,_centerY);
        // TODO draw bodies (with vectors if _showVectors is true)
        for (Body body : _bodies){
            //Pintar vectores
            if(_showVectors){
                this.drawLineWithArrow(g,
                        _centerX + (int) (body.getPos().getX()/_scale) + 5,
                        _centerY - (int) (body.getPos().getY()/_scale)+ 5,
                        _centerX + (int) (body.getForce().direction().scale(10).getX()),
                        _centerY - (int) (body.getForce().direction().scale(10).getY()),
                        3,3,
                        Color.green,Color.green);
                this.drawLineWithArrow(g,
                        _centerX + (int) (body.getPos().getX()/_scale) + 5,
                        _centerY - (int) (body.getPos().getY()/_scale) + 5,
                        _centerX + (int) (body.getVelocity().direction().scale(10).getX()),
                        _centerY - (int) (body.getVelocity().direction().scale(10).getY()),
                        3,3,
                        Color.red,Color.red);
            }
            //Pintar cuerpo
            gr.setColor(Color.blue);
            gr.drawOval(_centerX + (int) (body.getPos().getX()/_scale),_centerY - (int) (body.getPos().getY()/_scale),10,10);
            gr.fillOval(_centerX + (int) (body.getPos().getX()/_scale), _centerY - (int) (body.getPos().getY()/_scale), 10, 10);
            //Pintar id
            gr.setColor(Color.black);
            gr.drawString(body.getId(),_centerX + (int) (body.getPos().getX()/_scale),_centerY - (int) (body.getPos().getY()/_scale));
        }
// TODO draw help if _showHelp is true
        if(_showHelp){
            gr.setColor(Color.red);
            gr.drawString("h: toggle help, +: zoom. -: zoom^-1, =: scale",5,23);
            gr.drawString("v: toggle vectors",5,36);
            gr.drawString("scale: " + _scale,5,49);
        }
    }

    // other private/protected methods
// ...
    private void autoScale() {
        double max = 1.0;
        for (Body b : _bodies) {
            Vector2D p = b.getPos();
            max = Math.max(max, Math.abs(p.getX()));
            max = Math.max(max, Math.abs(p.getY()));
        }
        double size = Math.max(1.0, Math.min(getWidth(), getHeight()));
        _scale = max > size ? 4.0 * max / size : 1.0;
    }

    // This method draws a line from (x1,y1) to (x2,y2) with an arrow.
// The arrow is of height h and width w.
// The last two arguments are the colors of the arrow and the line
    private void drawLineWithArrow(//
       Graphics g, //
       int x1, int y1, //
       int x2, int y2, //
       int w, int h, //
       Color lineColor, Color arrowColor) {
            int dx = x2 - x1, dy = y2 - y1;
            double D = Math.sqrt(dx * dx + dy * dy);
            double xm = D - w, xn = xm, ym = h, yn = -h, x;
            double sin = dy / D, cos = dx / D;
            x = xm * cos - ym * sin + x1;
            ym = xm * sin + ym * cos + y1;
            xm = x;
            x = xn * cos - yn * sin + x1;
            yn = xn * sin + yn * cos + y1;
            xn = x;
            int[] xpoints = {x2, (int) xm, (int) xn};
            int[] ypoints = {y2, (int) ym, (int) yn};
            g.setColor(lineColor);
            g.drawLine(x1, y1, x2, y2);
            g.setColor(arrowColor);
            g.fillPolygon(xpoints, ypoints, 3);
    }

    @Override
    public void onRegistrer(List<Body> bodies, double time, double dt, String fLawsDesc) {
        _bodies = bodies;
        autoScale();
        repaint();
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
        _bodies = bodies;
        autoScale();
        repaint();
    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {
        _bodies = bodies;
        autoScale();
        repaint();
    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
        repaint();
    }

    @Override
    public void onDeltaTimeChanged(double dt) {

    }

    @Override
    public void onForceLawsChanged(String fLawsDesc) {

    }
// SimulatorObserver methods
// ...
}


