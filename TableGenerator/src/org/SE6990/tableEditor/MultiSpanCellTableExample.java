package org.SE6990.tableEditor;


import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;


/**
 * @version 1.0 11/26/98
 */
public class MultiSpanCellTableExample extends JFrame implements TableModelListener{

  HashMap<String, Object> cellText = new HashMap<String, Object>();
  static String[] str_size = { "tiny", "scriptsize", "footnotesize", "small", "normalsize", "large", "Large", "LARGE", "huge", "Huge" };
  
  
  AttributiveCellTableModel ml;	
  
  DefaultCellAttribute cellAtt;
	
  MultiSpanCellTableExample(int rows, int columns) {
    super( "Multi-Span Cell Example" );
    
       
    // create the table first parameter is rows, second is columns
    // TODO create interface to ask users for row and column number then feed that here.
//    AttributiveCellTableModel ml = new AttributiveCellTableModel(5,6);
    
    ml = new AttributiveCellTableModel(rows,columns);
    
    cellAtt = ml.getCellAttribute();
    final MultiSpanCellTable table = new MultiSpanCellTable( ml );
    JScrollPane scroll = new JScrollPane( table );

        
//    table.getModel().setValueAt("Avdesh", 0, 0);
//    System.out.println(table.getModel().getValueAt(0, 0));
    table.getModel().addTableModelListener(this);
    
    
    // button to combine cells
    // TODO be able to combine more than two cells
    JButton b_one   = new JButton("Combine");
    b_one.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
		  int[] columns = table.getSelectedColumns();
		  int[] rows    = table.getSelectedRows();
		  cellAtt.combine(rows,columns);
		  table.clearSelection();
		  table.revalidate();
		  table.repaint();
      }
    });
    
    // button to split two selected cells
    // TODO be able to split 3 merged cells
    JButton b_split = new JButton("Split");
    b_split.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
		  int column = table.getSelectedColumn();
		  int row    = table.getSelectedRow();
		  cellAtt.split(row,column);
		  table.clearSelection();
		  table.revalidate();
		  table.repaint();
      }
    });
    
    // button for inserting row. appears to not work as of 2/24
    // TODO make this work?
    JButton r_one   = new JButton("Insert Row");
    r_one.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
		  int[] columns = table.getSelectedColumns();
		  int[] rows    = table.getSelectedRows();
		  //cellAtt.addRow(null);
		  AttributiveCellTableModel cad=new AttributiveCellTableModel(10,6);
		  Vector v=new Vector(6);
		  cad.insertRow(2, v);
		  table.clearSelection();
		  table.revalidate();
		  table.repaint();
      }
    });
    
    
    // button for generating a text file with latex code
    // TODO pass text, color, merged cell info, and other data
    // TODO implement function to create actual LaTex code
    JButton b_generateTex   = new JButton("Generate LaTex");
    b_generateTex.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
    	  generateTxtFile(cellText);
    	  table.clearSelection();
		  table.revalidate();
		  table.repaint();
      }
    });
    
       
    /**
     * Background colors. They will show up on the GUI when cells are selected. 
     * The colors available are the default colors available in Latex.
     */
    String[] defaultColors = { "Background colors", "white", "black", "red", "green", "blue", "cyan", "magenta", "yellow"};
    final JComboBox b_bgcolor = new JComboBox(defaultColors);
    b_bgcolor.setSelectedIndex(0);
    b_bgcolor.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e){
    		
    		cellAtt.changeColor(b_bgcolor.getSelectedItem().toString());

    		String newColor = b_bgcolor.getSelectedItem().toString();
    		if (newColor.equals("Background colors")) newColor = "white";
    		
    		Color c;
    		try {
    		      Field field = Color.class.getField(newColor);
    		      c = (Color)field.get(null);
    		      table.setOpaque(true);
    		      table.setBackground(c);
//    		      table.setSelectionBackground(c);
    		      table.setSelectionForeground(c);
    		      
    		      table.clearSelection();
    			  table.revalidate();
    			  table.repaint();

    		} catch (Exception f) {
    		      c = null; // Not defined
    		      System.out.println("MSCTE Failed with color " + newColor);
    		  }
    	}
    	
    });
    
    
    /*
     * Font type for the cell text. Combobox is added on the GUI so that user can select
     * the font they want. Combobox for the size of the text and the style of the text
     * are also available.
     * Once the font, size and style is applied hit apply to set these features.
     */
    
    
    String[] str_style = { "medium", "bold", "upright", "italic", "slanted", "small caps" };
    String[] default_fonts = {"serif", "sans serif", "typewriter"};
//    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//    String[] fontFamilyNames = ge.getAvailableFontFamilyNames();
    
    
    JComboBox fontComboBox = new JComboBox(default_fonts);	
    fontComboBox.setSelectedIndex(0);
  
    JComboBox style = new JComboBox(str_style);
    style.setSelectedIndex(0);
    JComboBox size = new JComboBox(str_size);
    size.setSelectedIndex(0);
	
    JButton apply = new JButton("Apply Font");
    apply.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] columns = table.getSelectedColumns();
			int[] rows = table.getSelectedRows();
			
			Font font = new Font((String) fontComboBox.getSelectedItem(), style
		              .getSelectedIndex(), getSizeIndex(size.getSelectedItem()));
			
			cellAtt.setFont(font, rows, columns);
			
			table.clearSelection();
			table.revalidate();
			table.repaint();
			
			
		}
	});
    
    
   
    
    
    // button for exiting program through GUI
    JButton b_quit   = new JButton("Quit Program");
    b_quit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
    	  System.exit(0);
      }
    });

    
   
    // edit however you like 
    JPanel p_buttons = new JPanel();
    // set up the button grid (row, column)
    p_buttons.setLayout(new GridLayout(5,1));
    p_buttons.add(b_one);
    p_buttons.add(b_split);
    p_buttons.add(r_one);
    p_buttons.add(b_generateTex);
    p_buttons.add(b_bgcolor);
    p_buttons.add(fontComboBox);
    p_buttons.add(style);
    p_buttons.add(size);
    p_buttons.add(apply);
    p_buttons.add(b_quit);
    
    Box box = new Box(BoxLayout.X_AXIS);
    box.add(scroll);
    box.add(new JSeparator(SwingConstants.HORIZONTAL));
    box.add(p_buttons);
    getContentPane().add( box );
    setSize( 800, 300 );
    setVisible(true);
  }
  
  static int getSizeIndex(Object sizeName){
  	
  	int size_ind = 0;
  	
  	for(int i = 0; i < str_size.length; i++){
  		
  		if(sizeName.equals(str_size[i])){
  			
  			size_ind = i;
  			break;
  			
  		}
  		
  		
  	}
  	
  	return size_ind;
  }
  
  //commented by harish - moved to StartTableCreation.java file
  /*public static void main(String[] args) {
    MultiSpanCellTableExample frame = new MultiSpanCellTableExample();
    frame.addWindowListener( new WindowAdapter() {
      public void windowClosing( WindowEvent e ) {
        System.exit(0);
      }
    });
  }*/
  
  
//generates text file with LaTex Table code
	  public void generateTxtFile(HashMap<String, Object> cellText){
		  LaTexTable latexTable = new LaTexTable(ml, cellAtt, cellText);
		  latexTable.saveLatexToFile();
	  }	

 /*
  * method tableChanged tracks the changes in table cells (e.g. insertion of text in cells of the table)
  * then gets the row and column in which changes were occured and saves the text in the HASHMAP whose key is "row+","+column".
  * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
  * 
  */
//	@Override
	public void tableChanged(TableModelEvent e) {
		
		int row = e.getFirstRow();
	    int column = e.getColumn();
	    TableModel model = (TableModel)e.getSource();
	    String columnName = model.getColumnName(column);
//	    System.out.println("Column name "+columnName);
//	    Object data = model.getValueAt(row, column);
//		System.out.println(data.toString());
	    String key = row+","+column;
	    Object value = model.getValueAt(row, column);
	    cellText.put(key, value);
		
	}
	
	  
	
}

