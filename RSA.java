import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;

public class RSA {

	public static void main(String[] args) {
		String plainText = FileUtilities.uploadFile("PlainMessage_Example");
		HashMap<BigInteger, BigInteger> p_q_primes = generate_p_q_differentPrimes();

        BigInteger p = p_q_primes.keySet().iterator().next();
		BigInteger q = p_q_primes.get(p);
		BigInteger n = p.multiply(q);
		BigInteger fi_n = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

		BigInteger publicKey = getPublicKey(fi_n);
		BigInteger privateKey = publicKey.modInverse(fi_n);

		BigInteger encryptedMessage = encrypt(plainText, publicKey, n);
		System.out.println("Finish encryption");
		String decryptedText = decrypt(encryptedMessage, privateKey, n);
		System.out.println("Finish decryption");
		System.out.println("Decrypted message: " + decryptedText);

	}

	public static HashMap<BigInteger,BigInteger> generate_p_q_differentPrimes(){

		HashMap<BigInteger, BigInteger> map = new HashMap<>();
		int bitLength = 1024;
		Random random = new Random();

		BigInteger p = BigInteger.probablePrime(bitLength , random);
		BigInteger q = BigInteger.probablePrime(bitLength , random);

        while (p.equals(q)) {
			q = BigInteger.probablePrime(bitLength , random);
		}
		map.put(p, q);
		return map ;	
	}

	public static BigInteger encrypt(String plainText, BigInteger publicKey, BigInteger n) {
		byte[] plainTextBytes = plainText.getBytes();
		BigInteger plainTextBigInt = new BigInteger(1, plainTextBytes);
		return plainTextBigInt.modPow(publicKey, n);
	}


	public static String decrypt(BigInteger encryptedMessage,BigInteger privateKey,BigInteger n) {
		BigInteger decryptedBigInt = encryptedMessage.modPow(privateKey, n);
		return new String(decryptedBigInt.toByteArray());  		
	}

	public static BigInteger getPublicKey(BigInteger fi_n) {
		BigInteger publicKey = new BigInteger((fi_n.toByteArray().length + 4)/2, new Random());
		while(! fi_n.gcd(publicKey).equals(BigInteger.ONE) ) {
			publicKey = new BigInteger((fi_n.toByteArray().length + 4)/2, new Random());	//public key
		}
		return publicKey;
	}
}
