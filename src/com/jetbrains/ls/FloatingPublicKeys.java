package com.jetbrains.ls;


import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.Signature;

public class FloatingPublicKeys
{
  public static final java.security.PublicKey PUBLIC_ENCRYPTION_KEY;
  public static final String ENCRYPTION_MODULUS = "9616540267013058477253762977293425063379243458473593816900454019721117570003248808113992652836857529658675570356835067184715201230519907361653795328462699";
  public static final String ENCRYPTION_EXPONENT = "65537";
  
  static
  {
    try
    {
      KeyFactory localKeyFactory = KeyFactory.getInstance("RSA");
      PUBLIC_ENCRYPTION_KEY = localKeyFactory.generatePublic(new java.security.spec.RSAPublicKeySpec(new BigInteger(ENCRYPTION_MODULUS), new BigInteger(ENCRYPTION_EXPONENT)));

    }
    catch (Exception localException)
    {

      throw new RuntimeException(localException);
    }
  }
  
  public static Signature createVerifyingSignature() {
    try {
      Signature localSignature = Signature.getInstance("MD5withRSA");
      localSignature.initVerify(PUBLIC_ENCRYPTION_KEY);
      return localSignature;
    } catch (Exception localException) {
      throw new RuntimeException(localException);
    }
  }
  
  public FloatingPublicKeys() {}
}
