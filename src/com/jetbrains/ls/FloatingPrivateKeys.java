package com.jetbrains.ls;


import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.Signature;

public class FloatingPrivateKeys
{
  public static final java.security.PrivateKey PRIVATE_ENCRYPTION_KEY;
  public static final String ENCRYPTION_MODULUS  = "9616540267013058477253762977293425063379243458473593816900454019721117570003248808113992652836857529658675570356835067184715201230519907361653795328462699";
  public static final String ENCRYPTION_EXPONENT = "4802033916387221748426181350914821072434641827090144975386182740274856853318276518446521844642275539818092186650425384826827514552122318308590929813048801";
  
  static
  {
    try
    {
      KeyFactory localKeyFactory = KeyFactory.getInstance("RSA");
      PRIVATE_ENCRYPTION_KEY = localKeyFactory.generatePrivate(new java.security.spec.RSAPrivateKeySpec(new BigInteger(ENCRYPTION_MODULUS), new BigInteger(ENCRYPTION_EXPONENT)));
    }
    catch (Exception localException)
    {

      throw new RuntimeException(localException);
    }
  }
  
  public static Signature createSignature() {
    try {
      Signature localSignature = Signature.getInstance("MD5withRSA");
      localSignature.initSign(PRIVATE_ENCRYPTION_KEY);
      return localSignature;
    } catch (Exception localException) {
      throw new RuntimeException(localException);
    }
  }
  
  public FloatingPrivateKeys() {}
}
