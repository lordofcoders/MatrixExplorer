package at.tugraz.iicm.matrixexplorer.ui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import at.tugraz.iicm.matrixexplorer.MatrixExplorerView;

public class MainComponentListener extends ComponentAdapter {
	
	private MatrixExplorerView view = null;
	
	public MainComponentListener(MatrixExplorerView view) {
		this.view = view;
	}
	
	public void componentResized(ComponentEvent e) {
		view.fitTableToViewportSize();
	}
}
