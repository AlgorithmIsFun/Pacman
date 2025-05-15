import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class MoveImage extends JPanel implements KeyListener, MouseListener {

    private ImageIcon imageIcon, backgroundImage, whitedot, ghost;
    private int x, y;
    private int ghostx, ghosty;
    private int direction, ghostdirection; //1=up, 0=right, -1=down, 2=left
    private static int MAXL = 500;
    
    private int[][] dots = {
    		// 6 rows
    		{33, 50, 1}, {53, 50, 1}, {73, 50, 1}, {93, 50, 1}, {113, 50, 1}, {133, 50, 1}, {153, 50, 1}, {173, 50, 1}, {193, 50, 1}, {213, 50, 1}, {273, 50, 1}, {293, 50, 1}, {313, 50, 1}, {333, 50, 1}, {353, 50, 1}, {373, 50, 1}, {393, 50, 1}, {413, 50, 1}, {433, 50, 1}, {453, 50, 1}, {465, 50, 1},
    		{33, 110, 1}, {53, 110, 1}, {73, 110, 1}, {93, 110, 1}, {113, 110, 1}, {133, 110, 1}, {153, 110, 1}, {173, 110, 1}, {193, 110, 1}, {213, 110, 1}, {233, 110, 1}, {253, 110, 1}, {273, 110, 1}, {293, 110, 1}, {313, 110, 1}, {333, 110, 1}, {353, 110, 1}, {373, 110, 1}, {393, 110, 1}, {413, 110, 1}, {433, 110, 1}, {453, 110, 1}, {465, 110, 1},
    		{33, 150, 1}, {53, 150, 1}, {73, 150, 1}, {93, 150, 1}, {113, 150, 1}, {173, 150, 1}, {193, 150, 1}, {213, 150, 1}, {273, 150, 1}, {293, 150, 1}, {313, 150, 1}, {373, 150, 1}, {393, 150, 1}, {413, 150, 1}, {433, 150, 1}, {453, 150, 1}, {465, 150, 1},
    		{33, 330, 1}, {53, 330, 1}, {73, 330, 1}, {93, 330, 1}, {113, 330, 1}, {133, 330, 1}, {153, 330, 1}, {173, 330, 1}, {193, 330, 1}, {213, 330, 1}, {273, 330, 1}, {293, 330, 1}, {313, 330, 1}, {333, 330, 1}, {353, 330, 1}, {373, 330, 1}, {393, 330, 1}, {413, 330, 1}, {433, 330, 1}, {453, 330, 1},
    		{33, 420, 1}, {53, 420, 1}, {73, 420, 1}, {93, 420, 1}, {113, 420, 1}, {173, 420, 1}, {193, 420, 1}, {213, 420, 1}, {273, 420, 1}, {293, 420, 1}, {313, 420, 1}, {373, 420, 1}, {393, 420, 1}, {413, 420, 1}, {433, 420, 1}, {453, 420, 1},
    		{33, 375, 1}, {53, 375, 1}, {73, 375, 1}, {113, 375, 1}, {133, 375, 1}, {153, 375, 1}, {173, 375, 1}, {193, 375, 1}, {213, 375, 1}, {233, 375, 1}, {253, 375, 1}, {273, 375, 1}, {293, 375, 1}, {313, 375, 1}, {333, 375, 1}, {353, 375, 1}, {373, 375, 1}, {433, 375, 1}, {453, 375, 1},
    		{33, 465, 1}, {53, 465, 1}, {73, 465, 1}, {93, 465, 1}, {113, 465, 1}, {133, 465, 1}, {153, 465, 1}, {173, 465, 1}, {193, 465, 1}, {213, 465, 1}, {233, 465, 1}, {253, 465, 1}, {273, 465, 1}, {293, 465, 1}, {313, 465, 1}, {333, 465, 1}, {353, 465, 1}, {373, 465, 1}, {393, 465, 1}, {413, 465, 1}, {433, 465, 1}, {453, 465, 1},
    		// Empty spaces top half
    		{33, 80, 1}, {120, 80, 1}, {220, 80, 1},{273, 80, 1}, {373, 80, 1}, {465, 80, 1},
    		{33, 130, 1}, {120, 130, 1}, {170, 130, 1},{323, 130, 1}, {373, 130, 1}, {465, 130, 1},
    		{120, 175, 1}, {220, 175, 1}, {273, 175, 1}, {373, 175, 1},
    		{145, 240, 1}, {350, 240, 1},
    		{173, 310, 1}, {323, 310, 1},
    		{33, 350, 1}, {120, 350, 1}, {220, 350, 1}, {273, 350, 1}, {373, 350, 1}, {465, 350, 1},
    		{70, 400, 1}, {120, 400, 1}, {170, 400, 1}, {323, 400, 1}, {373, 400, 1}, {430, 400, 1},
    		{33, 440, 1}, {220, 440, 1}, {273, 440, 1}, {460, 440, 1},
    		//2 Columns
    		{120, 200, 1}, {120, 225, 1}, {120, 250, 1}, {120, 275, 1}, {120, 300, 1},
    		{373, 200, 1}, {373, 225, 1}, {373, 250, 1}, {373, 275, 1}, {373, 300, 1}, 
    		};
    private int ndots = dots.length;
    //1
    private int movementspeed = 1;
    //10
    private static int delay = 10;
    public MoveImage() {

        imageIcon = new ImageIcon("C:\\Users\\abdul\\eclipse-workspace\\MoveImage\\download.png"); // Replace with your image path
        Image newimg = imageIcon.getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newimg);
        backgroundImage = new ImageIcon("C:\\Users\\abdul\\eclipse-workspace\\MoveImage\\background.png");
        newimg = backgroundImage.getImage().getScaledInstance(480, 460, java.awt.Image.SCALE_SMOOTH);
        backgroundImage = new ImageIcon(newimg);
        whitedot = new ImageIcon("C:\\Users\\abdul\\eclipse-workspace\\MoveImage\\whitedot.png");
        newimg = whitedot.getImage().getScaledInstance(5, 5, java.awt.Image.SCALE_SMOOTH);
        whitedot = new ImageIcon(newimg);
        ghost = new ImageIcon("C:\\Users\\abdul\\eclipse-workspace\\MoveImage\\ghost.png");
        newimg = ghost.getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        ghost = new ImageIcon(newimg);
        ghostx = 250;
        ghosty = 190;
        x = 240;
        y = 370;
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
    
    }
    public void reset() {
    	for (int i = 0; i < ndots; i++) {
    		dots[i][2] = 1;
    	}
    	x = 240;
        y = 370;
        ghostx = 250;
        ghosty = 190;
    }
    //Wall: 300, 300		350, 350
    public void barrier( int change, int ghost_bot) {
    	boolean hit = false;
    	if (ghost_bot == 0) {
	    	if (x < 25) {
	    		x = 25;
	    		hit = true;
	    	}
	    	if (y < 42) {
	    		y = 42;
	    		hit = true;
	    	}
	    	if (x > MAXL-40) {
	    		x = MAXL-40;
	    		hit = true;
	    	}
	    	if (y > MAXL-40) {
	    		y = MAXL-40;
	    		hit = true;
	    	}
    	} else {
    		if (ghostx < 25) {
    			ghostx = 25;
	    		hit = true;
	    	}
	    	if (ghosty < 42) {
	    		ghosty = 42;
	    		hit = true;
	    	}
	    	if (ghostx > MAXL-40) {
	    		ghostx = MAXL-40;
	    		hit = true;
	    	}
	    	if (ghosty > MAXL-40) {
	    		ghosty = MAXL-40;
	    		hit = true;
	    	}
    	}
    	
    	//Level 1
    	hit = hit || wall(change, 33, 47, 110, 102, ghost_bot);
    	hit = hit || wall(change, 118, 47, 212, 102, ghost_bot);
    	hit = hit || wall(change, 222, 40, 264, 102, ghost_bot);
    	hit = hit || wall(change, 273, 47, 367, 102, ghost_bot);
    	hit = hit || wall(change, 375, 47, 453, 102, ghost_bot);
    	//Level 2
    	hit = hit || wall(change, 33, 106, 110, 146, ghost_bot);
    	hit = hit || wall(change, 117, 106, 161, 235, ghost_bot);
    	hit = hit || wall(change, 170, 106, 315, 146, ghost_bot);
    	hit = hit || wall(change, 325, 106, 367, 235, ghost_bot);
    	hit = hit || wall(change, 375, 106, 453, 146, ghost_bot);
    	//Level 3
    	hit = hit || wall(change, 25, 150, 110, 325, ghost_bot);
    	hit = hit || wall(change, 120, 150, 210, 190, ghost_bot);
    	hit = hit || wall(change, 220, 130, 265, 190, ghost_bot);
    	hit = hit || wall(change, 272, 150, 340, 190, ghost_bot);
    	hit = hit || wall(change, 375, 150, 487, 325, ghost_bot);
    	//Middle
    	hit = hit || wall(change, 170, 196, 315, 280, ghost_bot);
    	//Level 4
    	hit = hit || wall(change, 120, 240, 160, 325, ghost_bot);
    	hit = hit || wall(change, 170, 285, 315, 325, ghost_bot);
    	hit = hit || wall(change, 325, 240, 367, 325, ghost_bot);
    	//Level 5
    	hit = hit || wall(change, 33, 330, 110, 370, ghost_bot);
    	hit = hit || wall(change, 118, 330, 212, 370, ghost_bot);
    	hit = hit || wall(change, 222, 308, 265, 370, ghost_bot);
    	hit = hit || wall(change, 272, 330, 367, 370, ghost_bot);
    	hit = hit || wall(change, 375, 330, 453, 370, ghost_bot);
    	//Level 6
    	hit = hit || wall(change, 25, 373, 60, 413, ghost_bot);
    	hit = hit || wall(change, 67, 352, 110, 413, ghost_bot);
    	hit = hit || wall(change, 118, 375, 161, 435, ghost_bot);
    	hit = hit || wall(change, 171, 375, 315, 413, ghost_bot);
    	hit = hit || wall(change, 325, 375, 368, 435, ghost_bot);
    	hit = hit || wall(change, 375, 352, 418, 413, ghost_bot);
    	hit = hit || wall(change, 427, 373, 480, 413, ghost_bot);
    	//Level 7
    	hit = hit || wall(change, 32, 419, 211, 457, ghost_bot);
    	hit = hit || wall(change, 222, 396, 264, 457, ghost_bot);
    	hit = hit || wall(change, 273, 419, 455, 457, ghost_bot);
    	if (ghost_bot == 1 && hit) {
    		System.out.println("Ghost has hit wall");
    		 int randomNumber = (int)(Math.random() * 4) + 1;
    		 ghostdirection = randomNumber - 2;
    		 
    	}
    	if (ghost_bot == 0) {
    		for (int i = 0; i < ndots; i++) {
        		check_dot(i);
        	}
    	}	
    }
    public void check_dot(int dotnum) {
    	if (intersect(x, y, 15, 15, dots[dotnum][0], dots[dotnum][1], 5, 5)) {
    		dots[dotnum][2] = 0;
    	}
    }
    
    boolean intersect(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2)
    {
        boolean checkX = x1 < x2 && x1+w1 > x2 || x1 < x2+w2 && x1+w1 > x2+w2 || x1 > x2 && x1+w1 < x2+w2 || x1 < x2 && x1+w1 > x2+w2; 
        boolean checkY = y1 < y2 && y1+h1 > y2 || y1 < y2+h2 && y1+h1 > y2+h2 || y1 > y2 && y1+h1 < y2+h2 || y1 < y2 && y1+h1 > y2+h2;

        return checkX && checkY;
    }
    public boolean wall(int change, int sposx, int sposy, int fposx, int fposy, int ghost_bot) {
    	if (ghost_bot == 0) {
    		if (x > sposx && x < fposx && y > sposy & y < fposy) {
        		if (change == 2) {
        			x += movementspeed;
        		} else if (change == 1){
        			y += movementspeed;
        		} else if (change == 0){
        			x -= movementspeed;
        		} else if (change == -1){
        			y -= movementspeed;
        		}
        		return true;
           	}
        	return false;
    	} else {
	    	if (ghostx > sposx && ghostx < fposx && ghosty > sposy & ghosty < fposy) {
	    		if (change == 2) {
	    			ghostx += movementspeed;
	    		} else if (change == 1){
	    			ghosty += movementspeed;
	    		} else if (change == 0){
	    			ghostx -= movementspeed;
	    		} else if (change == -1){
	    			ghosty -= movementspeed;
	    		}
	    		return true;
	       	}
	    	return false;
    	}
    }
    public void move_ghost() {
    	//System.out.println("Pacman before pos: " + x + " " + y);
    	if (ghostdirection == 2) {
    		ghostx -= movementspeed;
    	} else if (ghostdirection == 1) {
    		ghosty -= movementspeed;
    	} else if (ghostdirection == 0) {
    		ghostx += movementspeed;
    	} else if (ghostdirection == -1) {
    		ghosty += movementspeed;
    	}
    	barrier(ghostdirection, 1);
    	if (intersect(ghostx, ghosty, 15, 15, x, y, 15, 15)) {
    		reset();
    	}
    	repaint();
    }
    public void move_image() {
    	//System.out.println("Pacman before pos: " + x + " " + y);
    	if (direction == 2) {
    		x -= movementspeed;
    	} else if (direction == 1) {
    		y -= movementspeed;
    	} else if (direction == 0) {
    		x += movementspeed;
    	} else if (direction == -1) {
    		y += movementspeed;
    	}
    	barrier(direction, 0);
    	move_ghost();
    	
    	repaint();
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(backgroundImage.getImage(), 10, 30, this);
        boolean win = true;
        for (int i = 0; i < ndots; i++) {
        	if (dots[i][2] == 1) {
        		win = false;
        		g.drawImage(whitedot.getImage(), dots[i][0], dots[i][1], this);
        	}
        }
        if (win) {
        	reset();
        }
        g.drawImage(imageIcon.getImage(), x, y, this);
        g.drawImage(ghost.getImage(), ghostx, ghosty, this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
    	int rot = 0;
    	int prev_direction = direction;
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
            	direction = 1;
                break;
            case KeyEvent.VK_DOWN:
            	direction = -1;
                break;
            case KeyEvent.VK_LEFT:
            	direction = 2;
                break;
            case KeyEvent.VK_RIGHT:
            	direction = 0;
                break;
        }
        if (prev_direction == direction) {
        	rot = 0;
        } else if (prev_direction == 1) {
        	if (direction == 0) {
        		rot = 90;
        	} else if (direction == -1) {
        		rot = 180;
        	} else if (direction == 2) {
        		rot = 270;
        	}
        } else if (prev_direction == 0) {
        	if (direction == -1) {
        		rot = 90;
        	} else if (direction == 2) {
        		rot = 180;
        	} else if (direction == 1) {
        		rot = 270;
        	}
        } else if (prev_direction == -1) {
        	if (direction == 2) {
        		rot = 90;
        	} else if (direction == 1) {
        		rot = 180;
        	} else if (direction == 0) {
        		rot = 270;
        	}
        } else if (prev_direction == 2) {
        	if (direction == 1) {
        		rot = 90;
        	} else if (direction == 0) {
        		rot = 180;
        	} else if (direction == -1) {
        		rot = 270;
        	}
        }
        imageIcon = rotateImageIcon(imageIcon, rot);
    }
    public ImageIcon rotateImageIcon(ImageIcon imageIcon, int rot) {
		
    	Image image = imageIcon.getImage();
        int w = image.getWidth(null);
        int h = image.getHeight(null);

        BufferedImage rotated = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.rotate(Math.toRadians(rot), w / 2.0, h / 2.0); // rotate around center
        at.translate(0, 0);

        g2d.drawImage(image, at, null);
        g2d.dispose();

        return new ImageIcon(rotated);
    	
    }
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
    
    

    public static void main(String[] args) throws InterruptedException {
    	JFrame frame = new JFrame("Move Image with Arrow Keys");
    	MoveImage image = new MoveImage();
    	frame.add(image);
    	frame.setSize(MAXL+10, MAXL+30);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        SwingUtilities.invokeLater(() -> image.setVisible(true));
        while(true) {
        	Thread.sleep(delay);
        	image.move_image();
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
    	int x = e.getX();
        int y = e.getY();
        System.out.println("Mouse clicked at: (" + x + ", " + y + ")");
    }
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}