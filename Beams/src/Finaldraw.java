import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Finaldraw {

	JButton clrButton, RcgnzBtn,addTemplatebtn;
	JTextField txtField;
	Sketchframe sketch;
	public Coordinates tempc;
	int pnt[][][], Npnt;
	ActionListener actionListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == clrButton) {
				sketch.clear();
			} else if (e.getSource()==addTemplatebtn){
				String templatename=txtField.getText();
				sketch.addTemplate(templatename);
			} else if (e.getSource() == RcgnzBtn) {
				String txtString ="Gesture";
				int z = sketch.Recognize(sketch.cords);
				switch (z) {
				case 0:
					txtString = "Triangle";
					break;
				case 1:
					txtString = "X";
					break;
				case 2:
					txtString = "Rectangle";
					break;
				case 3:
					txtString = "Circle";
					break;
				case 4:
					txtString = "Check";
					break;
				case 5:
					txtString = "Caret";
					break;
				case 6:
					txtString = "Question";
					break;
				case 7:
					txtString = "Arrow";
					break;
				case 8:
					txtString = "Left Square Bracket";
					break;
				case 9:
					txtString = "Right Square Bracket";
					break;
				case 10:
					txtString = "V";
					break;
				case 11:
					txtString = "Delete";
					break;
				case 12:
					txtString = "Left Curly Bracket";
					break;
				case 13:
					txtString = "Right Curly Bracket";
					break;
				case 14:
					txtString = "Star";
					break;
				case 15:
					txtString = "Pigtail";
					break;
				}
				txtField.setText(txtString);
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
		sketch = new Sketchframe();
		content.add(sketch, BorderLayout.CENTER);
		JPanel options = new JPanel();
		clrButton = new JButton("Clear");
		clrButton.addActionListener(actionListener);
		RcgnzBtn = new JButton("Recognize");
		RcgnzBtn.addActionListener(actionListener);
		addTemplatebtn = new JButton("Add Template");
		addTemplatebtn.addActionListener(actionListener);
		txtField= new JTextField("Gesture");
		txtField.setColumns(20);
		options.add(clrButton);
		options.add(RcgnzBtn);
		options.add(addTemplatebtn);
		options.add(txtField);
		content.add(options, BorderLayout.SOUTH);
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
