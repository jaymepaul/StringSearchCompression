/**
 * A new KMP instance is created for every substring search performed. Both the
 * pattern and the text are passed to the constructor and the search method. You
 * could, for example, use the constructor to create the match table and the
 * search method to perform the search itself.
 */
public class KMP {
	
	char[] patternArr;
	char[] textArr;

	int matchTable[];		//MatchTable
	
	public KMP(String pattern, String text) {
		
		this.patternArr = pattern.toCharArray();
		this.textArr = text.toCharArray();
		this.matchTable = createMatchTable();
		
	}
	

	/**
	 * Perform KMP substring search on the given text with the given pattern.
	 * 
	 * This should return the starting index of the first substring match if it
	 * exists, or -1 if it doesn't.
	 */
	public int search(String pattern, String text) {

		
		int s = 0;
		int t = 0;
		
		while( t + s < textArr.length){
			
			if( patternArr[s] == textArr[t+s]){
				s++;
				if( s == patternArr.length)
					return t;
			}
			else if( matchTable[s] == -1){
				s = 0;
				t = t + s + 1;
			}
			else{
				t = t + s - matchTable[s];
				s = matchTable[s];
			}
		}
		
		return -1;
	}
	
	/***
	 * Creates the match table for KMP algorithm
	 * 
	 * @return int[] array 
	 * */
	public int[] createMatchTable(){
		
		int[] matchT = new int[textArr.length];
		
		//Initialize
		matchT[0] = -1;
		matchT[1] = 0;
		int j = 0;
		int pos = 2;
		
		while( pos < matchT.length){
			if(textArr[pos-1] == textArr[j]){
				matchT[pos] = j + 1;
				pos++;
				j++;
			}
			else if( j > 0){
				j = matchT[j];
			}
			else{
				matchT[pos] = 0;
				pos++;
			}
		}
		
		return matchT;
	}
	
}
