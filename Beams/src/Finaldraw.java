import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.xml.internal.ws.api.server.Container;

public class Finaldraw {

	JButton clrButton,RcgnzBtn;
	Sketchframe sketch;
	int pnt[][][],Npnt;
	ActionListener actionListener = new ActionListener() {
		

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == clrButton) {
				sketch.clear();
			}else if(e.getSource() == RcgnzBtn){
				int z = sketch.Recognize();
			}

		}
	};

	public static void main(String[] args) {
		new Finaldraw().show();
	}

	public void show() {
		JFrame frame = new JFrame("Beam Sketch");
		java.awt.Container content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		sketch= new Sketchframe();
		content.add(sketch, BorderLayout.CENTER);
		JPanel options = new JPanel();
		clrButton=new JButton("Clear");
		clrButton.addActionListener(actionListener);
		RcgnzBtn=new JButton("Recognize");
		RcgnzBtn.addActionListener(actionListener);
		options.add(clrButton);
		options.add(RcgnzBtn);
		content.add(options, BorderLayout.SOUTH);
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
