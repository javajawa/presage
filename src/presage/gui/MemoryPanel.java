package presage.gui;

import presage.Plugin;
import presage.Simulation;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import java.io.File;
import org.simpleframework.xml.Element;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

//import org.jfree.chart.Legend;
//import org.jfree.chart.StandardLegend;

public class MemoryPanel extends JPanel implements Runnable  {
	
	JFreeChart chart;
	ChartPanel chartPanel;
//	 create the dataset...
	XYSeriesCollection dataset; // = new DefaultCategoryDataset();

	 final  XYSeries series1 = new XYSeries("Free");
	 final  XYSeries series2 = new XYSeries("Used");
	 final  XYSeries series3 = new XYSeries("Allocated");  
	 // final  XYSeries series4 = new XYSeries("Xmax");
	
	long time;
	long starttime;
	// GraphPanel graphPanel;
	
	Thread t;
	long interval = 500;
	
	
	 
	 public MemoryPanel (){
		 
			dataset = new XYSeriesCollection();

			series2.add(0, Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());		
			series3.add(0, Runtime.getRuntime().totalMemory());	
			series1.add(0, Runtime.getRuntime().freeMemory());
			
			//series4.add(0, Runtime.getRuntime().maxMemory());
			
		  dataset.addSeries(series1);
	      dataset.addSeries(series2);
	      dataset.addSeries(series3);
	      //dataset.addSeries(series4);
		  
		 chart = ChartFactory.createXYLineChart(
		            "Java Virtual Machine Memory Status",      // chart title
		            "time (seconds)",                      // x axis label
		            "bytes",                      // y axis label
		            dataset,                  // data
		            PlotOrientation.VERTICAL,
		            true,                     // include legend
		            true,                     // tooltips
		            false                     // urls
		        );
	    
		 chart.setBackgroundPaint(Color.WHITE);
	
		 chart.getXYPlot().setBackgroundPaint(Color.white);
		
		 
		 //chart.getLegend().setAnchor();
		 
		chartPanel = new ChartPanel(chart);
		this.setLayout(new BorderLayout());
		this.add(chartPanel, BorderLayout.CENTER);
		 
		 
		t=new Thread(this);
		t.start();
	 }

	 public void run(){
		 
		 starttime = System.currentTimeMillis();
		 
			while (true) {

				time = System.currentTimeMillis() - starttime;
				
				try {

					// do the thread code here;
					
					execute(time/1000);
					
					Thread.sleep(interval);

				} catch (InterruptedException e){
				}

			}
			
		}
	 
	public void execute(long time) {
		// TODO Auto-generated method stub

		
		
		series1.add(time, Runtime.getRuntime().freeMemory());
		
		series2.add(time, Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());		
		
		series3.add(time, Runtime.getRuntime().totalMemory());	
		
		// series4.add(Simulation.getTime(), Runtime.getRuntime().maxMemory());

		
//		if (Simulation.getTime() == Simulation.getExperimentLength() -1){
//			
//			
//			
//			try {
//				// this.wait(1000);
//			
//		        ChartUtilities.saveChartAsJPEG(new File("C:/chart.jpg"), chart, 1000, 300);
//		    } catch (Exception e) {
//		        System.out.println("Problem occurred creating chart.");
//		    }
//			
//		}
		
	}


}
