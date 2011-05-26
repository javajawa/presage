package presage.gui;

import com.jeta.forms.components.line.HorizontalLineComponent;
import com.jeta.forms.components.separator.TitledSeparator;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import java.util.*;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.DefaultListModel;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.UUID;

import presage.Plugin;
import presage.ScriptedEvent;
import presage.Environment;
import presage.EventScriptManager;
import presage.Plugin;
import presage.PluginManager;
import presage.PresageConfig;
import presage.Simulation;
import presage.configure.ConfigurationLoader;
import presage.Participant;

public class ControlCenter extends JFrame  {

	JLabel m_TimeField = new JLabel();
	JLabel m_InputFolderField = new JLabel();
	JLabel m_PWorldClassField = new JLabel();
	JLabel m_NetworkClassField = new JLabel();
	JLabel m_CycleLabel = new JLabel();
	JLabel m_RemainingLabel = new JLabel();
	JLabel m_InputLabel = new JLabel();
	JLabel m_PworldClassLabel = new JLabel();
	JLabel m_NetworkClassLabel = new JLabel();
	TitledSeparator m_StatusSeperator = new TitledSeparator();
	JProgressBar m_CycleProgrssBar = new JProgressBar();
	TitledSeparator m_ControlsSeperator = new TitledSeparator();
	JButton m_PlayButton = new JButton("Play");
	JButton m_StepButton = new JButton();
	JButton m_EndButton = new JButton();
	JTabbedPane m_jtabbedpane1 = new JTabbedPane();
	JTabbedPane m_jtabbedpane2 = new JTabbedPane();
	JPanel m_PluginManagerTab = new JPanel();
	JList m_PluginPane = new JList();
	JButton m_jbutton1 = new JButton();
	JButton m_PluginAddButton = new JButton();
	JPanel m_EventManagerTab = new JPanel();
	TitledSeparator m_AddEventSeperator = new TitledSeparator();
	TitledSeparator m_RemoveEventSeperator = new TitledSeparator();
	JList m_EventScriptJList = new JList();

	JButton m_MethodRemoveButton = new JButton();
	JLabel m_MethodRemoveLabel = new JLabel();
	JLabel m_MethodRemoveLabel1 = new JLabel();
	JButton m_MethodAddButton = new JButton();
	JTextArea m_jtextarea1 = new JTextArea();
	HorizontalLineComponent m_horizontallinecomponent1 = new HorizontalLineComponent();

	DefaultListModel eventlistmodel = new DefaultListModel();
	DefaultListModel pluginslistmodel = new DefaultListModel();

	ArrayList<EventListElement> activeEvents = new ArrayList<EventListElement>();

	ArrayList<OrderedListElement> pluginsListElements = new ArrayList<OrderedListElement>();

	TreeMap<UUID, Plugin> uuidToPlugin = new TreeMap<UUID, Plugin>();
	// TreeMap<Plugin, UUID> pluginToUUID = new TreeMap<Plugin, UUID>();
	
	
	int ordervalue = 0;

//	JMenu fileMenu;
//	JMenuItem openAction;
//	JMenuItem exitAction;

	private AddPluginDialog addplugindialog = null;
	private AddMethodDialog addMethoddialog = null;

	MemoryPanel memoryPanel;

	JPanel mainPanel;

	Simulation sim;

	PresageConfig presageConfig;

	// TreeMap<String, Participant> players;

	PluginManager pluginmanager;

	// Environment environment;

	EventScriptManager esm;

	/**
	 * Default constructor
	 */
	public ControlCenter(String path){

		super("PreSage Simulation Control Center");


		System.out.println("Path = " + path);

		presageConfig = ConfigurationLoader.loadPresageConfig(new File(path));

		System.out.println("Participants Path = " + presageConfig.getParticipantsConfigPath());

		TreeMap<String, Participant> players = ConfigurationLoader.loadParticipants(new File(presageConfig.getParticipantsConfigPath()));

		System.out.println("Plugins Path = " + presageConfig.getPluginsConfigPath());

		pluginmanager = ConfigurationLoader.loadPluginManager(new File(presageConfig.getPluginsConfigPath()));

		System.out.println("Environment Path = " + presageConfig.getEnvironmentConfigPath());

		Environment environment = ConfigurationLoader.loadEnvironment(new File(presageConfig.getEnvironmentConfigPath()), presageConfig.getEnvironmentClass());

		System.out.println("EventScriptManager Path = " + presageConfig.getEventscriptConfigPath());

		esm = 	ConfigurationLoader.loadEventScriptManager(new File(presageConfig.getEventscriptConfigPath()));


		PropertyChangeListener eventsChangeListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
				String property = propertyChangeEvent.getPropertyName();

				if ("events".equals(property)) {

					//System.out.println("Events Changed");
					updateAllEventInfo((ArrayList)propertyChangeEvent.getNewValue());
					// updateEventAdded((ScriptedEvent)propertyChangeEvent.getNewValue());

				} else  if ("preevents".equals(property)) {

					//System.out.println("Pre-events Changed");

				} else  if ("postevents".equals(property)) {

					//System.out.println("Post-events Changed");

				} else  if ("added_event".equals(property)) {
					addEvent((ScriptedEvent)propertyChangeEvent.getNewValue());

				} else if ("event_removed".equals(property)){    

					// System.out.println("Event: Event removed from simulation"); 
					removeEvent((String)propertyChangeEvent.getNewValue());

				} else if ("added_preevent".equals(property)){
					// (ScriptedEvent)propertyChangeEvent.getNewValue();

				} else if ("preevent_removed".equals(property)){	
					// (UUID)propertyChangeEvent.getNewValue();

				} else if ("added_postevent".equals(property)){	  
					// (ScriptedEvent)propertyChangeEvent.getNewValue();

				} else if ("postevent_removed".equals(property)){	
					// (UUID)propertyChangeEvent.getNewValue();

				} else {
					System.out.println("EventsChangeListener: recieved unknown propertyChangeEvent - property == " + property);
				}	        
			}
		};


		esm.addEventChangeListener(eventsChangeListener);


		PropertyChangeListener pluginsChangeListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
				String property = propertyChangeEvent.getPropertyName();

				if ("all_plugins_removed".equals(property)) {

					updateAllPluginInfo(new ArrayList<Plugin>());

				} else  if ("added_plugin".equals(property)) {

					Plugin p = (Plugin)propertyChangeEvent.getNewValue();

					addPlugin(p);
					
				} else if ("plugin_removed".equals(property)){    

					System.out.println("ControlCenter: Plugin removed from simulation" + ((UUID)propertyChangeEvent.getNewValue()).toString());
					
					removePlugin((UUID)propertyChangeEvent.getNewValue());

				} else {
					System.out.println("EventsChangeListener: recieved unknown propertyChangeEvent - property == " + property);
				}	        
			}
		};


		pluginmanager.addPluginChangeListener(pluginsChangeListener);

		
		// This is the important bit...
		sim = new Simulation(presageConfig, players, environment, pluginmanager, esm);		

		// Define PropertyChangeListener
		PropertyChangeListener timeChangeListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
				String property = propertyChangeEvent.getPropertyName();
				if ("time".equals(property)) {
					m_CycleProgrssBar.setValue(((Long)propertyChangeEvent.getNewValue()).intValue());
					m_CycleProgrssBar.setString(  m_CycleProgrssBar.getValue() + "/" +  m_CycleProgrssBar.getMaximum());	    		
				} else if ("timeLeft".equals(property)){    	
					long seconds =  (Long)propertyChangeEvent.getNewValue() / 1000;
					String s = String.format("%1$02d:%2$02d:%3$02d", seconds / (60*60), (seconds / 60) % 60, seconds % 60);
					m_TimeField.setText(s);		
				} else if ("length".equals(property)){
					// Set the progress bar maximum to the experiment length
					m_CycleProgrssBar.setMaximum( (Integer)propertyChangeEvent.getNewValue());
					m_CycleProgrssBar.setString(  m_CycleProgrssBar.getValue() + "/" + m_CycleProgrssBar.getMaximum());
				} else {
					System.out.println("TimeChangeListener: recieved unknown propertyChangeEvent - property == " + property);
				}	        
			}
		};

		// Attach Listeners
		sim.addTimeChangeListener(timeChangeListener);


		//"all_events_removed"  "all_preevents_removed" "all_postevents_removed"
		//"event_removed"  "preevent_removed"  "postevent_removed"
		//"added_event" "added_preevent""added_postevent"


		if (presageConfig.getAutorun()){
			m_PlayButton.setText("Pause");
		}

		setSize(1000, 600);
		setLocation(100, 100);

		initializePanel();

//		JMenuBar menuBar = new JMenuBar();
//		this.setJMenuBar(menuBar);

//		JMenu fileMenu = new JMenu("File");
//		JMenu configMenu = new JMenu("Configuration");

//		JMenuItem newAction = new JMenuItem("New Configuration");
//		newAction.addActionListener(new NewAction());
//		openAction = new JMenuItem("Open Configuration");
//		openAction.addActionListener(new OpenAction());
//		JMenuItem saveAction = new JMenuItem("Save Configuration");
//		saveAction.addActionListener(new SaveAction());
//		exitAction = new JMenuItem("Exit");
//		exitAction.addActionListener(new ExitAction());
//		JMenuItem editAction = new JMenuItem("Edit Configuration");
//		editAction.addActionListener(new EditAction());

//		fileMenu.add(newAction);
//		fileMenu.add(openAction);
//		fileMenu.add(saveAction);
//		fileMenu.add(exitAction);

//		configMenu.add(editAction);

//		menuBar.add(fileMenu);
//		menuBar.add(configMenu);


		m_PlayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JButton s = (JButton) ae.getSource();
				if (s.getText().equalsIgnoreCase("Pause")) {
					s.setText("Play");
					sim.pause();
					// Simulator.controlPanel.step = false;
				} else {
					s.setText("Pause");
					sim.play();					
					//Simulator.controlPanel.paused = false;
					//Simulator.controlPanel.step = false;
				}
			}
		});

		m_StepButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				m_PlayButton.setText("Play");	
				sim.step();		
			}
		});

		m_EndButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				sim.end();		
			}
		});


//		System.out.println(simConfig.getPresageConfig().getComment());
//		System.out.println(simConfig.getPresageConfig().getIterations());
//		System.out.println(simConfig.getPresageConfig().getRandomSeed());
//		System.out.println(simConfig.getPresageConfig().getThreadDelay());
//		System.out.println(simConfig.getPresageConfig().getAutorun());
//		System.out.println(simConfig.getPresageConfig().getPresagePort());
//		System.out.println(simConfig.getPresageConfig().getMysqlHost());
//		System.out.println(simConfig.getPresageConfig().getNetworkClassname());
//		System.out.println(simConfig.getPresageConfig().getPhysicalWorldClassname());

	}

	/**
	 * Main method for panel
	 */
	public static void main(String[] args)
	{
		ControlCenter cc;

		if (args.length < 1){
			cc = new ControlCenter(openSimConfigDialog());
		} else {
			cc = new ControlCenter(args[0]);
		}

		cc.setDefaultCloseOperation (cc.DO_NOTHING_ON_CLOSE);

		cc.addWindowListener( new WindowAdapter()
		{
			public void windowClosing( WindowEvent evt )
			{

				int button = JOptionPane.showConfirmDialog((ControlCenter)evt.getSource(), "Are you sure you want to exit?", "Confirm exit", JOptionPane.YES_NO_OPTION);
				if(button == JOptionPane.YES_OPTION)
				{
					System.exit(0);
				}        
			}
		});

		// frame.getContentPane().add();

		cc.setVisible(true);

		System.out.println("Back in main");

	}

	/**
	 * Adds fill components to empty cells in the first row and first column of the grid.
	 * This ensures that the grid spacing will be the same as shown in the designer.
	 * @param cols an array of column indices in the first row where fill components should be added.
	 * @param rows an array of row indices in the first column where fill components should be added.
	 */
	void addFillComponents( Container panel, int[] cols, int[] rows )
	{
		Dimension filler = new Dimension(10,10);

		boolean filled_cell_11 = false;
		CellConstraints cc = new CellConstraints();
		if ( cols.length > 0 && rows.length > 0 )
		{
			if ( cols[0] == 1 && rows[0] == 1 )
			{
				/** add a rigid area  */
				panel.add( Box.createRigidArea( filler ), cc.xy(1,1) );
				filled_cell_11 = true;
			}
		}

		for( int index = 0; index < cols.length; index++ )
		{
			if ( cols[index] == 1 && filled_cell_11 )
			{
				continue;
			}
			panel.add( Box.createRigidArea( filler ), cc.xy(cols[index],1) );
		}

		for( int index = 0; index < rows.length; index++ )
		{
			if ( rows[index] == 1 && filled_cell_11 )
			{
				continue;
			}
			panel.add( Box.createRigidArea( filler ), cc.xy(1,rows[index]) );
		}

	}

	/**
	 * Helper method to load an image file from the CLASSPATH
	 * @param imageName the package and name of the file to load relative to the CLASSPATH
	 * @return an ImageIcon instance with the specified image file
	 * @throws IllegalArgumentException if the image resource cannot be loaded.
	 */
	public ImageIcon loadImage( String imageName )
	{
		try
		{
			ClassLoader classloader = getClass().getClassLoader();
			java.net.URL url = classloader.getResource( imageName );
			if ( url != null )
			{
				ImageIcon icon = new ImageIcon( url );
				return icon;
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		throw new IllegalArgumentException( "Unable to load image: " + imageName );
	}

	public JPanel createPanel()
	{
		JPanel jpanel1 = new JPanel();
		FormLayout formlayout1 = new FormLayout("FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:25PX:NONE,FILL:150PX:NONE,FILL:4DLU:NONE,FILL:DEFAULT:GROW(1.0)","CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:26PX:NONE,FILL:DEFAULT:NONE,FILL:18PX:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,FILL:194PX:GROW(1.0)");
		CellConstraints cc = new CellConstraints();
		jpanel1.setLayout(formlayout1);

		m_TimeField.setName("TimeField");
		m_TimeField.setText("JLabel");
		jpanel1.add(m_TimeField,cc.xy(4,9));

		m_InputFolderField.setName("InputFolderField");
		m_InputFolderField.setText("JLabel");
		jpanel1.add(m_InputFolderField,cc.xy(4,11));

		m_PWorldClassField.setName("PWorldClassField");
		m_PWorldClassField.setText(presageConfig.getEnvironmentClass().getSimpleName());
		jpanel1.add(m_PWorldClassField,cc.xy(4,13));

		m_NetworkClassField.setName("NetworkClassField");
		m_NetworkClassField.setText("redundant");
		jpanel1.add(m_NetworkClassField,cc.xy(4,15));

		m_CycleLabel.setName("CycleLabel");
		m_CycleLabel.setText("Cycle:");
		jpanel1.add(m_CycleLabel,cc.xy(2,7));

		m_RemainingLabel.setName("RemainingLabel");
		m_RemainingLabel.setText("Remaining time:");
		jpanel1.add(m_RemainingLabel,cc.xy(2,9));

		m_InputLabel.setName("InputLabel");
		m_InputLabel.setText("Input Folder:");
		jpanel1.add(m_InputLabel,cc.xy(2,11));

		m_PworldClassLabel.setName("PworldClassLabel");
		m_PworldClassLabel.setText("PhysicalWorld Class:");
		jpanel1.add(m_PworldClassLabel,cc.xy(2,13));

		m_NetworkClassLabel.setName("NetworkClassLabel");
		m_NetworkClassLabel.setText("Network Class:");
		jpanel1.add(m_NetworkClassLabel,cc.xy(2,15));

		m_StatusSeperator.setName("StatusSeperator");
		m_StatusSeperator.setText("Status");
		jpanel1.add(m_StatusSeperator,cc.xywh(2,6,3,1));

		m_CycleProgrssBar.setName("CycleProgrssBar");
		m_CycleProgrssBar.setMaximum((int)presageConfig.getIterations());
		m_CycleProgrssBar.setValue(0);
		m_CycleProgrssBar.setStringPainted(true);
		m_CycleProgrssBar.setString(0 + "/" + presageConfig.getIterations());
		jpanel1.add(m_CycleProgrssBar,cc.xy(4,7));

		m_ControlsSeperator.setName("ControlsSeperator");
		m_ControlsSeperator.setText("Simulation Controls");
		jpanel1.add(m_ControlsSeperator,cc.xywh(2,2,3,1));

		m_PlayButton.setActionCommand("Play");
		m_PlayButton.setName("PlayButton");
		m_PlayButton.setRolloverEnabled(true);
		// m_PlayButton.setText("Play");
		m_PlayButton.setToolTipText("Toogles Run and Pause of the Simulation Execution");
		jpanel1.add(m_PlayButton,cc.xywh(2,3,3,1));

		m_StepButton.setActionCommand("Step");
		m_StepButton.setName("StepButton");
		m_StepButton.setRolloverEnabled(true);
		m_StepButton.setText("Step");
		m_StepButton.setToolTipText("Executes one time cycle at a time");
		jpanel1.add(m_StepButton,cc.xywh(2,4,3,1));

		m_EndButton.setActionCommand("End");
		m_EndButton.setName("EndButton");
		m_EndButton.setRolloverEnabled(true);
		m_EndButton.setText("End");
		m_EndButton.setToolTipText("Skips remaining simulation cycles and executes post methods");
		jpanel1.add(m_EndButton,cc.xywh(2,5,3,1));

		m_jtabbedpane1.addTab("Plugin Panels",null,createPanel1());
		m_jtabbedpane1.addTab("Plugin Manager",null,createPluginManagerTab());
		m_jtabbedpane1.addTab("Event Script Manager",null,createEventManagerTab());
		createMemoryPanel();
		m_jtabbedpane1.addTab("JVM memory",null, memoryPanel);
		jpanel1.add(m_jtabbedpane1,cc.xywh(6,2,1,18));

		TitledBorder titledborder1 = new TitledBorder(null,"Control Panel Information",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,null,new Color(49,106,196));
		m_jtextarea1.setBorder(titledborder1);
		JScrollPane jscrollpane1 = new JScrollPane();
		jscrollpane1.setViewportView(m_jtextarea1);
		jscrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jscrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jpanel1.add(jscrollpane1,cc.xywh(2,19,3,1));

		jpanel1.add(m_horizontallinecomponent1,cc.xywh(2,17,3,1));

		addFillComponents(jpanel1,new int[]{ 1,2,3,4,5,6 },new int[]{ 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19 });
		return jpanel1;
	}

	public JPanel createPanel1()
	{
		JPanel jpanel1 = new JPanel();
		FormLayout formlayout1 = new FormLayout("FILL:4DLU:NONE,FILL:DEFAULT:GROW(1.0),FILL:4DLU:NONE","CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0),CENTER:2DLU:NONE");
		CellConstraints cc = new CellConstraints();
		jpanel1.setLayout(formlayout1);

		JLabel jlabel1 = new JLabel();
		jlabel1.setText("If your Plugin extends a JPanel it will be added here. If you prefer a seperate Window visualisation use a JFrame.");
		jpanel1.add(jlabel1,cc.xy(2,2));

		TitledSeparator titledseparator1 = new TitledSeparator();
		titledseparator1.setText("Your Plugin Panels");
		jpanel1.add(titledseparator1,cc.xy(2,4));

		// m_jtabbedpane2.setBackground(Color.GRAY);

		jpanel1.add(m_jtabbedpane2,cc.xy(2,5));

		addFillComponents(jpanel1,new int[]{ 1,2,3 },new int[]{ 1,2,3,4,5,6 });

//		try{


//		m_jtabbedpane2.add(plg.getShortName(), (JPanel)plg);

//		} catch (Exception e){
//		System.err.println("not a JPanel" + e);

//		}



		return jpanel1;
	}

	public JPanel createPluginManagerTab()
	{
		m_PluginManagerTab.setName("PluginManagerTab");
		EmptyBorder emptyborder1 = new EmptyBorder(0,0,0,0);
		m_PluginManagerTab.setBorder(emptyborder1);
		FormLayout formlayout1 = new FormLayout("FILL:4DLU:NONE,FILL:DEFAULT:GROW(1.0),FILL:120PX:NONE","CENTER:DEFAULT:NONE,FILL:18PX:NONE,CENTER:26PX:NONE,CENTER:26PX:NONE,CENTER:26PX:NONE,CENTER:26PX:NONE,CENTER:2DLU:NONE,FILL:DEFAULT:GROW(1.0)");
		CellConstraints cc = new CellConstraints();
		m_PluginManagerTab.setLayout(formlayout1);


		m_PluginPane = new JList(pluginslistmodel);

		m_PluginPane.setName("PluginPane");
		TitledBorder titledborder1 = new TitledBorder(null,"Current Plugins",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,null,new Color(49,106,196));
		m_PluginPane.setBorder(titledborder1);


		JScrollPane jscrollpane1 = new JScrollPane();
		jscrollpane1.setViewportView(m_PluginPane);
		jscrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jscrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


		m_PluginManagerTab.add(jscrollpane1,cc.xywh(2,8,2,1));

		TitledSeparator titledseparator1 = new TitledSeparator();
		titledseparator1.setText("Add Plugin");
		m_PluginManagerTab.add(titledseparator1,cc.xywh(2,3,2,1));

		m_jbutton1.setActionCommand("Remove");
		m_jbutton1.setText("Remove");
		m_jbutton1.addActionListener(new RemovePluginAction());

		m_PluginManagerTab.add(m_jbutton1,cc.xy(3,6));

		m_PluginAddButton.setActionCommand("Add");
		m_PluginAddButton.setName("PluginAddButton");
		m_PluginAddButton.setText("Add");
		// ActionListener for showing the add plugin dialog
		m_PluginAddButton.addActionListener(new AddPluginAction());

		m_PluginManagerTab.add(m_PluginAddButton,cc.xy(3,4));

		JLabel jlabel1 = new JLabel();
		jlabel1.setText("To add a plugin during simulation execution:");
		m_PluginManagerTab.add(jlabel1,cc.xy(2,4));

		JLabel jlabel2 = new JLabel();
		jlabel2.setText("Plugin Manager allows you to view and edit currently active Plugins");
		m_PluginManagerTab.add(jlabel2,cc.xy(2,2));

		JLabel jlabel3 = new JLabel();
		jlabel3.setText("To remove a plugin during simulation execution:");
		m_PluginManagerTab.add(jlabel3,cc.xy(2,6));

		TitledSeparator titledseparator2 = new TitledSeparator();
		titledseparator2.setText("Remove Plugin");
		m_PluginManagerTab.add(titledseparator2,cc.xywh(2,5,2,1));

		addFillComponents(m_PluginManagerTab,new int[]{ 1,2,3 },new int[]{ 1,2,3,4,5,6,7,8 });
		return m_PluginManagerTab;
	}

	public JPanel createEventManagerTab()
	{
		m_EventManagerTab.setName("EventManagerTab");
		FormLayout formlayout1 = new FormLayout("FILL:4DLU:NONE,FILL:162PX:GROW(1.0),FILL:120PX:NONE","CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:26PX:NONE,CENTER:DEFAULT:NONE,CENTER:26PX:NONE,CENTER:26PX:NONE,CENTER:2DLU:NONE,FILL:DEFAULT:GROW(1.0)");
		CellConstraints cc = new CellConstraints();
		m_EventManagerTab.setLayout(formlayout1);

		m_AddEventSeperator.setName("AddEventSeperator");
		m_AddEventSeperator.setText("Add Event Method");
		m_EventManagerTab.add(m_AddEventSeperator,cc.xywh(2,3,2,1));

		m_RemoveEventSeperator.setName("RemoveEventSeperator");
		m_RemoveEventSeperator.setText("Remove Event Method");
		m_EventManagerTab.add(m_RemoveEventSeperator,cc.xywh(2,5,2,1));

		// eventlistmodel = new DefaultListModel();



		m_EventScriptJList = new JList(eventlistmodel);
		m_EventScriptJList.setName("EventScriptJList");
		TitledBorder titledborder1 = new TitledBorder(null,"Current Event Script",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,null,new Color(49,106,196));
		m_EventScriptJList.setBorder(titledborder1);
		m_EventScriptJList.setSelectedIndex(0);


		JScrollPane jscrollpane1 = new JScrollPane();
		jscrollpane1.setViewportView(m_EventScriptJList);
		jscrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jscrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		m_EventManagerTab.add(jscrollpane1,cc.xywh(2,8,2,1));

		m_MethodRemoveButton.setActionCommand("Remove");
		m_MethodRemoveButton.setName("MethodRemoveButton");
		m_MethodRemoveButton.setText("Remove");
		m_MethodRemoveButton.addActionListener(new RemoveMethodAction());
		m_EventManagerTab.add(m_MethodRemoveButton,cc.xy(3,6));


		m_MethodRemoveLabel.setName("MethodRemoveLabel");
		m_MethodRemoveLabel.setText("To remove Scripted Method select from list and click Remove ");
		m_EventManagerTab.add(m_MethodRemoveLabel,cc.xy(2,6));

		m_MethodRemoveLabel1.setName("MethodRemoveLabel");
		m_MethodRemoveLabel1.setText("To add a Scripted Method select from list and click Add ");
		m_EventManagerTab.add(m_MethodRemoveLabel1,cc.xy(2,4));

		m_MethodAddButton.setActionCommand("Add");
		m_MethodAddButton.setName("MethodAddButton");
		m_MethodAddButton.setText("Add");

//		ActionListener for showing the add method dialog
		m_MethodAddButton.addActionListener(new AddMethodAction());


		m_EventManagerTab.add(m_MethodAddButton,cc.xy(3,4));

//		m_MethodAddButton.addActionListener(new ActionListener() {
//		public void actionPerformed(ActionEvent ae) {
//		JButton s = (JButton) ae.getSource();



//		}
//		});

		JLabel jlabel1 = new JLabel();
		jlabel1.setText("The Event Script Manager allows you to view view add and remove scripted methods at runtime.");
		m_EventManagerTab.add(jlabel1,cc.xy(2,2));

		addFillComponents(m_EventManagerTab,new int[]{ 1,2,3 },new int[]{ 1,2,3,4,5,6,7,8 });
		return m_EventManagerTab;
	}

	public void createMemoryPanel()
	{
		//JPanel jpanel1 = new JPanel();
		//jpanel1
		//FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE","CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
		//CellConstraints cc = new CellConstraints();
		//jpanel1.setLayout(formlayout1);

		//addFillComponents(jpanel1,new int[]{ 1,2,3 },new int[]{ 1,2,3 });
		memoryPanel = new MemoryPanel();

	}

	/**
	 * Initializer
	 */
	protected void initializePanel()
	{
		setLayout(new BorderLayout());
		JMenuBar menuBar = new JMenuBar();

		// Add the menubar to the frame
		setJMenuBar(menuBar);

		mainPanel = createPanel();

		//mainPanel..disable();

		add(  mainPanel, BorderLayout.CENTER);


	}

	public void removePlugin(UUID uuid){
		try {
			// System.out.println("cc removing plugin panel" );
			System.out.println("CC: removing plugin" );

			Plugin plugin = uuidToPlugin.get(uuid);
			
			m_jtabbedpane2.remove((JPanel)plugin);	

			// remove from the Maps
			uuidToPlugin.remove(uuid);

			pluginslistmodel.removeElement(new OrderedListElement(uuid, 1, null));	

		} catch (Exception e){
			System.err.println("Control Center: removePlugin - " + e);
		}


	}

	public void addPlugin(Plugin plugin){
		
		System.out.println("CC: adding plugin" );

		// give it a uuid so it can be linked to from the listmodel
		UUID pluginuuid = UUID.randomUUID();
		
		// Store in a Treemap indexed by the uuid
		uuidToPlugin.put(pluginuuid, plugin);

		// Make a list Element this is comparable so it can be sorted by ordervalue;
		OrderedListElement ee = new OrderedListElement(pluginuuid, ordervalue++, plugin.getShortLabel());

		// OrderedListElement is added to a list of plugin elements
		pluginsListElements.add(ee);
		
		
		// Now see if you can add the plugin to the Plugin Panels Tab
		try {
			// System.out.println("cc removing plugin panel" );
			m_jtabbedpane2.add(plugin.getShortLabel(), (JPanel)plugin);	
		} catch (Exception e){}

		updatePluginJlist();
	}
	
	private void updatePluginJlist(){
		
		// The list model associated with the Jlist is Cleared
		pluginslistmodel.clear();
		
		// The List of elements is sorted by when they were added
		Collections.sort(pluginsListElements);
		
		// add each of them to the list model in order
		Iterator iterator = pluginsListElements.iterator();
		while(iterator.hasNext()){
			OrderedListElement ole = (OrderedListElement)iterator.next();	
			pluginslistmodel.addElement(ole);
		}	 		
	}
	
	public void updateAllPluginInfo(ArrayList<Plugin> plugins){

		// Clear all the current data;
		uuidToPlugin = new TreeMap<UUID, Plugin>();
		pluginsListElements = new ArrayList<OrderedListElement>();
		
		// Add all the plugins a fresh
		Iterator iterator = plugins.iterator();
		while(iterator.hasNext()){
			addPlugin((Plugin)iterator.next());
		}
	}

	public void addEvent(ScriptedEvent se){

		EventListElement ee = new EventListElement(se.uuidstring, se.executiontime, se.getShortLabel());

		eventlistmodel.clear();

		activeEvents.add(ee);

		Collections.sort(activeEvents);

		Iterator iterator = activeEvents.iterator();
		while(iterator.hasNext()){
			EventListElement event = (EventListElement)iterator.next();	
			eventlistmodel.addElement(event);
		}	 		
	}

	public void removeEvent(String uuid){

		activeEvents.remove(new EventListElement(uuid, 0, "comparator"));
		eventlistmodel.removeElement(new EventListElement(uuid, 0, "comparator"));	

	}

	public void updateAllEventInfo(ArrayList<ScriptedEvent> events){

		// System.out.println("updatingeventmodel"); 		

		eventlistmodel.clear();

		Collections.sort(events);

		Iterator iterator = events.iterator();
		while(iterator.hasNext()){
			ScriptedEvent event = (ScriptedEvent)iterator.next();	
			EventListElement ee = new EventListElement( event.uuidstring, event.executiontime, event.toString() );
			activeEvents.add(ee);
			eventlistmodel.addElement(ee);
		}

		Collections.sort(activeEvents);
	}

	public void simulationComplete(){

		// dirty fix.
		// System.exit(0);

	}

	public void updateParticipantInfo(){}

	public void updateNetworkInfo(){}

	public void updateWorldInfo(){}

	public void updateParticipantInfo(Map<String, Participant> participants){}

	public void updateProgressInfo(int cycle, int explength, long timeleft){

		m_CycleProgrssBar.setMaximum(explength);
		m_CycleProgrssBar.setValue(cycle);
		m_CycleProgrssBar.setStringPainted(true);
		m_CycleProgrssBar.setString(cycle + "/" + explength);

		long seconds =  timeleft / 1000;
		String s = String.format("%1$02d:%2$02d:%3$02d", seconds / (60*60), (seconds / 60) % 60, seconds % 60);

		m_TimeField.setText(s);
	}



	private class AddPluginAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			// if first time, construct dialog

			if (addplugindialog == null) 
				addplugindialog = new AddPluginDialog();

			// set default values
			// addplugindialog.setUser(new User("yourname", null));


			// pop up dialog
			if (addplugindialog.showDialog(ControlCenter.this, "Add Plugin" ))
			{
				// if accepted, retrieve user input

				// TODO
				System.out.println("getting plugin");

				Plugin plg = addplugindialog.plugin;

				System.out.println(plg.getLabel());

				// add the plugin
				sim.pluginmanager.addPlugin(plg);				
				
				// initialise it. (Note plugins added at the start before the creation of the simulation thread 
				// will be initialised by the construction of the simulation thread.
				// plg.initialise(sim);

			}
		}
	}

	private class AddMethodAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			// if first time, construct dialog

			if (addMethoddialog == null) 
				addMethoddialog = new AddMethodDialog();

			// set default values
			// addplugindialog.setUser(new User("yourname", null));


			// pop up dialog
			if (addMethoddialog.showDialog(ControlCenter.this, "Add Scripted Method" ))
			{
				// if accepted, retrieve user input

				// TODO
				System.out.println("getting ScriptElement");

				ScriptedEvent se = addMethoddialog.scriptedEvent;

				System.out.println(se.toString());

				sim.eventscriptmanager.addEvent(se);

				/// EventElement ee = new EventElement(sim.eventscriptmanager.addEvent(se), se);

				/// eventlistmodel.addElement(ee);

			}
		}
	}


	private class RemoveMethodAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			int i = m_EventScriptJList.getSelectedIndex();

			if(i != -1){
				EventListElement ee = (EventListElement) m_EventScriptJList.getModel().getElementAt(i);

				System.out.println("Attempting to remove " + ee.uuid +"  "+ ee.time +"  " + ee.label);

				// if first time, construct dialog
				sim.eventscriptmanager.removeEvent(ee.uuid);
			}

			if (i<1){
				m_EventScriptJList.setSelectedIndex(0);
			} else {
				m_EventScriptJList.setSelectedIndex(i-1);
			}

		}
	}

	private class RemovePluginAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{

			int i = m_PluginPane.getSelectedIndex();

			if(i != -1){
				OrderedListElement ee = (OrderedListElement) m_PluginPane.getModel().getElementAt(i);

				System.out.println("Attempting to remove " + ee.uuid +"  "+ ee.time +"  " + ee.label);

				// if first time, construct dialog
				sim.pluginmanager.removePlugin((Plugin)uuidToPlugin.get(ee.uuid), ee.uuid);
			}

			if (i<1){
				m_PluginPane.setSelectedIndex(0);
			} else {
				m_PluginPane.setSelectedIndex(i-1);
			}

		}
	}


//	class NewAction implements ActionListener {
//	public void actionPerformed(ActionEvent ae) {
//	// presage.ConfigFileEditorDialog(null);

//	System.out.println("new Simulation dialog opening...... " );
//	}
//	}


//	class SaveAction implements ActionListener {
//	public void actionPerformed(ActionEvent ae) {
//	// presage.ConfigFileEditorDialog(null);

//	System.out.println("Simulation saving...... " );
//	}
//	}



	public static String openSimConfigDialog(){

		// ... Open a file dialog.
		JFileChooser fileChooser = new JFileChooser();
		File currDir = new File(".");
		fileChooser.setCurrentDirectory(currDir);
		fileChooser.setFileFilter(new ControlCenter.XMLFileFilter());
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int retval = fileChooser.showOpenDialog(null);
		if (retval == JFileChooser.APPROVE_OPTION) {
			//... The user selected a file, get it, use it.
			File file = fileChooser.getSelectedFile();
			System.out.println("Simulation opening...... " + file.getAbsolutePath());
			return file.getAbsolutePath();
		} else {
			return null;
		}	 	 
	}

//	class OpenAction implements ActionListener {
//	public void actionPerformed(ActionEvent ae) {	 
//	openSimConfigDialog();

//	}}

//	class ExitAction implements ActionListener {
//	public void actionPerformed(ActionEvent ae) {
//	processEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
//	}
//	}

//	class EditAction implements ActionListener {
//	public void actionPerformed(ActionEvent ae) {
//	System.out.println("edit Simulation dialog opening...... " );
//	}
//	}

	static class XMLFileFilter extends javax.swing.filechooser.FileFilter {

		public boolean accept(File f) {
			return f.isDirectory() || f.getName().toLowerCase().endsWith(".xml");
		}

		public String getDescription() {
			return ".xml files";
		}
	}

	public class OrderedListElement implements Comparable<OrderedListElement> {

		UUID uuid;
		int time;
		//ScriptedEvent scriptedevent;

		String label;

		public OrderedListElement(UUID uuid, int time, String label){
			this.uuid = uuid;
			this.time = time;
			this.label = label;

		}

		public boolean equals(Object object) {

			if ( this == object ) return true;


			if ( !(object instanceof OrderedListElement) ) return false;

			OrderedListElement se = (OrderedListElement)object;

			return this.uuid == se.uuid;
		}

		public int compareTo(OrderedListElement other){
			// this is to order the elements by executiontime.		
			return this.time - other.time;
		}

		public String toString(){
			return label;
		}
	}
	
	
	public class EventListElement implements Comparable<EventListElement> {

		String uuid;
		int time;
		//ScriptedEvent scriptedevent;

		String label;

		public EventListElement(String uuid, int time, String label){
			this.uuid = uuid;
			this.time = time;
			this.label = label;

		}

		public boolean equals(Object object) {

			if ( this == object ) return true;


			if ( !(object instanceof EventListElement) ) return false;

			EventListElement se = (EventListElement)object;

			return this.uuid == se.uuid;
		}

		public int compareTo(EventListElement other){
			// this is to order the elements by executiontime.		
			return this.time - other.time;
		}

		public String toString(){
			return label;
		}
	}
}
