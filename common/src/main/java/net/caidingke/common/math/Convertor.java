package net.caidingke.common.math;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bowen
 * @create 2016-12-22 10:37
 */

public class Convertor {


    private static final Pattern REGEX = Pattern.compile("^(0|[1-9]\\d{0,9})(\\.?)(\\d{0,2}?)$");

    private static final String[] CHINESE = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

    private static final String[] INTEGER_UNIT = {"", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿"};

    private static final String[] FRACTION_UNIT = {"分", "角"};

    private static final String[] COMMON_UNIT = {"元", "整"};

    private static final int INTEGER = 1;

    private static final int FRACTION = 3;

    private Convertor() { }

    public static String convert(final BigDecimal amount) {
        Preconditions.checkNotNull(amount);
        return convert(amount.toString());
    }

    public static String convert(final String amount) {

        Matcher matcher = REGEX.matcher(amount);
        if (!matcher.find()) {
            throw new IllegalArgumentException("非法的参数!");
        }
        String integer = matcher.group(INTEGER);
        String fraction = matcher.group(FRACTION);

        StringBuffer chineseInteger = numberToChinese(integer);
        StringBuffer chineseFraction = numberToChinese(fraction);
        StringBuffer result = new StringBuffer();

        if (!isNullOrEmptyOrZero(integer)) {
            result = result.append(appendUnit(chineseInteger, true));
        }
        boolean flag = false;
        if (!isNullOrEmptyOrZero(fraction)) {
            flag = true;
            result = result.append(appendUnit(chineseFraction, false));
        }
        if (!flag) {
            result.append(COMMON_UNIT[1]);
        }
        return String.valueOf(result).replace("零分", "");
    }

    private static StringBuffer numberToChinese(final String number) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < number.length(); i++) {
            String index = String.valueOf(number.charAt(i));
            stringBuffer = stringBuffer.append(CHINESE[Integer.parseInt(index)]);
        }
        return stringBuffer;
    }

    private static StringBuffer appendUnit(StringBuffer amount, boolean integer) {
        String amountTemp = String.valueOf(amount);
        int i = 0;
        for (int j = amountTemp.length(); j > 0; j--) {
            if (integer) {
                amount = amount.insert(j, INTEGER_UNIT[i++]);
                if (j == 1) {
                    amount.append(COMMON_UNIT[0]);
                }
            } else {

                if (amountTemp.length() == 1) {
                    i++;
                }
                amount = amount.insert(j, FRACTION_UNIT[i++]);
            }
        }
        return amount;
    }

    private static boolean isNullOrEmptyOrZero(String fraction) {
        return Strings.isNullOrEmpty(fraction) || "00".equals(fraction) || "0".equals(fraction);
    }}
