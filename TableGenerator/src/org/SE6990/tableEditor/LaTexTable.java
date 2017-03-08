package org.SE6990.tableEditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class LaTexTable {
	
	  private DefaultCellAttribute cellAtt;

	  // this will hold the required code to begin a table in latex
	  // initialized in constructor
	  private String beginStr;
	  
	  
	  // what comes in {} after {tabular} specifies the column count of latex table
	  // the contructor will format the rest of this based on 
	  private String beginTab = "\\begin{tabular}{|";
	  
	  // this is the required lines to end a table in latex
	  private final String endStr = "\\end{tabular}\n" +
	  						  		"\\end{table}\n";
	  
	  // used to separate cells in latex
	  private final String separator = "	&	";
	  
	  // used as non separator for combined cells in a row
	  private final String noSeparator = "			";
	  
	  // used at end of row in latex to skip to new row and draw a horizontal line
	  private final String endOfRow = "	\\\\ ";
	  
	  // beginning part of cell color in latex. Hex color value
	  // can be added to this in getCellData method
	  private String cellColor = "\\cellcolor[HTML]{";
	  
	  // represents a cell that isn't combined with another
	  private final int[] unitCell = {1, 1}; 
	  
	  // LaTex uses this to denote horizontally spanning cells
	  private String multiColumn = "\\multicolumn{";
	  
	  private final int rows, columns;
	  private HashMap<String, Object> cellText = null;
	  
	  
	  
	  // need rows and columns to create generic table with all cells centered
	  public LaTexTable(int rows, int columns, HashMap<String, Object> cellText){
		 this.rows = rows;
		 this.columns = columns;
		 this.cellText = cellText;
		 
		 this.formatBeforeStr(rows, columns);
		
	  }
	  
	  
	  // constructor takes table object which holds all attributes and hashmap of text
	  public LaTexTable(AttributiveCellTableModel ml, DefaultCellAttribute cellAtt, HashMap<String, Object> cellText){
		  this.rows = ml.getRowCount();
		  this.columns = ml.getColumnCount();
		  this.cellText = cellText;
		  this.cellAtt = cellAtt;
		  
		  this.formatBeforeStr(this.rows, this.columns);
	  }
	  
	  
	  private void formatBeforeStr(int rows, int columns){
		// format the begin tabular line for number of columns to be represented
		  for(int column = 0; column < this.columns; column = column + 1){
			  beginTab = beginTab + " c |";
		  }
		  
		  // add formatted begin tabular line where it goes in beginStr
		  this.beginStr = "%% add this to top of document \\usepackage[table,xcdraw]{xcolor} \n" +
				  	 	  "\\begin{table}[]\n" +
						  "\\centering\n" +
						  "\\caption{My caption}\n" +
						  "\\label{my-label}\n" +
						  beginTab + "}\n" +
						  "\\hline \n";
	  }
	  
	  
	  // contructor given the text of each cell if we find that
//	  public LaTexTable(int rows, int columns, Vector cellTexts){
//		  // fill in accordingly
//		  //TODO make parameter some data structure that contains all
//		  //of the tables information (row number, column number, cell text, etc)
//	  }
	  
	  // allows user to select location and name to save their latex table code to
	  public void saveLatexToFile(){
		  
		  File latexTableTxt = null;
		  Formatter outputTxt;
		  
		  JFrame parentFrame = new JFrame();
		  
		  JFileChooser fileChooser = new JFileChooser();
		  
		  // open window for user to select location to save
		  int userSelection = fileChooser.showSaveDialog(parentFrame);
		  
		  // initialize file object to new file that user just named
		  if(userSelection == JFileChooser.APPROVE_OPTION){
			  latexTableTxt = fileChooser.getSelectedFile();
		  }
		  
		  // write the table code to the new file that user just selected above
		  try{
			  outputTxt = new Formatter(latexTableTxt);
			  
			  // write to the output file the LaTex code string
			  outputTxt.format(this.writeTable());
			  
			  outputTxt.close(); // close stream
			  System.out.println("success file write to " + latexTableTxt.getAbsolutePath()); //debug
		  }
		  catch(FileNotFoundException e){
			  System.out.println("Exception: " + e);
		  }
	  }
	  
	  
	  
	  // return the table source code as a String
	  public String writeTable(){
		  String cells = "";
		  
		  String horizontalLine;
		  
		  // if rows or columns equals 0, print error message to file
		  if(this.rows == 0 || this.columns == 0){
			  cells = "Supplied rows: " + rows + ". Supplied columns: " + columns + ".\n" +
					  "Neither of these values should be 0. Try again.";
			  
			  return cells;
		  }
		  
		  int columnSpan = 1;
		  int rowSpan = 1; // counter variable used to count through horizontally combined cells
		  
		  
		  // loop that prints out the cell spans
		  // for debug purposes only
		  for(int row = 0; row < this.rows; row = row + 1){
			  
			  for(int column = 0; column < this.columns; column = column + 1){
				  
				  System.out.println(" span of " + row +", " +column +": " + Arrays.toString(cellAtt.getSpan(row, column)));
			  } // end loop through colums
		  } // end loop through rows
		  
		  // print the tabular structure for the table based on number of columns and rows
		  for(int row = 0; row < this.rows; row = row + 1){
			  
			  for(int column = 0; column < this.columns; column = column + 1){
				  horizontalLine = this.getHorizontalLine(row); 
				  
				  int[] cellSpan = cellAtt.getSpan(row, column);
				  
				  // if current columns isn't the right-most
				  if(column != (this.columns - 1))
				  {
					  
					  
					  if(cellSpan[1] == 1){ //if the row span number is 0 or 1 there is no combination horizontally
						  
						  System.out.println(row +", " + column +" cellspan = unit cell true" + Arrays.equals(cellSpan, unitCell));
						  // when we are getting text, color, and other data from cells replace above line with this
						  cells = cells + this.getCellData(row, column) + separator;
						  
					  }
					  else if(rowSpan == 1){  // if rowSpan is 1 then this is first cell of the horizontal span
						  
						  System.out.printf("columnSpan: %d, rowSpan: %d. column: %d, row: %d. cellSpan %s%n", columnSpan, rowSpan, column, row, Arrays.toString(cellSpan));
						  // only set these if previous cell was unit cell but current one is not
						  System.out.println("cellspan: " + Arrays.toString(cellSpan) + ". unitCell: " + Arrays.toString(unitCell) + ". ==?: " +Arrays.equals(cellSpan, unitCell));
						  
						  columnSpan = cellSpan[0];
						  rowSpan = cellSpan[1]; // set rowSpan to number of cells to span horizontally
						  
						  if(rowSpan == 0){
							  int rsValue = 0;
							  for(int i = row; rsValue < 1; i = i - 1 ){
								  rsValue = cellAtt.getSpan(i, column)[1];
							  }
							  rowSpan = rsValue;
						  }
						  
						  if(rowSpan == 1){ // the row is not combined horizontally
							  cells = cells + this.getCellData(row, column) + separator;
						  }
						  else{
							// put latex code in that spans cells horizontally
							  if(column == 0){
								  cells = cells + multiColumn + rowSpan + "}{| c |}{" + this.getCellData(row, column) +"}";
							  }
							  else
							  {
								  cells = cells + multiColumn + rowSpan + "}{ c |}{" + this.getCellData(row, column) +"}";  
							  }
							  
							  
							  if(column + rowSpan >= this.columns){ // if the span ends at end of the row
								  cells = cells + noSeparator;
							  }
							  else{
								  cells = cells + separator;
							  }
						  }
						  
						  
					  }
					  else{ // if we are in a cell that is part of the horizontal span but isn't the first one to the left
						  
						  System.out.println(row +", " + column +". cellspan not unit. rowSpan: " + rowSpan);
						  cells = cells + noSeparator;
						  rowSpan = rowSpan - 1; //decrease rowSpan counter by 1
					  }
				  }
				  else // if we are at the right-most cell in a row
				  {	
					  if(cellSpan[1] == 1 || cellSpan[1] == 0){ // if the right-most cell is not part of a span

						  cells = cells + this.getCellData(row, column) + endOfRow + horizontalLine;
					  }
					  else{ // if the right-most cell is part of a span(would be the last in the span as well)
						  
						  cells = cells + endOfRow  + horizontalLine;
						  rowSpan = rowSpan - 1; // reset rowSpan to 1 for the next row
					  }
				  }
			  } // end loop through colums
		  } // end loop through rows
		  
		  return this.beginStr + cells + this.endStr;
	  } //end writeTable()
	  
	  
	  
	  
	  // returns string to generate proper horizontal line after the current row
	  // will be "\hline" if not column cells are combined and "\cline{#-#}" format otherwise
	  // caveat: only works for single column spans right now (4/10/2016)
	  private String getHorizontalLine(int row){
		  String lineStr = " ";
		  
		  if(this.columns == 1){  // single column tables won't need any line if cells are combined
			  lineStr = "\n";
		  }
		  else if(row == this.rows - 1){ // last row always has \hline after it
			  lineStr = "\\hline \n";
		  }
		  else{
			  int start = 0;
			  int end = 0;
			  
			  for(int i = 0; i < this.columns; i = i + 1){ // loop through current row
				  
				  // first 4 checks are for the last cell in the row
				  // TODO fix the last column bugs
				  if(i == this.columns - 1 && cellSpanDetected(row, i) && start != 0){ // end of row and span detected
					  if(isEndOfSpan(row, i)){ // if end of span a line will go under it
						  lineStr = lineStr + "\\cline{" + start + "-" + i + "} \\cline{" + this.columns + "-" + this.columns + "} ";
					  }
					  else { // the current span has ended
						  end = this.columns - 1;
						  lineStr = lineStr + "\\cline{" + start + "-" + end + "} ";
					  }
				  }
				  else if(i == this.columns - 1 && cellSpanDetected(row, i) && start == 0){ // end of row and has been spanned the whole way
					  if(isEndOfSpan(row,i)){
						  end = this.columns;
						  lineStr = lineStr + "\\cline{" + end + "-" + end + "} ";
					  }
					  else {
						  lineStr = lineStr + "\n"; // don't draw any line
					  }
				  }
				  else if(i == this.columns -1 && !cellSpanDetected(row, i) && start == 0){ // end of row and no span in last column
					  end = this.columns;
					  lineStr = lineStr + "\\cline{" + end + "-" + end + "} ";
				  }
				  else if(i == this.columns - 1 && !cellSpanDetected(row,i) && start != 0){ // end of row and no span had been detected
					  end = this.columns;
					  lineStr = lineStr + "\\cline{" + start + "-" + end + "} ";
				  }
				  
				  // last two checks are for any other case
				  else if(!cellSpanDetected(row, i) && start == 0){ // if it's the start of a line and there is no span, start the beginning index 
					  start = i + 1;
				  }
				  else if(cellSpanDetected(row,i) && start == 0){ // if cell is in a span and the line hasn't begun yet
					  if(isEndOfSpan(row,i)){ // if the cell is at the end of a span, start the line
						  start = i + 1;
					  }
				  }
				  else if(cellSpanDetected(row, i) && start != 0){ // if span is detected and start has been initialized
					  if(isEndOfSpan(row, i)){ // if end of span a line will go under it
						  // do nothing. a line will go under this
					  }
					  else { // the current line span has ended
						  end = i;
						  lineStr = lineStr + "\\cline{" + start + "-" + end + "} ";
						  
						  // reset values for next span 
						  start = 0;
					  }
				  }
				  
			  }
			  
			  if(lineStr.equals(" ")){ //if not cline was used above
				  lineStr = "\\hline";
			  }
			  
			  lineStr = lineStr + "\n";
		  }
		  
		  return lineStr;
	  }
	  
	  
	  private boolean cellSpanDetected(int row, int column){
		  boolean spanDetected = false;
		  
		  if(this.cellAtt.getSpan(row, column)[0] != 1){
			  int csValue = 0;
			  if(this.cellAtt.getSpan(row, column)[0] == 0){
				  
				  for(int i = column; csValue < 1; i = i - 1){
					  csValue = cellAtt.getSpan(row, i)[0];
				  }
			  }
			  if(csValue != 1){
				 spanDetected = true;  
			  }
			  
		  }
		  
		  return spanDetected;
	  }
	  
	  
	  //returns true if the current cell is the end of a span (requiring the line below it to be filled in)
	  private boolean isEndOfSpan(int row, int column){
		  boolean end = false;
		  int currentCellColumnValue = 0;
		  int nextCellColumnValue = 0; 
		  
		  if(row < (this.rows - 1)){
			  currentCellColumnValue = this.cellAtt.getSpan(row, column)[0];
			  nextCellColumnValue = this.cellAtt.getSpan(row + 1, column)[0];  
		  }
		  
		  if(currentCellColumnValue < 0 && nextCellColumnValue >= 0){
			  end = true;
		  }
		  
		  return end;
	  }
	  	  
	  
	  
	  // method that takes row and column number and retrieves the text, color, and other information
	  // given in a cell
	  private String getCellData(int row, int column){
		  String cellData = "";
		  //if cell has color add Hex color value to cell data like so:
	  			// cellData = cellData + cellColor + colorVector[row][column] + "}";
		  
		  //if cell has text add text string to cellData
		  String key = row+","+column;
		  
		  String cellString = "";
		  if(cellText.containsKey(key)){
			  System.out.println(" this cell has text " + key);
			  cellString = cellText.get(key).toString();
			  cellData = cellData + cellString;
		  
		  }else{
			  
			  cellData = cellData + key;
		  }
		  
		  //continue for other information
		  
		  return cellData;
	  }
	  
}
