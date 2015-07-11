using Cipher.NET.Handlers;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Cipher.NET
{
    class Program
    {
        static CryptoHandler _crypto;

        static void Main(string[] args)
        {
            _crypto = new CryptoHandler(CryptoHandler.PASSWORD);

            Console.Write(DesypherFile());
            Console.ReadLine();
        }

        private static string DesypherFile()
        {
            string result = null;
            byte[] bytes = File.ReadAllBytes(@"../../Files/classified_file.txt");
            result = _crypto.Decrypt(bytes);
            return result;
        }
    }
}
