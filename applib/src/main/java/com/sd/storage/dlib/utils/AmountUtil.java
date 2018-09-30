package com.sd.storage.dlib.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/***
 * 金额
 * <p>
 * 如果需要精确计算，必须用String来够造BigDecimal！ ！！
 * <p>
 * Java里面的商业计算，不能用float和double，因为他们无法 进行精确计算。
 * 但是Java的设计者给编程人员提供了一个很有用的类BigDecimal，
 * 他可以完善float和double类无法进行精确计算的缺憾。
 * BigDecimal类位于java.maths类包下。
 * 它的构造函数很多，最常用的:
 * BigDecimal(double val)
 * BigDecimal(String str)
 * BigDecimal(BigInteger val)
 * BigDecimal(BigInteger unscaledVal, int scale)
 *
 * @author wxw
 */
public class AmountUtil {

    /***
     * 保留2位小数
     * 四舍五入
     *
     * @param doubleVal
     * @param scale
     * @return 返回一个double类型的2位小数
     */
    public static Double get2Double(Double doubleVal, int scale) {
        if (null == doubleVal) {
            doubleVal = new Double(0);
        }
        return new BigDecimal(doubleVal).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /***
     * 格式化Double类型并保留scale位小数
     * 四舍五入
     *
     * @param doubleVal
     * @param scale     scale必须为大于0的正整数，不能等于0
     * @return
     */
    public static String formatBy2Scale(Double doubleVal, int scale) {
        if (null == doubleVal) {
            doubleVal = new Double(0);
        }
        StringBuffer sbStr = new StringBuffer("0.");
        for (int i = 0; i < scale; i++) {
            sbStr.append("0");
        }
        DecimalFormat myformat = new DecimalFormat(sbStr.toString());
        return myformat.format(doubleVal);
    }

    /***
     * Double类型相加 +
     * <p>
     * ROUND_HALF_UP 四舍五入
     *
     * @param val1
     * @param val2
     * @param scale 保留scale位小数
     * @return
     */
    public static Double add(Double val1, Double val2, int scale) {
        if (null == val1) {
            val1 = new Double(0);
        }
        if (null == val2) {
            val2 = new Double(0);
        }
        return new BigDecimal(Double.toString(val1)).add(new BigDecimal(Double.toString(val2))).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /***
     * Double类型相减 —
     * <p>
     * ROUND_HALF_UP 四舍五入
     *
     * @param val1
     * @param val2
     * @param scale 保留scale位小数
     * @return
     */
    public static Double subtract(Double val1, Double val2, int scale) {
        if (null == val1) {
            val1 = new Double(0);
        }
        if (null == val2) {
            val2 = new Double(0);
        }
        return new BigDecimal(Double.toString(val1)).subtract(new BigDecimal(Double.toString(val2))).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /***
     * Double类型相乘 *
     * <p>
     * ROUND_HALF_UP 四舍五入
     *
     * @param val1
     * @param val2
     * @param scale 保留scale位小数
     * @return
     */
    public static Double multiply(Double val1, Double val2, int scale) {
        if (null == val1) {
            val1 = new Double(0);
        }
        if (null == val2) {
            val2 = new Double(0);
        }
        return new BigDecimal(Double.toString(val1)).multiply(new BigDecimal(Double.toString(val2))).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /***
     * Double类型相除 /
     * <p>
     * ROUND_HALF_UP 四舍五入
     *
     * @param val1
     * @param val2
     * @param scale 保留scale位小数
     * @return
     */
    public static Double divide(Double val1, Double val2, int scale) {
        if (null == val1) {
            val1 = new Double(0);
        }
        if (null == val2 || val2 == 0) {
            val2 = new Double(1);
        }
    //    return new BigDecimal(Double.toString(val1)).divide(new BigDecimal(Double.toString(val2))).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        return new BigDecimal(Double.toString(val1)).divide(new BigDecimal(Double.toString(val2)),scale,BigDecimal.ROUND_HALF_UP).doubleValue();

    }


    /***
     * Double类型取余    %
     * <p>
     * ROUND_HALF_UP 四舍五入
     *
     * @param val1
     * @param val2
     * @param scale 保留scale位小数
     * @return
     */
    public static int divideAndRemainder(Double val1, Double val2, int scale) {
        if (null == val1) {
            val1 = new Double(0);
        }
        if (null == val2 || val2 == 0) {
            val2 = new Double(1);
        }
        return new BigDecimal(Double.toString(val1)).divideAndRemainder(new BigDecimal(Double.toString(val2)))[1].setScale(scale, BigDecimal.ROUND_HALF_UP).intValue();
    }

    /***
     * 格式化Double类型数据
     *
     * @param val
     * @param fmt                   NumberFormat currency = NumberFormat.getCurrencyInstance(); //建立货币格式化引用
     *                              NumberFormat percent = NumberFormat.getPercentInstance(); //建立百分比格式化引用
     * @param maximumFractionDigits 如果是百分比 设置小数位数（四舍五入）
     * @return
     */
    public static String formatByNumberFormat(Double val, NumberFormat fmt, int maximumFractionDigits) {
        if (fmt.equals(NumberFormat.getPercentInstance())) {
            fmt.setMaximumFractionDigits(maximumFractionDigits); //百分比小数点最多3位
        }
        return fmt.format(val);

    }

    /***
     * 比较大小
     * -1、0、1，即左边比右边数大，返回1，相等返回0，比右边小返回-1。
     * @param val1
     * @param val2
     * @return
     */
    public static int compareTo(Double val1, Double val2) {
        if (null == val1) {
            val1 = new Double(0);
        }
        if (null == val2) {
            val2 = new Double(0);
        }
        return new BigDecimal(val1).compareTo(new BigDecimal(val2));
    }


    public static void main(String[] args) {

//    System.out.println(AmountUtil.get2Double(null,3));
//    System.out.println(AmountUtil.add(12.2155, null,4));
//    System.out.println(AmountUtil.subtract(12.2155, 1D,2));
//    System.out.println(AmountUtil.multiply(12.2155, 2D,2));
//    System.out.println(AmountUtil.divide(44.13, 2D,2));
//    System.out.println(AmountUtil.divideAndRemainder(43D, 8D,0));
//    System.out.println(AmountUtil.formatByNumberFormat(0.123456, NumberFormat.getPercentInstance(),3));
//    System.out.println(AmountUtil.formatBy2Scale(12.23457,3));


        DecimalFormat df = new DecimalFormat("0.00\u2030"); //"\u2030"表示乘以1000并显示为千分数
        System.out.println(df.format(12.1233)); //8-->1234567.89‰

        df = new DecimalFormat("0,000.0#");//在数字中添加逗号
        System.out.println(df.format(123456789.12345)); //5-->-1,234.57


        df = new DecimalFormat("0");//不保留小数点 四舍五入
        System.out.println(df.format(123456789.9876)); //5-->-1,234.57
    }
}