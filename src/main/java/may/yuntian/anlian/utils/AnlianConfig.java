package may.yuntian.anlian.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 安联检测相关的配置
 * 包含文件上传、下载的路径信息
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020年09月13日
 */
@Configuration
public class AnlianConfig {
	/**
	 * 文件上传、下载的存储路径
	 */
	@Value("${anlian.path.file-path: /home/anliandata/}")
	public String filePath;
	
	/**
	 * 工艺流程图路径
	 */
	@Value("${anlian.path.prefix.craftProcess: /home/anliandata/craftProcess/}")
	public String craftProcessPath;
	
	/**
	 * 设备布局测点布置图路径
	 */
	@Value("${anlian.path.prefix.equipment: /home/anliandata/equipment/}")
	public String equipment;
	
    
	/**
     * 获取工艺流程图上传路径
     * @return
     */
//    public static String getCraftProcessUploadPath() {
//    	System.out.println("文件上传、下载的存储路径:"+AnlianConfig.filePath+" 工艺流程图路径:"+AnlianConfig.craftProcessPath);
//    	System.out.println("设备布局测点布置图路径:"+(new AnlianConfig()).equipment);
//        return AnlianConfig.filePath + AnlianConfig.craftProcessPath;
//    }
    
//	# 项目相关配置
//	anlian:
//	  # 文件上传、下载的存储路径
//	  path:
//	    file-path: /Users/mayong/develop/anlian/
//	    prefix:
//	      #工艺流程图路径
//	      craftProcess: craftProcess/
//	      #设备布局测点布置图路径
//	      equipment: equipment/
//	      download: download/
//	      upload: upload/

}
