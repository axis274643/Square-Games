package yee;

import java.awt.*;

import javax.swing.*;

public class Main {
	static JFrame frame = new JFrame();
	static JPanel panel = new JPanel();
	
	static Character c;
	
	public static void main(String[] args) {
		frame.setSize(1500,800);
		frame.setLocation(0,0);
		
		panel.setLayout(null);
		
		
		frame.add(panel);
		
		c = new Character(500,300);
		panel.add(c);
		
		panel.add(new Thing(0,600,2000,200,true,new Color(0,0,0),"ground"));
		
		panel.setVisible(true);
		frame.setVisible(true);
		
		Thread thread = new Thread() {
			public void run() {
				flappyGame();
			}
		};
		
		thread.start();
	}	                                                                                                                                                                                              
	
	public static void parkourGame() {
		for(int i = 0; i < 10; i++) {
			Thing thing = new Thing((int)(Math.random() * 1200),(int)(Math.random() * 600),(int)(Math.random()*200)+50,(int)(Math.random()*200)+50,true,new Color(0,0,0),"block");
			panel.add(thing);
		}
	}
	
	public static void flappyGame() {
		while(c.isAlive) {
			Thing thing = new Thing(1500,(int)(Math.random() * 400 + 300),50,800,true,new Color(255,0,0),"bad");
			panel.add(thing);
			Thing otherTHing = new Thing(1500,0,50,(int)(Math.random() * 100 + 100),true,new Color(255,0,0),"bad");
			panel.add(otherTHing);
			Thread thread = new Thread() {
				public void run() {
					thing.moveLeft(c);
					
				}
			};
			thread.start();
			
			Thread thread2 = new Thread() {
				public void run() {
					otherTHing.moveLeft(c);
				}
			};
			
			thread2.start();
			
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		c.kill();
		c = null;
		
		for(int i = 24; i >= 2; i--) {
			panel.setBackground(new Color(10 * i, 10 * i, 10 * i));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
