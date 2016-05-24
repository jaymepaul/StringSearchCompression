import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * A new instance of HuffmanCoding is created for every run. The constructor is
 * passed the full text to be encoded or decoded, so this is a good place to
 * construct the tree. You should store this tree in a field and then use it in
 * the encode and decode methods.
 */
public class HuffmanCoding {

	LinkedHashMap<Character, String> codes;		//A mapping of characters to bit codes
	Set<Character> UNQ_characters;				//An array of all UNIQUE characters in text
	TreeNode root;								//Binary Tree for decoding

	/**
	 * This would be a good place to compute and store the tree.
	 */
	public HuffmanCoding(String text) {
		// TODO fill this in.

		Queue<TreeNode> freqQueue = createFrequencyTable(text);

		while(freqQueue.size() > 1){

			TreeNode left = freqQueue.poll(), right = freqQueue.poll();												//Poll Top Two Nodes
			TreeNode n = new TreeNode(left.freq + right.freq, left.label + "_" + right.label, left, right);			//Create new Node with children

			freqQueue.offer(n);																						//Add New Node to Queue
		}

		//Final Node is Root of Tree
		root = freqQueue.poll();

		//Need to construct a map of characters to string containing bit codes!		Traverse down tree until label has exact match
		codes = new LinkedHashMap<Character, String>();

		//for each unique character find its code and store it into map
		for(char c : UNQ_characters){

			resetVisitedNodes(root);
			depthFirstSearch(root, c, "");

		}

	}

	public String depthFirstSearch(TreeNode node, char c, String code){


		node.visited = true;

		//Populate Map when Exact Match Found with its bit code
		if(node.label.length() == 1 && node.label.charAt(0) == c){
			codes.put(c, code);
			return code;
		}

		StringBuilder sb = new StringBuilder(code);

		char[] nodeArrLeft = node.left.label.toCharArray();
		char[] nodeArrRight = node.right.label.toCharArray();

		//Find Node
		for( int i = 0; i < node.label.length(); i++ ){
			if(i < nodeArrLeft.length && nodeArrLeft[i] == c){
				sb.append("0");
				node = node.left;
				break;
			}
			else if(i < nodeArrRight.length && nodeArrRight[i] == c){
				sb.append("1");
				node = node.right;
				break;
			}
		}

		if(!node.visited){
			depthFirstSearch(node, c, sb.toString());
		}

		return code;

	}

	public void resetVisitedNodes(TreeNode n){

		n.visited = false;

		if(n.left != null)
			resetVisitedNodes(n.left);
		if(n.right != null)
			resetVisitedNodes(n.right);


	}

	/**
	 * Take an input string, text, and encode it with the stored tree. Should
	 * return the encoded text as a binary string, that is, a string containing
	 * only 1 and 0.
	 */
	public String encode(String text) {
		// TODO fill this in.

		StringBuilder sb = new StringBuilder();

		for( char c : text.toCharArray())
			sb.append(codes.get(c));

		return sb.toString();
	}

	/**
	 * Take encoded input as a binary string, decode it using the stored tree,
	 * and return the decoded text as a text string.
	 */
	public String decode(String encoded) {
		// TODO fill this in.

		StringBuilder sbText = new StringBuilder();

		char[] encArr = encoded.toCharArray();

		TreeNode node = root;

		for(int i = 0; i < encArr.length; i++){

			if(encArr[i] == '0'){
				node = node.left;
				if(node.isLeaf){
					sbText.append(node.label);
					node = root;						//RESET
				}
			}
			else if(encArr[i] == '1'){
				node = node.right;
				if(node.isLeaf){
					sbText.append(node.label);
					node = root;						//RESET
				}
			}
		}

		return sbText.toString();
	}


	public Queue<TreeNode> createFrequencyTable(String text){

		//Identify Unique Characters + Count Frequency
		char[] textArr = text.toCharArray();
		Map<Character, Integer> freq = new HashMap<Character, Integer>();

		for(char c : textArr){
			if(!freq.containsKey(c)){
				freq.put(c, 1);
			}
			else{
				int val = freq.get(c);
				val++;
				freq.remove(c);
				freq.put(c, val);
			}
		}

		//Store unique characters
		this.UNQ_characters = freq.keySet();

		//Sort by Frequency Count
		List<Map.Entry<Character, Integer>> list =new LinkedList<Map.Entry<Character, Integer>>( freq.entrySet() );

		Collections.sort( list, new Comparator<Map.Entry<Character, Integer>>()
	       {
				@Override
				public int compare(Entry<Character, Integer> o1, Entry<Character, Integer> o2) {
					return (o1.getValue()).compareTo( o2.getValue() );
				}
	        } );

		//Populate Queue
		PriorityQueue<TreeNode> freqPriority = new PriorityQueue<TreeNode>();

	    for (Map.Entry<Character, Integer> entry : list)
	    	freqPriority.offer( new TreeNode( entry.getValue(), Character.toString(entry.getKey()), null, null ));


	    return freqPriority;

	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't wan to. It is called on every run and its return
	 * value is displayed on-screen. You could use this, for example, to print
	 * out the encoding tree.
	 */
	public String getInformation() {
		return "";
	}

	/**
	 * Inner Class TreeNode
	 * */
	class TreeNode implements Comparable<TreeNode>{

		int freq;
		String label;
		TreeNode left;		//Children
		TreeNode right;

		boolean visited = false;
		boolean isLeaf = false;

		public TreeNode(int freq, String label, TreeNode left, TreeNode right){

			this.freq = freq;
			this.label = label;
			this.left = left;
			this.right = right;

			if(label.length() == 1)
				isLeaf = true;
		}

		@Override
		public int compareTo(TreeNode n) {
			return this.freq - n.freq;
		}

		public String toString(){
			return "LABEL: "+label + "\t FREQ: "+freq+"\t TRLEFT: "+left.label+ "\t TRRIGHT: "+ right.label;
		}

	}
}
