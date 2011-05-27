package infection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.Iterator;
import java.io.File;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.simpleframework.xml.Element;

import presage.Plugin;
import presage.Simulation;
import presage.annotations.PluginConstructor;
import presage.util.ObjectCloner;

public class PercentageInfectedGraph extends JPanel implements Plugin {
	private static final long serialVersionUID = 1L;

	Simulation sim;

	@Element
	String outputpath;

	@Element
	int outputwidth;

	@Element
	int outputheight;

	@Element
	int updaterate;

	String title = "Percentage of Nodes Infected";

	String xaxis = "Simulation cycle";

	String yaxis = "% Infected";

	String label = "% Infected";

	StaticEnvDataModel dmodel;

	XYSeriesCollection data;

	// ChartPanel chartPanel;

	XYSeriesCollection datatempcopy;

	JPanel control = new JPanel();

	ChartPanel chartPanel;

	public PercentageInfectedGraph() {
		// TODO Auto-generated constructor stub
	}

	@PluginConstructor( { "outputpath", "outputwidth", "outputheight", "updaterate" })
	public PercentageInfectedGraph(String outputpath, int outputwidth,
			int outputheight, int updaterate) {
		super();
		this.outputpath = outputpath;
		this.outputwidth = outputwidth;
		this.outputheight = outputheight;
		this.updaterate = updaterate;
	}

	private double getPercentInfected(StaticEnvDataModel dm) {

		int n = dm.playermodels.size();
		int infected = 0;
		Iterator<String> iterator = dm.playermodels.keySet().iterator();
		while (iterator.hasNext()) {
			if (dm.playermodels.get(iterator.next()).infected)
				infected++;
		}
		
		System.out.println("infected = " + infected + ", nodes = " + n + ", infected/n = " + infected / n);
		return ((double)infected / n)*100;
	}

	public void execute() {
		// TODO Auto-generated method stub

		// simply get the environment datamodel
		dmodel = (StaticEnvDataModel) sim.getEnvDataModel();

		double infected = getPercentInfected(dmodel);

		synchronized (this) {

			XYSeries series;
			try {
				series = data.getSeries("infected");
				series.add(dmodel.time, infected);
			} catch (org.jfree.data.UnknownKeyException e) {
				series = new XYSeries("infected");
				data.addSeries(series);
				series.add(dmodel.time, infected);
			}

			if (dmodel.time % 100 == 0) {
				// this will cause the gui to update?
				updateChart();
			}
		}
	}

	public void updateChart() {

		this.remove(chartPanel);

		synchronized (this) {
			datatempcopy = clonedata(data);
		}

		chartPanel = newChart(datatempcopy);

		add(chartPanel, (new BorderLayout()).CENTER);

		chartPanel.updateUI();
	}

	public XYSeriesCollection clonedata(XYSeriesCollection data) {

		try {
			return (XYSeriesCollection) ObjectCloner.deepCopy(data);
		} catch (Exception e) {
			System.err.println("Exception in execute() - "
					+ data.getClass().getCanonicalName()
					+ " is not serializable" + e);
			return null;
		}
	}

	public ChartPanel newChart(XYSeriesCollection datatempcopy) {

		JFreeChart chart = ChartFactory.createXYLineChart(title, xaxis, yaxis,
				datatempcopy, PlotOrientation.VERTICAL, true, true, false);

		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(this.getWidth(),
				this.getHeight()));

		return chartPanel;

	}

	public String getLabel() {
		// TODO Auto-generated method stub
		return label;
	}

	public String getShortLabel() {
		// TODO Auto-generated method stub
		return label;
	}

	public void initialise(Simulation sim) {
		// TODO Auto-generated method stub
		System.out.println(" -Initialising....");

		this.sim = sim;

		setBackground(Color.GRAY);

		// dmodel = (StaticEnvDataModel) sim.getEnvDataModel();

		// System.out.println("DataModel Obtained");

		// series = new XYSeries("Agent0000");

		data = new XYSeriesCollection();

		datatempcopy = clonedata(data);

		chartPanel = newChart(datatempcopy);

		JLabel label = new JLabel("Graph will update every " + updaterate
				+ " Simulation cycles, to update now click: ");

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

		synchronized (this) {
			chartPanel = newChart(data);
		}

		this.setLayout(new BorderLayout());
		add(chartPanel, (new BorderLayout()).CENTER);

		chartPanel.updateUI();

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

}
