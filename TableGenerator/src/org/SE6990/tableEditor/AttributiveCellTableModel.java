package org.SE6990.tableEditor;
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

class AttributiveCellTableModel extends DefaultTableModel {

  protected DefaultCellAttribute cellAtt;
    
  public AttributiveCellTableModel() {
    this((Vector)null, 0);
  }
  
  /*
   * Method AttributiveCellTableModel
   * 
   * Comments added by Avdesh
   * setNumRows(numRows) is a obsolete method so it is commented and setRowCount(numRows) has been added
   * In latest versions of java setNumRows() is replaced by setRowCount()
   */
  public AttributiveCellTableModel(int numRows, int numColumns) {
    Vector names = new Vector(numColumns);	// set the size of the vector same as numColumns
    names.setSize(numColumns);				// Sets the size of this vector. If the new size is greater than the current size, new null items are added to the end of the vector. If the new size is less than the current size, all components at index newSize and greater are discarded.
    setColumnIdentifiers(names);			// If the number of newIdentifiers is greater than the current number of columns, new columns are added to the end of each row in the model. If the number of newIdentifiers is less than the current number of columns, all the extra columns at the end of a row are discarded.
    dataVector = new Vector();
//    setNumRows(numRows);					// Obsolete as of Java 2 platform v1.3. Please use setRowCount instead.
    setRowCount(numRows);
    cellAtt = new DefaultCellAttribute(numRows,numColumns);
  }
  
  public AttributiveCellTableModel(Vector columnNames, int numRows) {
    setColumnIdentifiers(columnNames);
    dataVector = new Vector();
    setNumRows(numRows);
    cellAtt = new DefaultCellAttribute(numRows,columnNames.size());
  }
  
  public AttributiveCellTableModel(Object[] columnNames, int numRows) {
    this(convertToVector(columnNames), numRows);
  }  
  
  public AttributiveCellTableModel(Vector data, Vector columnNames) {
    setDataVector(data, columnNames);
  }
  
  public AttributiveCellTableModel(Object[][] data, Object[] columnNames) {
    setDataVector(data, columnNames);
  }

  public void getValueAt() { 
      
	  System.out.println("in getValueAt");
	  
    }
    
  public void setDataVector(Vector newData, Vector columnNames) {
    if (newData == null)
      throw new IllegalArgumentException("setDataVector() - Null parameter");
    dataVector = new Vector(0);
   // setColumnIdentifiers(columnNames);
    this.columnIdentifiers = columnNames;
    dataVector = newData;
    
    //
    cellAtt = new DefaultCellAttribute(dataVector.size(),
                                       columnIdentifiers.size());
    
    newRowsAdded(new TableModelEvent(this, 0, getRowCount()-1,
     TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
  }

  public void addColumn(Object columnName, Vector columnData) {
    if (columnName == null)
      throw new IllegalArgumentException("addColumn() - null parameter");
    columnIdentifiers.addElement(columnName);
    int index = 0;
    Enumeration eeration = dataVector.elements();
    while (eeration.hasMoreElements()) {
      Object value;
      if ((columnData != null) && (index < columnData.size()))
    value = columnData.elementAt(index);
      else
  value = null;
      ((Vector)eeration.nextElement()).addElement(value);
      index++;
    }

    //
    cellAtt.addColumn();

    fireTableStructureChanged();
  }

  public void addRow(Vector rowData) {
    Vector newData = null;
    if (rowData == null) {
      newData = new Vector(getColumnCount());
    }
    else {
      rowData.setSize(getColumnCount());
    }
    dataVector.addElement(newData);

    //
    cellAtt.addRow();

    newRowsAdded(new TableModelEvent(this, getRowCount()-1, getRowCount()-1,
       TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
  }

  public void insertRow(int row, Vector rowData) {
    if (rowData == null) {
      rowData = new Vector(getColumnCount());
    }
    else {
      rowData.setSize(getColumnCount());
    }

    dataVector.insertElementAt(rowData, row);

    //
    cellAtt.insertRow(row);

    newRowsAdded(new TableModelEvent(this, row, row,
       TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
  }

  public DefaultCellAttribute getCellAttribute() {
    return cellAtt;
  }

  public void setCellAttribute(DefaultCellAttribute newCellAtt) {
    int numColumns = getColumnCount();
    int numRows    = getRowCount();
    if ((newCellAtt.getSize().width  != numColumns) ||
        (newCellAtt.getSize().height != numRows)) {
      newCellAtt.setSize(new Dimension(numRows, numColumns));
    }
    cellAtt = newCellAtt;
    fireTableDataChanged();
  }

  /*
  public void changeCellAttribute(int row, int column, Object command) {
    cellAtt.changeAttribute(row, column, command);
  }

  public void changeCellAttribute(int[] rows, int[] columns, Object command) {
    cellAtt.changeAttribute(rows, columns, command);
  }
  */
    
}