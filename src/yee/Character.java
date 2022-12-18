package yee;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

import yee.Character;

import java.util.*;

import java.math.*;


public class Character extends JLabel{
	
	public Double[] acceleration = new Double[2];
	
	public Double[] velocity = new Double[2];
	
	public Double[] position = new Double[2];
	
	public int[] size = new int[2];
	
	public static int MAX_SPEED = 10;
	
	public static int JUMP_HEIGHT;
	
	public boolean isAlive = true;
	
	
	public Character(int x, int y) {
		
		acceleration[0] = 0.0;
		acceleration[1] = 0.0;
		velocity[0] = 0.0;
		velocity[1] = 0.0;
		position[0] = (double) x;
		position[1] = (double) y;
		size[0] = 50;
		size[1] = 50;
		
		setText(":D");
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);
		setOpaque(true);
		setBackground(new Color(100,130,240));
		setForeground(new Color(255,255,255));
		
		setSize(50,50);
		setLocation((int)(this.position[0] - this.size[0]/2), (int)(this.position[1] - this.size[1]/2));
		
		add(new detectKeyPress(this));
		
		setVisible(true);
		
		
		Thread thread1 = new Thread() {
			public void run() {
				move();
			}
		};
		
		thread1.start();
		
		Thread thread2 = new Thread() {
			public void run() {
				friction();
			}
		};
		
		thread2.start();
		
		Thread thread3 = new Thread() {
			public void run() {
				gravity();
			}
		};
		
		thread3.start();
	}
	
	public void move() {
		while(true) {
			ArrayList<Thing> temp = new ArrayList<Thing>();
			for(int i = 0; i < Thing.allThings.size(); i++) {
				temp.add(Thing.allThings.get(i));
			}
			for(Thing thing : temp) {
				if(isInside(thing)&&!isOn(thing)) {
					if(isAlive && thing.color.equals(new Color(255,0,0))) {
						isAlive = false;
					}else {
						System.out.println("Inside " + thing);
						getRepelled(thing);
					}
				}
			}
			
			velocity[0] += acceleration[0];
			velocity[1] += acceleration[1];
			
			position[0] += velocity[0];
			position[1] += velocity[1];
			
			setLocation((int)(this.position[0] - this.size[0]/2), (int)(this.position[1] - this.size[1]/2));
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void gravity() {
		while(true) {
			if(!touchingGround()){
				velocity[1] += 1.0;
			}else {
				acceleration[1] = 0.0;
				velocity[1] = 0.0;
			}
			
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void friction() {
		while(true) {
			if(velocity[0] < 20 && velocity[0] < 0) {
				velocity[0] ++;
			}else if(velocity[0] < 20 && velocity[0] > 0){
				velocity[0] --;
			}
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public double getAngleOfDetection(Thing thing) {
		return Math.atan((double)thing.size[1]/thing.size[0]);
	}
	
	public void getRepelled(Thing thing) {
		double relativeAngle = radianToDegrees(getAngle(thing.position[0] - position[0],position[1] - thing.position[1]));
		double angle = radianToDegrees(getAngleOfDetection(thing));
		//thing is above
		if(relativeAngle >= angle && relativeAngle < 180 - angle) {
			if(velocity[1] == 0) {
				position[1] += 1;
			}else {
				velocity[1] = Math.abs(velocity[1])/2;
			}
		}
		//thing is left
		else if(relativeAngle >= 180 - angle && relativeAngle < 180 + angle) {
			if(velocity[0] == 0) {
				position[0] += 1;
			}else {
				velocity[0] = Math.abs(velocity[0])/2;
			}
		}
		//thing is below
		else if(relativeAngle >= 180 + angle && relativeAngle < 360 - angle) {
			position[1] += -1;
		}
		//thing is right
		else if(relativeAngle >= 360 - angle || relativeAngle < angle) {
			if(velocity[0] == 0) {
				position[0] += -1;
			}else {
				velocity[0] = -Math.abs(velocity[0])/2;
			}
		}
	}
	
	public boolean isInside(Thing c) {
		boolean overlapx = ((position[0] - size[0]/2) <= (c.position[0] + c.size[0]/2)) && ((position[0] + size[0]/2) >= (c.position[0] - c.size[0]/2));
		boolean overlapy = ((position[1] - size[1]/2) <= (c.position[1] + c.size[1]/2)) && ((position[1] + size[1]/2) >= (c.position[1] - c.size[1]/2));
		if(overlapx && overlapy) {
			return true;
		}
		return false;
	}
	
	public boolean isOn(Thing c) {
		boolean onTop = (position[1] + size[1]/2) == (c.position[1] - c.size[1]/2);
		if(onTop) {
			return true;
		}
		return false;
	}
	
	public boolean onGround() {
		for(Thing thing : Thing.allThings) {
			if(thing.isGround && isOn(thing) && Math.sin((position[1] - thing.position[1])/(position[0] - thing.position[0])) < Math.PI){
				return true;
			}
		}
		return false;
	}
	
	public boolean touchingGround() {
		for(Thing thing : Thing.allThings) {
			if(thing.isGround && isInside(thing) && Math.sin((position[1] - thing.position[1])/(position[0] - thing.position[0])) < Math.PI){
				return true;
			}
		}
		return false;
	}
	
	public static double radianToDegrees(double rad) {
		return rad * 180/Math.PI;
	}
	
	public static double getAngle(double x, double y) {
		double hyp = Math.sqrt(x*x + y*y);
		
		if(x >= 0 && y > 0) {
			return(Math.asin(y/hyp));
			
		}else if(x < 0 && y > 0) {
			return(Math.PI - Math.asin(y/hyp));
			
		}else if(x < 0 && y <= 0) {
			return((Math.atan(y/x) + Math.PI));
			
		}else if(x > 0 && y < 0){
			return(Math.PI*2 - Math.acos(x/hyp));
			
		}else if(x == 0 && y < 0){
			
			return(Math.acos(x/hyp) + Math.PI);
			
		}else {
			return(0);
		}
		
	}
	
	public class detectKeyPress extends JPanel {

        public detectKeyPress(Character c) {
            InputMap im = getInputMap();
            ActionMap am = getActionMap();
            
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "w");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "a");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "d");
            am.put("w", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Jump!");
                	//jump
                    c.velocity[1] = -10.0;
                }
            });
            am.put("a", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(Math.abs(c.velocity[0]) < Character.MAX_SPEED) {
                    	c.velocity[0] = -10.0;
                    }
                }
            });
            am.put("d", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(Math.abs(c.velocity[0]) < Character.MAX_SPEED) {
                  	  c.velocity[0] = 10.0;
                    }
                }
            });
            

            setFocusable(true);
            requestFocusInWindow();        
        }

    }
	
	public void kill() {
		setText(":(");
		setBackground(new Color(100,100,100));
		
	}
	

}
