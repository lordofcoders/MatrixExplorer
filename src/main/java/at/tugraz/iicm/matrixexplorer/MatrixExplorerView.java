package at.tugraz.iicm.matrixexplorer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.MenuElement;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.JTableHeader;

import org.jdesktop.application.Action;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;

import at.tugraz.iicm.matrixexplorer.algorithms.ReorderingAlgorithm;
import at.tugraz.iicm.matrixexplorer.algorithms.TwoDimSort;
import at.tugraz.iicm.matrixexplorer.data.DataSetReader;
import at.tugraz.iicm.matrixexplorer.data.DataSetWriter;
import at.tugraz.iicm.matrixexplorer.data.Matrix;
import at.tugraz.iicm.matrixexplorer.data.MatrixManager;
import at.tugraz.iicm.matrixexplorer.ui.DragDropRowTableUI;
import at.tugraz.iicm.matrixexplorer.ui.MainComponentListener;
import at.tugraz.iicm.matrixexplorer.ui.MatrixTable;
import at.tugraz.iicm.matrixexplorer.ui.MatrixTableModel;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * The application's main frame.
 * 
 * @author Christin Seifert
 */
public class MatrixExplorerView extends FrameView {

	private MatrixManager matrixManager;
	private final static Logger logger = Logger.getLogger(MatrixExplorerView.class.getSimpleName());
	private int startFontSize = 0;
	private int scalingSliderStaringValue = 0;

	/**
	 * Construct the frame for the application.
	 * 
	 * @param app
	 *            the application
	 */
	public MatrixExplorerView(SingleFrameApplication app) {
		super(app);

		initComponents();
		((MatrixTable) jTable).setRenderMode(MatrixTable.RENDER_AS_CIRCLES);

		jTable.updateUI();
		jTable.setShowHorizontalLines(true);

		// status bar initialization - message timeout, idle icon and busy
		// animation, etc
		ResourceMap resourceMap = getResourceMap();
		int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
		messageTimer = new Timer(messageTimeout, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statusMessageLabel.setText("");
			}
		});

		messageTimer.setRepeats(false);
		int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
		for (int i = 0; i < busyIcons.length; i++) {
			busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
		}

		busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
				statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
			}
		});

		idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
		statusAnimationLabel.setIcon(idleIcon);
		progressBar.setVisible(false);
		scalingSliderStaringValue = scalingSlider.getValue();
		
		// connecting action tasks to status bar via TaskMonitor
		TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
		taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
			public void propertyChange(java.beans.PropertyChangeEvent evt) {
				String propertyName = evt.getPropertyName();
				if ("started".equals(propertyName)) {
					if (!busyIconTimer.isRunning()) {
						statusAnimationLabel.setIcon(busyIcons[0]);
						busyIconIndex = 0;
						busyIconTimer.start();
					}
					progressBar.setVisible(true);
					progressBar.setIndeterminate(true);
				} else if ("done".equals(propertyName)) {
					busyIconTimer.stop();
					statusAnimationLabel.setIcon(idleIcon);
					progressBar.setVisible(false);
					progressBar.setValue(0);
				} else if ("message".equals(propertyName)) {
					String text = (String) (evt.getNewValue());
					statusMessageLabel.setText((text == null) ? "" : text);
					messageTimer.restart();
				} else if ("progress".equals(propertyName)) {
					int value = (Integer) (evt.getNewValue());
					progressBar.setVisible(true);
					progressBar.setIndeterminate(false);
					progressBar.setValue(value);
				}
			}
		});

		scalingSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				scaleMenuFonts();
				scaleContent();
			}
		});

		((MatrixTable) jTable).setScrollPane(jScrollPane1);

		// set the row DnD works.
		jTable.setUI(new DragDropRowTableUI());
		jTable.doLayout();

		// catch window resize event
		jScrollPane1.addComponentListener(new MainComponentListener((MatrixTable) jTable));
		jScrollPane1.setAutoscrolls(true);
	}

	/**
	 * Action for the about box.
	 */
	@Action
	public void showAboutBox() {
		if (aboutBox == null) {
			JFrame mainFrame = MatrixExplorerApp.getApplication().getMainFrame();
			aboutBox = new MatrixExplorerAboutBox(mainFrame);
			aboutBox.setLocationRelativeTo(mainFrame);
		}
		MatrixExplorerApp.getApplication().show(aboutBox);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		mainPanel = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTable = new MatrixTable();
		jTable.setFillsViewportHeight(true);
		menuBar = new javax.swing.JMenuBar();

		javax.swing.JMenu fileMenu = new javax.swing.JMenu();
		jMenuItem1 = new javax.swing.JMenuItem();
		jMenuItem4 = new javax.swing.JMenuItem();
		javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();

		jMenu1 = new javax.swing.JMenu();
		buttonDisplayRawData = new javax.swing.JRadioButtonMenuItem();
		buttonDisplayCircles = new javax.swing.JRadioButtonMenuItem();
		jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
		jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();

		buttonDo2DSort = new javax.swing.JMenu();
		jMenuItem2 = new javax.swing.JMenuItem();
		jMenuItem3 = new javax.swing.JMenuItem();
		javax.swing.JMenu helpMenu = new javax.swing.JMenu();
		javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
		statusPanel = new javax.swing.JPanel();
		javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
		statusMessageLabel = new javax.swing.JLabel();
		statusAnimationLabel = new javax.swing.JLabel();
		progressBar = new javax.swing.JProgressBar();
		buttonGroupDisplay = new javax.swing.ButtonGroup();

		mainPanel.setName("mainPanel"); // NOI18N

		jScrollPane1.setName("jScrollPane1"); // NOI18N

		jTable.setModel(new MatrixTableModel());
		jTable.setName("jTable"); // NOI18N
		jScrollPane1.setViewportView(jTable);

		javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
		mainPanelLayout
				.setHorizontalGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
						mainPanelLayout.createSequentialGroup().addContainerGap()
								.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
								.addContainerGap()));
		mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, mainPanelLayout.createSequentialGroup().addContainerGap()
						.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)));
		mainPanel.setLayout(mainPanelLayout);

		menuBar.setName("menuBar"); // NOI18N

		javax.swing.ActionMap actionMap = org.jdesktop.application.Application
				.getInstance(at.tugraz.iicm.matrixexplorer.MatrixExplorerApp.class).getContext()
				.getActionMap(MatrixExplorerView.class, this);
		fileMenu.setAction(actionMap.get("onSaveButton")); // NOI18N
		org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
				.getInstance(at.tugraz.iicm.matrixexplorer.MatrixExplorerApp.class).getContext()
				.getResourceMap(MatrixExplorerView.class);
		fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
		fileMenu.setName("fileMenu"); // NOI18N

		jMenuItem1.setAction(actionMap.get("onLoadButton")); // NOI18N
		jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
		jMenuItem1.setName("jMenuItem1"); // NOI18N
		jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem1ActionPerformed(evt);
			}
		});
		fileMenu.add(jMenuItem1);

		jMenuItem4.setAction(actionMap.get("onSaveButton")); // NOI18N
		jMenuItem4.setText(resourceMap.getString("jMenuItem4.text")); // NOI18N
		jMenuItem4.setName("jMenuItem4"); // NOI18N
		fileMenu.add(jMenuItem4);

		exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
		exitMenuItem.setName("exitMenuItem"); // NOI18N
		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);

		jMenu1.setAction(actionMap.get("onDisplayDataAsRaw")); // NOI18N
		jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
		jMenu1.setName("jMenu1"); // NOI18N

		buttonDisplayRawData.setAction(actionMap.get("onDisplayDataAsRaw")); // NOI18N
		buttonGroupDisplay.add(buttonDisplayRawData);
		buttonDisplayRawData.setText(resourceMap.getString("buttonDisplayRawData.text")); // NOI18N
		buttonDisplayRawData.setToolTipText(resourceMap.getString("buttonDisplayRawData.toolTipText")); // NOI18N
		buttonDisplayRawData.setName("buttonDisplayRawData"); // NOI18N
		jMenu1.add(buttonDisplayRawData);

		buttonDisplayCircles.setAction(actionMap.get("onDisplayDataAsCircles")); // NOI18N
		buttonGroupDisplay.add(buttonDisplayCircles);
		buttonDisplayCircles.setSelected(true);
		buttonDisplayCircles.setText(resourceMap.getString("buttonDisplayCircles.text")); // NOI18N
		buttonDisplayCircles.setToolTipText(resourceMap.getString("buttonDisplayCircles.toolTipText")); // NOI18N
		buttonDisplayCircles.setName("buttonDisplayCircles"); // NOI18N
		jMenu1.add(buttonDisplayCircles);

		jRadioButtonMenuItem1.setAction(actionMap.get("onDisplayDataAsBars")); // NOI18N
		buttonGroupDisplay.add(jRadioButtonMenuItem1);
		jRadioButtonMenuItem1.setText(resourceMap.getString("jRadioButtonMenuItem1.text")); // NOI18N
		jRadioButtonMenuItem1.setName("jRadioButtonMenuItem1"); // NOI18N
		jMenu1.add(jRadioButtonMenuItem1);

		jRadioButtonMenuItem2.setAction(actionMap.get("onDisplayAsBertinsVisuals")); // NOI18N
		buttonGroupDisplay.add(jRadioButtonMenuItem2);
		jRadioButtonMenuItem2.setText(resourceMap.getString("jRadioButtonMenuItem2.text")); // NOI18N
		jRadioButtonMenuItem2.setName("jRadioButtonMenuItem2"); // NOI18N
		jMenu1.add(jRadioButtonMenuItem2);

		menuBar.add(jMenu1);

		buttonDo2DSort.setAction(actionMap.get("resetMatrix")); // NOI18N
		buttonDo2DSort.setText(resourceMap.getString("buttonDo2DSort.text")); // NOI18N
		buttonDo2DSort.setName("buttonDo2DSort"); // NOI18N

		jMenuItem2.setAction(actionMap.get("on2DSort")); // NOI18N
		jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
		jMenuItem2.setName("jMenuItem2"); // NOI18N
		buttonDo2DSort.add(jMenuItem2);

		jMenuItem3.setAction(actionMap.get("resetMatrix")); // NOI18N
		jMenuItem3.setText(resourceMap.getString("jMenuItem3.text")); // NOI18N
		jMenuItem3.setToolTipText(resourceMap.getString("jMenuItem3.toolTipText")); // NOI18N
		jMenuItem3.setName("jMenuItem3"); // NOI18N
		buttonDo2DSort.add(jMenuItem3);

		menuBar.add(buttonDo2DSort);

		fontMenu = new JMenu("Fonts");
		menuBar.add(fontMenu);

		scalingSlider = new JSlider();
		fontMenu.add(scalingSlider);
		scalingSlider.setToolTipText("");
		scalingSlider.setPaintTicks(true);
		scalingSlider.setMajorTickSpacing(25);
		scalingSlider.setPaintLabels(true);

		scalingSlider.setValue(100);
		scalingSlider.setMinimum(100);
		scalingSlider.setMaximum(200);

		helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
		helpMenu.setName("helpMenu"); // NOI18N

		aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
		aboutMenuItem.setName("aboutMenuItem"); // NOI18N
		helpMenu.add(aboutMenuItem);

		menuBar.add(helpMenu);

		statusPanel.setName("statusPanel"); // NOI18N

		statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

		statusMessageLabel.setName("statusMessageLabel"); // NOI18N

		statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

		progressBar.setName("progressBar");

		javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
		statusPanelLayout.setHorizontalGroup(
			statusPanelLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(statusPanelSeparator, GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
				.addGroup(statusPanelLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(statusMessageLabel)
					.addPreferredGap(ComponentPlacement.RELATED, 758, Short.MAX_VALUE)
					.addComponent(statusAnimationLabel)
					.addContainerGap())
				.addGroup(statusPanelLayout.createSequentialGroup()
					.addContainerGap(610, Short.MAX_VALUE)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		statusPanelLayout.setVerticalGroup(
			statusPanelLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(statusPanelLayout.createSequentialGroup()
					.addComponent(statusPanelSeparator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(statusPanelLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(statusMessageLabel)
						.addComponent(statusAnimationLabel)
						.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(25))
		);
		statusPanel.setLayout(statusPanelLayout);

		setComponent(mainPanel);
		setMenuBar(menuBar);
		setStatusBar(statusPanel);
	}// </editor-fold>//GEN-END:initComponents

	private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem1ActionPerformed
	}// GEN-LAST:event_jMenuItem1ActionPerformed

	/**
	 * Action for the load button. Opens a file dialog and loads the given file.
	 * Informs the user if the file is not in correct format.
	 */
	@Action
	public void onLoadButton() {

		ProtectionDomain pd = MatrixExplorerView.class.getProtectionDomain();
		CodeSource cs = pd.getCodeSource();
		java.net.URL location = cs.getLocation();
		Preferences prefs = Preferences.userRoot().node(getClass().getName());
		JFileChooser chooser = new JFileChooser(prefs.get("last_used", location.getFile() + "../../data"));

		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Select Data File");
		chooser.showOpenDialog(null);

		File file = chooser.getSelectedFile();

		if (file != null) {

			String f = file.getAbsolutePath();
			prefs.put("last_used", file.getParent());

			try {
				JTextField delimiter = new JTextField(2);
				JTextField echaracter = new JTextField(2);
				String[] encodingStrings = { "US-ASCII", "ISO-8859-1", "UTF-8", "UTF-16BE", "UTF-16LE", "UTF-16" };
				// Create the combo box, select item at index 4.
				// Indices start at 0, so 4 specifies the pig.
				JComboBox encoding = new JComboBox(encodingStrings);
				encoding.setSelectedIndex(2);
				JPanel myPanel = new JPanel();
				myPanel.add(new JLabel("Encoding:"));
				myPanel.add(encoding);
				myPanel.add(Box.createVerticalStrut(15)); // a spacer
				myPanel.add(new JLabel("Delimiter:"));
				myPanel.add(delimiter);
				myPanel.add(Box.createVerticalStrut(15)); // a spacer
				myPanel.add(new JLabel("Escape Character"));
				myPanel.add(echaracter);
				delimiter.setText(",");
				int result = JOptionPane.showConfirmDialog(null, myPanel, "CSV Import Settings",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {

					Matrix matrix = DataSetReader.readDoubleMatrix(file, echaracter.getText(), delimiter.getText(),
							encodingStrings[encoding.getSelectedIndex()]);
					matrixManager = new MatrixManager(matrix);
					((MatrixTableModel) jTable.getModel()).setMatrix(matrixManager.getMatrix());
					((MatrixTableModel) jTable.getModel()).fireTableStructureChanged();

					// BertinVisualsGenerator ber = new
					// BertinVisualsGenerator();
					// ber.drawGraphics(matrix);
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(mainPanel, "Could not read input data from file " + f,
						"Error in reading input data", JOptionPane.ERROR_MESSAGE);
				logger.log(Level.SEVERE, "error in reading input data from file " + f);
			}

			jTable.updateUI();
			jTable.setUI(new DragDropRowTableUI());
			jTable.doLayout();
		}
	}

	/**
	 * Action for the save button. Opens a file dialog and saves the matrix to
	 * the given file. Informs the user if the file could not be saved.
	 */
	@Action
	public void onSaveButton() {
		ProtectionDomain pd = MatrixExplorerView.class.getProtectionDomain();
		CodeSource cs = pd.getCodeSource();
		java.net.URL location = cs.getLocation();
		Preferences prefs = Preferences.userRoot().node(getClass().getName());
		JFileChooser chooser = new JFileChooser(prefs.get("last_used", location.getFile() + "../../data"));
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Select File Location");
		chooser.showSaveDialog(null);
		File file = chooser.getSelectedFile();
		if (file != null) {
			prefs.put("last_used", file.getParent());
			try {
				JTextField delimiter = new JTextField(2);
				JTextField echaracter = new JTextField(2);
				String[] encodingStrings = { "US-ASCII", "ISO-8859-1", "UTF-8", "UTF-16BE", "UTF-16LE", "UTF-16" };
				// Create the combo box, select item at index 4.
				// Indices start at 0, so 4 specifies the pig.
				JComboBox encoding = new JComboBox(encodingStrings);
				encoding.setSelectedIndex(2);
				JPanel myPanel = new JPanel();
				myPanel.add(new JLabel("Encoding:"));
				myPanel.add(encoding);
				myPanel.add(Box.createVerticalStrut(15)); // a spacer
				myPanel.add(new JLabel("Delimiter:"));
				myPanel.add(delimiter);
				myPanel.add(Box.createVerticalStrut(15)); // a spacer
				myPanel.add(new JLabel("Escape Character"));
				myPanel.add(echaracter);
				delimiter.setText(",");
				int result = JOptionPane.showConfirmDialog(null, myPanel, "CSV Export Settings",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					DataSetWriter.writeDoubleMatrix(file.getCanonicalPath(), matrixManager.getMatrix(),
							echaracter.getText(), delimiter.getText(), encodingStrings[encoding.getSelectedIndex()]);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(mainPanel, "Could not save matrix", "Error in saving",
						JOptionPane.ERROR_MESSAGE);
				logger.log(Level.SEVERE, "error in saving matrix to  file");
			}
		}
	}

	/**
	 * Action for the display data as raw button. Shows the matrix data as
	 * numbers.
	 */
	@Action
	public void onDisplayDataAsRaw() {
		((MatrixTable) jTable).setRenderMode(MatrixTable.RENDER_AS_NUMBERS);

		jTable.doLayout();
		jTable.updateUI();

		// set the row DnD works.
		jTable.setUI(new DragDropRowTableUI());
	}

	/**
	 * Action for the display data as raw button. Shows the matrix data as
	 * circles.
	 */
	@Action
	public void onDisplayDataAsCircles() {
		((MatrixTable) jTable).setRenderMode(MatrixTable.RENDER_AS_CIRCLES);

		jTable.doLayout();
		jTable.updateUI();

		// set the row DnD works.
		jTable.setUI(new DragDropRowTableUI());
	}

	/**
	 * Action for the display data as raw button. Shows the matrix data as bars.
	 */
	@Action
	public void onDisplayDataAsBars() {
		((MatrixTable) jTable).setRenderMode(MatrixTable.RENDER_AS_BARS);

		jTable.doLayout();
		jTable.updateUI();

		// set the row DnD works.
		jTable.setUI(new DragDropRowTableUI());
	}

	/**
	 * Action for the display data as raw button. Shows the matrix data as
	 * Bertin's visuals.
	 */
	@Action
	public void onDisplayAsBertinsVisuals() {
		((MatrixTable) jTable).setRenderMode(MatrixTable.RENDER_AS_BERTINS_VISUALS);

		jTable.doLayout();
		jTable.updateUI();

		// set the row DnD works.
		jTable.setUI(new DragDropRowTableUI());
	}

	/**
	 * Action for the 2D sorting button. Sorts the matrix automatically.
	 */
	@Action
	public void on2DSort() {

		getApplication().getContext().getTaskService().execute(new Task(getApplication()) {

			@Override
			protected Object doInBackground() throws Exception {
				// setProgress(0, 0, 0);
				ReorderingAlgorithm sorter = new TwoDimSort();
				Matrix newMatrix = sorter.getSorted(((MatrixTableModel) jTable.getModel()).getMatrix());
				setMessage("Sorting matrix (2D Sort)...");
				return newMatrix;
			}

			@Override
			protected void succeeded(Object result) {
				super.succeeded(result);
				matrixManager.setMatrix((Matrix) result);
				((MatrixTableModel) jTable.getModel()).setMatrix(matrixManager.getMatrix());
				((MatrixTableModel) jTable.getModel()).fireTableStructureChanged();
				jTable.doLayout();
			}

		});
	}

	/**
	 * Resets the matrix to the loaded version.
	 */
	@Action
	public void resetMatrix() {
		matrixManager.resetMatrix();
		((MatrixTableModel) jTable.getModel()).setMatrix(matrixManager.getMatrix());
		((MatrixTableModel) jTable.getModel()).fireTableStructureChanged();
	}

	public void scaleMenuFonts() {

		int selectedScaling = scalingSlider.getValue();
		float scalingValue = (float) selectedScaling / 100;

		MenuElement[] menuElements = menuBar.getSubElements();
		for (MenuElement elem : menuElements) {

			scaleMenuElement(elem, scalingValue);
			MenuElement[] subElements = elem.getSubElements();

			// foreach popup menu
			for (MenuElement sub : subElements) {

				// get its items
				MenuElement[] popupMenuElems = sub.getSubElements();
				for (MenuElement ssub : popupMenuElems) {
					scaleMenuElement(ssub, scalingValue);
				}
			}
		}

		menuBar.updateUI();
	}

	public void scaleContent() {

		int selectedScaling = scalingSlider.getValue();
		float scalingValue = (float) selectedScaling / 100;

		// scale table header
		((MatrixTable) jTable).setMatrixTableHeaderFontSize(startFontSize, scalingValue);

		// now scale table fonts
		jTable.setFont(jTable.getFont().deriveFont((float) startFontSize * scalingValue));
		jTable.doLayout();
	}

	private void scaleMenuElement(MenuElement elem, float scalingValue) {

		Font menuElementFont = elem.getComponent().getFont();
		int fontSize = menuElementFont.getSize();

		if (startFontSize <= 0) {
			startFontSize = fontSize;
		}

		Font scaledFont = menuElementFont.deriveFont((float) startFontSize * scalingValue);
		elem.getComponent().setFont(scaledFont);
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JRadioButtonMenuItem buttonDisplayCircles;
	private javax.swing.JRadioButtonMenuItem buttonDisplayRawData;
	private javax.swing.JMenu buttonDo2DSort;
	private javax.swing.ButtonGroup buttonGroupDisplay;
	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenuItem jMenuItem1;
	private javax.swing.JMenuItem jMenuItem2;
	private javax.swing.JMenuItem jMenuItem3;
	private javax.swing.JMenuItem jMenuItem4;
	private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
	private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTable jTable;
	private javax.swing.JPanel mainPanel;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JProgressBar progressBar;
	private javax.swing.JLabel statusAnimationLabel;
	private javax.swing.JLabel statusMessageLabel;
	private javax.swing.JPanel statusPanel;
	// End of variables declaration//GEN-END:variables

	private final Timer messageTimer;
	private final Timer busyIconTimer;
	private final Icon idleIcon;
	private final Icon[] busyIcons = new Icon[15];
	private int busyIconIndex = 0;

	private JDialog aboutBox;
	private JSlider scalingSlider;
	private JMenu fontMenu;
}
