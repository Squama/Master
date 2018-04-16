package com.radish.master.system;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class GUID {

    // 共62个字符 0-9, A-Z, a-z, 支持 0-61的转换
    private static final char[] CHARACTER_TABLE = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
        'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    public static final int LEN_16 = 16;
    public static final int LEN_20 = 20;
    public static final int LEN_25 = 25;

    /**
     * 生成指定长度流水号, 流水号只包含数字
     * 
     * @param length
     *            流水号长度, 支持16,20,25
     * @return 指定长度的流水号
     * @throws Exception
     */
    public static String genTxNo(int length) throws CodeException {
        String txNo = null;
        switch (length) {
            case LEN_16:
                txNo = genTxNo(LEN_16, false);
                break;
            case LEN_20:
                txNo = genTxNo(LEN_20, false);
                break;
            case LEN_25:
                txNo = genTxNo(LEN_25, false);
                break;
            default:
                throw new CodeException("", "don't support length[" + length + "]");
        }
        return txNo;
    }

    /**
     * 生成指定长度流水号, 流水号可包含字母
     * <p>
     * 当流水号包含字母时, 字母不区分大小写
     * 
     * @param length
     *            流水号长度, 支持16,20,25
     * @param characterContained
     *            流水号是否包含字母: true包含, false不包含
     * @return 指定长度的流水号
     * @throws Exception
     */
    public static String genTxNo(int length, boolean characterContained) throws CodeException {
        String txNo = null;
        switch (length) {
            case LEN_16:
                if (characterContained) {
                    txNo = getTxNo16V2();
                } else {
                    txNo = getTxNo16();
                }
                break;
            case LEN_20:
                if (characterContained) {
                    txNo = getTxNoV2();
                } else {
                    txNo = getTxNo();
                }
                break;
            case LEN_25:
                if (characterContained) {
                    throw new CodeException("", "length [" + length + "] don't support generate txNo with charactes");
                } else {
                    txNo = getTxNo25();
                }
                break;
            default:
                throw new CodeException("", "don't support length[" + length + "]");
        }
        return txNo;
    }

    /**
     * 20位流水号
     * <p>
     * yyMMddHHmm+Random(10)
     */
    public static String getTxNo() {
        String timeString = new SimpleDateFormat("yyMMddHHmm").format(Calendar.getInstance().getTime());
        return timeString + RandomNumberGenerator.getRandomNumber(10);
    }

    /**
     * 16位流水号
     * <p>
     * yyMMdd+Random(10)
     */
    public static String getTxNo16() {
        String timeString = new SimpleDateFormat("yyMMdd").format(Calendar.getInstance().getTime());
        return timeString + RandomNumberGenerator.getRandomNumber(10);
    }

    /**
     * 20位流水号
     * <p>
     * yyyyMMddHHmmss(compressed with 9 digits) + 11 digits or characters(case
     * sensitive)
     * 
     * @throws Exception
     */
    private static String getTxNoV2() throws CodeException {
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        StringBuilder timeCoverted = new StringBuilder();
        // 转换
        timeCoverted.append(time.substring(0, 4))
                .append(convert(time.substring(4)))
                .append(RandomNumberGenerator.getRandomCharAndNumber(11, true));

        return timeCoverted.toString();
    }

    /**
     * 25位流水号
     * <p>
     * yyMMddHHmmssSSS+Random(10)
     */
    private static String getTxNo25() {
        String timeString = new SimpleDateFormat("yyMMddHHmmssSSS").format(Calendar.getInstance().getTime());
        return timeString + RandomNumberGenerator.getRandomNumber(10);
    }

    /**
     * 获取16位的流水号 版本2
     * <p>
     * yyMMddHHmmss(compressed with 7 digits) + 9 digits or characters(case
     * sensitive)
     */
    private static String getTxNo16V2() throws CodeException {
        String time = new SimpleDateFormat("yyMMddHHmmss").format(Calendar.getInstance().getTime());
        StringBuilder timeCoverted = new StringBuilder();
        // 转换
        timeCoverted.append(time.substring(0, 2))
                .append(convert(time.substring(2)))
                .append(RandomNumberGenerator.getRandomCharAndNumber(9, true));

        return timeCoverted.toString();
    }

    public static String convert(String time) throws CodeException {
        StringBuilder timeConverted = new StringBuilder();

        for (int index = 0; index < time.length(); index += 2) {
            int num = Integer.parseInt(time.substring(index, index + 2));
            timeConverted.append(convertNumber2Character(num));
        }

        return timeConverted.toString();
    }

    private static char convertNumber2Character(int num) throws CodeException {
        if (num > 61) {
            throw new CodeException("", "don't supprot digit larger than 61");
        }
        return CHARACTER_TABLE[num];
    }

}
