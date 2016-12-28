package net.caidingke.common.math;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * number to chinese convert
 *
 * @author bowen
 * @create 2016-12-23 15:46
 */

public class NumberConvertUtils {

    private static final Pattern REGEX = Pattern.compile("^(0|[1-9]\\d{0,9})(\\.?)(\\d{0,2}?)$");

    private static final char[] CHINESE = "零壹贰叁肆伍陆柒捌玖".toCharArray();

    private static final String[] U1 =  {"", "拾", "佰", "仟"};

    private static final String[] U2 = {"", "万", "亿"};

    private static final String[] COMMON_UNIT = {"元", "角", "分", "整"};

    private static final int INTEGER = 1;

    private static final int FRACTION = 3;

    public static String transform(final BigDecimal amount) {
        Preconditions.checkNotNull(amount, "must be not null");
        return transform(amount.toString());
    }

    public static String transform(final String amount) {
        Preconditions.checkNotNull(amount, "must be not null");
        if (amount.equals("0") || amount.equals("0.0") || amount.equals("0.00")) {
            return "零元整";
        }
        Matcher matcher = REGEX.matcher(amount);
        if (!matcher.find()) {
            throw new IllegalArgumentException("非法的参数!");
        }
        String integer = matcher.group(INTEGER);
        String fraction = matcher.group(FRACTION);

        String result = "";

        if (!isNullOrEmptyOrZero(integer)) {
            result += integer2rmb(integer) + COMMON_UNIT[0];
        }
        if (isNullOrEmptyOrZero(fraction)) {
            result += COMMON_UNIT[3]; // 添加[整]
        }else if (fraction.startsWith("0") && integer.equals("0")){
            result += fraction2rmb(fraction).substring(1); // 去掉分前面的[零]
        }else{
            result += fraction2rmb(fraction); // 小数部分
        }
        return result;
    }
    private static boolean isNullOrEmptyOrZero(String fraction) {
        return Strings.isNullOrEmpty(fraction) || "00".equals(fraction) || "0".equals(fraction);
    }
    private static String fraction2rmb(String fraction) {
        char corner = fraction.charAt(0); // 角
        char cent  = fraction.charAt(1); // 分
        return (CHINESE[corner - '0'] + (corner > '0' ? COMMON_UNIT[1] : ""))
                + (cent > '0' ? CHINESE[cent - '0'] + COMMON_UNIT[2] : "");
    }

    private static String integer2rmb(String integer) {
        StringBuilder buffer = new StringBuilder();
        // 从个位数开始转换
        int i, j;
        for (i = integer.length() - 1, j = 0; i >= 0; i--, j++) {
            char n = integer.charAt(i);
            if (n == '0') {
                // 当n是0且n的右边一位不是0时，插入[零]
                if (i < integer.length() - 1 && integer.charAt(i + 1) != '0') {
                    buffer.append(CHINESE[0]);
                }
                // 插入[万]或者[亿]
                if (j % 4 == 0) {
                    if (i > 0 && integer.charAt(i - 1) != '0'
                            || i > 1 && integer.charAt(i - 2) != '0'
                            || i > 2 && integer.charAt(i - 3) != '0') {
                        buffer.append(U2[j / 4]);
                    }
                }
            } else {
                if (j % 4 == 0) {
                    buffer.append(U2[j / 4]);     // 插入[万]或者[亿]
                }
                buffer.append(U1[j % 4]);         // 插入[拾]、[佰]或[仟]
                buffer.append(CHINESE[n - '0']); // 插入数字
            }
        }
        return buffer.reverse().toString();
    }

}
