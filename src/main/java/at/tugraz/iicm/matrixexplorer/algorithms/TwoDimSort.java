package at.tugraz.iicm.matrixexplorer.algorithms;

import at.tugraz.iicm.matrixexplorer.data.Matrix;
import cern.colt.matrix.DoubleMatrix2D;
import java.util.Vector;

/**
 * Implementation of matrix reordering as described in
 *  Erkki Maekinen and Harri Siirtola. Reordering the reorderable matrix as an algorithmic problem.
 *  In Diagrams ’00: Proceedings of the First International Conference on Theory and Application of Diagrams,
 *  pages 453–467, London, UK, 2000. Springer-Verlag.
 *
 * Note: performence could be improved by using DoubleMatrix2D native functions (Double2DProcedures)
 *
 * @author Huang Weinan, Christin Seifert
 * @version 10-Jun-2010
 */
public class TwoDimSort implements ReorderingAlgorithm {

    //@Override
    public Matrix getSorted(Matrix matrix)
    {
        Matrix Ma = copyMatrix(matrix);
        Matrix MaTemp = copyMatrix(matrix);


        boolean finish = false;
        int runtimes = 0;
        while (!finish)
        {
            runtimes++;
            sortbyRow(Ma);
            sortbyCol(Ma);
            
            if (equalsMatrix(Ma, MaTemp))
            {
                finish = true;
            } else
            {
                MaTemp = copyMatrix(Ma);
            }
            
            if (runtimes == 500)
            {
                finish = true;
            }
        }
        return Ma;
    }


    private float[] calculateColWeights(Matrix Ma)
    {
        DoubleMatrix2D WeightMatrix;
        Vector<String> rowNames;
        Vector<String> colNames;

        // get a copy of Ma, I do not want to change its contents.
        Matrix Mat = copyMatrix(Ma);

        WeightMatrix = Mat.getMatrix();
        rowNames = Mat.getRowNames();
        colNames = Mat.getColnames();

        int rowSize = rowNames.size();
        int colSize = colNames.size();

        // Calculate the weight for colume
        for (int i = 0; i < rowSize; i++)
        {
            float sumRow = 0;
            for (int j = 0; j < colSize; j++)
            {
                sumRow += WeightMatrix.get(i, j);
            }
            for (int j = 0; j < colSize; j++)
            {
                WeightMatrix.set(i, j, WeightMatrix.get(i, j) / sumRow);

            }
        }

        float[] colWights = new float[colSize];

        for (int j = 0; j < colSize; j++)
        {
            colWights[j] = 0;
        }

        for (int j = 0; j < colSize; j++)
        {
            for (int i = 0; i < rowSize; i++)
            {
                colWights[j] += WeightMatrix.get(i, j);
            }
        }

        return colWights;
    }

    private float[] calculateRowWeights(Matrix Ma)
    {
        DoubleMatrix2D WeightMatrix;
        Vector<String> rowNames;
        Vector<String> colNames;

        Matrix Mat = copyMatrix(Ma);

        WeightMatrix = Mat.getMatrix();
        rowNames = Mat.getRowNames();
        colNames = Mat.getColnames();

        int rowSize = rowNames.size();
        int colSize = colNames.size();

        float[] rowWights = new float[rowSize];

        for (int i = 0; i < rowSize; i++)
        {
            rowWights[i] = 0f;
        }

        // Calculate the weight for row
        for (int i = 0; i < colSize; i++)
        {
            float sumCol = 0;
            for (int j = 0; j < rowSize; j++)
            {
                sumCol += WeightMatrix.get(j, i);
            }
            for (int j = 0; j < rowSize; j++)
            {
                WeightMatrix.set(j, i, WeightMatrix.get(j, i) / sumCol);
            }
        }

        for (int i = 0; i < rowSize; i++)
        {
            for (int j = 0; j < colSize; j++)
            {
                rowWights[i] += WeightMatrix.get(i, j);
            }
        }
        return rowWights;
    }

    private void exchangeRows(Matrix Ma, int i, int j)
    {
        DoubleMatrix2D dataMatrix, temp_dataMatrix;
        Vector<String> rowNames;
        Vector<String> colNames;


        dataMatrix = Ma.getMatrix();
        rowNames = Ma.getRowNames();
        colNames = Ma.getColnames();
        temp_dataMatrix = dataMatrix.copy();

        int rowSize = rowNames.size();
        int colSize = colNames.size();

        for (int m = 0; m < rowSize; m++)
        {
            if (m == i)
            {
                for (int n = 0; n < colSize; n++)
                {
                    dataMatrix.set(m, n, dataMatrix.get(j, n));
                }
            } else if (m == j)
            {
                for (int n = 0; n < colSize; n++)
                {
                    dataMatrix.set(m, n, temp_dataMatrix.get(i, n));
                }
            }
        }

        String temp_rowNames = rowNames.get(i);
        rowNames.set(i, rowNames.get(j));
        rowNames.set(j, temp_rowNames);
    }

    private void exchangeCols(Matrix Ma, int i, int j)
    {
        DoubleMatrix2D dataMatrix, temp_dataMatrix;
        Vector<String> rowNames;
        Vector<String> colNames;

        dataMatrix = Ma.getMatrix();
        rowNames = Ma.getRowNames();
        colNames = Ma.getColnames();

        int rowSize = rowNames.size();
        int colSize = colNames.size();

        temp_dataMatrix = dataMatrix.copy();

        for (int m = 0; m < colSize; m++)
        {
            if (m == i)
            {
                for (int n = 0; n < rowSize; n++)
                {
                    dataMatrix.set(n, m, dataMatrix.get(n, j));
                }
            } else if (m == j)
            {
                for (int n = 0; n < rowSize; n++)
                {
                    dataMatrix.set(n, m, temp_dataMatrix.get(n, i));
                }
            }
        }
        String temp_colNames = colNames.get(i);
        colNames.set(i, colNames.get(j));
        colNames.set(j, temp_colNames);
    }

    private void sortbyRow(Matrix Mat)
    {
        Vector<String> rowNames = Mat.getRowNames();
        float[] rowWeights = calculateRowWeights(Mat);
        int rowSize = rowNames.size();

        boolean finish = false;

        while (!finish)
        {
            boolean flag = false;
            for (int i = 0; i < rowSize - 1; i++)
            {
                if (rowWeights[i] > rowWeights[i + 1])
                {
                    float temp = rowWeights[i];
                    rowWeights[i] = rowWeights[i + 1];
                    rowWeights[i + 1] = temp;
                    exchangeRows(Mat, i, i + 1);
                    flag = true;
                }
            }
            if (flag == false)
            {
                finish = true;
            }
        }
    }

    private void sortbyCol(Matrix Mat)
    {
        Vector<String> colNames = Mat.getColnames();

        float[] colWeights = calculateColWeights(Mat);
        int colSize = colNames.size();

        boolean finish = false;

        while (!finish)
        {
            boolean flag = false;
            for (int i = 0; i < colSize - 1; i++)
            {
                if (colWeights[i] > colWeights[i + 1])
                {
                    float temp = colWeights[i];
                    colWeights[i] = colWeights[i + 1];
                    colWeights[i + 1] = temp;
                    exchangeCols(Mat, i, i + 1);
                    flag = true;
                }
            }
            if (flag == false)
            {
                finish = true;
            }
        }
    }

    private static Matrix copyMatrix(Matrix Mat)
    {
        DoubleMatrix2D dataMatrix;
        Vector<String> rowNames;
        Vector<String> colNames;

        dataMatrix = Mat.getMatrix().copy();
        rowNames = (Vector<String>) Mat.getRowNames().clone();
        colNames = (Vector<String>) Mat.getColnames().clone();

        return new Matrix(dataMatrix, rowNames, colNames);

    }

    private boolean equalsMatrix(Matrix Mat1, Matrix Mat2)
    {
        Vector<String> rows1 = Mat1.getRowNames();
        Vector<String> rows2 = Mat2.getRowNames();

        Vector<String> cols1 = Mat1.getColnames();
        Vector<String> cols2 = Mat2.getColnames();

        if (!cols1.equals(cols2))
        {
            return false;
        } else if (!rows1.equals(rows2))
        {
            return false;
        } else
        {
            return true;
        }
    }

}
