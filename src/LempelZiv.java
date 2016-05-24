import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A new instance of LempelZiv is created for every run.
 */
public class LempelZiv {

	int textLength;
	/**
	 * Take uncompressed input as a text string, compress it, and return it as a
	 * text string.
	 */
	public String compress(String input) {
		// TODO fill this in.

		this.textLength = input.length();
		StringBuilder sbLZ = new StringBuilder();

		int cursor = 0;
		int windowSize = 3000;

		outer:
		while(cursor < input.length()){

			int lookAhead = 1;
			int prevMatch = 0;

			while(true){

				String toMatch = input.substring(cursor, (cursor + lookAhead >= input.length())?input.length():cursor+lookAhead);
//				String windowText = input.substring( (cursor < windowSize)?0:cursor-windowSize, (cursor < windowSize)?cursor:cursor - 1);
				String windowText = input.substring( (cursor < windowSize)?0:cursor-windowSize, cursor);

				int match = stringMatch(toMatch, windowText, cursor);

				if( match != -1){
					prevMatch = match;
					//System.out.println("cursor: "+ cursor + "	input: "+input.charAt(cursor + lookAhead -1));
					lookAhead++;

					if(cursor == input.length()-1){
						cursor -=1 ;
						sbLZ.append("["+prevMatch+"|"+ (lookAhead - 1)+"|"+input.charAt(cursor + lookAhead -1)+"]");
						break outer;
					}

				}
				else{

					sbLZ.append("["+prevMatch+"|"+ (lookAhead - 1)+"|"+input.charAt(cursor + lookAhead -1)+"]");		//[offset, length, nextCharacter] OR [0,0,character]
					//System.out.println("cursor: "+ cursor + "	input: "+input.charAt(cursor + lookAhead -1));
					cursor += lookAhead;
					break;
				}
			}

		}


		return sbLZ.toString();
	}

	/**
	 * Returns the lastIndex of string to match in the sliding window
	 * Returns -1 if there are no matches
	 *
	 * */
	private int stringMatch(String toMatch, String windowText, int cursor) {
		// TODO Auto-generated method stub

		if(toMatch.equals(""))
			return 0;
		else if(windowText.contains(toMatch)){
//			System.out.println("cursor: "+cursor+"	windowIndex: "+windowText.lastIndexOf(toMatch) + "	toMatch: "+toMatch);
//
			if(cursor > windowText.length()){
				int diff = cursor - windowText.length();					//Adjust according to text index value not just window index
				return cursor - (windowText.lastIndexOf(toMatch) + diff);
			}
			else
				return cursor - (windowText.lastIndexOf(toMatch));

		}
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

		String tupleRegex = "\\[(.*?)\\]";
		Pattern pattern = Pattern.compile(tupleRegex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(compressed);

		List<String> textTuples = new ArrayList<String>();
		while(matcher.find())
			textTuples.add(matcher.group());						//Split as Tuple Strings

		List<Tuple> tuples = new ArrayList<Tuple>();
		for(String tuple : textTuples){

			String sT = tuple.substring(1, tuple.length()-1);
			String[] sA = sT.split("\\|");							//Split as tokens

			int offSet = Integer.parseInt(sA[0]);
			int length = Integer.parseInt(sA[1]);
			char c = sA[2].charAt(0);

			tuples.add(new Tuple(offSet, length, c));				//Construct Tuple Objects

		}

		for(int i = 0; i < tuples.size(); i++){

			int offSet = tuples.get(i).offSet;
			int length = tuples.get(i).length;
			char c = tuples.get(i).c;

			if( offSet == 0 && length == 0){					//If [0,0] tuple simply get its character and append to string
				sbText.append(c);
				cursor++;
				continue;
			}
//
//			System.out.println("cursor: "+cursor+"	offSet: "+offSet+"	length: "+ length+ "	char: "+c);
//
			char[] patternMatch = new char[length];
			sbText.getChars( cursor - offSet, (cursor - offSet) + length, patternMatch, 0);		//Get the characters from offSet to cursor-Off + len

			for(char cN : patternMatch){			//Append character matches
				sbText.append(cN);
				cursor++;
			}

			if(i == tuples.size()-1)				//Break if at end
				break;

			sbText.append(c);						//Append next char in tuple
			cursor++;								//Advance cursor


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

	private class Tuple{

		int offSet;
		int length;
		char c;

		public Tuple(int offSet, int length, char c){
			this.offSet = offSet;
			this.length = length;
			this.c = c;
		}


	}
}
