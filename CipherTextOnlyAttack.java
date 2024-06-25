import java.util.HashSet;
import java.util.Set;

public class CipherTextOnlyAttack {
	private static final String KEY = "abcdefgh";
	private static final int MIN_MATCH_COUNT = 40;

	private static final String DICTIONARY[] = {"the", "of", "are", "and", "you", "can", "to", "have", "not", "you", "with",
	"this","but","his","from", "he", "her", "that", "in", "that", "was", "is", "has", "it",
	"him", "bright", "cold", "day", "april", "time", "example",	"by","for", "have","my",
	"not","yes","of","or","so","get","which"};


	public static void main(String[] args) {
		String cipherText = FileUtilities.uploadFile("CipherText_Example.txt");			//encrypted text
		String initVector = FileUtilities.uploadFile("IV_Example.txt");					//initial vector
		String newKey = keyAttack(cipherText, initVector);
        System.out.println("New key is: " + newKey);
	}

	private static String keyAttack(String encryptedText, String initVector) {
        Set<String> permutations = new HashSet<>();
        generatePermutations(KEY, "", permutations);

        String[] keysArray = permutations.toArray(new String[0]);

        for (String key : keysArray) {
            String decryptedText = CipherBlockChaining.decrypt(encryptedText, initVector, key);
            int count = countDictionaryMatches(decryptedText);

            if (count > MIN_MATCH_COUNT) {
                System.out.println(decryptedText);
                return key;
            }
        }
        System.out.println("Couldn't find the key");
        return null;
	}


    private static int countDictionaryMatches(String decryptedText) {
        int count = 0;
        for (String word : DICTIONARY) {
            String dictUpper = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            if (decryptedText.contains(" " + word) || decryptedText.contains(word) ||
                decryptedText.contains(" " + dictUpper) || decryptedText.contains(dictUpper)) {
                count++;
            }
        }
        return count;
    }



	private static void generatePermutations(String str, String ans, Set<String> permutations) { 	  
		if (str.length() == 0) { 
            permutations.add(ans);
			return; 
		} 
		for (int i = 0; i < str.length(); i++) { 
			char ch = str.charAt(i); 
			String ros = str.substring(0, i) + str.substring(i + 1); 
			generatePermutations(ros, ans + ch, permutations); 
		} 
	}

}
