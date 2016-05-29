package at.tugraz.iicm.matrixexplorer.ui;


import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.plaf.basic.BasicTableUI.MouseInputHandler;
import javax.swing.table.TableModel;
import at.tugraz.iicm.matrixexplorer.data.Matrix;
import java.util.Vector;


/**
 *
 * @author Huang
 */
public class DragDropRowTableUI extends BasicTableUI {
    private boolean draggingRow = false;
    private int startDragPoint;
    private int dyOffset;

    @Override
    protected MouseInputListener createMouseInputListener() {
        return (MouseInputListener) new DragDropRowMouseInputHandler();
    }

    @Override
    public void paint(Graphics g, JComponent c) {
    	super.paint(g, c);

    	if (draggingRow) {
            g.setColor(table.getParent().getBackground());
            Rectangle cellRect = table.getCellRect(table.getSelectedRow(), 0, false);
            g.copyArea(cellRect.x, cellRect.y, table.getWidth(), table.getRowHeight(), cellRect.x, dyOffset);
            if (dyOffset < 0) {
                g.fillRect(cellRect.x, cellRect.y + (table.getRowHeight() + dyOffset), table.getWidth(), (dyOffset * -1));
            } else {
                g.fillRect(cellRect.x, cellRect.y, table.getWidth(), dyOffset);
            }
    		}
    	}
    class DragDropRowMouseInputHandler extends MouseInputHandler {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            startDragPoint = (int)e.getPoint().getY();
        }
        @Override
        public void mouseDragged(MouseEvent e) {
            int fromRow = table.getSelectedRow();
            if (fromRow >= 0) {
                draggingRow = true;
                int rowHeight = table.getRowHeight();
                int middleOfSelectedRow = (rowHeight * fromRow) + (rowHeight / 2);
                int toRow = -1;
                int yMousePoint = (int)e.getPoint().getY();
                if (yMousePoint < (middleOfSelectedRow - rowHeight)) {
                    // Move row up
                    toRow = fromRow - 1;
                } else if (yMousePoint > (middleOfSelectedRow + rowHeight)) {
                    // Move row down
                    toRow = fromRow + 1;
                }

                if (toRow >= 0 && toRow < table.getRowCount()) {
                    TableModel model = (TableModel)table.getModel();
                    Matrix matrix=((MatrixTableModel)table.getModel()).getMatrix();

                    Vector<String> rowName=((MatrixTableModel)table.getModel()).getMatrix().getRowNames();

                    String temp_rowName=rowName.get(toRow);
                    rowName.set(toRow, rowName.get(fromRow));
                    rowName.set(fromRow,temp_rowName);

                    for (int i = 0; i < model.getColumnCount(); i++) {
                        Object fromValue = model.getValueAt(fromRow, i);
                        Object toValue = model.getValueAt(toRow, i);

                        double fromValue_matrix=matrix.getMatrix().get(fromRow, i);
                        double toValue_matrix=matrix.getMatrix().get(toRow, i);

                        
                        model.setValueAt(toValue, fromRow, i);

                        matrix.getMatrix().set(fromRow, i, toValue_matrix);

                        model.setValueAt(fromValue, toRow, i);

                        matrix.getMatrix().set(toRow, i, fromValue_matrix);
                    }
                    table.setRowSelectionInterval(toRow, toRow);
                    startDragPoint = yMousePoint;
                }
                dyOffset = (startDragPoint - yMousePoint) * -1;
                table.repaint();
        	}
        }
        @Override
        public void mouseReleased(MouseEvent e){
            super.mouseReleased(e);
            draggingRow = false;
            table.repaint();
        }
    }
}
