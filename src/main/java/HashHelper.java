import org.bouncycastle.util.encoders.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashHelper {
	public static String hashMessage(byte[] msg) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException ex) {
			System.out.println(ex.getMessage());
			return "";
		}
		md.update(msg);
		byte[] shaDig = md.digest();
		return new String(Hex.encode(shaDig));
	}
}
