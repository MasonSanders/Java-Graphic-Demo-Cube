import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.lang.Math;
import java.awt.color.*;

/*
 * GraphicDemo Class
 */
public class GraphicDemo extends JFrame {

	/*
	 * main()
	 */
	public static void main(String[] args) {
		new GraphicDemo();
	} // end main()

	/*
	 * GraphicDemo Constructor
	 */
	public GraphicDemo() {
		// Create the canvas.
		Canvas canv = new Canvas();
		
		// JFrame Setup.
		this.add(canv, BorderLayout.CENTER);
		this.setSize(1000, 1000);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	} // end GraphicDemo constructor

	/*
	 * Canvas class
	 */
	class Canvas extends JPanel implements MouseListener, MouseMotionListener, ActionListener, KeyListener {
		// faces for the cube
		Path2D.Double faceLeft;
		Path2D.Double faceRight;
		Path2D.Double faceTop;
		
		//colors for the cube faces
		Color faceLeftColor;
		Color faceRightColor;
		Color faceTopColor;
		
		// important points/values for the cube
		// the initial origins will always stay the same
		// current origins are used to create perspective shift.
		// left and right x define the left and right side of the regular hexagon
		int initialOriginX;
		int initialOriginY;
		int rightX;
		int leftX;
		int currentOriginX;
		int currentOriginY;
		// x and y value for the origin
		int dx;
		int dy;
		//cube radius will change with dx
		int hexRadius;
		
		//timer
		Timer t = new Timer(17, this); 
		
		public Canvas(){
			
			// randomize the colors for the cube initially
			this.faceLeftColor = new Color(this.randomValue(), this.randomValue(), this.randomValue());
			this.faceRightColor = new Color(this.randomValue(), this.randomValue(), this.randomValue());
			this.faceTopColor = new Color(this.randomValue(), this.randomValue(), this.randomValue());
			
			// create the faces of the cube during initialization
			// so that the paintComponent method doesn't create them each time.
			this.initialOriginX = 500;
			this.initialOriginY = 500;
			this.hexRadius = 200;
			this.currentOriginX = this.initialOriginX;
			this.currentOriginY = this.initialOriginY;
			this.dy = 0;
			this.dx = 0;
			this.createCubeFaces();
			
			
			//add the mouse and key listeners
			this.setFocusable(true);
			this.requestFocus();
			addMouseListener(this);
			addMouseMotionListener(this);
			addKeyListener(this);
			this.t.start();
		}

		/*
		 * paintComponenet method
		 */
		public void paintComponent(Graphics g) {
			
			super.paintComponent(g);
			
			// implement Graphics2D
			Graphics2D g2d = (Graphics2D)g;
			
			this.setBackground(new Color(0, 0, 0));
			
			/*
			 * The rendered image will be a cube with color changing sides when the area is clicked
			 */
			 
			//draw the left face
			g2d.setColor(this.faceLeftColor);
			g2d.fill(this.faceLeft);
			
			//draw the right face
			g2d.setColor(this.faceRightColor);
			g2d.fill(this.faceRight);
			
			//draw the top face
			g2d.setColor(this.faceTopColor);
			g2d.fill(this.faceTop);
			
			//draw the word CUBE
			g2d.setFont(new Font("SansSerif", 0, 60));
			g2d.setColor(new Color(255, 255, 255));
			g2d.drawString("CUBE", 20, 70);
			
		}// end paintComponent
		
		// createCubeFaces method. Defines the shape of the cube faces.
		public void createCubeFaces() {
			
			// these ints get the x position of the left and right sides of the
			// regular hexagon
			this.leftX = this.initialOriginX - (int)Math.round(Math.sqrt(Math.pow(this.hexRadius, 2) - Math.pow(this.hexRadius / 2, 2)));
			this.rightX = this.initialOriginX + (int)Math.round(Math.sqrt(Math.pow(this.hexRadius, 2) - Math.pow(this.hexRadius / 2, 2)));
			
			//difference between intialorigin position and current
			
			//update the current origin position.
			this.currentOriginY += this.dy;
			
			this.hexRadius += this.dx;
			
			
			// use Path2D to create 3 faces
			this.faceLeft = new Path2D.Double();
			this.faceRight = new Path2D.Double();
			this.faceTop = new Path2D.Double();
			
			// Left Face
			this.faceLeft.moveTo(this.currentOriginX, this.currentOriginY);
			this.faceLeft.lineTo(this.currentOriginX, this.initialOriginY + this.hexRadius);
			this.faceLeft.lineTo(this.leftX, this.initialOriginY + (this.hexRadius / 2)); 
			this.faceLeft.lineTo(this.leftX, this.currentOriginY - (this.hexRadius / 2));
			this.faceLeft.closePath();
			
			// Right Face
			this.faceRight.moveTo(this.currentOriginX, this.currentOriginY);
			this.faceRight.lineTo(this.currentOriginX, this.initialOriginY + this.hexRadius);
			this.faceRight.lineTo(this.rightX, this.initialOriginY + (this.hexRadius / 2));
			this.faceRight.lineTo(this.rightX, this.currentOriginY - (this.hexRadius / 2));
			this.faceRight.closePath();
			
			// Top Face
			this.faceTop.moveTo(this.currentOriginX, this.currentOriginY);
			this.faceTop.lineTo(this.rightX, this.currentOriginY - (this.hexRadius / 2));
			this.faceTop.lineTo(this.initialOriginX, this.currentOriginY - this.hexRadius);
			this.faceTop.lineTo(this.leftX, this.currentOriginY - (this.hexRadius / 2));
			this.faceTop.closePath();
		}//end createCubeFaces
		
		// randomValue method
		public int randomValue() {
			// return a random value between 0 and 255
			// this function just makes it so defining a color at random
			// isn't longer than the screen width when reading the code.
			return (int)(Math.random() * 256);
		}// end randomValue
		
		// updateCubeColorsRGB method
		public void updateCubeColorsRGB() {
			// randomize the colors for the cube initially
			this.faceLeftColor = new Color(this.randomValue(), this.randomValue(), this.randomValue());
			this.faceRightColor = new Color(this.randomValue(), this.randomValue(), this.randomValue());
			this.faceTopColor = new Color(this.randomValue(), this.randomValue(), this.randomValue());
		}// end updateCubeColors
		
		// updateCubeColorsHSB method
		public void updateCubeColorsHSB() {
			// Add to the hue of the faces when moving the mouse.
			// Float array to hold HSB values from the face color
			float[] HSBArr;
			
			// left face color
			//get the rgb components from the color
			int rComp = this.faceLeftColor.getRed();
			int gComp = this.faceLeftColor.getGreen();
			int bComp = this.faceLeftColor.getBlue();
			
			// get the current HSB values for the face
			HSBArr = Color.RGBtoHSB(rComp, gComp, bComp, null);
			
			// add to the hue value, wrap around if necessary
			HSBArr[0] = HSBArr[0] + 0.01f;
			if (HSBArr[0] > 1.0f) {
				HSBArr[0] = 0.0f; 
			}
			this.faceLeftColor = Color.getHSBColor(HSBArr[0], HSBArr[1], HSBArr[2]);
			
			// right face color
			rComp = this.faceRightColor.getRed();
			gComp = this.faceRightColor.getGreen();
			bComp = this.faceRightColor.getBlue();
			
			HSBArr = Color.RGBtoHSB(rComp, gComp, bComp, null);
			
			HSBArr[0] = HSBArr[0] + 0.01f;
			if (HSBArr[0] > 1.0f) {
				HSBArr[0] = 0.0f; 
			}
			this.faceRightColor = Color.getHSBColor(HSBArr[0], HSBArr[1], HSBArr[2]);
			
			// top face color
			rComp = this.faceTopColor.getRed();
			gComp = this.faceTopColor.getGreen();
			bComp = this.faceTopColor.getBlue();
			
			HSBArr = Color.RGBtoHSB(rComp, gComp, bComp, null);
			
			HSBArr[0] = HSBArr[0] + 0.01f;
			if (HSBArr[0] > 1.0f) {
				HSBArr[0] = 0.0f; 
			}
			this.faceTopColor = Color.getHSBColor(HSBArr[0], HSBArr[1], HSBArr[2]);
		}//end updateCubeColorsHSV

		// mouseClicked method
		public void mouseClicked(MouseEvent e) {
			this.updateCubeColorsRGB();
		}// end mouseClicked

		// mouseMoved method
		public void mouseMoved(MouseEvent e) {
			this.updateCubeColorsHSB();
		}// end mouseMoved
		
		// actionPerformed method
		public void actionPerformed(ActionEvent e) {
			//if dx or dy aren't 0, then redraw the cube
			if (this.dx != 0 || this.dy != 0) {
				this.createCubeFaces();
			}
			 
			this.repaint();
		}// end actionPerformed
		
		// keyPressed method
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_UP) {
				this.dy = -4;
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				this.dy = 4;
			}
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				this.dx = -2;
			}
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				this.dx = 2;
			}
			
		}// end keyPressed method
		
		// keyReleased method
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_UP) {
				this.dy = 0;
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				this.dy = 0;
			}
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				this.dx = 0;
			}
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				this.dx = 0;
			}
		}// end keyReleased method
		
		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {}

		public void mouseReleased(MouseEvent e) {}
		
		public void mouseDragged(MouseEvent e) {}
		
		public void keyTyped(KeyEvent e) {}
		
	}
} // end GraphicDemo
