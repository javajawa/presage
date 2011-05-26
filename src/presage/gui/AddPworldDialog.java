package presage.gui;

import com.jeta.forms.components.separator.TitledSeparator;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Frame;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.SwingUtilities;

import presage.Event;
import presage.ScriptedEvent;
import presage.util.*;

import java.awt.Component;

public class AddPworldDialog extends JPanel implements TreeSelectionListener
{

	private static final long serialVersionUID = 1L;

	Dimension dialogSize = new Dimension(600, 500);
	JDialog dialog;
	private boolean ok;

	JTree m_InspectorJTree = new JTree();
	JPanel m_ParameterPanel = new JPanel();
	JButton m_AddButton = new JButton();
	JButton m_CancelButton = new JButton();
	JTextField m_jtextfield1 = new JTextField();

	// For the dynamic parameter fields
	JTextField[] parameterfields;

	ScriptedEvent scriptedEvent;


	/**
	 * Default constructor
	 */
	public AddPworldDialog()
	{
		initializePanel();
	}

	/** Required by TreeSelectionListener interface. */
	public void valueChanged(TreeSelectionEvent e) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode)m_InspectorJTree.getLastSelectedPathComponent();

		if (node == null) return;

		Object nodeInfo = node.getUserObject();
		if (node.isLeaf() && nodeInfo.getClass().equals(ConstructorInfo.class)) {
			ConstructorInfo eventinfo = (ConstructorInfo)nodeInfo;

			m_ParameterPanel.removeAll();

			m_ParameterPanel.setLayout(new GridLayout(0,2));

			parameterfields = new JTextField[eventinfo.parameterclasses.length];

			for (int i = 0; i < eventinfo.parameterclasses.length; i++){

				JLabel label = new JLabel(eventinfo.parameterclasses[i].getSimpleName() + "  "+ eventinfo.parameternames[i] + " = ", JLabel.TRAILING);      	
				m_ParameterPanel.add(label);
				JTextField field = new JTextField();
				// field.setSize(new Dimension(90, 30));	
				label.setLabelFor(field);
				m_ParameterPanel.add(field);
				parameterfields[i] = field;

			}

			// m_ParameterPanel.setVisible(true);
			this.revalidate();
			this.repaint();

			System.out.println("Method Selected");



		} else { // i.e. its a class not method
			m_ParameterPanel.removeAll();
			m_ParameterPanel.add(new JLabel("Select a method"));    
			this.revalidate();
			this.repaint();
		}

	}

	private ScriptedEvent makeScriptedEvent(){

		DefaultMutableTreeNode node = (DefaultMutableTreeNode)m_InspectorJTree.getLastSelectedPathComponent();

		// show an info box?
		if (node == null){
			JOptionPane.showMessageDialog(null, "You must select a Event Constructor, then input parameter values and execution time.", "Information", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}

		Object nodeInfo = node.getUserObject();
		if (node.isLeaf()) {
			if(nodeInfo.getClass().equals(ConstructorInfo.class)){
				ConstructorInfo eventInfo = (ConstructorInfo)nodeInfo;

				Object[] parametervalues = new Object[eventInfo.parameterclasses.length]; 

				for (int i = 0; i < parameterfields.length; i++){

					Class<?> c = eventInfo.parameterclasses[i];

					try {
						if (c.equals(int.class)){
							parametervalues[i] = Integer.parseInt(parameterfields[i].getText());
						} else if (c.equals(double.class)){
							parametervalues[i] = Double.parseDouble(parameterfields[i].getText());
						} else if (c.equals(float.class)){
							parametervalues[i] = Float.parseFloat(parameterfields[i].getText());
						} else if (c.equals(boolean.class)){
							parametervalues[i] =Boolean.parseBoolean(parameterfields[i].getText());
						} else if (c.equals(long.class)){
							parametervalues[i] =Long.parseLong(parameterfields[i].getText());
						} else {
							try{
								Constructor<?> ct = c.getConstructor(new Class[]{String.class});
								parametervalues[i] = ct.newInstance(new Object[]{parameterfields[i].getText()});

							} catch (Exception e){
								System.err.println(eventInfo.classname + " must only contain parameter types int, double, float, boolean or those which have a constructor which accepts a String failed on parameter " + eventInfo.parameternames[i] + " class = " + c.getName() );
								JOptionPane.showMessageDialog(this, "Error creating parameter objects. Could not construct " + c.getSimpleName() + " "+ eventInfo.parameternames[i] + " with value = " + parameterfields[i].getText(), "Error", JOptionPane.ERROR_MESSAGE);
								return null;
							}
						}
					} catch (NumberFormatException e){
						System.err.println(eventInfo.classname + " must only contain parameter types int, double, float, boolean or those which have a constructor which accepts a String failed on parameter " + eventInfo.parameternames[i] + " class = " + c.getName() );
						JOptionPane.showMessageDialog(this, "Error creating parameter objects. Could not construct " + c.getSimpleName() + " " + eventInfo.parameternames[i] + " with value = " + parameterfields[i].getText(), "Error", JOptionPane.ERROR_MESSAGE);
						return null;
					}
				}

				int execycle;

				if (m_jtextfield1.getText().equalsIgnoreCase("")){
					execycle = 10; // Simulation.cycle + 1;
				} else {
					try{
						execycle = Integer.parseInt(m_jtextfield1.getText());
					} catch (NumberFormatException e){
						JOptionPane.showMessageDialog(this, "Execution time incorrectly specified.", "Error", JOptionPane.ERROR_MESSAGE);
						return null;
					}
				} 

				if (execycle < 0){
					JOptionPane.showMessageDialog(this, "Execution time incorrectly specified. Negative values not allowed.", "Error", JOptionPane.ERROR_MESSAGE);
					return null;
				}

//				for (int i = 0; i < parametervalues.length; i++)
//				System.out.println(parametervalues[i].toString());

				//scriptedEventInfo.classname, scriptedEventInfo.methodname, scriptedEventInfo.parameterclasses, scriptedEventInfo.parameternames, parametervalues


				Event event;

				try{
					Class c = Class.forName(eventInfo.classname);
					Constructor<?> ct = c.getConstructor(eventInfo.parameterclasses);
					event = (Event)ct.newInstance(parametervalues);

					//System.out.println(event.getLabel());
					
				} catch (Exception e){
					System.err.println(eventInfo.classname + ": failed to construct event" + e);
					return null;
				}

				 ScriptedEvent se = new ScriptedEvent(execycle, event);

				//System.out.println(se.getLabel());
				
				return se;
				// System.out.println(scriptElement.toString());

				// return scriptedEvent;

			} else {
				JOptionPane.showMessageDialog(this, "Your selection is not a method.", "Information", JOptionPane.INFORMATION_MESSAGE);
				return null;

			}
		} else {
			JOptionPane.showMessageDialog(this, "Your selection is not a method.", "Information", JOptionPane.INFORMATION_MESSAGE);
			return null;

		}

	}

	public boolean checkAnnotation(Class classObject, AnnotatedElement[] elts, DefaultMutableTreeNode top){
		boolean result = false;
		for(AnnotatedElement e : elts) { 
			if (checkAnnotation(classObject, e, top))
				result = true;
		}
		return result;
	}

	public boolean checkAnnotation(Class classObject, AnnotatedElement e, DefaultMutableTreeNode top){

		if (e == null){
			// System.out.println("DEBUG null"); 
			return false;
		}

		if (!e.isAnnotationPresent(presage.annotations.EventConstructor.class)) {
			//	System.out.println("DEBUG no annotation");
			return false;
		}

		// System.out.println("DEBUG found annotation");

		presage.annotations.EventConstructor u = e.getAnnotation(presage.annotations.EventConstructor.class);

		String[] parameternames = u.value();	    

		ConstructorInfo evinfo = new ConstructorInfo(classObject.getName(), classObject.getSimpleName(), ((Constructor)e).getParameterTypes(), parameternames);

		System.out.println("params: " + Arrays.asList(parameternames));

		DefaultMutableTreeNode eventnode = new DefaultMutableTreeNode(evinfo);
		
		top.add(eventnode);	

		return true;

	}

//	public boolean handleClass(Class c, DefaultMutableTreeNode top){
//
//		DefaultMutableTreeNode classnode = new DefaultMutableTreeNode(new ClassInfo(c.getSimpleName()));
//		
//		// is it an event?
//		Class[] interfaces = c.getInterfaces();	
//
//		if (interfaces.length == 0){
//			System.out.println( " : does not implement any interfaces");
//		} else { 
//
//			for (int j = 0; j < interfaces.length; j++){
//
//				try{
//					if (interfaces[j].equals(Class.forName("presage.Event")) ){
//
//						System.out.println( c.getName() + " : implements presage.Event");
//
//						if (checkAnnotation(c, c.getConstructors(), classnode)){
//							top.add(classnode);
//							return true;
//						}
//
//					}
//
//				} catch (ClassNotFoundException e){
//					System.err.println("" + e);
//				}
//			}
//		}
//		return false;
//	}

	public boolean handleFile(File file, DefaultMutableTreeNode top){

//		System.out.println("File Found: " + file.getName());
//		DefaultMutableTreeNode filenode = new DefaultMutableTreeNode(new FileInfo(file.getName()));

		String ext = StringParseTools.extensionForFilename(file.getName());

		if (ext.equalsIgnoreCase("class")){

			// add to list of class files!

			String temp = file.getPath(); // + "/" +  presage.util.StringParseTools.readTokens(file.getName(), ".")[0];

			temp = temp.replace("bin\\","");
			temp = temp.replace('\\','.');
			temp = StringParseTools.filenameNoExtension(temp);

			System.out.print("Class file found: " + temp);

			try{
				Class<?> c = Class.forName(temp);

				// only add classes that contain events.
				// DefaultMutableTreeNode classnode = new DefaultMutableTreeNode(new ClassInfo(c.getSimpleName()));
				
				// is it an event?
				Class[] interfaces = c.getInterfaces();	

				if (interfaces.length == 0){
					System.out.println( " : does not implement any interfaces");
				} else { 

					for (int j = 0; j < interfaces.length; j++){

						try{
							if (interfaces[j].equals(Class.forName("presage.Event")) ){

								System.out.println( c.getName() + " : implements presage.Event");

								if (checkAnnotation(c, c.getConstructors(), top)){
									// top.add(classnode);
									return true;
								}

							}

						} catch (ClassNotFoundException e){
							System.err.println("" + e);
						}
					}
				}
			} catch (ClassNotFoundException e){
				System.err.println("" + e);
			}
		}
		return false;
	}


	private boolean handleFolder(String path, DefaultMutableTreeNode top){

		File toF = new File(path);
		
		boolean result = false;
		DefaultMutableTreeNode filenode = new DefaultMutableTreeNode(new FileInfo(toF.getName()));


		//get a list of everything in it
		File[] files = toF.listFiles();
		
		System.out.println(Arrays.asList(files));

		try {
			for (int i = 0; i < files.length; i++) {
				// if its a file check if its a class file!
				if (files[i].isFile()) {
					System.out.println("File Found: " + files[i].getName());
					if (handleFile(files[i], filenode)){	
						top.add(filenode);
						result = true;
					}

				} else if (files[i].isDirectory()) {
					System.out.println("Folder Found " + files[i].getName());
					if (handleFolder(files[i].getPath(), filenode)){	
						top.add(filenode);
						result = true;
					}
				}

			}
		} catch (NullPointerException e) {
			System.err.println("MethodAddForm: " + e);
		}

		return result;
	}

	private void createNodes(DefaultMutableTreeNode top) {
		String path = "bin/";	
		handleFolder(path, top);
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

	public JPanel createbuttonpanel(){
		
		JPanel jpanel1 = new JPanel();
		
		m_AddButton.setActionCommand("Add");
		m_AddButton.setName("AddButton");
		m_AddButton.setText("Add");
		
		jpanel1.add(m_AddButton); // ,cc.xy(3,16));

		m_AddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {		
				// JButton s = (JButton) ae.getSource();

				scriptedEvent = makeScriptedEvent();
				
				if (scriptedEvent == null)
					return;
					
				// System.out.println(scriptedEvent.getLabel());

				ok = true;
				dialog.setVisible(false);
			}	
		});

		m_CancelButton.setActionCommand("Cancel");
		m_CancelButton.setName("CancelButton");
		m_CancelButton.setText("Cancel");
		jpanel1.add(m_CancelButton); // ,cc.xy(5,16));
		m_CancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {				
				ok = false;				
				scriptedEvent = null;
				dialog.setVisible(false);
			}	
		});
		
		return jpanel1;
		
	}
	
	public JPanel createPanel()
	{
		JPanel jpanel1 = new JPanel();
		FormLayout formlayout1 = new FormLayout("FILL:4DLU:NONE,FILL:DEFAULT:GROW(1.0),FILL:75PX:NONE,FILL:4DLU:NONE,FILL:75PX:NONE,FILL:75PX:GROW(1.0),FILL:4DLU:NONE","CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,FILL:DEFAULT:GROW(1.0),FILL:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,FILL:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE");
		CellConstraints cc = new CellConstraints();
		jpanel1.setLayout(formlayout1);

		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Your Source");

		createNodes(top);

		m_InspectorJTree = new JTree(top);

		m_InspectorJTree.addTreeSelectionListener(this);


		m_InspectorJTree.setName("InspectorJTree");
		JScrollPane jscrollpane1 = new JScrollPane();
		jscrollpane1.setViewportView(m_InspectorJTree);
		jscrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jscrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jpanel1.add(jscrollpane1,cc.xywh(2,4,5,3));

		m_ParameterPanel.setName("ParameterPanel");
		jpanel1.add(m_ParameterPanel,cc.xywh(2,9,5,1));

		TitledSeparator titledseparator1 = new TitledSeparator();
		titledseparator1.setText("Step 1: Select Event from Tree:");
		jpanel1.add(titledseparator1,cc.xywh(2,2,5,1));

		TitledSeparator titledseparator2 = new TitledSeparator();
		titledseparator2.setText("Step 2: Enter Constructor Parameters");
		jpanel1.add(titledseparator2,cc.xywh(2,7,5,1));

		TitledSeparator titledseparator3 = new TitledSeparator();
		titledseparator3.setText("Step 3: Enter Event  Execution Time");
		jpanel1.add(titledseparator3,cc.xywh(2,11,5,1));

		

		TitledSeparator titledseparator4 = new TitledSeparator();
		titledseparator4.setText("Step 4: Click Add");
		jpanel1.add(titledseparator4,cc.xywh(2,15,5,1));

		JLabel jlabel1 = new JLabel();
		jlabel1.setText("Simulation Cycle (Blank for next cycle):\n");
		jlabel1.setHorizontalAlignment(JLabel.RIGHT);
		jpanel1.add(jlabel1,cc.xywh(2,13,2,1));

		jpanel1.add(m_jtextfield1,cc.xywh(5,13,2,1));

		addFillComponents(jpanel1,new int[]{ 1,2,3,4,5,6,7 },new int[]{ 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17 });
		return jpanel1;
	}




	/**
	 * Initializer
	 */
	protected void initializePanel()
	{
		setLayout(new BorderLayout());
		add(createPanel(), BorderLayout.CENTER);
		add(createbuttonpanel(), BorderLayout.SOUTH);
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
			dialog.getRootPane().setDefaultButton(m_AddButton);
			dialog.pack();
		}

		// set title and show dialog



		dialog.setTitle(title);
		dialog.setSize(dialogSize);

		dialog.setLocation( parent.getLocationOnScreen().x + (parent.getWidth() - dialog.getWidth())/2, parent.getLocationOnScreen().y + (parent.getHeight() - dialog.getHeight())/2)  ;  // (parent.getX() + parent.getWidth()

		dialog.setVisible(true);
		return ok;
	}


}
