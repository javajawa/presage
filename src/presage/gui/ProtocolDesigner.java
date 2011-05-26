package presage.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import presage.demo.SPVAnimatorClick;

public class ProtocolDesigner extends JPanel implements MouseListener {

	
	
	
	
	public void mouseEntered(MouseEvent me) {}
	
	public void mouseExited(MouseEvent me) {}

	public void mousePressed(MouseEvent me) {}
	
	public void mouseClicked(MouseEvent me) {}
	
	public void mouseReleased(MouseEvent me) {}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame f = new JFrame();
		f.getContentPane().add(new SPVAnimatorClick("Agent0000"));

		f.setSize(200, 200);
		f.show();
		
		
	}

}
