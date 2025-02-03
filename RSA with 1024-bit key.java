import java.io.*;
import java.security.*;
import java.util.Base64;
import javax.crypto.Cipher;
public class RSA1024Encryption {
public static void main(String[] args) throws Exception {
String studentId = "0424312030";
KeyPair keyPair = generateRSAKeyPair(studentId);
PublicKey publicKey = keyPair.getPublic();
PrivateKey privateKey = keyPair.getPrivate();
System.out.println("Public Key: " +
Base64.getEncoder().encodeToString(publicKey.getEncoded()));
System.out.println("Private Key: " +
Base64.getEncoder().encodeToString(privateKey.getEncoded()));
String[] inputFiles = { "1MB_file.txt", "100MB_file.txt", "1GB_file.txt" };
String[] encryptedFiles = { "RSA_1MB_encrypted.txt", "RSA_100MB_encrypted.txt",
"RSA_1GB_encrypted.txt" };
String[] decryptedFiles = { "RSA_1MB_decrypted.txt", "RSA_100MB_decrypted.txt",
"RSA_1GB_decrypted.txt" };
for (int i = 0; i < inputFiles.length; i++) {
String inputFile = inputFiles[i];
String encryptedFile = encryptedFiles[i];
String decryptedFile = decryptedFiles[i];
long startTime = System.nanoTime();
encryptFileWithRSA(inputFile, publicKey, encryptedFile);
long endTime = System.nanoTime();
double encTime = (endTime - startTime) / 1_000_000_000.0;
System.out.println("Encryption time for " + inputFile + ": " + encTime + " seconds");
startTime = System.nanoTime();

decryptFileWithRSA(encryptedFile, privateKey, decryptedFile);
endTime = System.nanoTime();
double decTime = (endTime - startTime) / 1_000_000_000.0;
System.out.println("Decryption time for " + inputFile + ": " + decTime + " seconds");
}
}
public static KeyPair generateRSAKeyPair(String studentId) throws Exception {
byte[] seed = studentId.getBytes();
SecureRandom secureRandom = new SecureRandom(seed);
KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
keyGen.initialize(1024, secureRandom);
return keyGen.generateKeyPair();
}
public static void encryptFileWithRSA(String inputFile, PublicKey publicKey, String
outputFile) throws Exception {
Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
try (FileInputStream fis = new FileInputStream(inputFile);
FileOutputStream fos = new FileOutputStream(outputFile)) {
byte[] inputBytes = new byte[62];
int bytesRead;
while ((bytesRead = fis.read(inputBytes)) != -1) {
byte[] encryptedData = cipher.doFinal(inputBytes, 0, bytesRead);
fos.write(encryptedData);
}
}
}
public static void decryptFileWithRSA(String inputFile, PrivateKey privateKey, String
outputFile) throws Exception {
Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
cipher.init(Cipher.DECRYPT_MODE, privateKey);
try (FileInputStream fis = new FileInputStream(inputFile);
FileOutputStream fos = new FileOutputStream(outputFile)) {
byte[] inputBytes = new byte[128];
int bytesRead;
while ((bytesRead = fis.read(inputBytes)) != -1) {
byte[] decryptedData = cipher.doFinal(inputBytes, 0, bytesRead);
fos.write(decryptedData);
}
}
}
}
