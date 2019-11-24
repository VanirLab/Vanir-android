package app.vanir.package_;


public class BagEncryptionParams implements Parcelable {

 private final String mEncryptionAlgorithm;
 private final IvParameterSpec mEncryptionSpec;
 private final SecretKey mEncryptionKey;
 private final String mMacAlgorithm;
 private final AlgorithmParameterSpec mMacSpec;
 private final SecretKey mMacKey;
 private final byte[] mMacTag;
 private final long mAuthenticatedDataStart;
 private final long mEncryptedDataStart;
 private final long mDataEnd;

}
