package user.util;

import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * jose的payLoad为String是jws
 * 为Gson是jwk
 */
@Component
public class TokenUtil {

//    private JwtConsumer jwtConsumer;
//
//    public Key getKey() {
//        String keyText = "iue98623diDEs096";
//        return new HmacKey(keyText.getBytes());
//    }
//
//    public String createToken() throws JoseException {
//        Key key = getKey();
//
//        JwtClaims claims = getJwtClaims();
//
//        JsonWebSignature jws = new JsonWebSignature();
//        jws.setPayload(claims.toJson());
//        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
//        jws.setKey(key);
//        jws.setDoKeyValidation(false);
//
//        String idToken = jws.getCompactSerialization();
//
//        return idToken;
//    }
//
//    public void parseToken(String token) throws InvalidJwtException {
//        JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
//    }
//
//    public void getConsumer() {
//        Key key = getKey();
//
//        jwtConsumer = new JwtConsumerBuilder()
////                .setRequireExpirationTime() // the JWT must have an expiration time
//                .setMaxFutureValidityInMinutes(300) // but the  expiration time can't be too crazy
//                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
//                .setVerificationKey(key) // verify the signature with the public key
//                .setRelaxVerificationKeyValidation()
//                .build(); // create the JwtConsumer instance
//    }
//
//    private JwtClaims getJwtClaims() {
//        JwtClaims claims = new JwtClaims();
//        claims.setExpirationTimeMinutesInTheFuture(1); // time when the token will expire (10 minutes from now)
//        claims.setGeneratedJwtId(); // a unique identifier for the token
//        claims.setIssuedAtToNow();  // when the token was issued/created (now)
//        claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
//        claims.setClaim("email", "mail@example.com"); // additional claims/attributes about the subject can be added
//        return claims;
//    }

    //todo暂时先不设置过期时间
        public Key getKey() {
//            String keyText = "443881c1851f4892ac730e792e80e9ad";
//            String keyText = "iue98623diDEs096";
            String keyText = "443881c1851f4892"; //貌似128只能用16位text
            Key key = new HmacKey(keyText.getBytes());

            return key;
    }

    public String createToken(String uid) throws JoseException {
        Key key = getKey();
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.PBES2_HS256_A128KW);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
//        jwe.setDoKeyValidation(false);
        jwe.setKey(key);
        jwe.setPayload(uid);

        String idToken = jwe.getCompactSerialization();

        return idToken;
    }

    public String parseToken(String token) throws JoseException {
        Key key = getKey();
        JsonWebEncryption jwe2 = new JsonWebEncryption();
        jwe2.setKey(key);
        jwe2.setCompactSerialization(token);

        String uid = jwe2.getPayload();

        return uid;
    }
}
