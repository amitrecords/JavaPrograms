import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import javax.swing.border.LineBorder;

import com.sun.prism.Image;

import java.awt.GridLayout;

public class Sketchframe extends JComponent {

	private int Npnt, oldx, oldy, nwx, nwy;
	private ArrayList cords;
	private Graphics2D g2;
	private java.awt.Image image;

	public Sketchframe() {
		// cords = new ArrayList();
		setDoubleBuffered(false);
		addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e2) {
				oldx = e2.getX();
				oldy = e2.getY();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e1) {
				nwx = e1.getX();
				nwy = e1.getY();

				if (g2 != null) {

					g2.drawLine(oldx, oldy, nwx, nwy);
					repaint();
					oldx = nwx;
					oldy = nwy;
				}
				// Coordinates c = new Coordinates(e1.getX(), e1.getY());
				// Npnt = Npnt + 1;
				// System.out.println("points are:" + c.getX() + " , " +
				// c.getY());
				// System.out.println("number of pnts are:" + Npnt);

			}

		});
	}

	protected void paintComponent(Graphics g) {
		if (image == null) {
			image = createImage(getSize().width, getSize().height);
			g2 = (Graphics2D) image.getGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			clear();
		}
		g.drawImage(image, 0, 0, null);
	}

	public void clear() {
		g2.setPaint(Color.WHITE);
		g2.fillRect(0, 0, getSize().width, getSize().height);
		g2.setPaint(Color.black);
		repaint();
	}

	public int Recognize() {
		int z;
		z=2;
		return z;
		
	}
}
