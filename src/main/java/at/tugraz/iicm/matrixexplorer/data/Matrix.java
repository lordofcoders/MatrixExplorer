package at.tugraz.iicm.matrixexplorer.data;

import cern.colt.matrix.DoubleMatrix2D;
import java.util.Vector;

/**
 * Basic Data Structure
 * @author Christin Seifert
 */
public class Matrix {
    /** The data **/
    private DoubleMatrix2D matrix;
    /** The names of rows */
    private Vector<String> rowNames;
    /** The names of columns */
    private Vector<String> colNames;


    /**
     * Construction of a Reorderable matrix, given the values, the names of rows and the names
     * of the columns.
     * @param matrix the matrix values
     * @param rowNames the names of the rows
     * @param colnames the names of the columns
     */
    public Matrix(DoubleMatrix2D matrix, Vector<String> rowNames, Vector<String> colnames) {
        this.matrix = matrix;
        this.rowNames = rowNames;
        this.colNames = colnames;
    }

    /**
     * Returns the matrix values.
     * @return the matrix values
     */
    public DoubleMatrix2D getMatrix() {
        return matrix;
    }
    /**
     * Returns the column names.
     * @return the column names
     */
    public Vector<String> getColnames() {
        return colNames;
    }

    /**
     * Returns the row names.
     * @return the row names
     */
    public Vector<String> getRowNames() {
        return rowNames;
    }


    /**
     * Returns the maximum value in the matrix. 0 if the matrix is not initialized yet.
     * @return the maximum value
     */
    public double getMaxValue(){
      return getMaxValue(-1);
    }

    /**
     * Returns the minimum value in the matrix. 0 if the matrix is not initialized yet.
     * @return the minimum value
     */
    public double getMinValue() {
        return getMinValue(-1);
    }

    /**
     * Returns the maximum value in the given row of the matrix. If  row < 0 then the max value for
     * all rows is returned. Returns <code>0</code>  if the matrix is not initialized.
     * @param row the row of the matrix
     * @return the maximum value
     */
    public double getMaxValue(int row)
    {
        if (matrix == null) {
                return 0;
        }
        double max = Double.MIN_VALUE;
        if (row < 0) {
            for (int i = 0; i < matrix.rows(); i++)
            {
                for (int j = 0; j < matrix.columns(); j++)
                {
                    if (Double.compare(matrix.get(i, j), max) > 0)
                    {
                        max = matrix.get(i, j);
                    }
                }
            }
        } else {
            int i = row;
            for (int j = 0; j < matrix.columns(); j++)
            {
                if (Double.compare(matrix.get(i, j), max) > 0)
                {
                    max = matrix.get(i, j);
                }
            }
        }
        return max;
    }

    /**
     * Returns the minimum value in the given row of the matrix. If  row < 0 then the min value for
     * all rows is returned. Returns <code>0</code>  if the matrix is not initialized.
     * @param row the row of the matrix
     * @return the minimum  value
     */
    public double getMinValue(int row)
    {
        if (matrix == null) {
                return 0;
        }
        double min = Double.MAX_VALUE;
        if (row < 0) {
            for (int i = 0; i < matrix.rows(); i++)
            {
                for (int j = 0; j < matrix.columns(); j++)
                {
                    if (Double.compare(matrix.get(i, j), min) < 0)
                    {
                        min = matrix.get(i, j);
                    }
                }
            }
        } else {
            int i = row;
            for (int j = 0; j < matrix.columns(); j++)
            {
                if (Double.compare(matrix.get(i, j), min) < 0)
                {
                    min = matrix.get(i, j);
                }
            }
        }
        return min;
    }
}
