package com.radish.master.system;

import java.util.Random;

/**
 * 
 * <pre>
 * Modify Information:
 * Author       Date        Description
 * ============ =========== ============================
 * dongyan      2017-11-02  Create this file
 * </pre>
 * 
 */
public class RandomNumberGenerator {

    private static Random random = new Random();

    private static final char[] CHARACTER_TABLE = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

    public static String getRandomNumber(int length) {
        StringBuilder sb = new StringBuilder();

        do {
            long randomLong = random.nextLong();
            while (randomLong == Integer.MIN_VALUE || randomLong == Long.MIN_VALUE) {
                randomLong = random.nextLong();
            }

            sb.append(Math.abs(randomLong));
        } while (sb.length() < length);

        return sb.substring(0, length);
    }

    /**
     * 获取数字加字母随机组合的字符串
     * 
     * @param length
     *            字符串长度
     * @param upperCaseSupported
     *            字符串中是否支持大写字母 true:包含大小写字母 false:只有小写字母
     * @return
     */
    public static String getRandomCharAndNumber(int length, boolean upperCaseSupported) {
        StringBuilder rsb = new StringBuilder();
        if (upperCaseSupported) {
            for (int i = 0; i < length; i++) {
                rsb.append(CHARACTER_TABLE[random.nextInt(62)]);
            }
        } else {
            for (int i = 0; i < length; i++) {
                rsb.append(CHARACTER_TABLE[random.nextInt(36)]);
            }
        }
        return rsb.toString();
    }

}
