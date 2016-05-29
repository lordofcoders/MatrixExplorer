package at.tugraz.iicm.matrixexplorer.algorithms;

import at.tugraz.iicm.matrixexplorer.data.Matrix;

/**
 * Interface for the Reodering Algorihtm, an algorithm which takes a matrix as input and returns the reordered matrix.
 * The way of reordering is implementation detail.
 * @author Christin Seifert
 * @version 10-Jun-2010
 */
public interface ReorderingAlgorithm {

    /**
     * Reorderes a reordered matrix matrix.
     * @param matrix a matrix to reorder
     * @return the reordered matrix
     */
    public abstract Matrix getSorted(Matrix matrix);

}
