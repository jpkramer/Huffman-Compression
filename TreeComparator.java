import java.util.Comparator;

	/**
	 * TreeComparator.java
	 * 
	 * Used by the priority queue to determine the order
	 * 
	 * 
	 *@author Jonathan Kramer
	 */
  public class TreeComparator implements Comparator<BinaryTree<Data>> {
  	
     public int compare(BinaryTree<Data> x, BinaryTree<Data> y) {
         if (x.getValue().getFreq() < y.getValue().getFreq())
              return -1;
         else if (x.getValue().getFreq() > y.getValue().getFreq())
              return 1;
         else
        	 		return 0;
      }
  }