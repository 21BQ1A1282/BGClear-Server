package com.sai.BGClear.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;

@Component
public class ClerkJwksProvider {  // Jwks - JSON Web Key Set

    @Value("${clerk.jwks-url}")
    private String jwksUrl;

    private final Map<String, PublicKey> keyCache = new HashMap<>();
    private long lastFetchTime = 0;
    private static final long CACHE_TTL = 3600000; // 1 hour in milliseconds

    public PublicKey getPublicKey(String kId) throws Exception {
        long currentTime = System.currentTimeMillis();
        if(keyCache.containsKey(kId) && (currentTime - lastFetchTime) < CACHE_TTL) {
            return keyCache.get(kId);
        } else {
            refreshKeys();
            return keyCache.get(kId);
        }
    }

    private void refreshKeys() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jwks = mapper.readTree(new URL(jwksUrl));

        JsonNode keys = jwks.get("keys");
        for(JsonNode keyNode: keys) {
            String kid = keyNode.get("kid").asText();
            String ktype = keyNode.get("kty").asText();
            String kalg = keyNode.get("alg").asText();

            if("RSA".equals(ktype) && "RS256".equals(kalg)) {
                String n = keyNode.get("n").asText();
                String e = keyNode.get("e").asText();

                PublicKey publicKey = createPublicKey(n, e);
                keyCache.put(kid, publicKey);
            }
        }
        lastFetchTime = System.currentTimeMillis();
    }

    private PublicKey createPublicKey(String modulus, String exponent) throws Exception {
        byte[] modBytes = Base64.getUrlDecoder().decode(modulus);
        byte[] expBytes = Base64.getUrlDecoder().decode(exponent);

        BigInteger mod = new BigInteger(1, modBytes);
        BigInteger exp = new BigInteger(1, expBytes);

        RSAPublicKeySpec spec = new RSAPublicKeySpec(mod, exp);
        KeyFactory factory = KeyFactory.getInstance("RSA");

        return factory.generatePublic(spec);
    }

}
