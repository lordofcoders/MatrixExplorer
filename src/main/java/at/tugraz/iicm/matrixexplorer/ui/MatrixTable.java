package at.tugraz.iicm.matrixexplorer.ui;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

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
	private MatrixTableHeaderRenderer matrixRenderer = null;
	private int headerInitialFontSize = 15;
	private float headerFontScalingFactor = 1.0f;
	
	
	public MatrixTable() {
		this.matrixRenderer = new MatrixTableHeaderRenderer(headerInitialFontSize, headerFontScalingFactor);
		this.tableHeader.setDefaultRenderer(matrixRenderer);
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
			return new MatrixTableHeaderRenderer(headerInitialFontSize, headerFontScalingFactor);
		} else {
			switch (renderMode) {
			case RENDER_AS_CIRCLES:
				return new CircleCellRenderer();
			case RENDER_AS_BARS:
				return new BarCellRenderer();
			case RENDER_AS_BERTINS_VISUALS:
				return new BertinCellRenderer();
			default:
				return new DefaultTableCellRenderer();
			}

		}

	}

	private void setColumnWidths() {
		TableColumnModel model = getColumnModel();
		if (model != null && model.getColumnCount() > 0) {
			TableColumn column = getColumnModel().getColumn(0);
			if (column != null) {
				column.setMinWidth(170);
				column.setPreferredWidth(170);
			}
		}
	}

	@Override
	public void doLayout() {
		super.doLayout();
		setColumnWidths();
	}
	
	public void setMatrixTableHeaderFontSize(int initialFontSize, float scalingFactor) {
		this.headerInitialFontSize = initialFontSize;
		this.headerFontScalingFactor = scalingFactor;
	}
}
