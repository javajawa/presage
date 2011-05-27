package example.plugins;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import presage.Plugin;
import presage.Simulation;
import presage.util.ObjectCloner;



import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import java.io.File;
import org.simpleframework.xml.Element;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;

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


import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainCategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYSeries;

import example.ExampleEnvDataModel;

import java.util.Iterator;
import java.util.UUID;

public class ParticipantFailuresGraph extends JPanel implements Plugin {
	private static final long serialVersionUID = 1L;


	Simulation sim;
	
	@Element
	String outputpath;

	@Element
	int updaterate;

	ExampleEnvDataModel dmodel;

	XYSeriesCollection data;
	// ChartPanel chartPanel;

	XYSeriesCollection datatempcopy;

	JPanel control = new JPanel(); 

	ChartPanel chartPanel;

	public ParticipantFailuresGraph() {
		// TODO Auto-generated constructor stub
	}

	public ParticipantFailuresGraph(String outputpath, int updaterate) {
		this.outputpath = outputpath;
		this.updaterate = updaterate;
	}
	
	public void execute() {
		// TODO Auto-generated method stub
		// simply get the environment datamodel


		dmodel = (ExampleEnvDataModel)sim.getEnvDataModel();

		synchronized(this){

			Iterator<String> iterator = dmodel.playermodels.keySet().iterator();
			while (iterator.hasNext()){
				String id = iterator.next();
				XYSeries series;
				try {
					series = data.getSeries(id);
					series.add(dmodel.time, dmodel.playermodels.get(id).failedTargets);
				} catch ( org.jfree.data.UnknownKeyException e) {
					series = new XYSeries(id);
					data.addSeries(series);
					series.add(dmodel.time, dmodel.playermodels.get(id).failedTargets);
				}
			}

			if (dmodel.time%100 == 0){
				// this will cause the gui to update?
				updateChart();
			}
		}
	}

	public void updateChart(){

		this.remove(chartPanel);
		
		synchronized(this){
			datatempcopy = clonedata(data);
		}
		
		chartPanel = newChart(datatempcopy);

		add(chartPanel, (new BorderLayout()).CENTER);

		chartPanel.updateUI();
	}

	public XYSeriesCollection clonedata(XYSeriesCollection data){

		try{
			return (XYSeriesCollection)ObjectCloner.deepCopy(data);
		} catch (Exception e){
			System.err.println("Exception in ParticipantFailuresGraph.execute() - " + data.getClass().getCanonicalName() + " is not serializable" + e);
			return null;
		}
	}

	public ChartPanel newChart(XYSeriesCollection datatempcopy){

		JFreeChart chart = ChartFactory.createXYLineChart(
				"Participant Failures",
				"Simulation Cycle", 
				"Number of Goal Failures", 
				datatempcopy,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
		);

		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(this.getWidth(), this.getHeight()));

		return chartPanel;

	}

	public String getLabel() {
		// TODO Auto-generated method stub
		return "ParticipantFailuresGraph";
	}

	public String getShortLabel() {
		// TODO Auto-generated method stub
		return "ParticipantFailuresGraph";
	}

	
	public void initialise(Simulation sim) {
		// TODO Auto-generated method stub
		System.out.println(" -Initialising....");

		this.sim = sim;
		
		setBackground(Color.GRAY);

		dmodel = (ExampleEnvDataModel)sim.getEnvDataModel();

		// series = new XYSeries("Agent0000");

		data = new XYSeriesCollection();

		datatempcopy = clonedata(data);

		chartPanel = newChart(datatempcopy);

		JLabel label = new JLabel("Graph will update every " + updaterate + " Simulation cycles, to update now click: ");

		JButton updateButton = new JButton("Update Graph");

		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				updateChart();
			}
		});


		control.add(label);
		control.add(updateButton);

		this.setLayout(new BorderLayout());
		add(control, (new BorderLayout()).NORTH);
		add(chartPanel, (new BorderLayout()).CENTER);
	}

	public void onDelete() {
		// TODO Auto-generated method stub

	}
	public void onSimulationComplete() {
		// TODO Auto-generated method stub

		this.removeAll();

		synchronized (this){
		chartPanel = newChart(data);
		}
		
		this.setLayout(new BorderLayout());
		add(chartPanel, (new BorderLayout()).CENTER);

		chartPanel.updateUI();

		File file;

		try {
			file = new File(outputpath);
		} catch (Exception e){	
			System.err.println("ParticipantScoreGraph: Invalid output path" + e);
			return;
		}

		try {
			int width = 1920;
			int height = 1200;
			ChartUtilities.saveChartAsPNG(file, chartPanel.getChart(), width, height );
		} catch (Exception e) {
			System.out.println("Problem occurred creating chart.");
		}

	}

}
