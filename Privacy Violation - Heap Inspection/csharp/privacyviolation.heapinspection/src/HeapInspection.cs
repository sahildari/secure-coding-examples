using System;
using System.Text;

public class HeapInspectionCharArrayExample
{
    public static void Main(string[] args)
    {
        Console.WriteLine("Before clearing sensitive data");
        /*
        Never Hardcode the passwords or any sensitive information in your code and use the Secret Managers or Secret Vaults to store the sensitive data. If this value is being populated from the Secret Managers or Secret Vaults, even then follow the same approach to clear the sensitive data.
        */

        char[] passwordChars = { 'Y', 'o', 'u', 'r', 'S', 't', 'r', 'o', 'n', 'g', 'P', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3', '!' };
        StringBuilder connectionString = new StringBuilder("jdbc:sqlserver://localhost:1433;encrypt=true;user=sa;password=").Append(passwordChars).Append("columnEncryptionSetting=Enabled;");

        Console.WriteLine("Password: ");
        foreach (char c in passwordChars)
        {
            Console.Write(c);
        }
        Console.WriteLine("");
        Console.WriteLine("Connection String: " + connectionString);

        /*
        Codebase to do some operation with the sensitive data
        */

        // Clear the password and connection string from memory
        ClearSensitiveData(passwordChars);
        ClearSensitiveData(connectionString);

        Console.WriteLine("");
        Console.WriteLine("After clearing sensitive data");
        Console.WriteLine("Password: ");
        foreach (char c in passwordChars)
        {
            Console.Write(c);
        }
        Console.WriteLine("");
        Console.WriteLine("Connection String: " + connectionString);
    }

    private static void ClearSensitiveData(char[] arr)
    {
        for (int i = 0; i < arr.Length; i++)
        {
            arr[i] = '\0';
        }
        arr = null;
    }

    private static void ClearSensitiveData(StringBuilder sb)
    {
        sb.Clear();
        sb = null;
    }
}