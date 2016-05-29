

package at.tugraz.iicm.matrixexplorer.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Implements rendering of matrix table as bars.
 * @author Christin Seifert
 *
 * @author Christin Seifert
 * @version 02-Jun-2010
 */
public class BarCellRenderer implements TableCellRenderer {

       public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        // TODO catch double conversion exception
        final Double  dValue = (Double)value;
        // find matrix maximum value in order to scale values
        final Double maxValue = ((MatrixTableModel) table.getModel()).getMatrix().getMaxValue(row);
        // find matrix minimum value in order to scale values
        final Double minValue = ((MatrixTableModel) table.getModel()).getMatrix().getMinValue(row);

        JLabel  label = new JLabel(""){
            @Override
            public void paint(Graphics grphcs)
            {
                super.paint(grphcs);
                Graphics2D g2d = (Graphics2D) grphcs;
                g2d.setRenderingHint(
	            RenderingHints.KEY_ANTIALIASING,
	            RenderingHints.VALUE_ANTIALIAS_ON);
	        g2d.setColor(Color.blue);
                int maxHeight = this.getHeight()-2;
                int size = (int) Math.round((maxHeight/(maxValue-minValue))*(dValue-minValue));
                g2d.fillRect(2,this.getHeight()-size,this.getWidth()-4,size);
            }

        };
        label.setOpaque(true);
        return label;
    }

}
