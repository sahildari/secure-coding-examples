import com.amazonaws.regions.Regions;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import org.identityconnectors.common.security.GuardedString;

import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;

public class HeapInspectionGuardedStringExample {

    public static GuardedString getPasswordFromSecretsManager(String secretName) {
        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(Regions.US_EAST_1) // Change to your AWS region
                .build();

        GetSecretValueRequest request = new GetSecretValueRequest()
                .withSecretId(secretName);

        GetSecretValueResult result = client.getSecretValue(request);

        char[] passwordChars;
        if (result.getSecretBinary() != null) {
            // Convert binary secret to char array
            ByteBuffer secretBinary = result.getSecretBinary();
            byte[] secretBytes = new byte[secretBinary.remaining()];
            secretBinary.get(secretBytes);
            passwordChars = new String(secretBytes, StandardCharsets.UTF_8).toCharArray();

            // Overwrite the original byte array immediately
            overwriteByteArray(secretBytes);
        } else {
            // Fallback to getSecretString() (only use if necessary)
            passwordChars = result.getSecretString().toCharArray();

            // Overwrite the original string immediately
            overwriteString(result.getSecretString());
        }

        return new GuardedString(passwordChars);
    }

    private static void overwriteByteArray(byte[] bytes) {
        if (bytes != null) {
            java.util.Arrays.fill(bytes, (byte) 0); // Overwrite memory
        }
    }

    private static void overwriteString(String str) {
        if (str != null) {
            char[] chars = str.toCharArray();
            java.util.Arrays.fill(chars, '\0'); // Overwrite memory
        }
    }

    private static void clearSensitiveData(StringBuffer sb){
        sb.delete(0, sb.length());
        sb = null;
    }

    public static GuardedString usePasswordSecurely(GuardedString guardedPassword) {
        // Securely access the password value
        guardedPassword.access(chars -> {
            // Construct the connection string securely
            StringBuffer connectionString = new StringBuffer("jdbc:sqlserver://localhost:1433;encrypt=true;user=sa;password=")
                    .append(chars)
                    .append(";columnEncryptionSetting=Enabled;");
    
            System.out.println("Connection String: " + connectionString);
            // Perform operations with the connection string here

            // After performing operations, clear the sensitive data

            overwriteByteArray(chars); // Overwrite the password in memory
            clearSensitiveData(connectionString); // Clear the connection string from memory
        });
    }

    public static void main(String[] args) {
        GuardedString password = getPasswordFromSecretsManager("myDatabasePassword");

        try {
            usePasswordSecurely(password);
        } finally {
            password.dispose(); // Clears memory securely
        }
    }
}
