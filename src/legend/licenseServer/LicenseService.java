package legend.licenseServer;

import java.io.UnsupportedEncodingException;
import java.security.Signature;
import java.security.SignatureException;

import com.jetbrains.ls.*;
import com.jetbrains.ls.util.VerificationFailedException;

public class LicenseService
{

//	public static void main(String[] args) throws Exception
//	{
//		Signature privateSignature =  FloatingPrivateKeys.createSignature();
//		Signature publicSignature =  FloatingPublicKeys.createVerifyingSignature();
//		
//		String d = "4010a889068f8478b91d8e9a76111a6813ad83eab1ba151395f81625f6c68805b19d1a78143edfdb04fa5d6103e21c3e0da3171293242565dba41bb65091afc7";
//		String c = "<ObtainTicketResponse><message></message><prolongationPeriod>607875500</prolongationPeriod><responseCode>OK</responseCode><salt>1451191810341</salt><ticketId>1</ticketId><ticketProperties>licensee=legend123	licenseType=0	</ticketProperties></ObtainTicketResponse>";
//
////		byte[] f = {0x30, (byte)255};
////		System.out.println(bytesToString(f));
//		
//		d = generateSignature(c, privateSignature);
//		verifySignature(c, d, publicSignature);
//	}
	
	public static String generateSignature(String content)
	{
		Signature privateSignature =  FloatingPrivateKeys.createSignature();
		return generateSignature(content, privateSignature);
	}
	
	public static String generateSignature(String paramString1, Signature paramSignature)
	{
		try {
			paramSignature.update(paramString1.getBytes("UTF-8"));
		
			byte[] signData = paramSignature.sign();
			
			return bytesToString(signData);
			
		} catch (SignatureException localSignatureException) {
			
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			
		}
		
		return null;
	}
	
	public static void verifySignature(String paramString1, String paramString2, Signature paramSignature)
			throws VerificationFailedException {
		try {
			paramSignature.update(paramString1.getBytes("UTF-8"));

			byte[] arrayOfByte = hexStringToBytes(paramString2);
			if (!paramSignature.verify(arrayOfByte)) {
				throw new VerificationFailedException("Verification failed");
			}
		} catch (SignatureException localSignatureException) {
//			throw new VerificationFailedException(localSignatureException);
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
//			throw new VerificationFailedException(localUnsupportedEncodingException);
		}
	}
	
	final static char[] digits = {
        '0' , '1' , '2' , '3' , '4' , '5' ,
        '6' , '7' , '8' , '9' , 'a' , 'b' ,
        'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
        'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
        'o' , 'p' , 'q' , 'r' , 's' , 't' ,
        'u' , 'v' , 'w' , 'x' , 'y' , 'z'
    };
	
	private static String bytesToString(byte[] arrayOfByte)
	{
		char[] arrayOfChar = new char[arrayOfByte.length << 1];
		for (int j = 0, i = 0; j < arrayOfByte.length; j++)
		{
			int hex = arrayOfByte[j] & 0xff;
			arrayOfChar[i++] = digits[hex >> 4];
			arrayOfChar[i++] = digits[hex & 0xf];
		}
		return String.valueOf(arrayOfChar);
	}
	private static byte[] hexStringToBytes(String paramString) throws VerificationFailedException {
		byte[] arrayOfByte = new byte[paramString.length() >> 1];
		
		for (int j = 0, i = 0; j < arrayOfByte.length; j++) {
			int k = charToInt(paramString.charAt(i++));
			int m = charToInt(paramString.charAt(i++));
			arrayOfByte[j] = ((byte) ((k << 4) + m));
		}
		return arrayOfByte;
	}
	 
	
	private static int charToInt(char paramChar) throws VerificationFailedException {
		if (('0' <= paramChar) && (paramChar <= '9')) {
			return paramChar - '0';
		}
		if (('A' <= paramChar) && (paramChar <= 'F')) {
			return paramChar - 'A' + 10;
		}
		if (('a' <= paramChar) && (paramChar <= 'f')) {
			return paramChar - 'a' + 10;
		}
		
		throw new VerificationFailedException("Unexpected character in signature");
	}
}
