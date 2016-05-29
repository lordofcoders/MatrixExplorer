package at.tugraz.iicm.matrixexplorer.ui;

import java.io.*;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.*;
import at.tugraz.iicm.matrixexplorer.data.Matrix;
import cern.colt.matrix.DoubleMatrix2D;

/**
 *
 * @author Huang
 */
public class BertinVisualsGenerator {

    static String SVG_Ground_Width="10";
    static String SVG_Ground_Height="20";
    
    private Document createDocument(int row, int col, double ratio){
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        Document document = impl.createDocument(svgNS, "svg", null);
        Element root = document.getDocumentElement();
        root.setAttributeNS(null, "width", SVG_Ground_Width);
        root.setAttributeNS(null, "height", SVG_Ground_Height);
        root.setAttributeNS(null, "fill-opacity", "0.0");

        String height=String.valueOf(ratio*10);
        String ystart=String.valueOf(20-ratio*10);

        // Add some content to the document.
        Element e1 = document.createElementNS(svgNS, "rect");
        e1.setAttributeNS(null, "x", "0");
        e1.setAttributeNS(null, "y", ystart);
        e1.setAttributeNS(null, "width", SVG_Ground_Width);
        e1.setAttributeNS(null, "height", height);
        if (ratio<1){
            e1.setAttributeNS(null, "style", "fill:rgb(0,255,255);stroke:rgb(0,0,255);stroke-width:0;fill-opacity:1.0");
        } else{
            e1.setAttributeNS(null, "style", "fill:rgb(0,0,255);stroke:rgb(0,0,255);stroke-width:0;fill-opacity:1.0");
        }
        
        root.appendChild(e1);

        return document;
    }

    private void save(Document document, int row, int column){
        // Create a JPEGTranscoder and set its quality hint.
            JPEGTranscoder t = new JPEGTranscoder();
            t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));

        // Set the transcoder input and output.
            TranscoderInput input = new TranscoderInput(document);
        
            
            String OutputFileName="images/"+"Element_"+String.valueOf(row)+"_"+String.valueOf(column)+".jpg";
            try {
                OutputStream ostream = new FileOutputStream(OutputFileName.toString());
                TranscoderOutput output = new TranscoderOutput(ostream);
                t.transcode(input, output);
                ostream.flush();
                ostream.close();
            } catch (IOException ex) {
                System.err.println("FML!: file not found");
            } catch (TranscoderException ex) {
                System.err.println("FML!: I don't know what is the fucking problem!!!");
            } catch(Exception ex){
                System.err.println("FML!: file not written!");
            }
            }
        
    

    
    // Perform the transcoding.
      

    public void drawGraphics(Matrix mat){

        DoubleMatrix2D svgMatrix;

        svgMatrix = mat.getMatrix();


        int rowSize = mat.getRowNames().size();
        int colSize = mat.getColnames().size();

        //System.err.println(String.valueOf(rowSize));
        //System.err.println(String.valueOf(colSize));

       
        String strDir="images";
        try{
            (new File(strDir)).mkdir();
        } catch(Exception e){
            System.err.println("Directory error!");
        }


        // Calculate the weight for row
        for (int i = 0; i < rowSize; i++)
        {
            float sumCol = 0;
            for (int j = 0; j < colSize; j++)
            {
                sumCol += svgMatrix.get(i,j);
            }
            for (int j = 0; j < colSize; j++)
            {
                svgMatrix.set(i, j, svgMatrix.get(i,j) / sumCol*colSize);
            }
        }

        for (int i=0;i<rowSize;i++){
            for (int j=0;j<colSize;j++){
                Document doc=createDocument(i,j,svgMatrix.get(i, j));
                save(doc,i,j);
            }
        }
    }
}




