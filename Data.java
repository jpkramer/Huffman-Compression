	/**
	 * Data.java
	 * 
	 * Provides an object containing both character and frequency values --> used as elements in Binary Tree
	 * 
	 * @author Jonathan Kramer
	 */

  public class Data {
    private final char ch;
    private final int freq;

    /**
     * Constructor
     * 
     * @param ch
     * @param freq
     */
    Data(char ch, int freq) {
        this.ch    = ch;
        this.freq  = freq;
    }
    
    /**
     * Getter method for the ch variable
     * @return
     */
    public char getChar() {
    	return ch;
    }
    
    /**
     * Getter method for the freq variable
     * @return
     */
    public int getFreq() {
    	return freq;
    }
    
    /**
     * Overrided toString() method
     */
    public String toString() {
    	
    	return "char=" + ch + " freq=" + freq;
    }
  }