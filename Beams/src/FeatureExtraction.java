import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class FeatureExtraction {
	private ArrayList<Coordinates> cords;
	private double[][] pnt;

	public FeatureExtraction(ArrayList<Coordinates> cords) {
		this.cords = cords;
		int Npnt = cords.size();
		pnt = new double[Npnt][2];
		int k = 0;
		for (Coordinates j : cords) {
			pnt[k][0] = j.getX();
			pnt[k][1] = j.getY();
			k = k + 1;
		}
	}

	public ArrayList<Coordinates> Resample(int n) {
		double D = 0;
		double d = PathLength(cords);
		double I = d / (n - 1);
		ArrayList<Coordinates> newpnts = new ArrayList<Coordinates>();
		newpnts.add(cords.get(0));
	//	int counter = 0;
		for (int i = 0; i < cords.size() - 1; i++) {
			d = Distance(pnt[i + 1][0], pnt[i + 1][1], pnt[i][0], pnt[i][1]);
			if (D + d > I) {
				double xt =  (pnt[i][0] + ((I - D) / d)
						* (pnt[i + 1][0] - pnt[i][0]));
				double yt =  (pnt[i][1] + ((I - D) / d)
						* (pnt[i + 1][1] - pnt[i][1]));
				Coordinates temp = new Coordinates((int)xt,(int) yt);
				newpnts.add(temp);
				pnt[i + 1][0] = xt;
				pnt[i + 1][1] = yt;
				//counter = counter + 1;
		//		System.out.println("counter:" + counter);
				D = 0;
				
			} else {
				D = D + d;
			}
		}
		newpnts.add(cords.get(cords.size()-1));
		return newpnts;

	}

	private double Distance(double pnt2, double pnt3, double pnt4, double pnt5) {

		double s = Math.pow(pnt2 - pnt4, 2) + Math.pow(pnt3 - pnt5, 2);
		double d = Math.sqrt(s);
		return d;
	}

	private double PathLength(ArrayList<Coordinates> cords2) {
		double dist = 0;
		for (int c = 0; c < cords2.size() - 1; c++) {
			Coordinates p1 = cords2.get(c);
			Coordinates p2 = cords2.get(c + 1);
			double t = Distance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
			dist = dist + t;
		}

		return dist;
	}

	public ArrayList<Coordinates> Rotatetozero(ArrayList<Coordinates> nwpnts) {
		int k = 0;
		int Npnt = nwpnts.size();
		int[][] pnt = new int[Npnt][2];
		for (Coordinates j : nwpnts) {
			pnt[k][0] = j.getX();
			pnt[k][1] = j.getY();
			k = k + 1;
		}
		double[] c = new double[2];
		c = Centroid(pnt);
		double th = Math.atan2( c[1]-pnt[0][1], c[0] - pnt[0][0] );
		ArrayList<Coordinates> newpnts = Rotateby(pnt, -th, c);
		return newpnts;
	}

	private ArrayList<Coordinates> Rotateby(int[][] pnt2, double th, double[] c) {
		int Npnt = pnt2.length;
		ArrayList<Coordinates> newpnt = new ArrayList<Coordinates>();
		int[] newtmp = new int[2];
		for (int i = 0; i < Npnt; i++) {
			newtmp[0] = (int) ((pnt2[i][0] - c[0]) * Math.cos(th)
					- (pnt2[i][1] - c[1]) * Math.sin(th) + c[0]);
			newtmp[1] = (int) ((pnt2[i][0] - c[0]) * Math.sin(th)
					+ (pnt2[i][1] - c[1]) * Math.cos(th) + c[1]);
			Coordinates crd = new Coordinates(newtmp[0], newtmp[1]);
			newpnt.add(crd);

		}

		return newpnt;
	}

	private double[] Centroid(int[][] pnt2) {
		int xsum = 0, ysum = 0;
		double[] cent = new double[2];
		for (int i = 0; i < pnt2.length; i++) {
			xsum = xsum + pnt2[i][0];
			ysum = ysum + pnt2[i][1];
		}
		cent[0] = xsum / pnt2.length;
		cent[1] = ysum / pnt2.length;
		return cent;
	}

	public ArrayList<Coordinates> ScaletoSquare(ArrayList<Coordinates> nwpnts,
			double d) {
		int[] B = new int[2];
		int k = 0;
		int Npnt = nwpnts.size();
		int[][] pnt = new int[Npnt][2];
		for (Coordinates j : nwpnts) {
			pnt[k][0] = j.getX();
			pnt[k][1] = j.getY();
			k = k + 1;
		}
		B = Bounding_box(pnt);
		ArrayList<Coordinates> newpnt = new ArrayList<Coordinates>();
		int[] newtmp = new int[2];
		for (int i = 0; i < Npnt; i++) {
			newtmp[0] = (int) (pnt[i][0] * (d / B[0]));
			newtmp[1] = (int) (pnt[i][1] * (d / B[1]));
			Coordinates crd = new Coordinates(newtmp[0], newtmp[1]);
			newpnt.add(crd);

		}
		double[] c= new double[2];
		c=Centroid(pnt);
		newpnt=Translatetoorigin(newpnt,c);
		return newpnt;
	}

	private ArrayList<Coordinates> Translatetoorigin(
			ArrayList<Coordinates> newpnt, double[] c) {
		int Npnt=newpnt.size();
		ArrayList<Coordinates> newcrd= new ArrayList<Coordinates>();
		
		for(int i=0;i<Npnt;i++){
			Coordinates temp= new Coordinates();
			double X= newpnt.get(i).getX()-c[0];
			double Y= newpnt.get(i).getY()-c[1];
			temp.setX((int) X);temp.setY((int)Y);
			newcrd.add(temp);
		}
		return newcrd;
	}

	private int[] Bounding_box(int[][] pnt2) {
		int xmax = 0, xmin = 1000000, ymax = 0, ymin = 1000000;
		for (int i = 0; i < pnt2.length; i++) {
			if (xmax < pnt2[i][0]) {
				xmax = pnt2[i][0];
			}
			if (xmin > pnt2[i][0]) {
				xmin = pnt2[i][0];
			}
			if (ymax < pnt2[i][1]) {
				ymax = pnt2[i][1];
			}
			if (ymin > pnt2[i][1]) {
				ymin = pnt2[i][1];
			}
		}
		int[] B = new int[2];
		B[0] = xmax - xmin;
		B[1] = ymax - ymin;
		return B;
	}

	public ArrayList<Templates> loadtemplates() {
		ArrayList<Templates> templates = new ArrayList<Templates>();
		// Read the file
		// File file= new File("Training_files/left curly bracket");
		// Create the coordinate object and set name
		try {

			ArrayList<String> filnames = new ArrayList<String>(Arrays.asList(
					"Triangle", "X", "Rectangle", "Circle", "Check", "Caret", "Question",
					"Arrow", "Leftsquarebracket", "Rightsquarebracket", "V", "Delete", "Leftcurlybracket", "Rightcurlybracket",
					"Star", "Pigtail"));
			for (int count = 0; count < 16; count++) {
				ArrayList<Coordinates> coordinates = new ArrayList<Coordinates>();
				Templates template = new Templates();
				template.setName(filnames.get(count));	
				for (String line : Files.readAllLines(Paths
						.get("Training_files/" + filnames.get(count) + ".txt"))) {
					Coordinates c = new Coordinates();
					int flag = 0;
					for (String part : line.split(",")) {
						Integer i = Integer.valueOf(part);
						if (flag == 0) {
							c.setX(i);
							flag = 1;
						} else {
							c.setY(i);

						}
					}
					coordinates.add(c);
				}
				template.setCords(coordinates);
				templates.add(template);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return templates;
	}

	public double[] Recog(ArrayList<Coordinates> nwpnts,
			ArrayList<Templates> templates) {
		double b = 10000000;
		double[] score = new double[16];
		int th = 45;
		int counter = 0;
		for (Templates templt : templates) {
			double dist = DistanceatBestangle(nwpnts, templt, th, -th, 2);
			if (dist < b) {
				b = dist;
			}
			score[counter] = 1 - b / (0.5 * Math.sqrt(500 * 500 + 500 * 500));
			counter = counter + 1;
		}
		return score;
	}

	private double DistanceatBestangle(ArrayList<Coordinates> nwpnts,
			Templates templt, double theta1, double theta2, double delth) {
		double phi = 0.5 * (-1 + Math.sqrt(5));
		double x1 = phi * theta1 + (1 - phi) * theta2;
		double f1 = DistanceatAngle(nwpnts, templt, x1);
		double x2 = phi * theta2 + (1 - phi) * theta1;
		double f2 = DistanceatAngle(nwpnts, templt, x2);
		while (Math.abs(theta2 - theta1) > delth) {
			if (f1 > f2) {
				theta2 = x2;
				x2 = x1;
				f2 = f1;
				x1 = phi * theta1 + (1 - phi) * theta2;
				f1 = DistanceatAngle(nwpnts, templt, x1);
			} else {
				theta1 = x1;
				x1 = x2;
				f1 = f2;
				x2 = phi * theta2 + (1 - phi) * theta1;
				f2 = DistanceatAngle(nwpnts, templt, x2);
			}
		}
		if (f1 < f2) {
			return f1;
		} else {
			return f2;

		}

	}

	private double DistanceatAngle(ArrayList<Coordinates> nwpnts,
			Templates templt, double x2) {
		ArrayList<Coordinates> newpnts = new ArrayList<Coordinates>();
		int k = 0;
		int[][] pnt = new int[64][2];
		for (Coordinates j : nwpnts) {
			pnt[k][0] = j.getX();
			pnt[k][1] = j.getY();
			k = k + 1;
		}
		double[] c = new double[2];
		c = Centroid(pnt);

		newpnts = Rotateby(pnt, Math.toRadians(x2), c);
		double d = Path_distance(newpnts, templt);
		return d;
	}

	private double Path_distance(ArrayList<Coordinates> newpnts,
			Templates templt) {
		double dist = 0;
		ArrayList<Coordinates> newpnt2 = templt.getCords();
		for (int i = 0; i < 64; i++) {
			dist = dist + Distance2(newpnts.get(i), newpnt2.get(i));
		}
		dist = dist / newpnts.size();
		return dist;
	}

	private double Distance2(Coordinates coordinates1, Coordinates coordinates2) {
		double dist;
		int x1 = coordinates1.getX();
		int x2 = coordinates2.getX();
		int y1 = coordinates1.getY();
		int y2 = coordinates2.getY();
		dist = Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2));
		return dist;
	}
}
