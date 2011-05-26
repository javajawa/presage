package presage.gui;

import com.jeta.forms.components.line.HorizontalLineComponent;
import com.jeta.forms.components.separator.TitledSeparator;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFileChooser;

import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


import java.io.*;

import presage.PresageConfig;


public class ConfigFileEditorDialog extends JPanel
{

	private static final long serialVersionUID = 1L;

	Dimension dialogSize = new Dimension(700, 540);
	JDialog dialog;
	private boolean ok;

	PresageConfig presageConfig;

	String startpath = ".";

	JFileChooser fileChooser = new JFileChooser();


	JTextField m_simConfigfileField = new JTextField();
	JTextArea m_commentField = new JTextArea();
	JTextField m_iterationsField = new JTextField();
	JTextField m_randomField = new JTextField();
	JTextField m_jtextfield1 = new JTextField();
	JTextField m_threadDelayField = new JTextField();
	JTextField m_serverPortField = new JTextField();
	JTextField m_pluginsConfigfileField = new JTextField();
	JTextField m_eventsConfigfileField = new JTextField();
	JTextField m_participantsConfigfileField = new JTextField();
	JButton m_PartConfigButton = new JButton();
	JButton m_EventsConfigButton2 = new JButton();
	JButton m_PluginsConfigButton = new JButton();
	JButton m_SimConfigFileButton = new JButton();
	JTextField m_networkConfigfileField = new JTextField();
	JTextField m_pworldConfigfileField = new JTextField();
	JButton m_NetConfigButton = new JButton();
	JButton m_WorldConfigButton = new JButton();
	JLabel m_NetworkClassLabel = new JLabel();
	JLabel m_PWorldClassLabel = new JLabel();
	JButton m_jbutton7 = new JButton();
	JButton m_jbutton8 = new JButton();
	HorizontalLineComponent m_horizontallinecomponent1 = new HorizontalLineComponent();
	JButton m_SaveButton = new JButton();
	JButton m_CancelButton = new JButton();
	JRadioButton m_autorunFalseRadio = new JRadioButton();
	ButtonGroup m_buttongroup1 = new ButtonGroup();
	JRadioButton m_autorunTrueRadio = new JRadioButton();

	/**
	 * Default constructor
	 */
	public ConfigFileEditorDialog(PresageConfig config)
	{

		if (config == null){

			//... Open a file dialog.
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setCurrentDirectory(new File("."));
			fileChooser.setDialogTitle("Choose or create folder for inputfiles:");

			int retval = fileChooser.showOpenDialog(ConfigFileEditorDialog.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				//... The user selected a file, get it, use it.
				File file = fileChooser.getSelectedFile();

				//... Update user interface.

//				wordCountTF.setText("" + countWordsInFile(file));

			}
		} else {
			
			presageConfig = config;
			
		}

		initializePanel();
	}

	private PresageConfig makePresageConfig(){


		PresageConfig presageConfig = new PresageConfig();

		presageConfig.setComment("cat in a creamery");
		presageConfig.setIterations(20);
		presageConfig.setRandomSeed(43253252);
		// FIXME: Uncomment these lines
		//presageConfig.setPresagePort(9361);
		//presageConfig.setMysqlHost("localhost");
		presageConfig.setThreadDelay(100);

		presageConfig.setPluginsConfigPath( "plugins.xml");
		presageConfig.setEventscriptConfigPath("methods.xml");
		presageConfig.setParticipantsConfigPath("participants.xml");
		// FIXME: Uncomment these lines
		//presageConfig.setNetworkConfigPath("network.xml");
		//presageConfig.setPhysicalworldConfigPath("pworld.xml");

		// FIXME: Uncomment these lines
		//PhysicalWorld pworld = (PhysicalWorld) new presage.example.TestWorld(200, 300);
		//presageConfig.setPhysicalWorldClassname(pworld.getClass().getCanonicalName());
		//System.out.println(presageConfig.getPhysicalWorldClassname());

		// FIXME: Uncomment these lines
		//Network network = (Network) new presage.example.TestNetwork();
		//presageConfig.setNetworkClassname(network.getClass().getCanonicalName());
		//System.out.println(presageConfig.getNetworkClassname());

		return presageConfig;

	}


	/**
	 * Main method for panel
	 */
//	public static void main(String[] args)
//	{
//	JFrame frame = new JFrame();
//	frame.setSize(600, 400);
//	frame.setLocation(100, 100);
//	frame.getContentPane().add(new ConfigFileEditor());
//	frame.setVisible(true);

//	frame.addWindowListener( new WindowAdapter()
//	{
//	public void windowClosing( WindowEvent evt )
//	{
//	System.exit(0);
//	}
//	});
//	}

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
		FormLayout formlayout1 = new FormLayout("FILL:4DLU:NONE,FILL:167PX:NONE,FILL:DEFAULT:NONE,FILL:120PX:NONE,FILL:31PX:NONE,FILL:111PX:NONE,FILL:4DLU:NONE,FILL:80DLU:NONE,FILL:4DLU:NONE","CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE");
		CellConstraints cc = new CellConstraints();
		jpanel1.setLayout(formlayout1);

		JLabel jlabel1 = new JLabel();
		jlabel1.setText("Simulation inputfile (*.xml):");
		jpanel1.add(jlabel1,new CellConstraints(2,4,1,1,CellConstraints.RIGHT,CellConstraints.DEFAULT));

		JLabel jlabel2 = new JLabel();
		jlabel2.setText("Comment:");
		jpanel1.add(jlabel2,new CellConstraints(2,26,1,1,CellConstraints.RIGHT,CellConstraints.TOP));

		JLabel jlabel3 = new JLabel();
		jlabel3.setText("Iterations:");
		jpanel1.add(jlabel3,new CellConstraints(2,30,1,1,CellConstraints.RIGHT,CellConstraints.DEFAULT));

		JLabel jlabel4 = new JLabel();
		jlabel4.setText("Random seed:");
		jpanel1.add(jlabel4,new CellConstraints(2,32,1,1,CellConstraints.RIGHT,CellConstraints.DEFAULT));

		m_simConfigfileField.setName("simConfigfileField");
		jpanel1.add(m_simConfigfileField,cc.xywh(4,4,3,1));

		m_commentField.setName("commentField");
		m_commentField.setText(presageConfig.getComment());
		JScrollPane jscrollpane1 = new JScrollPane();
		jscrollpane1.setViewportView(m_commentField);
		jscrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jscrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jpanel1.add(jscrollpane1,cc.xywh(4,26,5,1));

		m_iterationsField.setName("iterationsField");
//		m_iterationsField.setText(Integer.toString(presageConfig.getIterations()));
		jpanel1.add(m_iterationsField,cc.xy(4,30));

		m_randomField.setName("randomField");
		m_randomField.setText(Long.toString(presageConfig.getRandomSeed()));
		jpanel1.add(m_randomField,cc.xy(4,32));

		TitledSeparator titledseparator1 = new TitledSeparator();
		titledseparator1.setText("MySQL Connection");
		jpanel1.add(titledseparator1,cc.xywh(2,37,7,1));

		JLabel jlabel5 = new JLabel();
		jlabel5.setText("Host*:");
		jpanel1.add(jlabel5,new CellConstraints(2,39,1,1,CellConstraints.RIGHT,CellConstraints.DEFAULT));

		jpanel1.add(m_jtextfield1,cc.xywh(4,39,3,1));

		JLabel jlabel6 = new JLabel();
		jlabel6.setText("Thread delay (ms):");
		jpanel1.add(jlabel6,new CellConstraints(6,30,1,1,CellConstraints.RIGHT,CellConstraints.DEFAULT));

		JLabel jlabel7 = new JLabel();
		jlabel7.setText("Presage port:");
		jpanel1.add(jlabel7,new CellConstraints(6,32,1,1,CellConstraints.RIGHT,CellConstraints.DEFAULT));

		m_threadDelayField.setName("threadDelayField");
		m_threadDelayField.setText(Integer.toString(presageConfig.getThreadDelay()));
		jpanel1.add(m_threadDelayField,cc.xy(8,30));

		m_serverPortField.setName("serverPortField");
//		m_serverPortField.setText(Integer.toString(presageConfig.getPresagePort()));
		jpanel1.add(m_serverPortField,cc.xy(8,32));

		JLabel jlabel8 = new JLabel();
		jlabel8.setText("* Warning: Using a remote MySQL server can adversely affect simulation speed.");
		jpanel1.add(jlabel8,new CellConstraints(2,42,7,1,CellConstraints.CENTER,CellConstraints.DEFAULT));

		TitledSeparator titledseparator2 = new TitledSeparator();
		titledseparator2.setText("Simulation Configuration File");
		jpanel1.add(titledseparator2,cc.xywh(2,2,7,1));

		JLabel jlabel9 = new JLabel();
		jlabel9.setText("Plugins inputfile:");
		jpanel1.add(jlabel9,new CellConstraints(2,9,1,1,CellConstraints.RIGHT,CellConstraints.DEFAULT));

		JLabel jlabel10 = new JLabel();
		jlabel10.setText("EventScript inputfile:");
		jpanel1.add(jlabel10,new CellConstraints(2,11,1,1,CellConstraints.RIGHT,CellConstraints.DEFAULT));

		JLabel jlabel11 = new JLabel();
		jlabel11.setText("Participants inputfile:");
		jpanel1.add(jlabel11,new CellConstraints(2,13,1,1,CellConstraints.RIGHT,CellConstraints.DEFAULT));

		m_pluginsConfigfileField.setName("pluginsConfigfileField");
		//m_pluginsConfigfileField
		jpanel1.add(m_pluginsConfigfileField,cc.xywh(4,9,3,1));

		m_eventsConfigfileField.setName("eventsConfigfileField");
		//m_eventsConfigfileField
		jpanel1.add(m_eventsConfigfileField,cc.xywh(4,11,3,1));

		m_participantsConfigfileField.setName("participantsConfigfileField");
		//m_participantsConfigfileField
		jpanel1.add(m_participantsConfigfileField,cc.xywh(4,13,3,1));

		TitledSeparator titledseparator3 = new TitledSeparator();
		titledseparator3.setText("Simulation Variables");
		jpanel1.add(titledseparator3,cc.xywh(2,24,7,1));

		TitledSeparator titledseparator4 = new TitledSeparator();
		titledseparator4.setText("Input Files");
		jpanel1.add(titledseparator4,cc.xywh(2,7,7,1));

		m_PartConfigButton.setActionCommand("Browse");
		m_PartConfigButton.setText("Browse");
		jpanel1.add(m_PartConfigButton,cc.xy(8,13));
		m_PartConfigButton.addActionListener(new OpenPartFileAction());


		m_EventsConfigButton2.setActionCommand("Browse");
		m_EventsConfigButton2.setText("Browse");
		jpanel1.add(m_EventsConfigButton2,cc.xy(8,11));
		m_EventsConfigButton2.addActionListener(new OpenEventsFileAction());

		m_PluginsConfigButton.setActionCommand("Browse");
		m_PluginsConfigButton.setText("Browse");
		jpanel1.add(m_PluginsConfigButton,cc.xy(8,9));
		m_PluginsConfigButton.addActionListener(new OpenPluginsFileAction());

		m_SimConfigFileButton.setActionCommand("Browse");
		m_SimConfigFileButton.setText("Browse");
		jpanel1.add(m_SimConfigFileButton,cc.xy(8,4));
		m_SimConfigFileButton.addActionListener(new OpenSimFileAction());

		JLabel jlabel12 = new JLabel();
		jlabel12.setText("Network inputfile:");
		jpanel1.add(jlabel12,new CellConstraints(2,17,1,1,CellConstraints.RIGHT,CellConstraints.DEFAULT));

		JLabel jlabel13 = new JLabel();
		jlabel13.setText("PhysicalWorld inputfile:");
		jpanel1.add(jlabel13,new CellConstraints(2,21,1,1,CellConstraints.RIGHT,CellConstraints.DEFAULT));

		m_networkConfigfileField.setName("networkConfigfileField");
		jpanel1.add(m_networkConfigfileField,cc.xywh(4,17,3,1));

		m_pworldConfigfileField.setName("pworldConfigfileField");
		jpanel1.add(m_pworldConfigfileField,cc.xywh(4,21,3,1));

		m_NetConfigButton.setActionCommand("Browse");
		m_NetConfigButton.setText("Browse");
		jpanel1.add(m_NetConfigButton,cc.xy(8,17));
		m_NetConfigButton.addActionListener(new OpenNetFileAction());

		m_WorldConfigButton.setActionCommand("Browse");
		m_WorldConfigButton.setText("Browse");
		jpanel1.add(m_WorldConfigButton,cc.xy(8,21));
		m_WorldConfigButton.addActionListener(new OpenWorldFileAction());

		JLabel jlabel14 = new JLabel();
		jlabel14.setText("auto-run");
		jpanel1.add(jlabel14,new CellConstraints(2,34,1,1,CellConstraints.RIGHT,CellConstraints.DEFAULT));

		JLabel jlabel15 = new JLabel();
		jlabel15.setText("Network Class:");
		jpanel1.add(jlabel15,new CellConstraints(2,15,1,1,CellConstraints.RIGHT,CellConstraints.DEFAULT));

		JLabel jlabel16 = new JLabel();
		jlabel16.setText("PhysicalWorld Class:");
		jpanel1.add(jlabel16,new CellConstraints(2,19,1,1,CellConstraints.RIGHT,CellConstraints.DEFAULT));

		m_NetworkClassLabel.setName("NetworkClassLabel");
		jpanel1.add(m_NetworkClassLabel,cc.xywh(4,15,3,1));

		m_PWorldClassLabel.setName("PWorldClassLabel");
		jpanel1.add(m_PWorldClassLabel,cc.xywh(4,19,3,1));

		m_jbutton7.setActionCommand("Select");
		m_jbutton7.setText("Select");
		jpanel1.add(m_jbutton7,cc.xy(8,15));

		m_jbutton8.setActionCommand("Select");
		m_jbutton8.setText("Select");
		jpanel1.add(m_jbutton8,cc.xy(8,19));

		jpanel1.add(m_horizontallinecomponent1,cc.xywh(2,44,7,1));

		m_SaveButton.setActionCommand("Save");
		m_SaveButton.setText("Save");
		jpanel1.add(m_SaveButton,cc.xy(6,46));

		m_SaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {		
				// JButton s = (JButton) ae.getSource();

				presageConfig = makePresageConfig();

				if (presageConfig == null)
					return;

				// System.out.println(scriptedEvent.getLabel());

				ok = true;
				dialog.setVisible(false);
			}	
		});

		m_CancelButton.setActionCommand("Cancel");
		m_CancelButton.setText("Cancel");
		jpanel1.add(m_CancelButton,cc.xy(8,46));
		m_CancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {				
				ok = false;				
				presageConfig = null;
				dialog.setVisible(false);
			}	
		});




		jpanel1.add(createPanel1(),new CellConstraints(4,34,1,1,CellConstraints.CENTER,CellConstraints.DEFAULT));
		addFillComponents(jpanel1,new int[]{ 1,2,3,4,5,6,7,8,9 },new int[]{ 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47 });
		return jpanel1;
	}

	public JPanel createPanel1()
	{
		JPanel jpanel1 = new JPanel();
		FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0),FILL:DEFAULT:NONE","CENTER:DEFAULT:NONE");
		CellConstraints cc = new CellConstraints();
		jpanel1.setLayout(formlayout1);

		m_autorunFalseRadio.setActionCommand("false");
		m_autorunFalseRadio.setName("autorunFalseRadio");
		m_autorunFalseRadio.setText("false");
		m_buttongroup1.add(m_autorunFalseRadio);
		jpanel1.add(m_autorunFalseRadio,cc.xy(3,1));

		m_autorunTrueRadio.setActionCommand("true");
		m_autorunTrueRadio.setName("autorunTrueRadio");
		m_autorunTrueRadio.setText("true");
		m_buttongroup1.add(m_autorunTrueRadio);
		jpanel1.add(m_autorunTrueRadio,cc.xy(1,1));

		ButtonModel model = m_autorunTrueRadio.getModel();
		m_buttongroup1.setSelected(model, true);

		addFillComponents(jpanel1,new int[]{ 2 },new int[0]);
		return jpanel1;
	}

	/**
	 * Initializer
	 */
	protected void initializePanel()
	{
		File currDir = new File(".");
		fileChooser.setCurrentDirectory(currDir);
		fileChooser.setFileFilter(new XMLFileFilter());

		presageConfig = new PresageConfig();

		setLayout(new BorderLayout());
		add(createPanel(), BorderLayout.CENTER);
	}

	/**
   Show the chooser panel in a dialog
   @param parent a component in the owner frame or null
   @param title the dialog window title
	 */
	public boolean showDialog(Component parent, String title)
	{  
		ok = false;

		// locate the owner frame

		Frame owner = null;
		if (parent instanceof Frame)
			owner = (Frame) parent;
		else 
			owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parent);

		// if first time, or if owner has changed, make new dialog
		if (dialog == null || dialog.getOwner() != owner) 
		{      
			System.out.println(owner.getClass().toString());

			dialog = new JDialog(owner, true);
			dialog.add(this);
			dialog.getRootPane().setDefaultButton(m_SaveButton);
			dialog.pack();
		}

		// set title and show dialog

		dialog.setTitle(title);
		dialog.setSize(dialogSize);

		dialog.setLocation( parent.getLocationOnScreen().x + (parent.getWidth() - dialog.getWidth())/2, parent.getLocationOnScreen().y + (parent.getHeight() - dialog.getHeight())/2)  ;  // (parent.getX() + parent.getWidth()

		dialog.setVisible(true);
		return ok;
	}


//	class OpenFileAction implements ActionListener {
//	public void actionPerformed(ActionEvent ae) {
//	//... Open a file dialog.
//	fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//	int retval = fileChooser.showOpenDialog(ConfigFileEditorDialog.this);
//	if (retval == JFileChooser.APPROVE_OPTION) {
//	//... The user selected a file, get it, use it.
//	File file = fileChooser.getSelectedFile();

//	//... Update user interface.
//	m_participantsConfigfileField.setText(file.getAbsolutePath());
////	wordCountTF.setText("" + countWordsInFile(file));
//	}
//	}
//	}

	class OpenPartFileAction implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			//... Open a file dialog.

			fileChooser.setCurrentDirectory(new File(m_participantsConfigfileField.getText()));

			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int retval = fileChooser.showOpenDialog(ConfigFileEditorDialog.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				//... The user selected a file, get it, use it.
				File file = fileChooser.getSelectedFile();

				//... Update user interface.
				try{
					m_participantsConfigfileField.setText(file.getCanonicalPath());
				} catch (Exception e){}

			}
		}
	}

	class OpenNetFileAction implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			//... Open a file dialog.
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int retval = fileChooser.showOpenDialog(ConfigFileEditorDialog.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				//... The user selected a file, get it, use it.
				File file = fileChooser.getSelectedFile();

				//... Update user interface.
				m_networkConfigfileField.setText(file.getAbsolutePath());
//				wordCountTF.setText("" + countWordsInFile(file));
			}
		}
	}

	class OpenWorldFileAction implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			//... Open a file dialog.
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int retval = fileChooser.showOpenDialog(ConfigFileEditorDialog.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				//... The user selected a file, get it, use it.
				File file = fileChooser.getSelectedFile();

				//... Update user interface.
				m_pworldConfigfileField.setText(file.getAbsolutePath());
//				wordCountTF.setText("" + countWordsInFile(file));
			}
		}
	}

	class OpenEventsFileAction implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			//... Open a file dialog.
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int retval = fileChooser.showOpenDialog(ConfigFileEditorDialog.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				//... The user selected a file, get it, use it.
				File file = fileChooser.getSelectedFile();

				//... Update user interface.
				m_eventsConfigfileField.setText(file.getAbsolutePath());
//				wordCountTF.setText("" + countWordsInFile(file));
			}
		}
	}

	class OpenPluginsFileAction implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			//... Open a file dialog.
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int retval = fileChooser.showOpenDialog(ConfigFileEditorDialog.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				//... The user selected a file, get it, use it.
				File file = fileChooser.getSelectedFile();

				//... Update user interface.
				m_pluginsConfigfileField.setText(file.getAbsolutePath());
//				wordCountTF.setText("" + countWordsInFile(file));
			}
		}
	}

	class OpenSimFileAction implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			//... Open a file dialog.
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int retval = fileChooser.showOpenDialog(ConfigFileEditorDialog.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				//... The user selected a file, get it, use it.
				File file = fileChooser.getSelectedFile();

				//... Update user interface.
				m_simConfigfileField.setText(file.getAbsolutePath());
//				wordCountTF.setText("" + countWordsInFile(file));
			}
		}
	}

	class OpenFolderAction implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			//... Open a file dialog.
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int retval = fileChooser.showOpenDialog(ConfigFileEditorDialog.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				//... The user selected a file, get it, use it.
				File file = fileChooser.getSelectedFile();

				//... Update user interface.

//				wordCountTF.setText("" + countWordsInFile(file));
			}
		}
	}

	class XMLFileFilter extends javax.swing.filechooser.FileFilter {

		public boolean accept(File f) {
			return f.isDirectory() || f.getName().toLowerCase().endsWith(".xml");
		}

		public String getDescription() {
			return ".xml files";
		}


	}
}
