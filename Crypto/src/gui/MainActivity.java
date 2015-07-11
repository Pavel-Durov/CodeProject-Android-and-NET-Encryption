package gui;


import handlers.CryptoHandler;
import handlers.FileStreamHandler;
import handlers.StringHandler;
import net.tutorial.crypto.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
		
	CryptoHandler _crypto;
	TextView _tvMessage, _tvResult;
	FileStreamHandler _streamHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		InitComponents();
	}
	
	/**
	 * Initialize class members intances
	 */
	private void InitComponents() 
	{
		_tvMessage = (TextView)findViewById(R.id.tvMessage);
		_tvResult = (TextView)findViewById(R.id.tvEncryptedResult);
		_crypto = new CryptoHandler(CryptoHandler.PASSWORD);
		
		_streamHandler = new FileStreamHandler(this);
	}
	 /**
	 * On Button Click event
	 * @param sender 
	 * 			the button which invoked the event
	 */
	public void OnButtonClick(View sender)
	{
		int id = sender.getId();
		
		switch(id)
		{
			case R.id.bEncrypt:
			{
				EncryptMessage();break;
			}
			case R.id.bDecrypt:
			{
				DecryptMessage();break;
			}
			default:break;
		}
	}
	
	/**
	 * Decrypt the message from saved local file content.
	 * calls ShowToast() method
	 * */
	private void DecryptMessage() 
	{
		byte[] fileContent = _streamHandler
				.ReadFromLocalFile(FileStreamHandler.ENCRYPTED_TXT_FILENAME);
		
		if(fileContent != null && fileContent.length > 0)
		{
			//preforms decryption of the fuile content
			byte[] decrypted = _crypto.Decrypt(fileContent);
			
			//Creates new String instance of passed byte[] as UTF-8
			String readableData = StringHandler.GetString(decrypted);
			String encrypted = StringHandler.GetString(fileContent);
			
			if(readableData != null && encrypted != null)
			{
				//showing toast
				ShowToast
				(	
						getString(R.string.msg_decrypted) + readableData,
						getString(R.string.msg_encrypted) + encrypted
				);
			}
		}
		else
		{	//if file not exist or file content is empty
			ShowToast(":(", "!");
		}
	}
	
	
	/**
	 * Encrypts the String value that entered in the _tvMessage EditText View
	 * and saves is to file on local file system
	 * */
	private void EncryptMessage() 
	{
		String message  = _tvMessage.getText().toString();
		if(message != null && message.length() > 0)
		{
			//performs text encryption
			byte[] bytes = _crypto.Encrypt(message.getBytes());
			//sets view value
			SetTextViewEncryptresult(bytes);
			
			//saves encrypted text to internal file
			_streamHandler.SaveTextFile
			(
					FileStreamHandler.ENCRYPTED_TXT_FILENAME, 
					bytes
			);
		}	
	}
	
	/**
	 * Shows simple Toast message  
	 * */
	private void ShowToast(String enctypted, String decrypted)
	{
		Toast message = Toast.makeText(this, String.format("%s\r\n%s", enctypted, decrypted), Toast.LENGTH_LONG);
		message.setGravity(Gravity.TOP, 0, 0);
		message.show();
	}

	
	/**
	 * Sets the encrypted byte[] as text in _tvResult EditText View, using UTF-8 
	 * */
	private void SetTextViewEncryptresult(byte[] bytes) 
	{
		if(bytes != null && bytes.length > 0)
		{
			String encrypted = StringHandler.GetString(bytes);
			if(encrypted != null)
			{
				_tvResult.setText(encrypted);
			}
		}
	}
}
