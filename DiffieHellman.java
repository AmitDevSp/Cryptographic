
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;


public class DiffieHellman {

	public static void main(String[] args) {
		String plainText = FileUtilities.uploadFile("PlainMessage_Example");			//short message
		String senderInfo = "group_4";
		String CAInfo = "CyberSecurity2020";
		int length = (plainText.getBytes().length +4)/2;

		HashMap<BigInteger, BigInteger> p_q_primes = RSA.generate_p_q_differentPrimes ();

		BigInteger p = new BigInteger(p_q_primes.keySet().toArray()[0].toString());
		BigInteger q = p_q_primes.get(p);
		BigInteger n = p.multiply(q);
		BigInteger fi_n = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

		BigInteger e = RSA.getPublicKey(fi_n);
		BigInteger d = e.modInverse(fi_n);

		BigInteger private_Xa = BigInteger.probablePrime(length , new Random());	//	private key A
		BigInteger private_Xb = BigInteger.probablePrime(length , new Random());	//	private key B

		while(private_Xa.compareTo(q) >= 0 || private_Xb.compareTo(q) >= 0) {
			private_Xa = BigInteger.probablePrime(length , new Random());
			private_Xb = BigInteger.probablePrime(length , new Random());
		}

		q = BigInteger.valueOf(7);
		private_Xa = BigInteger.valueOf(5);
		private_Xb = BigInteger.valueOf(3);

		BigInteger[] DiffieHellmanArray = diffieHellman(q, private_Xa, private_Xb);
		byte[] hash = getDigest(senderInfo, DiffieHellmanArray[4], CAInfo);

		byte[] digest = signertificate(hash, e, n);
		byte[] digestOut = getHashFromCertificate(digest, d, n);

		System.out.println(Arrays.equals(hash, digestOut));

	}


	private static BigInteger[] diffieHellman(BigInteger q, BigInteger private_Xa, BigInteger private_Xb) {
		BigInteger alpha = generatePrimitiveRoot(q);
		System.out.println("Alpha: " + alpha);

		BigInteger Ya = alpha.modPow(private_Xa, q);	//	public key A
		BigInteger Yb = alpha.modPow(private_Xb, q);	//	public key B

		BigInteger Ka = Yb.modPow(private_Xa, q);	//symmetric key	A
		BigInteger Kb = Ya.modPow(private_Xb, q);	//symmetric key B

		BigInteger[] results = {Kb,Ka,Yb,private_Xb,Ya,private_Xa,alpha};
		return results;
	}

	private static byte[] getDigest(String senderInfo, BigInteger senderPublicKey, String CAInfo) {
		byte[] si = senderInfo.getBytes();
		byte[] pk = senderPublicKey.toByteArray();
		byte[] ca = CAInfo.getBytes();
        byte[] combined = new byte[si.length + pk.length + ca.length];

		System.arraycopy(si,0,combined,0,si.length);
		System.arraycopy(pk,0,combined,si.length,pk.length);
		System.arraycopy(ca,0,combined,si.length+pk.length,ca.length);
		return combined;
	}

	private static byte[] signertificate(byte[] hash, BigInteger publicKey, BigInteger n) {
		return RSA.encrypt(new String(hash), publicKey, n).toByteArray();
	}

	private static byte[] getHashFromCertificate(byte[] signedCertificate,BigInteger privateKey,BigInteger n) {
		return RSA.decrypt(new BigInteger(signedCertificate), privateKey, n).getBytes();	
	}

	private static BigInteger generatePrimitiveRoot(BigInteger q) {
        BigInteger alpha = BigInteger.valueOf(2);

        while (!alpha.modPow(q.subtract(BigInteger.ONE), q).equals(BigInteger.ONE)) {
            alpha = alpha.add(BigInteger.ONE);
        }

        return alpha;
    }

}
