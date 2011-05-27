package example.plugins;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import example.ExampleEnvDataModel;
import example.EnvPlayerModel;
import example.inputs.Target;

import presage.Plugin;
import presage.Simulation;

public class ExampleEnvironmentViewer extends JPanel implements Plugin {

	Simulation sim;
	
	Color pConnectionColor = Color.BLACK;
	Color lConnectionColor = Color.ORANGE;
	Color goalColor = Color.BLUE;
	Color commandColor = Color.GREEN;
	Color reconColor = Color.MAGENTA;
	
	ExampleEnvDataModel dmodel;

	public ExampleEnvironmentViewer() {}

	public void paint(Graphics g){

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, dmodel.WIDTH, dmodel.HEIGHT);
		
		g.setColor(Color.blue);
		// offscr.drawString("Visualisation Software", 5, 15);
		g.drawString("Cycle = " + dmodel.time, 5, 15);

		g.setColor(Color.RED);
		g.draw3DRect(dmodel.safeboxX0, dmodel.safeboxY0, dmodel.safeboxX1 - dmodel.safeboxX0, dmodel.safeboxY1 - dmodel.safeboxY0, true);
		
		drawLogicalNetwork(g);
		drawPhysicalNetwork(g);

		EnvPlayerModel player;
		Iterator<String> iterator = dmodel.playermodels.keySet().iterator();

		while (iterator.hasNext()) {
			player = dmodel.playermodels.get(iterator.next());
			drawParticipant(g, player);
		}
	}

	public void drawParticipant(Graphics g, EnvPlayerModel pm){
		Color color;
		if (pm.roles.contains("command")){
			color = commandColor;
		} else {
			color = reconColor;
		}

		float[] colorArray = color.getRGBColorComponents(null);
		// System.out.println(colorArray[0] +" "+ colorArray[1] +" "+
		// colorArray[2]);
		Color rangeColor = new Color(colorArray[0], colorArray[1], colorArray[2],(float) 0.04);

		g.setColor(rangeColor);
		g.fillOval(pm.positionX - (pm.wirelessRange), pm.positionY - (pm.wirelessRange),
				pm.wirelessRange * 2, pm.wirelessRange * 2);


		g.setColor(color);
		g.drawString(pm.participantId, (pm.positionX - 5), (pm.positionY - 5));
		g.fillOval(pm.positionX - 5, pm.positionY - 5, 10, 10);

		color = goalColor;
		Iterator<Target> iterator = pm.targets.iterator();
		while (iterator.hasNext()) {
			Target goal = iterator.next();
			g.drawRect(goal.getRegion().x, goal.getRegion().y, goal.getRegion().width, goal.getRegion().height);
			g.drawString("Goal: "+ goal.getOwner() + "<"+ goal.getRegion().x + ", "+ goal.getRegion().y + ">", (goal.getRegion().x), (goal.getRegion().y));
		}
	}


	public void drawLogicalNetwork(Graphics g) {
		g.setColor(lConnectionColor);
		g.drawString("Logical Network", 5, 45);

		// iterate over the logical topology
		for (int row = 0; row < dmodel.logicalTopology.length; row++) {
			for (int col = 0; col < dmodel.logicalTopology.length; col++) {

				// if (col >= row) {
				// break;
				// }
				//				
				if (dmodel.logicalTopology[row][col]) {
					// draw a line between the nodes if there is a connection.
					String name0 = dmodel.indexToName[row];
					String name1 = dmodel.indexToName[col];

					g.drawLine(dmodel.playermodels.get(name0).positionX,  dmodel.playermodels.get(name0).positionY + 1, 
							dmodel.playermodels.get(name1).positionX, dmodel.playermodels.get(name1).positionY + 1);

				}

			}
		}
	}

	public void drawPhysicalNetwork(Graphics g) {
		g.setColor(pConnectionColor);
		g.drawString("Physical Network", 5, 30);

		// iterate over the physical topology
		for (int row = 0; row < dmodel.physicalTopology.length; row++) {
			for (int col = 0; col < dmodel.physicalTopology.length; col++) {
				// if (col >= row) {
				// break;
				// }
				if (dmodel.physicalTopology[row][col]) {
					// draw a line between the nodes if there is a connection.

					String name0 = dmodel.indexToName[row];
					String name1 = dmodel.indexToName[col];

					g.drawLine(dmodel.playermodels.get(name0).positionX,  dmodel.playermodels.get(name0).positionY + 1, 
							dmodel.playermodels.get(name1).positionX, dmodel.playermodels.get(name1).positionY + 1);

				}
			}
		}
	}

	public void execute() {
		// simply get the environment datamodel
		dmodel = (ExampleEnvDataModel)sim.getEnvDataModel();
		// and call repaint
		repaint();
	
	}

	public String getLabel() {
		return "ExampleEnvironmentViewer";
	}

	public String getShortLabel() {
		return "ExampleEnvironmentViewer";
	}

	public void initialise(Simulation sim) {
		// super("Physical World and Network Animator Prototype");
		System.out.println(" -Initialising....");

		this.sim = sim; 
		
		setBackground(Color.GRAY);

		dmodel = (ExampleEnvDataModel)sim.getEnvDataModel();
		
		repaint();
	}

	public void onDelete() {}

	public void onSimulationComplete() {	
		
		BufferedImage image = new BufferedImage(dmodel.WIDTH, dmodel.HEIGHT, BufferedImage.TYPE_INT_BGR);
		Graphics g = image.createGraphics();
		
		this.paint(g);
		
		try {
			ImageIO.write( image, "bmp", new File ( "E:/networkimage_"+ dmodel.time +".jpg" ) );
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
