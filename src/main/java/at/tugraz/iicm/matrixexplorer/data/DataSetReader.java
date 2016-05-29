package at.tugraz.iicm.matrixexplorer.data;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Reader for the matrix data format. The data format is described in detail in the project report.
 * @author Christin Seifert
 */
public class DataSetReader {
 


    /**
     * Reads a matrix from the given files.
     * @return the matrix
     * @throws Exception 
     */
    public static Matrix readDoubleMatrix (File file) throws Exception {
        Vector<String> cols = new Vector<String>();
        Vector<String> rows = new Vector<String>();
        DoubleMatrix2D matrix = null;
     
            FileInputStream dataStream = new FileInputStream(file);
            BufferedReader dataBuffer = new BufferedReader(new InputStreamReader(dataStream));

            // read first line, number of rows and columns
            String[] firstLine = dataBuffer.readLine().split(" ");
            int r = Integer.parseInt(firstLine[0]);
            int c = Integer.parseInt(firstLine[1]);

            // read rows
            String line = dataBuffer.readLine();
            if (!line.equalsIgnoreCase(FileFormat.ROW_HEADER)) {
                throw new IOException("error reading input file, expected " + FileFormat.ROW_HEADER + " got " + line);
            }
            for (int i = 0; i< r; i++) {
                line = dataBuffer.readLine();
                rows.add(line);
            }
            // read columns
            line = dataBuffer.readLine();
            if (!line.equalsIgnoreCase(FileFormat.COL_HEADER)) {
                throw new IOException("error reading input file, expected " + FileFormat.COL_HEADER + " got " + line);
            }
            for (int i = 0; i< c; i++) {
                line = dataBuffer.readLine();
                cols.add(line);
            }
            // read data
            line = dataBuffer.readLine();
            if (!line.equalsIgnoreCase(FileFormat.DATA_HEADER)) {
                throw new IOException("error reading input file, expected " + FileFormat.DATA_HEADER + " got " + line);
            }

            double[][] data = new double[rows.size()][cols.size()];
            // read data
            int row = 0;
            line = dataBuffer.readLine();
            while(line!=null) {
                 String[] split = line.split(",");
                   for (int csvCol = 0; csvCol < split.length; csvCol++)
                    {
                         String s = split[csvCol].trim();
                         data[row][csvCol] = Double.parseDouble(s);
                   }
                 row++;
                  line = dataBuffer.readLine();
            }
            matrix = new DenseDoubleMatrix2D(data);
            dataStream.close();

            Matrix m = new Matrix(matrix, rows, cols);

        return m;
    }

}
