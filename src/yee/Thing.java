package yee;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.*;

public class Thing extends JLabel{
	public boolean isGround;
	public Color color;
	
	public static ArrayList<Thing> allThings = new ArrayList<Thing>();
	
	public Double[] position = new Double[2];
	
	public int[] size = new int[2];

	public Thing(int x, int y, int width, int height, boolean ground, Color color, String label) {
		isGround = ground;
		this.color = color;
		
		position[0] = (double) x + width/2;
		position[1] = (double) y + height/2;
		size[0] = width;
		size[1] = height;
		
		allThings.add(this);
		
		setText(label);
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);
		setOpaque(true);
		setBackground(color);
		setForeground(new Color(255,255,255));
		
		setSize(size[0], size[1]);
		setLocation(x, y);
		
		setVisible(true);
		
	}
	
	public void moveLeft(Character c) {
		while(c.isAlive && position[0] > -100) {
			position[0] -= 5;
			setLocation((int)(this.position[0] - this.size[0]/2), (int)(this.position[1] - this.size[1]/2));
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String toString() {
		return getText();
	}
}
