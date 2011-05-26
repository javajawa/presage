package chainstore;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.simpleframework.xml.Element;



import presage.*;
import presage.annotations.EventConstructor;

public class ChainstoreEvents {

	public static class DrawGraph implements Event {

		@Element
		String outputpath;

		@Element
		int outputwidth;

		@Element
		int outputheight;

		@Element
		int updaterate;

		String title = "Percentage of Compliance";

		String xaxis = "Round";

		String yaxis = "% Compliance";

		String label = "% Compliance";

		ArbiterDataModel dmodel;

		XYSeriesCollection data;

		// ChartPanel chartPanel;

		// XYSeriesCollection datatempcopy;

		JPanel control = new JPanel();

		ChartPanel chartPanel;
		
		public DrawGraph(){
		
		}
		@EventConstructor( {"outputpath", "outputwidth", "outputheight", "updaterate"})
		public DrawGraph(String outputpath, int outputwidth, int outputheight, int updaterate){
			
			this.outputpath = outputpath;
			this.outputwidth = outputwidth;
			this.outputheight = outputheight;
			this.updaterate = updaterate;
			
		}

		public void execute(Simulation sim) {		
			dmodel = (ArbiterDataModel) sim.getPlayerDataModel("Arbiter");
			
			data = new XYSeriesCollection();
			
			XYSeries series = new XYSeries("compliance");
			data.addSeries(series);
			
			
			
			
			int n = dmodel.getCompetitorActions().size();
			
			if (n == 0)
				return;
			
			int compliant = 0;
			Iterator iterator = dmodel.getCompetitorActions().keySet().iterator();
			while (iterator.hasNext()) {
				String cptrId = (String)iterator.next();
				if (dmodel.getCompetitorActions().get(cptrId).get(dmodel.getCompetitorActions().get(cptrId).size()) == 0)
					 compliant++;
			}
			
			System.out.println("infected = " +  compliant + ", nodes = " + n + ", infected/n = " +  compliant / n);
			
			series.add(dmodel.time, ((double) compliant / n)*100);
			
			JFreeChart chart = ChartFactory.createXYLineChart(title, xaxis, yaxis,
					data, PlotOrientation.VERTICAL, true, true, false);

			chartPanel = new ChartPanel(chart);
			
			// chartPanel.setPreferredSize(new java.awt.Dimension(this.getWidth(),
			// 		this.getHeight()));

			File file;

			try {
				file = new File(outputpath);
			} catch (Exception e) {
				System.err.println("Graph Plugin " + label
						+ " : Invalid output path" + e);
				return;
			}

			try {
				int width = 1920;
				int height = 1200;
				ChartUtilities.saveChartAsPNG(file, chartPanel.getChart(), width,
						height);
			} catch (Exception e) {
				System.out.println("Problem occurred creating chart." + label);
			}
		}
		
		public String toString(){
			return getShortLabel();
		}
		
		public String getShortLabel()
		{ // This is worth doing if you want the gui components to look right
			return this.getClass().getName() + "(" + ");";		
		}

	}
	
	
	
	public static class PrintResults implements Event {

		public PrintResults(){
		
		}
		@EventConstructor({"n"})
		public PrintResults(int n){

		}

		public void execute(Simulation sim) {		
			ArbiterDataModel dmodel = (ArbiterDataModel) sim.getPlayerDataModel("Arbiter");
			
			System.out.println();
			System.out.print("Author Actions, ");
			Iterator it = dmodel.getAuthorActions().iterator();
			while (it.hasNext()) {
				System.out.print(it.next() + ",");
			}
			
			System.out.println();
			System.out.println();
			
			System.out.print("Author Score, ");
			it = dmodel.getAuthorScore().iterator();
			while (it.hasNext()) {
				System.out.print(it.next() + ",");
			}
			
			System.out.println();
			System.out.println();
			
			TreeMap<String, ArrayList<Integer>> competitorActions = dmodel.getCompetitorActions();
			
			it = competitorActions.keySet().iterator();
			while (it.hasNext()){
				String cptrId = (String)it.next();
				System.out.print(cptrId + ",");
				ArrayList<Integer> cptrActs = competitorActions.get(cptrId);
				Iterator it_cptracts = cptrActs.iterator();
				while (it_cptracts.hasNext()){
					System.out.print(it_cptracts.next() + ",");
				}
				System.out.println();
			}
			
			System.out.println();
			System.out.println();
			
			TreeMap<String, ArrayList<Integer>> competitorScores = dmodel.getCompetitorScore();
			
			it = competitorScores.keySet().iterator();
			while (it.hasNext()){
				String cptrId = (String)it.next();
				System.out.print(cptrId + ",");
				ArrayList<Integer> scores = competitorScores.get(cptrId);
				Iterator it_cptrscores = scores.iterator();
				while (it_cptrscores.hasNext()){
					System.out.print(it_cptrscores.next() + ",");
				}
				System.out.println();
			}
			
			
		}
		
		public String toString(){
			return getShortLabel();
		}
		
		public String getShortLabel()
		{ // This is worth doing if you want the gui components to look right
			return this.getClass().getName() + "(" + ");";		
		}

	}
	

	public static class FileResults implements Event {

		@Element
		String filename;
		
		public FileResults(){
		
		}
		@EventConstructor({"filename"})
		public FileResults(String filename){
			this.filename = filename;
		}

		public void execute(Simulation sim) {		
			ArbiterDataModel dmodel = (ArbiterDataModel) sim.getPlayerDataModel("Arbiter");
			
			
			try {
			
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			
			out.write("Author Actions, ");
			
			Iterator it = dmodel.getAuthorActions().iterator();
			while (it.hasNext()) {
				out.write(it.next() + ",");
			}
			
			out.newLine();
			out.newLine();
			
			out.write("Author Score, ");
			it = dmodel.getAuthorScore().iterator();
			while (it.hasNext()) {
				out.write(it.next() + ",");
			}
			
			out.newLine();
			out.newLine();
			
			
			TreeMap<String, ArrayList<Integer>> competitorActions = dmodel.getCompetitorActions();
			
			it = competitorActions.keySet().iterator();
			while (it.hasNext()){
				String cptrId = (String)it.next();
				out.write(cptrId + ",");
				ArrayList<Integer> cptrActs = competitorActions.get(cptrId);
				Iterator it_cptracts = cptrActs.iterator();
				while (it_cptracts.hasNext()){
					out.write(it_cptracts.next() + ",");
				}
				out.newLine();
			}
			
			out.newLine();
			out.newLine();
			
			TreeMap<String, ArrayList<Integer>> competitorScores = dmodel.getCompetitorScore();
			
			it = competitorScores.keySet().iterator();
			while (it.hasNext()){
				String cptrId = (String)it.next();
				out.write(cptrId + ",");
				ArrayList<Integer> scores = competitorScores.get(cptrId);
				Iterator it_cptrscores = scores.iterator();
				while (it_cptrscores.hasNext()){
					out.write(it_cptrscores.next() + ",");
				}
				out.newLine();
			}
			
			out.close();
			
			}
			
			catch (IOException e)
			
			{
			
			System.out.println("Exception ");
			
			}

			
			
			
			
		}
		
		public String toString(){
			return getShortLabel();
		}
		
		public String getShortLabel()
		{ // This is worth doing if you want the gui components to look right
			return this.getClass().getName() + "(" + ");";		
		}

	}
	
	
}
