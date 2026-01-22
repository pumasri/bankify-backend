package seniorproject.bankifycore.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

//Generate raw key → show to client (ONCE)
//        ↓
//Hash with pepper → store in DB
//        ↓
//Client sends raw key
//        ↓
//Hash again → compare with DB

public class ApiKeyUtils {

    private static final SecureRandom RNG = new SecureRandom();

    private ApiKeyUtils() {}

    public static String generateRawKey(){
        byte[] bytes = new byte[32]; //256-bit
        RNG.nextBytes(bytes);
        return toHex(bytes);
    }

    public static String hash(String rawKey,String pepper){
        return sha256(rawKey+pepper);
    }

    private static String sha256(String value){
            try{
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
                return toHex(digest);
            } catch (Exception e) {
                throw new RuntimeException("SHA-256 not available",e);
            }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }


}
