/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.iicm.matrixexplorer.ui;

import java.awt.Component;
import java.net.URL;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;






/**
 *
 * @author Huang
 */


public class BertinCellRenderer implements TableCellRenderer {

    //@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        // TODO catch double conversion exception
        final Double  dValue = (Double)value;
        // find matrix maximum value in order to scale values
        final Double maxValue = ((MatrixTableModel) table.getModel()).getMatrix().getMaxValue(row);
        // find matrix minimum value in order to scale values
        final Double minValue = ((MatrixTableModel) table.getModel()).getMatrix().getMinValue(row);

        JLabel label=new JLabel("");
        
        String element="Element_"+String.valueOf(row)+"_"+String.valueOf(column-1)+".jpg";
        //label.setOpaque(true);
        URL imgURL=getClass().getResource("images/"+element);
        if (imgURL!=null){
            ImageIcon icon=new ImageIcon(imgURL);
            label.setIcon(icon);
            
        }else{
            System.err.println("FML!the file is not opened correctly!!!");
            System.err.println(element);
        }
        label.setHorizontalAlignment(label.CENTER);

        //table.setAlignmentX(table.CENTER_ALIGNMENT);
        
        return label;

    }


}

