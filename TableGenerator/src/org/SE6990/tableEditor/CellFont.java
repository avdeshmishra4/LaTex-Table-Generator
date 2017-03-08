package org.SE6990.tableEditor;
import java.awt.Font;

interface CellFont {
    
    public Font getFont(int row, int column);
    public void setFont(Font font, int row, int column);
    public void setFont(Font font, int[] rows, int[] columns);


}