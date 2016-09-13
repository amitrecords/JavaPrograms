import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Sketchframe extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5772370848777933377L;
	public int Npnt, oldx, oldy, nwx, nwy;
	public  ArrayList<Coordinates> cords;
	private Graphics2D g2;
	public  Coordinates c,c2;
	private java.awt.Image image;

	public Sketchframe() {
		 cords = new ArrayList<Coordinates>();
		 setDoubleBuffered(false);
		 
		 addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e2) {
				oldx = e2.getX();
				oldy = e2.getY();
				 c2= new Coordinates(oldx, oldy);
				cords.add(c2);
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
				 c2 = new Coordinates(e1.getX(), e1.getY());
				 Npnt = Npnt + 1;		
                 cords.add(c2);
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
		cords.clear();
	}

	public int Recognize(ArrayList<Coordinates> cords2) {
		
		FeatureExtraction F= new FeatureExtraction(cords2);
		ArrayList<Coordinates> nwpnts= new ArrayList<Coordinates>();
		nwpnts=	F.Resample(64);
		nwpnts=F.Rotatetozero(nwpnts);
		nwpnts=F.ScaletoSquare(nwpnts,500);
		ArrayList<Templates> templates = F.loadtemplates();
		double[] score =new double[16];
		score=F.Recog(nwpnts,templates);
		double max=0;
		int z=0;
		for(int i=0;i<score.length;i++){
			if (score[i]>max){
				max=score[i];
				z=i;
			}
		}
		
		return z;
		
	}

	public ArrayList<Coordinates> getPnts() {
		//Coordinates c3= new Coordinates(nwx,nwy);
		return cords;
	}

	public int getNpnt() {
		Npnt= cords.size();
		
		return Npnt;
	}

	public void addTemplate(String Templatename) {
		try {
			FeatureExtraction F= new FeatureExtraction(cords);
			ArrayList<Coordinates> nwpnts= new ArrayList<Coordinates>();
			nwpnts=	F.Resample(64);
			nwpnts=F.Rotatetozero(nwpnts);
			nwpnts=F.ScaletoSquare(nwpnts,500);
			
			ArrayList<String> lines = new ArrayList<String>();
			for(Coordinates i :nwpnts){
				Integer x=i.getX();
				Integer y=i.getY();
				lines.add(x.toString()+","+y.toString());
			}
			
			Files.write(Paths.get("Training_files/"+Templatename+".txt"),lines);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
}
