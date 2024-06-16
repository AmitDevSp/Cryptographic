import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;

public class RSA {

	public static void main(String[] args) {
		String plain_message = FileUtilities.uploadFile("PlainMessage_Example");
		HashMap<BigInteger, BigInteger> p_q_primes = generate_p_q_different_primes();

        BigInteger p = (BigInteger) p_q_primes.keySet().toArray()[0];
		BigInteger q = p_q_primes.get(p);
		BigInteger n = p.multiply(q);
		BigInteger fi_n = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

		BigInteger e = getPublicKey(fi_n);
		BigInteger d = e.modInverse(fi_n);

		byte[] encrypted_message = encrypt(plain_message, e, n);
		System.out.println("finish encrypt");
		String decrypted_text = decrypt(encrypted_message, d, n);
		System.out.println("finish dencrypt");
		System.out.println("dycripted_message: "+decrypted_text);
	}

	public static HashMap<BigInteger,BigInteger> generate_p_q_different_primes(){

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

	public static byte[] encrypt(String plain_message, BigInteger public_key, BigInteger n) {
		byte[] plain_message_bytes = plain_message.getBytes();
		BigInteger plain_message_biginteger = new BigInteger(1, plain_message_bytes);
		BigInteger sol = plain_message_biginteger.modPow(public_key, n);
		return sol.toByteArray();
	}

	public static String decrypt(byte[] encrypted_message,BigInteger private_key,BigInteger n) {
		BigInteger encrypted_message_biginteger = new BigInteger(encrypted_message);
		BigInteger sol = encrypted_message_biginteger.modPow(private_key, n);
		return new String(sol.toByteArray());   
	}

	public static BigInteger getPublicKey(BigInteger fi_n) {
		BigInteger publicKey = new BigInteger((fi_n.toByteArray().length + 4)/2, new Random());
		while(! fi_n.gcd(publicKey).equals(BigInteger.ONE) ) {
			publicKey = new BigInteger((fi_n.toByteArray().length + 4)/2, new Random());	//public key
		}
		return publicKey;
	}
}
