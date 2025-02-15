import java.io.*;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class DES3Encryption{
private static final int MAX_CHUNK_SIZE = 64 * 1024;
public static void main(String[] args) throws Exception {
String studentId = "0424312030";
byte[] key = generateKey(studentId);
System.out.println("3DES Key (Base64): " + Base64.getEncoder().encodeToString(key));
String[] inputFiles = {"1MB_file.txt", "100MB_file.txt", "1GB_file.txt"};
String[] encryptedFiles = {"3DES1MB_encrypted.txt", "3DES100MB_encrypted.txt",
"3DES1GB_encrypted.txt"};
String[] decryptedFiles = {"3DES1MB_decrypted.txt", "3DES100MB_decrypted.txt",
"3DES1GB_decrypted.txt"};
for (int i = 0; i < inputFiles.length; i++) {
String inputFile = inputFiles[i];
String encryptedFile = encryptedFiles[i];
String decryptedFile = decryptedFiles[i];
long startTime = System.nanoTime();
processFile(inputFile, encryptedFile, key, Cipher.ENCRYPT_MODE);
long endTime = System.nanoTime();
double encTime = (endTime - startTime) / 1_000_000_000.0;
System.out.println("Encryption time for " + inputFile + ": " + encTime + " seconds");
startTime = System.nanoTime();
processFile(encryptedFile, decryptedFile, key, Cipher.DECRYPT_MODE);
endTime = System.nanoTime();
double decTime = (endTime - startTime) / 1_000_000_000.0;
System.out.println("Decryption time for " + inputFile + ": " + decTime + " seconds");

}
}
public static byte[] generateKey(String studentId) {
byte[] key = studentId.getBytes();
byte[] paddedKey = new byte[24];
System.arraycopy(key, 0, paddedKey, 0, Math.min(key.length, 24));
Arrays.fill(paddedKey, key.length, 24, (byte) '0');
return paddedKey;
}
public static void processFile(String inputFile, String outputFile, byte[] key, int mode) throws
Exception {
SecretKeySpec secretKey = new SecretKeySpec(key, "DESede");
Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
cipher.init(mode, secretKey);
try (FileInputStream fis = new FileInputStream(inputFile);
FileOutputStream fos = new FileOutputStream(outputFile)) {
byte[] inputBuffer = new byte[MAX_CHUNK_SIZE];
int bytesRead;
while ((bytesRead = fis.read(inputBuffer)) != -1) {
byte[] outputBuffer = cipher.update(inputBuffer, 0, bytesRead);
if (outputBuffer != null) {
fos.write(outputBuffer);
}
}
byte[] finalBlock = cipher.doFinal();
if (finalBlock != null) {
fos.write(finalBlock);
}
}
}
}
