package com.school.uniform.util;

import java.util.Random;
import java.util.stream.Collectors;

public class RandomUtil {
    public static final String NUMBERS = "0123456789";
    public static final String UPPER_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final Random random = new Random();

    public static String randomNumbers(int len) {
        return random.ints(len, 0, 9).mapToObj(i -> String.valueOf(i)).collect(Collectors.joining(""));
    }
}
