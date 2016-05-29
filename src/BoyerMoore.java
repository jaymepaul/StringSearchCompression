public class BoyerMoore {
	
	public BoyerMoore(String pattern, String text) {}

	public int search(String pattern, String text) {
		
		char[] textArr = text.toCharArray();
		char[] patternArr = pattern.toCharArray();
		
        if (patternArr.length == 0) {
            return 0;
        }
        
        int charTable[] = makeBC_Table(patternArr);				//Construct the lookup Table for BadCharacters
        int offsetTable[] = makeGS_Table(patternArr);			//Construct the lookup Table for GoodSuffix Rule
        
        for (int i = patternArr.length - 1, j; i < textArr.length;) {
            for (j = patternArr.length - 1; patternArr[j] == textArr[i]; --i, --j) {
                if (j == 0) {
                    return i;									//Found exact match
                }
            }
            i += Math.max(offsetTable[patternArr.length - 1 - j], charTable[textArr[i]]);		//Take the max index between the GS Table and BC table
        }
        return -1;
    }

	/**Constructs the table based on the bad character rule where:
	 * upon mismatch it checks if there are any other occurences of that mismatched 
	 * character in the pattern, if so shift by the difference between the matched
	 * characters
	 * */
	private int[] makeBC_Table(char[] pattern) {
        final int ALPHABET_SIZE = 256;
        int[] table = new int[ALPHABET_SIZE];
        for (int i = 0; i < table.length; ++i) {
            table[i] = pattern.length;
        }
        for (int i = 0; i < pattern.length - 1; ++i) {
            table[pattern[i]] = pattern.length - 1 - i;
        }
        return table;
    }
	
	/**
	 * Constructs the table based on the good suffix rule where:
	 * if there is a match of x amount of characters then it aims to check 
	 * the pattern for areas where there is a matching prefix given a 
	 * matching suffix
	 * */
	private int[] makeGS_Table(char[] pattern) {
        int[] table = new int[pattern.length];
        int lastPrefixPosition = pattern.length;
        for (int i = pattern.length - 1; i >= 0; --i) {
            if (isPrefix(pattern, i + 1)) {
                lastPrefixPosition = i + 1;
            }
            table[pattern.length - 1 - i] = lastPrefixPosition - i + pattern.length - 1;
        }
        for (int i = 0; i < pattern.length - 1; ++i) {
            int slen = suffixLength(pattern, i);
            table[slen] = pattern.length - 1 - i + slen;
        }
        return table;
    }
	
	/**
	 * Returns true if the given index p is a prefix of the pattern
	 * otherwise it immediately returns false when there is a mismatch
	 * in the pattern
	 * */
	private boolean isPrefix(char[] pattern, int p) {
        for (int i = p, j = 0; i < pattern.length; ++i, ++j) {
            if (pattern[i] != pattern[j]) {
                return false;
            }
        }
        return true;
    }
	
	/**
	 * Returns the length of the suffix from a given index point in the pattern
	 * */
	private int suffixLength(char[] pattern, int p) {
        int len = 0;
        for (int i = p, j = pattern.length - 1;
                 i >= 0 && pattern[i] == pattern[j]; --i, --j) {
            len += 1;
        }
        return len;
    }

}
