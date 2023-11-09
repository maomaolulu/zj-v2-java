package may.yuntian.anlian.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.regex.Pattern;

public class Calculation {
	
	
	/**
	 * 曲线方程格式转换 y=ax+b -> x=(y-b)/a
	 * @param curveEquation
	 * @return
	 */
	public static String curveEquation(String x,String y,String curveEquation) {
//		String aString = "y=0.1646x+0.0022";
		String a1String = curveEquation.replace("+", "@");
		String[] aStringStr = a1String.split("@");
		String b = aStringStr[1];
		String a2String = aStringStr[0];
		String a2 = a2String.replace("x", "");
		String a3 = a2.replace("=", "@");
		String[] a4 = a3.split("@");
		String a = a4[1];
		
		return x+"="+"("+y+"-"+b+")"+"/"+a;
	}
	
    /**
     * 判断一个对象中的所有属性是否都为空
     * @param object
     * @return
     */
    public static boolean chackObjAllfieldsIsNull(Object object) {
		if (object == null) {
			return true;
		}
		try {
			for(Field f : object.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				if (f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return true;
	}

    /**
     * @param value 需要科学计算的数据
     * @param digit 保留的小数位
     * @return
     * 功能：四舍六入五成双计算法
     */
    public static String sciCal(double value, int digit){
        String result = "-999";
        try {
            double ratio = Math.pow(10, digit);
            double _num = value * ratio;
            double mod = _num % 1;
            double integer = Math.floor(_num);
            double returnNum;
            if(mod > 0.5){
                returnNum=(integer + 1) / ratio;
            }else if(mod < 0.5){
                returnNum=integer / ratio;
            }else{
                returnNum=(integer % 2 == 0 ? integer : integer + 1) / ratio;
            }
            BigDecimal bg = new BigDecimal(returnNum);
//            System.out.println(bg);
            if (bg.compareTo(BigDecimal.ZERO)==0){
//                System.out.println("=-=-=");
                result = String.valueOf(value);
            }else {
//                System.out.println("===---");
                result = bg.setScale((int)digit, BigDecimal.ROUND_HALF_UP).toString();
            }

        } catch (RuntimeException e) {
            throw e;
        }
        return result;
    }

    /**
     * 判断一个字符串是否为整数
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 获取小数字符串的小数位数
     * @param string
     * @return
     */
    public static int decimalDigit(String string){
        String[] b = string.split("\\.");
        int a = b[1].length();
        return a;
    }
	
}
