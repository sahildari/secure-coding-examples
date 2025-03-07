public class HeapInspectionCharArrayExample {
    public static void main(String[] args) {

        System.out.println("Before clearing sensitive data");
        /*
        Never Hardcode the passwords or any sensitive information in your code and user the Secret Managers or Secret Vaults to store the sensitive data. If this value is being populated from the Secret Managers or Secret Vaults, even then follow the same approach to clear the sensitive data.
         */
        
        char[] passwordChars = {'Y', 'o', 'u', 'r', 'S', 't', 'r', 'o', 'n', 'g', 'P', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3', '!'}; 
        StringBuffer connectionString = new StringBuffer("jdbc:sqlserver://localhost:1433;encrypt=true;user=sa;password=").append(passwordChars).append("columnEncryptionSetting=Enabled;");

        System.out.println("Password: ");
        for(char c : passwordChars) {
            System.out.print(c);
        }
        System.out.println("");
        System.out.println("Connection String: " + connectionString);

        /*
        Codebase to do some operation with the sensitive data
        */

        // Clear the password and connection string from memory
        clearSensitiveData(passwordChars);
        clearSensitiveData(connectionString);

        System.out.println("");
        System.out.println("After clearing sensitive data");
        System.out.println("Password: ");
        for(char c : passwordChars) {
            System.out.print(c);
        }
        System.out.println("");
        System.out.println("Connection String: " + connectionString);
    }

    private static void clearSensitiveData(char[] arr){
        for(int i = 0; i < arr.length; i++) {
            arr[i] = '\0';
        }
        arr = null;
    }

    private static void clearSensitiveData(StringBuffer sb){
        sb.delete(0, sb.length());
        sb = null;
    }
}