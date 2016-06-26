package at.tugraz.iicm.matrixexplorer.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Writer for the matrix data format. The data format is described in detail in the project report.
 * @author Christin Seifert
 * @version 10-Jun-2010
 */
public class DataSetWriter {

    /**
     * Writes the given matrix to the given file. 
     * @param matrix the matrix to write
     * @throws IOException 
     */
    public static void writeDoubleMatrix(String file, Matrix matrix,String eCharacter,String delimiter,String encoding) throws IOException {

        FileOutputStream outStream = new FileOutputStream(new File(file));
        BufferedWriter outputBuffer = new BufferedWriter(new OutputStreamWriter(outStream,encoding));
        // write first line, number of rows and number of columsn

        
        outputBuffer.write("RowTitles,");//empty column name at the beginning;

         for (int i = 0; i < matrix.getColnames().size(); i++) {
            outputBuffer.write(matrix.getColnames().get(i));
            if (i != matrix.getColnames().size() - 1) {
                outputBuffer.write(delimiter);
            }
        }
         outputBuffer.newLine();

        // write csv data

        for (int i = 0; i < matrix.getMatrix().rows(); i++) {
            outputBuffer.write(matrix.getRowNames().get(i));
            outputBuffer.write(delimiter);
            for (int j = 0; j < matrix.getMatrix().columns(); j++) {
                outputBuffer.write("" + matrix.getMatrix().get(i, j));
                if (j != matrix.getMatrix().columns() - 1) {
                    outputBuffer.write(delimiter);
                }
            }
            if (i != matrix.getMatrix().rows() - 1) {
                outputBuffer.newLine();
            }
        }
        outputBuffer.close();
        outStream.close();
    }
}
