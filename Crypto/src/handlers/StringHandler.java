package handlers;

import java.io.UnsupportedEncodingException;

import android.util.Log;

public class StringHandler 
{
	public static final String TAG = "StringHandler"; 
	public static final String UTF_TAG = "UTF-8";
	
	/**
	 * Encodes given byte[] to String as UTF-8 
	 * 
	 * @param bytes 
	 * 			bytes of the string
	 * @return
	 * 			String value of the bytes 
	 * */
	public static String GetString(byte[] bytes)
	{
		String result = null;
				
		try 
		{
			result = new String(bytes, UTF_TAG);
		} 
		catch (UnsupportedEncodingException e) 
		{
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
		return result;
	}
	
}
