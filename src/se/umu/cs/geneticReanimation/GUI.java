package se.umu.cs.geneticReanimation;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import processing.core.*;



public class GUI extends JFrame {
	
	public static void main(String args[]) {
        //PApplet.main(new String[] {"se.umu.cs.geneticReanimation.Test" }); // "--present"
		new GUI();
    }
	
	public GUI () {
		super("Testing testing...");
		setLayout(new BorderLayout());
		
		PApplet embed = new Test();
		add(embed, BorderLayout.CENTER);
		JPanel bottom = new JPanel();
		JButton doNothing = new JButton("do nothing!");
		bottom.add(doNothing);
		add(bottom, BorderLayout.SOUTH);
		
		//VIKTIG
		embed.init();
		pack();
		this.setPreferredSize(new Dimension(900,600));
		this.setVisible(true);
		//this.setPreferredSize(new Dimension(900,600));
	}

}
