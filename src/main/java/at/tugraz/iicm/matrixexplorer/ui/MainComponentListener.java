package at.tugraz.iicm.matrixexplorer.ui;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import at.tugraz.iicm.matrixexplorer.MatrixExplorerView;

public class MainComponentListener extends ComponentAdapter {

	private MatrixTable table = null;

	public MainComponentListener(MatrixTable table) {
		this.table = table;
	}

	public void componentResized(ComponentEvent e) {
		
	}
}
