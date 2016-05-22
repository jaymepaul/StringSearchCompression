import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A new instance of LempelZiv is created for every run.
 */
public class LempelZiv {
	
	
	/**
	 * Take uncompressed input as a text string, compress it, and return it as a
	 * text string.
	 */
	public String compress(String input) {
		// TODO fill this in.
		
		StringBuilder sbLZ = new StringBuilder();
		
		int cursor = 0;
		int windowSize = 1000;
		
		while(cursor < input.length()){
			
			int lookAhead = 0;
			int prevMatch = 0;
			
		
			while(true){
				
				System.out.println("CURSOR: " + cursor + "	LOOKAHEAD: " + lookAhead);
				String toMatch = input.substring(cursor, (cursor + lookAhead >= input.length())?input.length()-1:cursor+lookAhead);
				String windowText = input.substring( (cursor < windowSize)?0:cursor-windowSize, (cursor < windowSize)?0:cursor - 1);
				
				int match = stringMatch( toMatch, windowText, (cursor < windowSize)?0:cursor-windowSize );
		
				if( match != -1){
					prevMatch = match;
					lookAhead++;
				}
				else{
					sbLZ.append("["+prevMatch+","+ (lookAhead - 1)+","+input.charAt(cursor + lookAhead -1)+"]");		//[offset, length, nextCharacter] OR [0,0,character]
					cursor += lookAhead;
					break;
				}
			}
			
		}
		
		return sbLZ.toString();
	}
	

	private int stringMatch(String toMatch, String windowText, int winStart) {
		// TODO Auto-generated method stub
		
		//What is the index relative to the position of the entire text
		if(windowText.contains(toMatch))
			return windowText.indexOf(toMatch);
		
		
		return -1;
	}


	/**
	 * Take compressed input as a text string, decompress it, and return it as a
	 * text string.
	 */
	public String decompress(String compressed) {
		// TODO fill this in.
		
		StringBuilder sbText = new StringBuilder();
		
		int cursor = 0;
		
		String[] comp = compressed.split("(\\[\\d,\\d,\\w\\])");
		String text = null;
		
		for(String tuple : comp){
			
			if(tuple.charAt(0) == '0' && tuple.charAt(2) == '0')
				sbText.append(tuple.subSequence(4, 4), cursor, cursor);
				
			
			for(int j = 0; j < Character.getNumericValue(tuple.charAt(2))-1; j++){
				
			}
		}
		
		return sbText.toString();
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't want to. It is called on every run and its return
	 * value is displayed on-screen. You can use this to print out any relevant
	 * information from your compression.
	 */
	public String getInformation() {
		return "";
	}
}
