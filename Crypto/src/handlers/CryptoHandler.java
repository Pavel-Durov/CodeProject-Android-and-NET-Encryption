package handlers;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

public class CryptoHandler 
{
    public static final String TAG = "CryptoHandler";
	public static final String PASSWORD = "CodeProject";
	
    private Cipher _aesCipher;
    private SecretKey _secretKey;
    private IvParameterSpec _ivParameterSpec;

    private static String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static String CIPHER_ALGORITHM = "AES";
    
    // Replace me with a 16-byte key, share between Sender and Reciever
    private static byte[] _rawSecretKey = 
    	{
    		0x12, 0x00, 0x22, 0x55, 0x33, 0x78, 0x25, 0x11,
    		0x33, 0x45, 0x00, 0x00, 0x34, 0x00, 0x23, 0x28
    	};

    private static String MESSAGEDIGEST_ALGORITHM = "MD5";

    /**
     * Constructor, intiate the object, creates a SecretKeySpec and IvParameterSpec
     *  @param passphrase
     *            the password phrase on which the cryptography will be depended
     */
    public CryptoHandler(String passphrase) 
    {
    	//decodes passd phrase to encrypted byte[] 
        byte[] passwordKey = encodeDigest(passphrase);

        try 
        {
        	//_aesCipher instantiation passing transformation parameter
            _aesCipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        } 
        catch (NoSuchAlgorithmException e) 
        {	//Invalid algorithm in passed transformation parameter
            Log.e(TAG, "No such algorithm " + CIPHER_ALGORITHM, e);
        } 
        catch (NoSuchPaddingException e) 
        {	//Invalid padding in passed transformation parameter
            Log.e(TAG, "No such padding PKCS5", e);
        }

        //Encodes the passed password phrase to a byte[] 
        //that will be stored in class private member of SecretKey type
        _secretKey = new SecretKeySpec(passwordKey, CIPHER_ALGORITHM);
        
        //Creates a new IvParameterSpec instance with the bytes 
        //from the specified buffer iv used as initialization vector.
        _ivParameterSpec = new IvParameterSpec(_rawSecretKey);
    }
    
    /**
     *  Encrypts the passed byte array
     *  @param clearData
     *          original byte[] data
     *            
     *  @return byte[]
     *  		encrypted bytes
     */
    public byte[] Encrypt(byte[] clearData) 
    {
    	byte[] result = null;
    	
        try 
        {	//Cipher initialization in ENCRYPT_MODE
            _aesCipher.init(Cipher.ENCRYPT_MODE, _secretKey, _ivParameterSpec);
        }
        catch (InvalidKeyException e) 
        {	
            Log.e(TAG, "Invalid key", e);
        } 
        catch (InvalidAlgorithmParameterException e) 
        {
            Log.e(TAG, "Invalid algorithm " + CIPHER_ALGORITHM, e);
        }
        
        result = DoWork(clearData);
        
        return result;
    }

    /**
     *  Decrypts the passed byte array
     *  @param clearData
     *          encrypted byte[] data
     *            
     *  @return byte[]
     *  		encrypted bytes
     */
    public byte[] Decrypt(byte[] data) 
    {
    	byte[] result = null;
    	
        try 
        {	//Cipher initialization in DECRYPT_MODE
            _aesCipher.init(Cipher.DECRYPT_MODE, _secretKey, _ivParameterSpec);
        } 
        catch (InvalidKeyException e) 
        {
            Log.e(TAG, "Invalid key", e);
        } 
        catch (InvalidAlgorithmParameterException e) 
        {
            Log.e(TAG, "Invalid algorithm " + CIPHER_ALGORITHM, e);
        }
  
        result = DoWork(data);
        
        return result;
    }
    /*Performs Cryptology, based on _aesCipher Cipher mode initialization*/
    /**
     * 
     * @param data on which the work will be performed.
     * @return Returns result of Cipher.doFinal() method
     */
    public byte[] DoWork(byte[] data)
    {
    	 byte[] result = null;
    	 
         try 
         {
        	 result = _aesCipher.doFinal(data);
         } 
         catch (IllegalBlockSizeException e) 
         {
             Log.e(TAG, "Illegal block size", e);
         } 
         catch (BadPaddingException e) 
         {
             Log.e(TAG, "Bad padding", e);
         }
          
         return result;
    }

    /**
	 * Performs the final update and then computes and returns the final hash value for this MessageDigest. After the digest is computed the receiver is reset.  
     * @param password phrase
     * @return
     */
    private byte[] encodeDigest(String text) 
    {
    	byte[] result = null;
        MessageDigest digest;
        try 
        {
            digest = MessageDigest.getInstance(MESSAGEDIGEST_ALGORITHM);
            result =  digest.digest(text.getBytes());
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.e(TAG, "No such algorithm " + MESSAGEDIGEST_ALGORITHM, e);
        }

        return result;
    }
}