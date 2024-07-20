package com.btrsystem.btrsystem.payload.token;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBlacklist {

    // Set to store invalidated JWT tokens
    private static Set<String> invalidatedTokens = ConcurrentHashMap.newKeySet();

    // Add token to the blacklist
    public static void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }

    // Check if token is blacklisted
    public static boolean isTokenInvalid(String token) {
        return invalidatedTokens.contains(token);
    }
    
}