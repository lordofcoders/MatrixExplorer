package at.tugraz.iicm.matrixexplorer;

import at.tugraz.iicm.matrixexplorer.algorithms.ReorderingAlgorithm;
import at.tugraz.iicm.matrixexplorer.algorithms.TwoDimSort;
import at.tugraz.iicm.matrixexplorer.ui.MatrixTable;
import at.tugraz.iicm.matrixexplorer.data.DataSetReader;
import at.tugraz.iicm.matrixexplorer.data.DataSetWriter;
import at.tugraz.iicm.matrixexplorer.data.Matrix;
import at.tugraz.iicm.matrixexplorer.data.MatrixManager;
import at.tugraz.iicm.matrixexplorer.ui.MatrixTableModel;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import at.tugraz.iicm.matrixexplorer.ui.BertinVisualsGenerator;
import at.tugraz.iicm.matrixexplorer.ui.DragDropRowTableUI;

/**
 * The application's main frame.
 * @author  Christin Seifert
 */
public class MatrixExplorerView extends FrameView {

    private MatrixManager matrixManager;
    private final static Logger logger = Logger.getLogger(MatrixExplorerView.class.getSimpleName());

    /**
     * Construct the frame for the application.
     * @param app the application
     */
    public MatrixExplorerView(SingleFrameApplication app) {
        super(app);

        initComponents();
        ((MatrixTable) jTable).setRenderMode(MatrixTable.RENDER_AS_CIRCLES);
        
        jTable.updateUI();

        
     
        // status bar initialization - message timeout, idle icon and busy animation, etc
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
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });

        // set the row DnD works.
        jTable.setUI(new DragDropRowTableUI());
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new MatrixTable();
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
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(at.tugraz.iicm.matrixexplorer.MatrixExplorerApp.class).getContext().getActionMap(MatrixExplorerView.class, this);
        fileMenu.setAction(actionMap.get("onSaveButton")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(at.tugraz.iicm.matrixexplorer.MatrixExplorerApp.class).getContext().getResourceMap(MatrixExplorerView.class);
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

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 376, Short.MAX_VALUE)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap(228, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * Action for the load button. Opens a file dialog and loads the given file. Informs the user if the file
     * is not in correct format.
     */
    @Action
    public void onLoadButton() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Select Data File");
        chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();
        if (file != null)
        {
            String f = file.getAbsolutePath();
            try
            {

                Matrix matrix = DataSetReader.readDoubleMatrix(file);
                matrixManager = new MatrixManager(matrix);
                ((MatrixTableModel) jTable.getModel()).setMatrix(matrixManager.getMatrix());
                ((MatrixTableModel) jTable.getModel()).fireTableStructureChanged();
                BertinVisualsGenerator ber = new BertinVisualsGenerator();
                ber.drawGraphics(matrix);
                //jTable.setUI(new DragDropRowTableUI());
            } catch (Exception ex)
            {
                JOptionPane.showMessageDialog(mainPanel, "Could not read input data from file " + f, "Error in reading input data", JOptionPane.ERROR_MESSAGE);
                logger.log(Level.SEVERE, "error in reading input data from file " + f);
            }
        }
    }

    /**
     * Action for the save button. Opens a file dialog and saves the matrix to the given file. Informs the user
     * if the file could not be saved.
     */
    @Action
    public void onSaveButton() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Select File Location");
        chooser.showSaveDialog(null);
        File file = chooser.getSelectedFile();
        if (file != null) {
            try
            {
                DataSetWriter.writeDoubleMatrix(file.getCanonicalPath(), matrixManager.getMatrix());
            } catch (Exception e)
            {
                JOptionPane.showMessageDialog(mainPanel, "Could not save matrix", "Error in saving", JOptionPane.ERROR_MESSAGE);
                logger.log(Level.SEVERE, "error in saving matrix to  file");
            }
        }
    }



    /**
     * Action for the display data as raw button. Shows the matrix data as numbers.
     */
    @Action
    public void onDisplayDataAsRaw() {
        ((MatrixTable) jTable).setRenderMode(MatrixTable.RENDER_AS_NUMBERS);
        jTable.updateUI();
     }

    /**
     * Action for the display data as raw button. Shows the matrix data as circles.
     */
    @Action
    public void onDisplayDataAsCircles() {
        ((MatrixTable) jTable).setRenderMode(MatrixTable.RENDER_AS_CIRCLES);
        jTable.updateUI();
    }

    /**
     * Action for the display data as raw button. Shows the matrix data as bars.
     */
    @Action
    public void onDisplayDataAsBars() {
        ((MatrixTable) jTable).setRenderMode(MatrixTable.RENDER_AS_BARS);
        jTable.updateUI();
      }
    
    /**
     * Action for the display data as raw button. Shows the matrix data as Bertin's visuals.
     */
    @Action
    public void onDisplayAsBertinsVisuals()
    {
        ((MatrixTable) jTable).setRenderMode(MatrixTable.RENDER_AS_BERTINS_VISUALS);
        jTable.updateUI();
    }


    /**
     * Action for the 2D sorting button. Sorts the matrix automatically.
     */
    @Action
    public void on2DSort() {
            getApplication().getContext().getTaskService().execute(new Task(getApplication()) {
                @Override
                protected Object doInBackground() throws Exception {
                    //setProgress(0, 0, 0);
                    ReorderingAlgorithm sorter = new TwoDimSort();
                    Matrix newMatrix = sorter.getSorted( ((MatrixTableModel)jTable.getModel()).getMatrix());
                    setMessage("Sorting matrix (2D Sort)...");
                    return newMatrix;
                }

            @Override
            protected void succeeded(Object result)
            {
                super.succeeded(result);
                matrixManager.setMatrix((Matrix)result);
                ((MatrixTableModel)jTable.getModel()).setMatrix(matrixManager.getMatrix());
                ((MatrixTableModel)jTable.getModel()).fireTableStructureChanged();
            }

            });

    }

    /**
     * Resets the matrix to the loaded version.
     */
    @Action
    public void resetMatrix() {
        matrixManager.resetMatrix();
       ((MatrixTableModel)jTable.getModel()).setMatrix(matrixManager.getMatrix());
       ((MatrixTableModel)jTable.getModel()).fireTableStructureChanged();
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
}