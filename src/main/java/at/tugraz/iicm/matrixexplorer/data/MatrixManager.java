package at.tugraz.iicm.matrixexplorer.data;

/**
 * Simple holder class to keep track of changes of the orginal matrix.
 * TODO:
 * * reorderedd matrix could be implemented with just 2 index arrays which holds the changes
 * compared to the original matrix.
 * * reordering history could then easily be implemented by mainintaining a list of these index arrays
 * @author Christin Seifert
 * @version 10-Jun-2010
 */
public class MatrixManager {

    private Matrix originalMatrix;
    private Matrix matrix;

    /**
     * Construction,
     * Initializes the original and the current matrix with the given matrix.
     * @param matrix initial matrix
     */
    public MatrixManager(Matrix  matrix) {
        this.originalMatrix = matrix;
        this.matrix = matrix;
    }

    /**
     * Sets the current matrix.
     * @param matrix the matrix
     */
    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }
    /**
     * Returns the matrix.
     * @return the matrix
     */
    public Matrix getMatrix() {
        return matrix;
    }
    /**
     * Resets the matrix to the originally loaded version.
     */
    public void resetMatrix()
    {
       this.matrix = originalMatrix;
    }

}
