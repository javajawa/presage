package infection;

//import infection.AddNodeDemo.MyVertexStringer;
import infection.StaticEnvDataModel.*;
import infection.LogEvent;
import infection.LogEvent.EdgeAddEvent;
import infection.LogEvent.VertexAddEvent;
import infection.LogEvent.VertexInfectedEvent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Timer;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.load.Persister;

import presage.Participant;
import presage.configure.ParticipantWrapper;
import presage.gui.ControlCenter;


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



public class NetworkView extends JPanel {
	private static final long serialVersionUID = 1L;

	String eventlogpath;
	
	ArrayList<LogEvent> events; // = createEventMap();

	// Simulation sim;

	// StaticEnvDataModel dmodel;

	String label = "NetworkViewer";

	private Graph g = null;
	private VisualizationViewer vv = null;
	private LayoutMutable layout = null;
	private StringLabeller labeler = null;

	//protected VertexStringer vs;

	protected NodeColor vcf;

	Timer timer;
	int adddelay = 3000;
	int relaxersleep = 20;

	Vertex v_prev = null;

	TreeMap<String, Vertex> vertexes = new TreeMap<String, Vertex>();
	TreeMap<String, DirectedSparseEdge> edges = new TreeMap<String, DirectedSparseEdge>();

	// RandomAccessFile participantsFile;

	long time = 0; 
	
	protected JButton switchLayout;

	public static final LengthFunction UNITLENGTHFUNCTION = new SpringLayout.UnitLengthFunction(100);

	boolean updatelayout = false;
	
	public NetworkView(String eventlogpath) {
		super();
		this.eventlogpath = eventlogpath;
	}

	public void execute() {
		// TODO Auto-generated method stub

		// ArrayList<LogEvent> events = testevents.get(new Long(time));

		System.out.println(time + ", " + events.size());
		
		if (events != null){

			Iterator<LogEvent> it = events.iterator();
			while (it.hasNext()){			
				LogEvent le = it.next();
				if (time == le.getTime())
					handle(le);
			}	
		}
		if (updatelayout)
			layout.update();
		
		if (!vv.isVisRunnerRunning())
			vv.init();
		
		vv.repaint();	
		
		time++;
	}

	protected void handle(LogEvent event){

		if (event instanceof VertexAddEvent){		
			handleVertexAddEvent((VertexAddEvent)event);
			 updatelayout = true;
		} else if (event instanceof EdgeAddEvent){	
			handleEdgeAddEvent((EdgeAddEvent)event);
			updatelayout = true;
		} else if (event instanceof VertexInfectedEvent){
			handleVertexInfectedEvent((VertexInfectedEvent)event);
		}
	}

	protected void handleVertexInfectedEvent(VertexInfectedEvent event){
		System.out.println(event.time + " Vertex infected Event " + event.name);
		(vertexes.get(event.name)).setUserDatum("color", selectColor(true,false), UserData.CLONE);
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

		Vertex v1 = vertexes.get(event.from);
		Vertex v2 = vertexes.get(event.to);

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
	
	
	public ArrayList<LogEvent> loadEvents(File file){
		
		EventLogger el;
		
		Serializer serializer = new Persister();
		
		try{
			el = serializer.read(EventLogger.class, file);
			return el.eventlog;
		} catch (Exception e){
			System.err.println(" loadEvents: "  + e);
		}
		
		return null;
	}

	public void initialise() {

		// dmodel = (StaticEnvDataModel) sim.getEnvDataModel();

		 events = loadEvents(new File(eventlogpath));
		
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

//		scaler.scale(vv, 1.1f, vv.getCenter());

//		scaler.scale(vv, 1.1f, vv.getCenter());	

//		scaler.scale(vv, 1.1f, vv.getCenter());

//		scaler.scale(vv, 1.1f, vv.getCenter());	

		labeler = StringLabeller.getLabeller(g);

		timer = new Timer();

		//vs = new StringLabeller(g);
		
		validate();

		//set timer so to fire layout and event processing
		timer.schedule(new RemindTask(), adddelay, adddelay); //subsequent rate

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

	class RemindTask extends TimerTask {

		public void run() {
			execute();
		}
	}

	public static void main(String[] args) {
		
		JFrame frame = new JFrame();

		frame.setBounds ( 30 , 50 , 1000 , 1000 );

		// frame.setResizable(false);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		if (args.length < 1){

			System.out.println("Usage: java NetworkView events.xml");
			System.out.println("");
			System.out.println("where events.xml is the network event log created by infection.EventLogger");
			System.exit(0);
			
		} else {
			
			NetworkView and = new NetworkView(args[0]);

			 and.initialise();

			 frame.getContentPane().add(and);

			 frame.setVisible(true);
		}
	}
}
