package at.tugraz.iicm.matrixexplorer.data;

import cern.colt.Arrays;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    public static Matrix readDoubleMatrix (File file,String eCharacter,String delimiter,String encoding) throws Exception {
        Vector<String> rows = new Vector<String>();
        DoubleMatrix2D matrix = null;
        Vector<String> cols;
            FileInputStream dataStream = new FileInputStream(file);
            BufferedReader dataBuffer = new BufferedReader(new InputStreamReader(dataStream,encoding));

            // read rows
            String[] tokens;
            String line;
            line=dataBuffer.readLine();
            if(line!=null)
            {
		            tokens = line.split(delimiter);
		            cols =new  Vector<String>(tokens.length-1);
		                for (int i = 1; i< tokens.length; i++) 
		                {
		                    cols.add(tokens[i]);
		                }
		                
		 
		            // read data
		                ArrayList<String[]> data_array = new ArrayList<String[]>();
		
		            while((line=dataBuffer.readLine())!=null)
		            {
		            	
		                tokens = line.split(delimiter);
		                if(tokens.length-1>0)
		                {
		                String[] temp=new String[tokens.length-1]; 
		                rows.add(tokens[0]);
		
		                for (int i = 1; i< tokens.length; i++) 
		                {
		                	temp[i-1]=tokens[i];
		                }
		                data_array.add(temp);                    
		                }
		            }                
		            int nRow=data_array.size();
		            int nCol=data_array.get(0).length;
		            double[][] data = new double[nRow][nCol];
		            // read data
		            for (int i = 0; i<nRow ; i++) 
		            {
		                for (int j = 0; j< nCol; j++) 
		                {
		                         data[i][j] = Double.parseDouble(data_array.get(i)[j]);
		                }
		            }
		       
		            matrix = new DenseDoubleMatrix2D(data);
		            dataStream.close();
		            Matrix m = new Matrix(matrix, rows, cols);
		
		        return m;
		               
            }
    dataStream.close();        
    return null;

    }
}
