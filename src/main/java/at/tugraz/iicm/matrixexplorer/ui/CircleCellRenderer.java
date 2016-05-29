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
 * Implements rendering of matrix table as circles.
 * @author Christin Seifert
 */
public class CircleCellRenderer implements TableCellRenderer {

   // @Override
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
                int centerX = this.getWidth()/2;
                int centerY = this.getHeight()/2;
                int maxHeight = (this.getWidth() < this.getHeight())?this.getWidth()-2:this.getHeight()-2;
                int radius = (int) Math.round(((maxHeight/(maxValue-minValue))*(dValue-minValue))/2);
                g2d.fillOval(centerX - radius, centerY - radius, 2*radius, 2*radius);
            }
            
        };
        label.setOpaque(true);

        return label;
                                
    }


}
