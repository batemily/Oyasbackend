package com.example.security.util;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class RandomPasswordGenerator {

    private static final String LOWER_CHAR = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_CHAR = LOWER_CHAR.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*()_+-=[]?";
    private static final String PASSWORD_BASE = LOWER_CHAR + UPPER_CHAR + NUMBER + OTHER_CHAR;
    private static final String PASSWORD_BASE_MORE = makeItMoreRandom(PASSWORD_BASE);
    private static final SecureRandom secureRandom = new SecureRandom();


    public static String generateRandomPassword(int passwordLength) {
        StringBuilder returnedPassword = new StringBuilder(passwordLength);
        if (passwordLength < 6) {
            throw new IllegalArgumentException("The password must contain at least 6 characters.");
        }
        for (int i = 0; i < passwordLength; i++) {
              int moreRandomValue = secureRandom.nextInt(PASSWORD_BASE_MORE.length());
              char moreRandomChar = PASSWORD_BASE_MORE.charAt(moreRandomValue);
              returnedPassword.append(moreRandomChar);
        }
        return returnedPassword.toString();

    }

    //if want to make it more random
    private static String makeItMoreRandom(String passwordBase) {
        List<String> letterList = Arrays.asList(passwordBase.split(""));
        Collections.shuffle(letterList);
        return letterList.stream().collect(Collectors.joining());

    }
}
