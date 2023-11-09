package may.yuntian.anlian.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


/**
 * 字符串格式化
 * 样品编号格式化转换
 *
 * @author MaYong
 */
public class AnlianFormatter {
	
	private static final Logger log = LoggerFactory.getLogger(AnlianFormatter.class);
	
	/**
	 * 将采样计划中的样品编号转换成数组供单个样品使用
	 * @param sampleCode 样品编号
	 * @param sampleNum 样品数量
	 * @return
	 */
	public static String[] formatSampleCode(String sampleCode,Integer sampleNum) {
		//String sampleCode = "ZJ2088317-[01,02,03]";//样品编号
		String[] codes=null;
		if(StringUtils.isNotBlank(sampleCode)) {
			int s1=sampleCode.indexOf("[");
			int s2=sampleCode.indexOf("]");
			//System.out.println("s1="+s1+",s2="+s2);//s1=11,s2=20
			String code1=null;
			String code2=null;
			if(s1>0 && s2>0) {
				code1 = sampleCode.substring(0, s1);
				code2 = sampleCode.substring(s1+1, s2);
//				System.out.println("code1="+code1+" ,code2="+code2);//code1=ZJ20196200- ,code2=01,02,03
				
				codes = code2.split(","); // 用,分割
				
				for(int i=0;i<codes.length;i++) {
					codes[i] = code1 + codes[i];
				}
				
				if(codes.length!=sampleNum) {
					log.error("将采样计划中的样品编号转换成数组数量不符合规范codes.length="+codes.length+" ,sampleNum="+sampleNum);
				}
			}else {
				codes = new String[]{sampleCode};
			}
			
//			log.evInfo("将采样计划中的样品编号转换成数组codes="+Arrays.toString(codes));
		}else {
			log.error("采样计划样品编号前缀不能为空！");
		}
		return codes;
	}
    
	/**
	 * 验证采样计划中的样品编号是否规范
	 * ZJ2088317-[01,02,03]
	 * @param sampleCode 样品编号
	 * @param sampleNum 样品数量
	 * @return
	 */
	public static boolean valiSampleCode(String sampleCode,Integer sampleNum) {
		//String sampleCode = "ZJ2088317-[01,02,03]";//样品编号
		String[] codes=null;
		boolean retFal = true;
		if(StringUtils.isNotBlank(sampleCode)) {
			int s1=sampleCode.indexOf("[");
			int s2=sampleCode.indexOf("]");
			log.info("s1="+s1+",s2="+s2);
			String code1=null;
			String code2=null;
			if(s1>0 && s2>0) {
				code1 = sampleCode.substring(0, s1);
				code2 = sampleCode.substring(s1+1, s2);
				log.info("code1="+code1+" ,code2="+code2);
				
				codes = code2.split(","); // 用,分割
				
				if(codes.length!=sampleNum) {
					log.error("将采样计划中的样品编号转换成数组数量不符合规范codes.length="+codes.length+" ,sampleNum="+sampleNum);
					retFal = false;
				}
			}else {
				if(sampleNum>1)
					retFal = false;
			}
			
			log.info("将采样计划中的样品编号转换成数组codes="+Arrays.toString(codes));
		}else {
			log.error("采样计划样品编号前缀不能为空！");
			retFal = false;
		}
		
		return retFal;
	}
	
	
}
