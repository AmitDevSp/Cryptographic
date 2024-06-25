//to verify

public class CipherBlockChaining {

	public static void main(String[] args) {
		String plainText = FileUtilities.uploadFile("PlainText_Example.txt");	//text
		String key_ah = FileUtilities.uploadFile("Key_Example.txt");			//a-h key
		String initVector = FileUtilities.uploadFile("IV_Example.txt");		//initial vector

		String encrypted_text = encrypt(plainText, initVector, key_ah);
		String decrypted_text = decrypt(encrypted_text, initVector, key_ah);
		System.out.println("\nDecrypted text: \n\n" + decrypted_text);
	}


public static String encrypt(String plain_text, String initVector, String key) {
	String[] blocks = str2Blocks(plain_text);
	String[] cipher_text = new String[blocks.length];
	char[][] keyValueArry = getStringKeyValue(key);
	String iv = initVector;

	for (int i = 0; i < blocks.length; i++) {
		String xorBlock = XORBlock(blocks[i], iv);
		cipher_text[i] = encryptWithKey(xorBlock, keyValueArry);
		iv = cipher_text[i];
	}
	return blocks2Str(cipher_text);
}

public static String decrypt(String encryptedText, String initVector, String key) {
	String[] encryptedBlocks = str2Blocks(encryptedText);
	char[][] keyValueArry = getStringKeyValue(key);
	String[] results = new String[encryptedBlocks.length];
	String iv = initVector;

	for (int i = 0; i < encryptedBlocks.length; i++) {
		String decryptedBlock = unCryptWithKey(encryptedBlocks[i], keyValueArry);
		results[i] = XORBlock(decryptedBlock, iv);
		iv = encryptedBlocks[i];
	}
	return blocks2Str(results).replace("\0", "");
}


public static String[] str2Blocks(String text) {
	int rows = text.length() / 10 + ((text.length() % 10 > 0) ? 1 : 0);
	String[] stringArray = new String[rows];

	for (int i = 0; i < rows; i++) {
		int start = i * 10;
		int end = Math.min(start + 10, text.length());
		stringArray[i] = text.substring(start, end);
		while (stringArray[i].length() < 10) {
			stringArray[i] += "\0";
		}
	}
	return stringArray;
}


private static String blocks2Str(String[] blocks) {
	StringBuilder text = new StringBuilder();
	for (String block : blocks) {
		text.append(block);
	}
	return text.toString();
}

private static String XORBlock(String block, String initVector) {
	StringBuilder result = new StringBuilder();
	for (int j = 0; j < block.length(); j++) {
		result.append((char)(block.charAt(j) ^ initVector.charAt(j)));
	}
	return result.toString();
}

public static char[][] getStringKeyValue(String text) {
	text = text.replaceAll("[^A-Za-z]", "");
	int length = text.length() / 2;
	char[][] keyValueArry = new char[2][length];
	for (int i = 0; i < length; i++) {
		keyValueArry[0][i] = text.charAt(i * 2);
		keyValueArry[1][i] = text.charAt(i * 2 + 1);
	}
	return keyValueArry;
}


private static String encryptWithKey(String text, char[][] keyValueArry) {
	StringBuilder encrypted = new StringBuilder(text);
	for (int j = 0; j < text.length(); j++) {
		for (int m = 0; m < keyValueArry[0].length; m++) {
			if (text.charAt(j) == keyValueArry[0][m]) {
				encrypted.setCharAt(j, keyValueArry[1][m]);
				break;
			}
		}
	}
	return encrypted.toString();
}

private static String unCryptWithKey(String encryptedString, char[][] keyValueArry) {
	StringBuilder decrypted = new StringBuilder(encryptedString);
	for (int j = 0; j < encryptedString.length(); j++) {
		for (int m = 0; m < keyValueArry[1].length; m++) {
			if (encryptedString.charAt(j) == keyValueArry[1][m]) {
				decrypted.setCharAt(j, keyValueArry[0][m]);
				break;
			}
		}
	}
	return decrypted.toString();
}









}
