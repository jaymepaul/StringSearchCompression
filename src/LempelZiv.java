import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

			//FAILING HERE
			if(cursor == 3196212)
				System.out.println();

			while(true){

				String toMatch = input.substring(cursor, (cursor + lookAhead >= input.length())?input.length()-1:cursor+lookAhead);
				String windowText = input.substring( (cursor < windowSize)?0:cursor-windowSize, (cursor < windowSize)?cursor:cursor - 1);

				int match = stringMatch(toMatch, windowText, cursor);

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

		//TOTAL BITS: 3196247 - war and peace
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
		else if(windowText.contains(toMatch))
			return cursor - windowText.lastIndexOf(toMatch);

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
		Pattern pattern = Pattern.compile(tupleRegex);
		Matcher matcher = pattern.matcher(compressed);

		List<String> textTuples = new ArrayList<String>();
		while(matcher.find())
			textTuples.add(matcher.group());

		List<Tuple> tuples = new ArrayList<Tuple>();
		for(String tuple : textTuples){

			//REGEX FOR SPLITTING A TUPLE BY COMMA i.e. \\d,\\d,\\w
			//String s = tuple.split(regex);
			//tuples.add(new Tuple(Integer.parseInt(s[0]), Integer.parseInt(s[1]), s[2].charAt(0)));

		}

		for(Tuple tuple : tuples){

			int offSet = tuple.offSet;
			int length = tuple.length;
			char c = tuple.c;

			if( offSet == 0 && length == 0){					//If [0,0] tuple simply get its character and append to string
				sbText.append(c);
				cursor++;
				continue;
			}

			char[] patternMatch = new char[length];
			sbText.getChars( cursor - offSet, (cursor - offSet) + length, patternMatch, 0);

			for(char cN : patternMatch)
				sbText.append(cN);

			cursor += length;

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
