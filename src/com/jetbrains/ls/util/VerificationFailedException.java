package com.jetbrains.ls.util;


public class VerificationFailedException
  extends Exception
{
  public VerificationFailedException() {}
  
  public VerificationFailedException(String paramString)
  {
    super(paramString);
  }
  
  public VerificationFailedException(String paramString, Throwable paramThrowable) {
    super(paramString, paramThrowable);
  }
  
  public VerificationFailedException(Throwable paramThrowable) {
    super(paramThrowable);
  }
}
