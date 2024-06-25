import java.math.BigInteger;
import java.util.HashMap;

public class DigitalEnvelope {

	public static void main(String[] args) {
		String plainText = FileUtilities.uploadFile("PlainMessage_Example.txt");			//short message
		String initVector = FileUtilities.uploadFile("IV_Example.txt");						//initial vector
		String symmetricKey = FileUtilities.uploadFile("Key_Example.txt");							//a-h key

		HashMap<BigInteger, BigInteger> p_q_primes = RSA.generate_p_q_differentPrimes();

		BigInteger p = p_q_primes.keySet().iterator().next();
		BigInteger q = p_q_primes.get(p);
		BigInteger n = p.multiply(q);
		BigInteger fi_n = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

		BigInteger publicKey = RSA.getPublicKey(fi_n);
		BigInteger d = publicKey.modInverse(fi_n);

		HashMap<BigInteger,String> DigitalEnvelope=createEnvelope(initVector, plainText, symmetricKey, publicKey, n);
		String decryptedMessage = openEnvelope(DigitalEnvelope,initVector,d,n);
		System.out.println("Dycripted message: " + decryptedMessage);
	}

    private static HashMap<BigInteger, String> createEnvelope(String initVector, String plainText, String symmetricKey, BigInteger publicKey, BigInteger n) {
        String cipherText = CipherBlockChaining.encrypt(plainText, initVector, symmetricKey);
        BigInteger encryptedKey = RSA.encrypt(symmetricKey, publicKey, n);

        HashMap<BigInteger, String> envelope = new HashMap<>();
        envelope.put(encryptedKey, cipherText);
        return envelope;
    }

    private static String openEnvelope(HashMap<BigInteger, String> envelope, String initVector, BigInteger privateKey, BigInteger n) {
        BigInteger encryptedKey = envelope.keySet().iterator().next();
        String decryptedKey = RSA.decrypt(encryptedKey, privateKey, n);
        String decryptedText = CipherBlockChaining.decrypt(envelope.get(encryptedKey), initVector, decryptedKey);
        return decryptedText;
    }

}
