using System;
using System.IO;
using System.Runtime.InteropServices;
using System.Text.Json;
using System.Security;
using Amazon;
using Amazon.SecretsManager;
using Amazon.SecretsManager.Model;

public class HeapInspectionSecureStringExample
{
    public static SecureString GetPasswordFromSecretsManager(string secretName)
    {
        using (var client = new AmazonSecretsManagerClient(RegionEndpoint.USEast1)) // Change to your AWS region
        {
            var request = new GetSecretValueRequest { SecretId = secretName };
            GetSecretValueResponse result = client.GetSecretValueAsync(request).Result;

            SecureString securePassword = new SecureString();

            if (result.SecretBinary != null)
            {
                // Handling binary secrets
                byte[] decodedBinarySecret = Convert.FromBase64String(result.SecretBinary.ToString());
                string secretString = System.Text.Encoding.UTF8.GetString(decodedBinarySecret);
                ExtractPasswordToSecureString(secretString, securePassword);
            }
            else
            {
                // Handling JSON or plaintext secrets
                ExtractPasswordToSecureString(result.SecretString, securePassword);
            }

            // Overwrite the original string immediately
            OverwriteString(result.SecretString);

            return securePassword;
        }
    }

    private static void ExtractPasswordToSecureString(string secret, SecureString securePassword)
    {
        if (!string.IsNullOrEmpty(secret))
        {
            try
            {
                var secretJson = JsonSerializer.Deserialize<JsonElement>(secret);
                if (secretJson.TryGetProperty("password", out JsonElement passwordElement))
                {
                    string password = passwordElement.GetString();
                    foreach (char c in password)
                    {
                        securePassword.AppendChar(c);
                    }
                    securePassword.MakeReadOnly(); // Secure the SecureString
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error parsing secret JSON: " + ex.Message);
            }
        }
    }

    private static void OverwriteString(string str)
    {
        if (!string.IsNullOrEmpty(str))
        {
            char[] chars = str.ToCharArray();
            for (int i = 0; i < chars.Length; i++)
            {
                chars[i] = '\0'; // Overwrite memory
            }
        }
    }

    public static void UseSecureString(SecureString securePassword)
{
    IntPtr ptr = Marshal.SecureStringToGlobalAllocUnicode(securePassword);
    try
    {
        string password = Marshal.PtrToStringUni(ptr); // Extract password safely

        // Construct connection string
        StringBuilder connectionString = new StringBuilder("jdbc:sqlserver://localhost:1433;encrypt=true;user=sa;password=")
            .Append(password)
            .Append(";columnEncryptionSetting=Enabled;");

        Console.WriteLine("Connection String: " + connectionString);

        // Overwrite password in memory
        OverwriteString(password);
    }
    finally
    {
        Marshal.ZeroFreeGlobalAllocUnicode(ptr); // Clears memory securely
    }
}

    public static void Main(string[] args)
    {
        SecureString securePassword = GetPasswordFromSecretsManager("MyDatabaseSecret");

        try
        {
            UseSecureString(securePassword);
        }
        finally
        {
            securePassword.Dispose(); // Clears memory securely
        }
    }
}