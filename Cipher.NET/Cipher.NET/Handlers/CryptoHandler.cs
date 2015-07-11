using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace Cipher.NET.Handlers
{
    class CryptoHandler
    {
        private ICryptoTransform _Decryptor;
        private ICryptoTransform _Encryptor;

        //Need to be the sabe as sets on Android
        public static readonly string PASSWORD = "CodeProject";

        //share between Java and C#
        private static byte[] _rawSecretKey = 
      	{
    		0x12, 0x00, 0x22, 0x55, 0x33, 0x78, 0x25, 0x11,
    		0x33, 0x45, 0x00, 0x00, 0x34, 0x00, 0x23, 0x28
    	};

        public CryptoHandler(string passphrase)
        {
            byte[] passwordKey = encodeDigest(passphrase);
            RijndaelManaged rijndael = new RijndaelManaged();

            _Decryptor = rijndael.CreateDecryptor(passwordKey, _rawSecretKey);
            _Encryptor = rijndael.CreateEncryptor(passwordKey, _rawSecretKey);
        }

        /// <summary>
        /// Decrypt the data to plain text
        /// </summary>
        /// <param name="encryptedData">encrypted bytes</param>
        /// <returns></returns>
        public string Decrypt(byte[] encryptedData)
        {
            byte[] newClearData = _Decryptor.TransformFinalBlock(encryptedData, 0, encryptedData.Length);
            return Encoding.ASCII.GetString(newClearData);
        }

        /// <summary>
        /// Same is on Android...
        /// </summary>
        /// <param name="passPhrase">password key</param>
        /// <returns></returns>
        private byte[] encodeDigest(string passPhrase)
        {
            MD5CryptoServiceProvider x = new System.Security.Cryptography.MD5CryptoServiceProvider();
            byte[] data = Encoding.UTF8.GetBytes(passPhrase);
            return x.ComputeHash(data);
        }
    }
}
