package at.tugraz.iicm.matrixexplorer.ui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Christin Seifert
 */
public class MatrixTableHeaderRenderer implements TableCellRenderer{

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
         JLabel label;

        label = new JLabel(""+value);
        label.setToolTipText(""+value);
        label.setBackground(Color.lightGray);
        label.setOpaque(true);
        return label;
    }

}
