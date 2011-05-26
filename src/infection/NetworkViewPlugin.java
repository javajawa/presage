package infection;

// import infection.AddNodeDemo.MyVertexStringer;
import infection.StaticEnvDataModel.*;
import infection.LogEvent.EdgeAddEvent;
import infection.LogEvent.VertexAddEvent;
import infection.LogEvent.VertexInfectedEvent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Timer;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.StringLabeller;
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.utils.UserData;
import edu.uci.ics.jung.visualization.FRLayout;
import edu.uci.ics.jung.visualization.LayoutMutable;
import edu.uci.ics.jung.visualization.PickedInfo;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.ShapePickSupport;
import edu.uci.ics.jung.visualization.SpringLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.SpringLayout.LengthFunction;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;

import presage.Plugin;
import presage.Simulation;

public class NetworkViewPlugin extends JPanel implements Plugin {
	Simulation sim;
	
	StaticEnvDataModel dmodel;
	
	String label = "NetworkViewer";
	
	private Graph g = null;
	private VisualizationViewer vv = null;
	private LayoutMutable layout = null;
	private StringLabeller labeler = null;
	
	//protected VertexStringer vs;

	protected NodeColor vcf;

	Timer timer;
	
	int relaxersleep = 20;
	
	Vertex v_prev = null;

	TreeMap vertexes = new TreeMap();
	TreeMap edges = new TreeMap();
	
	// RandomAccessFile participantsFile;
	
	

	protected JButton switchLayout;

	public static final LengthFunction UNITLENGTHFUNCTION = new SpringLayout.UnitLengthFunction(100);

	public NetworkViewPlugin() {
		super();
	}

	public void execute() {
		// TODO Auto-generated method stub
		
		// simply get the environment datamodel
		dmodel = (StaticEnvDataModel) sim.getEnvDataModel();
		
		ArrayList<LogEvent> events = dmodel.eventlog;
		
		System.out.println("NetworkVis time = " + dmodel.time);
		System.out.println("Events.size = " + events.size());
		
		if (events != null){
		
			g.removeAllEdges();
			g.removeAllVertices();
			
			vertexes.clear();
			edges.clear();
			
		Iterator it = events.iterator();
			while (it.hasNext()){
				LogEvent le = (LogEvent)it.next();
				// if(le.getTime() == dmodel.time)
					handle(le);
			}	
		}
		
		layout.update();
		
		if (!vv.isVisRunnerRunning())
			vv.init();
			
//			try{
//				Thread.sleep(1000);
//			} catch (Exception e){}			
			
		
		vv.repaint();		
	}
	

	
	protected void handle(LogEvent event){
		
		if (event instanceof VertexAddEvent){		
			 handleVertexAddEvent((VertexAddEvent)event);
			 
		} else if (event instanceof EdgeAddEvent){	
			handleEdgeAddEvent((EdgeAddEvent)event);
			
		} else if (event instanceof VertexInfectedEvent){
			 handleVertexInfectedEvent((VertexInfectedEvent)event);
		} else {
			System.out.println("NetworkViewPlugin: not sure what to do with - " + event.getClass().getCanonicalName());
		}
	}
	
	protected void handleVertexInfectedEvent(VertexInfectedEvent event){
		System.out.println(event.time + " Vertex infected Event " + event.name);
		((Vertex)vertexes.get(event.name)).setUserDatum("color", selectColor(true, false), UserData.CLONE);
	}
	
	protected void handleVertexAddEvent(VertexAddEvent event){
		System.out.println(event.time + " Vertex Add Event " + event.name);
		
		Vertex v1 = g.addVertex(new SparseVertex());		
		v1.addUserDatum("color", selectColor(event.infected, event.secure), UserData.CLONE);
		// v1.addUserDatum("outline", event.secure, UserData.CLONE);
		v1.addUserDatum("name", event.name, UserData.CLONE);

		//System.out.println(v1.toString());
		
		vertexes.put(event.name, v1);
	}
	
	protected void handleEdgeAddEvent(EdgeAddEvent event){
		
		System.out.println(event.time + " EdgeAddEvent " + event.from + ", " + event.to);
		
		Vertex v1 = (Vertex)vertexes.get(event.from);
		Vertex v2 = (Vertex)vertexes.get(event.to);

		DirectedSparseEdge e0 = new DirectedSparseEdge(v1, v2);

		edges.put(event.from + event.to, e0);

		g.addEdge(e0);
		
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
		
		this.sim = sim;
		
		// dmodel = (StaticEnvDataModel) sim.getEnvDataModel();
		
		// create a graph
		g = new DirectedSparseGraph();

		//create a layout
		layout = new FRLayout(g);
		
		PluggableRenderer pr = new PluggableRenderer();

		NodeColor  vcf = new NodeColor();

		pr.setVertexPaintFunction(vcf);
		
		pr.setVertexStringer(new MyVertexStringer());

		vv = new VisualizationViewer(layout, pr);

		System.out.println(vv.getSize().width + ", " + vv.getSize().width);
		
		// vv.setSize(this.getSize().width, this.getSize().height);
		
		// JRootPane rp = this.getRootPane();
		// rp.putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);

		this.setLayout(new BorderLayout());
		this.setBackground(java.awt.Color.WHITE);
		this.setFont(new Font("Serif", Font.PLAIN, 12));

		//set a visualization viewer
		vv.getModel().setRelaxerThreadSleepTime(relaxersleep);
		vv.setPickSupport(new ShapePickSupport());
		vv.setGraphMouse(new DefaultModalGraphMouse());

		vv.setBackground(Color.WHITE);
		
		this.add(vv);

		// this.setSize(size,size);
		
//		final ScalingControl scaler = new CrossoverScalingControl();	
//		scaler.scale(vv, 1.1f, vv.getCenter());
//		
//		scaler.scale(vv, 1.1f, vv.getCenter());
//		
//		scaler.scale(vv, 1.1f, vv.getCenter());	
//		
//		scaler.scale(vv, 1.1f, vv.getCenter());
//		
//		scaler.scale(vv, 1.1f, vv.getCenter());	
		
		labeler = StringLabeller.getLabeller(g);

		timer = new Timer();

		//vs = new StringLabeller(g);

		layout.update();
		if (!vv.isVisRunnerRunning())
		//	vv.init();
		vv.repaint();	
		
		
	}

	public void onDelete() {
		// TODO Auto-generated method stub

	}

	public void onSimulationComplete() {
		// TODO Auto-generated method stub

	}
	
	private Color selectColor(boolean infected, boolean secure){
		
		if (infected){
			return Color.RED;
		} else if (!secure){
			return Color.YELLOW;
			
		} else {
			return Color.GREEN;
		}
	}
	
	
	private final class NodeColor implements VertexPaintFunction
	{
		protected PickedInfo pi;
		protected final static float dark_value = 0.8f;
		protected final static float light_value = 0.2f;
		protected boolean seed_coloring;

		public NodeColor()
		{
		}

		public Paint getDrawPaint(Vertex v)
		{
			return (Color) v.getUserDatum("");
		}

		public Paint getFillPaint(Vertex v)
		{
			return (Color) v.getUserDatum("color");
		}

	}
	class MyVertexStringer implements VertexStringer {
		
		public String getLabel(ArchetypeVertex vertex){
			// return "";
			return (String)vertex.getUserDatum("name");
		} 
	}
}
