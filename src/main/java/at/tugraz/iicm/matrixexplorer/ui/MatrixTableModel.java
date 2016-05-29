package at.tugraz.iicm.matrixexplorer.ui;

import at.tugraz.iicm.matrixexplorer.data.Matrix;
import javax.swing.table.AbstractTableModel;


/**
 * The table model for the reorderable matrix, consisting of row and column names
 * and the matrix data.
 * @author Christin Seifert
 */
public class MatrixTableModel extends AbstractTableModel {
    Matrix matrix;

    public Matrix getMatrix()
    {
        return matrix;
    }


    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }
    
    public int getRowCount() {
        if (matrix != null) {
            return matrix.getMatrix().rows();
        }
        else return 0;
       }

    public int getColumnCount() {
        if (matrix != null) {
            return matrix.getMatrix().columns()+1;
        }
        else
        return 0;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return matrix.getRowNames().get(rowIndex);
        }
        else {
            if (matrix != null) {
                return matrix.getMatrix().get(rowIndex, columnIndex-1);
            }
            else
                return "?";
        }
    }

    @Override
    public String getColumnName(int col)
    {
            if (col == 0) {
                return "";
            }
            else {
                 return matrix.getColnames().get(col-1);
            
            }
        }
}
