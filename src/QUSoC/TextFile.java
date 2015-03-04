package QUSoC;

import java.io.*;
import java.text.DecimalFormat;

/**
 * A simple I/O class to help students read from or write to a text file. The
 * I/O classes that come with Java are very flexible, but are difficult for
 * beginners to learn. This class is a substitute that does most of the
 * simple things you need to do in intro Java courses.
 * <p>
 * None of the methods from this class throw exceptions. When errors occur, they
 * print error messages and abort the program.
 * <p>
 * For more details, see method descriptions below as well as example programs
 * provided with this class.
 * 
 * @author Margaret Lamb
 * @version 1.4, June 2007
 */
/* Versions: 
 * 1.1: July 2005, initial version
 * 1.2: September 2005: fixed minor problem with println
 * 1.3: August 2006: internal changes so package works with Java 1.4,
 *   added padding and formatting functions
 * 1.3a: September 2006: more user-friendly error message when readInt hits end of file
 * 1.4: June 2007: added INPUT and OUTPUT constants
 */
public class TextFile {

  /**
   * A TextFile object for reading user input from the keyboard ("standard
   * input")
   */
  public static final TextFile KEYBOARD = new TextFile(true);

  /**
   * A TextFile object for writing to the computer screen ("standard output").
   * This provides the same functionality as System.out, but is included for
   * completeness, and also contains formatted print and println methods for
   * ints and doubles.
   */
  public static final TextFile SCREEN = new TextFile(false);
  
  /**
   * Constant for use in the TextFile constructor, meaning that the file will be 
   * an input file for reading.
   */
  public static final boolean INPUT = true;
  
  /**
   * Constant for use in the TextFile constructor, meaning that the new file will be
   * an output file for writing.
   */
  public static final boolean OUTPUT = false;

  // This object does all the real work. It's either an InputFileObject or an
  // OutputFileObject. The only reason for the indirection is to make it
  // possible to use regular constuctors instead of a static "factory" method 
  // to create TextFile objects.
  private FileObject fileObj;

  /**
   * Creates a new TextFile object for reading from or writing to a file.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error message if the file can't be opened or if you attempt to read from
   * an input file which does not exist.
   * 
   * @param mode indicates whether the new file will be an input file (for reading)
   *   or an output file (for writing).  Use one of the constants <tt>TextFile.INPUT</tt>
   *   or <tt>TextFile.OUTPUT</tt>.
   * @param fileName the name of the file -- may be an absolute name
   *   ("c:/Java/Assignment 1/inputfile.txt") or a simple name
   *   ("inputfile.txt"). Simple names will be found in the same
   *   folder as your program (.java) files.
   */
  public TextFile(boolean mode, String fileName) {
    if (mode == INPUT)
      fileObj = new InputFileObject(fileName);
    else
      fileObj = new OutputFileObject(fileName);
  } // end constructor

  // special constructor for creating an object for reading from the standard
  // input or writing to the standard output
  private TextFile(boolean inputFile) {
    if (inputFile)
      fileObj = new InputFileObject();
    else
      fileObj = new OutputFileObject();
  } // end constructor

  /**
   * Reads a single character from the (input) file. Does not skip
   * whitespace characters.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an output file, or if we have already reached the end of
   * the file.
   * 
   * @return the character read from the file
   */
  public char readChar() {
    return fileObj.readChar();
  } // end readChar
  
  /**
   * Reads the next line from the (input) file.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an output file, or if we have already reached the end of
   * the file.
   * 
   * @return the line read from the file (minus the ending '\n')
   */
   public String readLine() {
     return fileObj.readLine();
   } // end readLine

  /**
   * Reads the next "word" from the (input) file. A word is a sequence of
   * characters other than the "whitespace" characters (space, tab and
   * newline). It will skip any initial whitespace and then read characters
   * until it sees either a whitespace character or the end of the file.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an output file, or if we have already reached the end of
   * the file.
   * 
   * @return the word read from the file
   */
   public String readWord() {
     return fileObj.readWord();
   } // end readWord

   /**
    * Reads an integer from the (input) file. It skips any whitespace
    * characters, reads the next "word" and converts it to an integer.
    * <p>
    * <b>Errors:</b> <br>
    * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
    * error if this is an output file, if we have already reached the end of
    * the file, or if the next word is not in legal integer format
    * 
    * @return the integer read from the file
    */
   public int readInt() {
     return fileObj.readInt();
   } // end readInt

   /**
    * Reads a double from the (input) file. It skips any whitespace
    * characters, reads the next "word" and converts it to a double.
    * <p>
    * <b>Errors:</b> <br>
    * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
    * error if this is an output file, if we have already reached the end of
    * the file, or if the next word is not in legal double format
    * 
    * @return the integer read from the file
    */
   public double readDouble() {
     return fileObj.readDouble();
   } // end readDouble

   /**
    * Skips "white space" in the (input) file. White space characters are
    * blanks, tabs and newlines. This method throws away white space characters
    * from the input until it reaches either a non-white character or the end
    * of the file. It's not an error to call this method when you're already at
    * the end of the file.
    * <p>
    * <b>Errors:</b> <br>
    * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
    * error if this is an output file.
    */
   public void skipWhiteSpace() {
     fileObj.skipWhiteSpace();
   } // end skipWhiteSpace

   /**
    * Checks whether we've reached the end of the (input) file.
    * <p>
    * <b>Errors:</b> <br>
    * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
    * error if this is an output file.
    * 
    * @return true if we've reached the end of the file
    */
   public boolean eof() {
     return fileObj.eof();
   } // end eof

   /**
    * Writes a single character to the (output) file, followed by an
    * end-of-line character.
    * <p>
    * <b>Errors:</b> <br>
    * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
    * error if this is an input file.
    * 
    * @param c the character to write
    */
   public void println(char c) {
     fileObj.println(c);
   } // end println

   /**
    * Writes a string to the (output file), followed by an end-of-line
    * character. 
    * <p>
    * <b>Errors:</b> <br>
    * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
    * error if this is an input file.
    * 
    * @param s the string to write
    */
   public void println(String s) {
     fileObj.println(s);
   } // end println

  /**
   * Writes a string to the (output) file, using a specified minimum width,
   * followed by an end-of-line character.
   * If the string is shorter than the width, it is padded on the left with
   * blanks.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param s     the string to write
   * @param width the minimum width
   */
  public void println(String s, int width) {
    fileObj.println(s, width);
  } // end print
  
  /**
   * Writes an integer to the (output) file, followed by an end-of-line
   * character.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param i the integer to write
   */
   public void println(int i) {
     fileObj.println(i);
   } // end println

  /**
   * Writes an integer to the (output) file, using a specified minimum width,
   * followed by an end-of-line character. If the integer has fewer than the
   * specified number of digits, it is padded on the left with blanks. If the
   * integer has more than the specified number of digits, the minimum width
   * is disregarded and the full integer is printed.
   * <p>
   * <p>
   * <b>Example:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(1234,5)</tt> will print <tt>" 12345"</tt> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(1234,3)</tt> will print <tt>"1234"</tt>.
   * 
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param i the integer to write
   * @param width the minimum number of output characters to write; the method
   * will add spaces at the beginning if necessary to make this
   * number of characters.
   */
  public void println(int i, int width) {
    fileObj.println(i, width);
  } // end println

  /**
   * Writes a double number to the (output) file in the default format,
   * followed by an end-of-line character. Uses as many digits as required
   * without leading or trailing zeros.
   * <p>
   * <b>Examples:</b><br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(123.45)</tt> prints <tt>"123.45"</tt>. <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(11.0/7.0)</tt> prints <tt>"1.5714285714285714"</tt>.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param d
   *            the number to write.
   */
  public void println(double d) {
    fileObj.println(d);
  } // end println

  /**
   * Writes a double number to the (output) file using a specified minimum
   * width, followed by an end-of-line character. Always uses 6 digits after
   * the decimal point, even if that includes trailing zeros. Decimal digits
   * after 6 are rounded. Pads with spaces on the left, if necessary, to reach
   * the minimum width.
   * <p>
   * <b>Examples:</b><br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(123.45,12)</tt> prints <tt>"  123.450000"</tt>. <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(11.0/7.0,9)</tt> prints <tt>" 1.571429"</tt>.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param d the number to write
   * @param width the minimum number of output characters to write; the method
   *    will add spaces at the beginning if necessary to make this
   *    number of characters.
   */
  public void println(double d, int width) {
    fileObj.println(d, width);
  } // end println

  /**
   * Writes a double number to the (output) file using a specified minimum
   * width and precision, followed by an end-of-line character. Always uses
   * exactly the specified number of digits after the decimal point, rounding
   * the rest. (If the precision is negative, prints zero digits after the
   * decimal point.) Pads with spaces on the left, if necessary, to reach the
   * minimum width. If you want to specify the precision but not a minimum
   * width, use a width of 1.
   * <p>
   * <b>Examples:</b><br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(123.45,9,3)</tt> prints <tt>"  123.450"</tt>. <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(11.0/7.0,3,1)</tt> prints <tt>" 1.6"</tt>. <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(11.0/7.0,1,2)</tt> prints <tt>"1.57"</tt>.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param d the number to write
   * @param width the minimum number of output characters to write; the method
   *   will add spaces at the beginning if necessary to make this
   *   number of characters.
   */
  public void println(double d, int width, int precision) {
   fileObj.println(d, width, precision);
  } // end println

  /**
   * Writes a boolean value to the (output) file (as "true" or "false"),
   * followed by an end-of-line character.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param b the boolean value to write
   */
  public void println(boolean b) {
    fileObj.println(b);
  } // end println

  /**
   * Writes an object to the (output) file, followed by an end-of-line
   * character. The printing format corresponds to the "toString" method of
   * that object's type.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param obj
   *            the object to write
   */
  public void println(Object obj) {
    fileObj.println(obj);
  } // end println

  /**
   * Writes an end-of-line character to the (output) file.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   */
  public void println() {
    fileObj.println();
  } // end println

  /**
   * Writes a single character to the (output) file.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param c the character to write
   */
  public void print(char c) {
    fileObj.print(c);
  } // end print

  /**
   * Writes a string to the (output) file. 
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param s     the string to write
   */
  public void print(String s) {
    fileObj.print(s);
  } // end print

  /**
   * Writes a string to the (output) file, using a specified minimum width.
   * If the string is shorter than the width, it is padded on the left with
   * blanks.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param s the string to write
   * @param width the minimum width
  */
  public void print(String s, int width) {
    fileObj.print(s, width);
  } // end print

  /**
   * Writes an integer to the (output) file.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param i
   *            the integer to write
   */
  public void print(int i) {
    fileObj.print(i);
  } // end print

  /**
   * Writes an integer to the (output) file, using a specified minimum width.
   * If the integer has fewer than the specified number of digits, it is
   * padded on the left with blanks. If the integer has more than the
   * specified number of digits, the minimum width is disregarded and the full
   * integer is printed.
   * <p>
   * <b>Example:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(1234,5)</tt> will print <tt>" 12345"</tt> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(1234,3)</tt> will print <tt>"1234"</tt>.
   * 
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param i the integer to write
   * @param width the minimum number of output characters to write; the method
   *   will add spaces at the beginning if necessary to make this
   *   number of characters.
   */
  public void print(int i, int width) {
      fileObj.print(i, width);
  } // end print

  /**
   * Writes a double number to the (output) file in the default format. Uses
   * as many digits as required without leading or trailing zeros.
   * <p>
   * <b>Examples:</b><br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(123.45)</tt> prints <tt>"123.45"</tt>. <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(11.0/7.0)</tt> prints <tt>"1.5714285714285714"</tt>.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param d the number to write.
   */
  public void print(double d) {
    fileObj.print(d);
  } // end print

  /**
   * Writes a double number to the (output) file using a specified minimum
   * width. Always uses 6 digits after the decimal point, even if that
   * includes trailing zeros. Decimal digits after 6 are rounded. Pads with
   * spaces on the left, if necessary, to reach the minimum width.
   * <p>
   * <b>Examples:</b><br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(123.45,12)</tt> prints <tt>"  123.450000"</tt>. <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(11.0/7.0,9)</tt> prints <tt>" 1.571429"</tt>.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param d the number to write
   * @param width the minimum number of output characters to write; the method
   *   will add spaces at the beginning if necessary to make this
   *   number of characters.
   */
  public void print(double d, int width) {
    fileObj.print(d, width);
  } // end print

  /**
   * Writes a double number to the (output) file using a specified minimum
   * width and precision. Always uses exactly the specified number of digits
   * after the decimal point, rounding the rest. (If the precision is
   * negative, prints zero digits after the decimal point.) Pads with spaces
   * on the left, if necessary, to reach the minimum width. If you want to
   * specify the precision but not a minimum width, use a width of 1.
   * <p>
   * <b>Examples:</b><br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(1.5714,5,1)</tt> prints <tt>"  1.6"</tt>. <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(1.5714,15,2)</tt> prints <tt>" 1.57"</tt>. <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>println(1.5714,1,6)</tt> prints <tt>"1.571400"</tt>.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param d the number to write
   * @param width the minimum number of output characters to write; the method
   *   will add spaces at the beginning if necessary to make this
   *   number of characters.
   */
  public void print(double d, int width, int precision) {
    fileObj.print(d, width, precision);
  } // end print

  /**
   * Writes a boolean value to the (output) file (as "true" or "false").
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param b the boolean value to write
   */
  public void print(boolean b) {
    fileObj.print(b);
  } // end print

  /**
   * Writes an object to the (output) file. The printing format corresponds to
   * the "toString" method of that object's type.
   * <p>
   * <b>Errors:</b> <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will abort with an
   * error if this is an input file.
   * 
   * @param obj the object to write
   */
  public void print(Object obj) {
    fileObj.print(obj);
  } // end print

  /**
   * Closes the file when you're done using it. It's important to remember to
   * do this to free system resources associated with the file and to make
   * sure any buffered characters really make it to the file. It's not
   * necessary to close the special Keyboard or Screen files.
   */
  public void close() {
    fileObj.close();
  } // end close
    
  /**
   * Aborts the program with an error message.  Included here just because it's
   * generally useful, both for I/O and other purposes.
   *
   * @param msg the message to print before ending the program execution
   */
  public static void abort(String msg) {
    System.out.println(msg);
    System.exit(1);
  } // end abort
    
  /***************************************************************************
   * Helper methods for formatting numbers.  These are used by the print 
   * methods.  
   **************************************************************************/

  /**
   * Pads a string with spaces on the left to reach a target width.
   * If the string is already longer than the width, leaves it alone --
   * doesn't truncate.
   * <p>
   * <b>Examples:</b><br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>padLeft("hello", 7)</tt> returns <tt>"  hello"</tt>. <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>padLeft("hello", 4)</tt> returns <tt>"hello"</tt>. 
   * <p>
   * @param s the string to pad
   * @param width the target width (number of characters)
   * @return the string with spaces inserted on the left as needed
   */
  private static String padLeft(String str, int width) {
    String result = str;
    while (result.length() < width) 
      result = " " + result;
      return result;    
  } // end padLeft
    
  /**
   * Formats an integer for output, padding it to a target width.
   * Adds blank spaces to the left as needed to reach the width.
   * If the integer contains more spaces than the width, no digits
   * are truncated.  
   * <p>
   * <b>Examples:</b><br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>formatInt("12345", 7)</tt> returns <tt>"  12345"</tt>. <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>formatInt("12345", 4)</tt> returns <tt>"12345"</tt>. 
   * <p>
   * @param value the integer to format
   * @param width the target width
   * @return the integer in string form, with blank spaces inserted on the
   *   left as needed to reach the target width
   */
  private static String formatInt(int value, int width) {
    return padLeft(Integer.toString(value), width);
  } // end formatInt
  
  /**
   * Formats a double for output, with a specified precision and total width.
   * Returns the value that the print method would print given the same
   * double value, width and precision.
   * <p>
   * <b>Examples:</b><br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>formatDouble(1.5714,5,1)</tt> returns <tt>"  1.6"</tt>. <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>formatDouble(1.5714,5,2)</tt> returns <tt>" 1.57"</tt>. <br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   * <tt>formatDouble(1.5714,1,6)</tt> returns <tt>"1.571400"</tt>.
   * <p>
   *
   * @param value the double to format
   * @param width the target width (total number of characters in the string)
   * @param precision the target precision (digits after the decimal point)
   * @return the double value as a string, formatted as specified
   */
  private static String formatDouble(double value, int width, int precision) {
    String formatString = "0.";
    for (int i = 0; i < precision; i++)
      formatString += "0";
    DecimalFormat pattern = new DecimalFormat(formatString);
    String formatted = pattern.format(value);
    return padLeft(formatted, width);
  } // end formatDouble
  
  /**
   * Formats a double for output, with a specified total width and the
   * default precision (6 digits after the decimal point). 
   *
   * @param value the double to format
   * @param width the target width (total number of characters in the string)
   * @return the double value as a string, formatted as specified
   */
  private static String formatDouble(double value, int width) {
    return formatDouble(value, width, 6);
  } // end formatDouble
    

  /***************************************************************************
   * The inner class FileObject does all the real work. A FileObject is either
   * an InputFileObject for reading or an OutputFileObject for writing.
   **************************************************************************/
  private abstract class FileObject {
    // the name of the file
    protected String fileName;

    // All methods are abstract. InputFileObject implements the read methods
    // and aborts if the write methods are called. OutputFileObject
    // implements the write methods and aborts if the read methods are called. See the
    // top-level TextFile methods for descriptions of the functionality,
    // parameters, etc.
    public abstract String readLine();
    public abstract String readWord();
    public abstract void skipWhiteSpace();
    public abstract char readChar();
    public abstract int readInt();          
    public abstract double readDouble();
    public abstract void println(String s);
    public abstract void println(String s, int width);
    public abstract void println(char c);
    public abstract void println(int i);
    public abstract void println(int i, int midWidth);
    public abstract void println(double d);
    public abstract void println(double d, int width);
    public abstract void println(double d, int width, int precision);
    public abstract void println(boolean b);
    public abstract void println(Object obj);
    public abstract void println();
    public abstract void print(String s);
    public abstract void print(String s, int width);
    public abstract void print(char c);
    public abstract void print(int i);
    public abstract void print(int i, int width);
    public abstract void print(double d);
    public abstract void print(double d, int width);
    public abstract void print(double d, int width, int precision);
    public abstract void print(boolean b);
    public abstract void print(Object obj);
    public abstract void close();
    public abstract boolean eof();
  } // end class FileObject

  /***************************************************************************
   * An InputFileObject implements an input file, using the BufferedReader
   * class.
   **************************************************************************/
  private class InputFileObject extends FileObject {
    // The reader that does the real work.
    private BufferedReader reader;

    // Read-ahead; these characters have not been returned yet.
    // When we reach the end of the input file, buffer is set to null.
    private StringBuffer buffer = new StringBuffer();

    // Value returned by private function to denote end of file.
    private static final int EOF_VALUE = -1;

    // True if this file is the standard input (so that we won't
    // try to close it!)
    private boolean standardInput = false;

    public InputFileObject(String fileName) {
      this.fileName = fileName;
      try {
        reader = new BufferedReader(new FileReader(fileName));
      } 
      catch (FileNotFoundException e) {
        abort("Error: input file \"" + fileName + "\" does not exist");
      }
    } // end constructor

    public InputFileObject() {
      this.fileName = "standard input";
      reader = new BufferedReader(new InputStreamReader(System.in));
      standardInput = true;
    } // end constructor

    // Three specific abort methods with frequently-used error messages
    private void errorAbort() {
      abort("I/O error while reading from file \"" + fileName + "\"");
    } // end errorAbort

    private void eofAbort() {
      abort("attempt to read past end of input file \"" + fileName + "\"");
    } // end eofAbort

    private void writeAbort() {
      abort("error: attempt to write to input file \"" + fileName + "\"");
    } // end writeAbort

    // Reads the next character from the input file and returns it as an
    // int. If we're at the end of the file, returns -1 instead.
    private int readCharOrEOF() {
      try {
        if (buffer == null) { // already hit EOF
          return EOF_VALUE;
        } 
        else if (buffer.length() == 0) {
          String newLine = reader.readLine();
          if (newLine == null)
            return EOF_VALUE;
          else {
            buffer.append(newLine);
            buffer.append('\n');
          } // end if
        } // end if

        // If we get here, the buffer is not null or length 0, so return
        // the next character
        char result = buffer.charAt(0);
        buffer.deleteCharAt(0);
        return result;
      } // end try
      catch (IOException e) {
        errorAbort();
        return ' '; // keep compiler happy
      } // end catch
    } // end readCharOrEOF

    // Pushes a character back onto the input stream.  Used when we
    // read a character we're not ready to send to the user.
    private void pushChar(char ch) {
      if (buffer == null)
        buffer = new StringBuffer(ch);
      else
        buffer.insert(0, ch);
    } // end pushChar

    // Returns true if ch is a "whitespace" character
    private boolean isWhite(char ch) {
      switch (ch) {
        case ' ':
        case '\t':
        case '\n':
          return true;
        default:
          return false;
      } // end switch
    } // end isWhite

    /* ***********************************************************************
     * PUBLIC METHODS
     * 
     * See header comments in top-level TextFile methods for descriptions
     * of functionality, parameters, etc.
     * ***********************************************************************/
    public boolean eof() {
      int nextChar = readCharOrEOF();
      if (nextChar == EOF_VALUE)
        return true;
      else {
        pushChar((char) nextChar);
        return false;
      } // end if
    } // end eof

    public void skipWhiteSpace() {
      char ch = ' ';
      while (true) {
        if (eof()) {
          buffer = null;
          return;
        } 
        else if (!isWhite(ch)) {
          pushChar(ch);
          return;
        } 
        else
          // we just read a whitespace character, so keep going
          ch = readChar();
      } // end while
    } // end skipWhiteSpace

    public String readWord() {
      // skip white space, then read and accumulate characters
      // until non white space character or eof
      StringBuffer word = new StringBuffer();
      skipWhiteSpace();
      if (eof()) 
        eofAbort();
      while (true) {
        if (eof())
          return word.toString();
        char ch = readChar();
        if (isWhite(ch)) {
          pushChar(ch);
          return word.toString();
        } else {
          word.append(ch);
        } // end if
      } // end while
    } // end readWord

    public String readLine() {
      if (eof())
        eofAbort();
      // read and accumulate characters until end of line or end of file
      StringBuffer newLine = new StringBuffer();
      int newChar = readCharOrEOF();
      while (newChar != '\n' && newChar != EOF_VALUE) {
        newLine.append((char) newChar);
        newChar = readCharOrEOF();
      } // end while
      return newLine.toString();
    } // end readLine

    public char readChar() {
      int ch = readCharOrEOF();
      if (ch == EOF_VALUE)
        eofAbort();
      return (char) ch;
    } // end readChar
    
    public int readInt() {
      String intWord = readWord();
      try {
        return Integer.parseInt(intWord);
      } // end try
      catch (NumberFormatException e) {
        abort("error: \"" + intWord + "\" is not a legal integer");
        return 0; // keep compiler happy
      } // end catch
    } // end readInt

    public double readDouble() {
      String doubleWord = readWord();
      try {
        return Double.parseDouble(doubleWord);
      } // end try
      catch (NumberFormatException e) {
        abort("error: \"" + doubleWord + "\" is not a legal double");
        return 0; // keep compiler happy
      } // end catch
    } // end readDouble

    public void close() {
      if (standardInput)
        return; // don't close the standard input
      try {
        reader.close();
      } 
      catch (IOException e) {
        abort("Error while attempting to close file \"" + fileName
          + "\"");
      }
      fileName = "closed file";
    } // end close

    // Calling an output method for an object of this class is an error.
    public void println(String s) {
      writeAbort();
    } // end println
    public void println(String s, int width) {
      writeAbort();
    } // end println
    public void println(int i) {
      writeAbort();
    } // end println
    public void println(int i, int midWidth) {
      writeAbort();
    }
    public void println(double d) {
      writeAbort();
    } // end println
    public void println(double d, int width) {
      writeAbort();
    } // end println
    public void println(double d, int width, int precision) {
      writeAbort();
    } // end println
    public void println(char c) {
      writeAbort();
    } // end println
    public void println(boolean b) {
      writeAbort();
    } // end println
    public void println(Object obj) {
      writeAbort();
    } // end println
    public void println() {
      writeAbort();
    } // end println
    public void print(String s) {
      writeAbort();
    } // end print
    public void print(String s, int width) {
      writeAbort();
    } // end print
    public void print(int i) {
      writeAbort();
    } // end print
    public void print(int i, int width) {
      writeAbort();
    } // end print
    public void print(double d) {
      writeAbort();
    } // end print
    public void print(double d, int width) {
      writeAbort();
    } // end print
    public void print(double d, int width, int precision) {
      writeAbort();
    } // end print
    public void print(char c) {
      writeAbort();
    } // end print
    public void print(boolean b) {
      writeAbort();
    } // end print
    public void print(Object obj) {
      writeAbort();
    } // end print
        
  } // end class InputFileObject

  /***************************************************************************
   * An OutputFileObject implements an output file, using the PrintStream
   * class.
   **************************************************************************/
  private class OutputFileObject extends FileObject {
    // the writer that does the real work
    private PrintStream writer;

    public OutputFileObject(String fileName) {
      this.fileName = fileName;
      try {
        writer = new PrintStream(new FileOutputStream(fileName));
      } 
      catch (FileNotFoundException e) {
        abort("Error: can't open file \"" + fileName + "\" for writing");
      }
    } // end constructor

    public OutputFileObject() {
      this.fileName = "standard output";
      writer = System.out;
    } // end constructor

    // abort with a common error message
    private void readAbort() {
      abort("Error: attempt to read from output file \"" + fileName
      + "\"");
    } // end readAbort

    /* ***********************************************************************
     * PUBLIC METHODS
     *  
     * See header comments in top-level TextFile methods for descriptions
     * of functionality, parameters, etc.
     * ***********************************************************************/
    public void println(String s) {
      print(s);
      println();
    } // end println

    public void println(String s, int width) {
      print(s, width);
      println();
    } // end println

    public void println(int i) {
      print(i);
      println();
    } // end println

    public void println(int i, int width) {
      print(i, width);
      println();
    } // end println

    public void println(double d) {
      print(d);
      println();
    } // end println

    public void println(double d, int width) {
      print(d, width);
      println();
    } // end println

    public void println(double d, int width, int precision) {
      print(d, width, precision);
      println();
    } // end println

    public void println(char c) {
      print(c);
      println();
    } // end println

    public void println(boolean b) {
      print(b);
      println();
    } // end println

    public void println(Object obj) {
      print(obj);
      println();
    } // end println

    public void println() {
      writer.println();
    } // end println

    public void print(String s) {
      writer.print(s);
    } // end print

    public void print(String s, int width) {
      print(padLeft(s,width));
    } // end print
        
    public void print(int i) {
      print(Integer.toString(i));
    } // end println

    public void print(int i, int width) {
      print(formatInt(i, width));
    } // end print

    public void print(double d) {
      print(Double.toString(d));
    } // end print

    public void print(double d, int width) {
      print(formatDouble(d, width));
    } // end print

    public void print(double d, int width, int precision) {
      print(formatDouble(d, width, precision));
    } // end print

    public void print(char c) {
      print(c + "");
    } // end print

    public void print(boolean b) {
      if (b)
        print("true");
      else
        print("false");
    } // end print

    public void print(Object obj) {
      print(obj.toString());
    } // end println

    public void close() {
      if (writer != System.out) {
        writer.close();
        fileName = "closed file";
      } // end if
    } // end close

    /* ***********************************************************************
     * Calling an input method for an object of this class is an error.
     * **********************************************************************/
    public String readLine() {
      readAbort();
      return null; // keep compiler happy
    } // end readLine
    public int readInt() {
      readAbort();
      return 0; // keep compiler happy
    } // end readInt
    public double readDouble() {
      readAbort();
      return 0; // keep compiler happy
    } // end readInt
    public boolean eof() {
      readAbort();
      return false; // keep compiler happy
    } // end eof
    public char readChar() {
      readAbort();
      return ' '; // keep compiler happy
    } // end readChar
    public String readWord() {
      readAbort();
      return null; // keep compiler happy
    } // end readChar
    public void skipWhiteSpace() {
      readAbort();
    } // end skipWhiteSpace

  } // end class OutputFileObject

} // end class TextFile

