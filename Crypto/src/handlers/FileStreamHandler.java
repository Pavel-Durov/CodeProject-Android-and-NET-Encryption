package handlers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

public class FileStreamHandler 
{
	public static final String ENCRYPTED_TXT_FILENAME = "classified_file.txt";
	
	Context _context;
	
	public FileStreamHandler(Context context)
	{
		_context = context;
	}	
	/**
	 * Returns an absolute path to local files directory
	 * */
	public File GetInternalPath() 
	{
		return _context.getFilesDir();
	}
	/**
	 * Saves data to text file by given name
	 * 
	 * @param fileName 
	 * 			name of the file
	 * @param data
	 * 			content of the file
	 * @return
	 * 			boolean - whether operation succeeded 
	 * **/
	public boolean SaveTextFile(String fileName, byte[] data) 
	{
		String absolute = GenerateAbsolutePath(fileName);
		CheckFileCreatetion(absolute);
		boolean result = false;
		FileOutputStream out = null;
		try 
		{
			out = new FileOutputStream(absolute);
			out.write(data);
			result = true;
		} 
		catch (FileNotFoundException e1) 
		{	
			e1.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(out != null)
				{
					out.close() ;
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * Generates Absolute path to file
	 *  
	 * @param fileName
	 * 			name of the file
	 * 
	 * @return
	 * 		absolute path to the file
	 * */
	private String GenerateAbsolutePath(String fileName) 
	{
		return GetInternalPath() + File.separator + fileName;
	}
	/***
	 * Determines whether the given file is created or not, if not it creates a new file.
	 * @param fileName
	 * 			name of the file
	 * */
	private void CheckFileCreatetion(String fileName) 
	{
		File file = new File(fileName);
		if(!file.exists())
		{
			try 
			{
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Returns all text stored in file
	 * @param fileName 
	 * 			name of the desired file
	 * @return byte[] 
	 * 			the content of the file
	 * 	
	 */
	public byte[] ReadFromLocalFile(String fileName) 
	{
		byte[] result = null;
		
		if(fileName != null)
		{
			File file = new File(GenerateAbsolutePath(fileName));
			if(file != null && file.exists())
			{
				 int size = (int) file.length();
				 result = new byte[size];
				 try 
				 {
				     BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
				     buf.read(result, 0, result.length);
				     buf.close();
				 } 
				 catch (FileNotFoundException e) 
				 {
					 // TODO Auto-generated catch block
				     e.printStackTrace();
				 } 
				 catch (IOException e) 
				 {
				     // TODO Auto-generated catch block
					 e.printStackTrace();
				 }
			}
		}
		
		return result;
	}
}
