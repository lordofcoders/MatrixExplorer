package at.tugraz.iicm.matrixexplorer.ui;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.TableView.TableRow;

/**
 * A JTable with custom cell renderers for showing different visual
 * representations of the table data.
 * 
 * @author Christin Seifert
 */
public class MatrixTable extends JTable {

	/**
	 * Render mode: show data as circles of different size.
	 */
	public static final int RENDER_AS_CIRCLES = 1;
	/**
	 * Render mode: show data as numbers.
	 * 
	 */
	public static final int RENDER_AS_NUMBERS = 2;
	/**
	 * Render mode: show data as bars of different size.
	 */
	public static final int RENDER_AS_BARS = 3;
	/**
	 * Render mode: show data as Bertin's visuals.
	 */
	public static final int RENDER_AS_BERTINS_VISUALS = 4;
	private int renderMode;

	private JScrollPane scrollPane = null;

	// shared renderers, define how the affected cells are rendered
	private MatrixTableHeaderRenderer matrixHeaderRenderer = null;
	private MatrixTableFirstColumnRenderer firstColumnRenderer = null;
	private CircleCellRenderer circleRenderer = null;
	private BarCellRenderer barRenderer = null;
	private BertinCellRenderer bertinRenderer = null;

	// scaling variables
	private int headerInitialFontSize = 15;
	private int initialColWidth = 0;
	private float headerFontScalingFactor = 1.0f;
	private int minRowHeight = 20;
	private int currentTableWidth = 0;

	public MatrixTable() {

		this.matrixHeaderRenderer = new MatrixTableHeaderRenderer(this.headerInitialFontSize,
				this.headerFontScalingFactor);
		this.firstColumnRenderer = new MatrixTableFirstColumnRenderer(this.headerInitialFontSize,
				this.headerFontScalingFactor);
		this.tableHeader.setDefaultRenderer(this.matrixHeaderRenderer);

		this.circleRenderer = new CircleCellRenderer();
		this.barRenderer = new BarCellRenderer();
		this.bertinRenderer = new BertinCellRenderer();

		TableColumnModel model = getColumnModel();
		int columnCount = model.getColumnCount();

		if (model != null && columnCount > 0) {
			for (int i = 0; i < columnCount; i++) {
				TableColumn column = getColumnModel().getColumn(i);
				column.setHeaderRenderer(this.matrixHeaderRenderer);
			}
		}
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	/**
	 * Sets the render mode.
	 * 
	 * @param renderMode
	 *            the render mode.
	 */
	public void setRenderMode(int renderMode) {
		this.renderMode = renderMode;
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {

		if (column == 0) {
			return this.firstColumnRenderer;
		} else {

			switch (renderMode) {
			case RENDER_AS_CIRCLES:
				return this.circleRenderer;
			case RENDER_AS_BARS:
				return this.barRenderer;
			case RENDER_AS_BERTINS_VISUALS:
				return this.bertinRenderer;
			default:
				return new DefaultTableCellRenderer();
			}
		}
	}

	private void setColumnWidths() {
		
		int currentTableWidthTemp = 0;
		
		TableColumnModel model = getColumnModel();
		int columnCount = model.getColumnCount();

		if (model != null && columnCount > 0) {

			TableColumn column = getColumnModel().getColumn(0);

			if (column != null) {
				column.setMinWidth(170);
				column.setPreferredWidth(170);
				currentTableWidthTemp += 170;
			}

			if (this.initialColWidth == 0) {
				this.initialColWidth = 35;
			}

			for (int i = 1; i < columnCount; i++) {

				column = getColumnModel().getColumn(i);

				column.setMinWidth((int) Math.ceil(this.initialColWidth * this.headerFontScalingFactor));
				column.setPreferredWidth((int) Math.ceil(this.initialColWidth * this.headerFontScalingFactor));
				currentTableWidthTemp += (int) Math.ceil(this.initialColWidth * this.headerFontScalingFactor);
			}
			
			currentTableWidth = currentTableWidthTemp;
		}
	}

	private void setRowHeights() {

		if (scrollPane == null)
			return;

		// calculate ideal row height for current window size
		Rectangle bounds = scrollPane.getBounds();

		double currentHeight = bounds.getHeight();
		int rowCount = this.getModel().getRowCount();
		if (rowCount <= 0) {
			return;
		}

		int rowHeight = ((int) Math.floor(currentHeight) / (rowCount + 1));

		if (rowHeight > minRowHeight) {

			this.setRowHeight(rowHeight);

			// set table header row height
			JTableHeader header = this.getTableHeader();
			Dimension prefSize = header.getPreferredSize();

			int headerHeight = rowHeight;

			prefSize.height = headerHeight;
			header.setPreferredSize(prefSize);

			// set font size to fit row size
			adjustFontSizeToRowHeight(rowHeight);
		}

	}

	private void adjustFontSizeToRowHeight(int rowHeight) {
		this.setFont(this.getFont().deriveFont(rowHeight / 2));
	}

	@Override
	public void doLayout() {

		super.doLayout();

		if (shouldTableHaveHorizontalScrollbar()) {
			this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		} else {
			this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		}

		setMatrixTableHeaderFontSize(this.headerInitialFontSize, this.headerFontScalingFactor);
	}

	public void setMatrixTableHeaderFontSize(int initialFontSize, float scalingFactor) {

		this.headerInitialFontSize = initialFontSize;
		this.headerFontScalingFactor = scalingFactor;

		this.matrixHeaderRenderer.setFontScaling(scalingFactor);
		this.firstColumnRenderer.setFontScaling(scalingFactor);

		setColumnWidths();
		setRowHeights();
	}

	private boolean shouldTableHaveHorizontalScrollbar() {

		int columnCount = this.getColumnCount();

		if (columnCount <= 0 || scrollPane == null)
			return false;

		if (this.currentTableWidth > scrollPane.getViewport().getSize().getWidth()) {
			return true;
		}

		return false;
	}
}
