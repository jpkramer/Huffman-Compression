import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import javax.swing.JFileChooser;

/**
 * Huffman.java
 * 
 * implements Huffman coding in order to compress/decompress a file
 * 
 * @author JonathanKramer
 */

public class CopyOfHuffman {

	/**
	 * 
	 * Creates frequency map for characters in a file
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
  public static Map<Character, Integer> makeFreqMap(String filename) throws IOException {
  	
  	
  	Map <Character, Integer> freqMap = new HashMap<Character, Integer>();
  	BufferedReader buffer = new BufferedReader(new FileReader(filename));
  	
  	int c = buffer.read();	
  	while(c != -1) {
  		if (!freqMap.containsKey((char) c)) {
  			freqMap.put((char) c, 1);
  		} else {
  			freqMap.put((char) c, freqMap.get((char) c)+1);
  		}
  		c = buffer.read();
  	}
  	
  	buffer.close();
  	return freqMap;
	}
  
  /**
   * Makes a min Priority Queue from a frequency map
   * 
   * @param freqMap
   * @return
   */
  public static PriorityQueue<BinaryTree<Data>> createMinPQ(Map<Character, Integer> freqMap) {
  
  	Comparator<BinaryTree<Data>> comparator = new TreeComparator();
  	PriorityQueue<BinaryTree<Data>> queue = new PriorityQueue<BinaryTree<Data>>(10, comparator);
  
  	Iterator<Character> mapIterator = freqMap.keySet().iterator();
  
  	while(mapIterator.hasNext()) {
  		char c = mapIterator.next();
  		BinaryTree<Data> singleton = new BinaryTree<Data>(new Data(c, freqMap.get(new Character(c))));
  		queue.add(singleton);
  	}
  	
  	return queue;
 }
					
  /**
   * Uses priority queue of singletons to create one full Binary Tree
   * 
   * @param queue
   * @return
   */
  public static BinaryTree<Data> createBinaryTree(PriorityQueue<BinaryTree<Data>> queue) {
  	
  	BinaryTree<Data> node1, node2;
  	char c = '\0';
  	
  	while(queue.size() > 1){
  		node1 = queue.remove();
  		node2 = queue.remove();
  		BinaryTree<Data> r = new BinaryTree<Data>(new Data(new Character(c),node1.getValue().getFreq() + node2.getValue().getFreq()), node1, node2);
  		queue.add(r);
  	}
  	
  	return queue.poll();
  	
  }
  
	/**
	 * Retrieves codes and puts them into a code map
	 * 
	 * @param fullTree
	 * @return
	 */
  public static Map<Character, String> retrieveCodes(BinaryTree<Data> fullTree) {
  	
  	Map<Character, String> codeMap = new HashMap<Character, String>();
  	retrieveCodesHelper(codeMap, fullTree, "");
  	return codeMap;
  }
  
  /**
   * Helper method that performs recursion through the BT
   * 
   * @param cMap
   * @param current
   * @param pathSoFar
   */
  public static void retrieveCodesHelper(Map<Character, String> cMap, BinaryTree<Data> current, String pathSoFar) {
  	
  	if (current.isLeaf()) {
  		cMap.put(current.getValue().getChar(), pathSoFar);
  		return ;
  	} 
  	else {
  		retrieveCodesHelper(cMap, current.getLeft(), pathSoFar + "0");
  		retrieveCodesHelper(cMap, current.getRight(), pathSoFar + "1");
  	}
  }
		
  
  /**
   * Helper method that implements the methods necessary to make the final Binary Tree
   * 
   * @param fPath
   * @return
   * @throws IOException
   */
	public static BinaryTree<Data> makeFinalBT(String fPath) throws IOException {
		
		Map<Character, Integer> frequencyMap = makeFreqMap(fPath);
		System.out.println(frequencyMap.toString());
		
  	PriorityQueue<BinaryTree<Data>> priorityQ = createMinPQ(frequencyMap);
  	
  	BinaryTree<Data> binaryTree = createBinaryTree(priorityQ);
  	System.out.println(binaryTree.toString());
  	
  	return binaryTree;
		
	}
  
	/**
	 * Compresses a file
	 * 
	 * @param fPath
	 * @throws IOException
	 */
  public static void CompressDecompress(String fPath) throws IOException {
  	BinaryTree<Data> binaryTree = makeFinalBT(fPath);
  	Map<Character, String> finalMap = retrieveCodes(binaryTree);
  	
  	BufferedReader inputFile =  new BufferedReader(new FileReader(fPath));
  	BufferedBitWriter bitOutputFile = new BufferedBitWriter(fPath.substring(0,fPath.length()-5) + "_compressed" + ".txt");
  	
  	int newChar = inputFile.read();
  	
  	while(newChar != -1) {
  		if (finalMap.containsKey((char)newChar)) {
  			String codeString = finalMap.get((char)newChar);
  			for (int i = 0; i < codeString.length(); i++)
  				if (codeString.charAt(i) == '1') 
  					bitOutputFile.writeBit(1);
  				else
  					bitOutputFile.writeBit(0);
  		}
  		newChar = inputFile.read();
  	}
  	
  	//String fPath2 = bitOutputFile.toString();
  	String fPath2 = getFilePath();
  	
  	BinaryTree<Data> tempBinaryTree = binaryTree;
  	
  	BufferedBitReader bitInputFile =  new BufferedBitReader(fPath2);
  	BufferedWriter outputFile = new BufferedWriter(new FileWriter(fPath2.substring(0, fPath2.length()-5) + "_decompressed" + ".txt"));
  	
  	int newBit = bitInputFile.readBit(); 
  	
  	while(newBit != -1) {
  		
  		while(!tempBinaryTree.isLeaf()) {
  		if (newBit == 0) 
  			tempBinaryTree = tempBinaryTree.getLeft();
  		else
  			tempBinaryTree = tempBinaryTree.getRight();
  		}
  	 	
  	outputFile.write((char)tempBinaryTree.getValue().getChar());
  	newBit = bitInputFile.readBit(); 
  	}
  	
  	inputFile.close();
  	bitOutputFile.close();
  	bitInputFile.close();
  	outputFile.close();
  
}
  
  /**
   * Puts up a fileChooser and gets the path name for the file to be opened.
   * Returns an empty string if the user clicks Cancel.
   * @return path name of the file chosen
   */
 public static String getFilePath() {
   // Create a file chooser.
   JFileChooser fc = new JFileChooser();
    
   int returnVal = fc.showOpenDialog(null);
   if (returnVal == JFileChooser.APPROVE_OPTION) {
     File file = fc.getSelectedFile();
     String pathName = file.getAbsolutePath();
     return pathName;
   }
   else
     return "";
  }
  
 /**
  * Prompts user for a file to be compressed. Then prompts the user asking if they want to decompress their compressed file.
  * 
  * @param args
  * @throws IOException
  */
  public static void main(String [] args) throws IOException {
  	
  	// ask user to get the file path
  	System.out.print("Choose a file to be compressed!");
  	String filePath1 = getFilePath();
  	CompressDecompress(filePath1);
  	
  }
 
}

