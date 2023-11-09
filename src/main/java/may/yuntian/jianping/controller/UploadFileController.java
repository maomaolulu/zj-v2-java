package may.yuntian.jianping.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.utils.AnlianConfig;
import may.yuntian.anlian.utils.FileUploadUtils;
import may.yuntian.anlian.utils.FileUtils;
import may.yuntian.anlian.utils.MimeTypes;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.R;
import may.yuntian.sys.utils.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

/**
 * 文件上传管理
 * WEB请求处理层
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-09-17
 */
@RestController
@Api(tags="文件上传接口,支持：工艺流程图、设备布局测点布置图、采样影像记录")
@RequestMapping("anlian/uploadFile")
public class UploadFileController {
    @Autowired
    private AnlianConfig anlianConfig;

    /**
     * 上传图片接口
     */
    @PostMapping("/uploadImage")
    @SysLog("上传图片接口")
    @ApiOperation("上传图片接口")
    @RequiresPermissions("zj:uploadFile:uploadImage")
//    @RequiresPermissions("anlian:uploadFile:image")
    public Result uploadImage(HttpServletRequest request, String module, MultipartFile file){
//    	System.out.println("上传工艺流程图getModule："+module);
//        String uploadPath = "D:\\new_java\\new_zj_java\\uploadFile"+File.separator+module;  //本地
        String uploadPath = "/opt/module/apache-tomcat-9.0.37/webapps/uploadFile"+File.separator+module;  //线上

    	//String uploadPath = anlianConfig.filePath+anlianConfig.craftProcessPath;//获取工艺流程图上传路径
//    	String uploadPath = FileUtils.getAbsPathOfProject(request);	// Windows获取当前项目路径：D:\\Tomcat9.0\\webapps\\anlian\\
    	//项目部署在tomcat下为anlian的文件夹，此时替换为uploadFile,目的是项目与资源文件分开
    	//比如后台项目访问路径是http://81.68.89.219/anlian，前端请求图片路径为http://81.68.89.219/uploadFile/craftProcess/**.jpg
//    	uploadPath = uploadPath.replace("anlian_zj", "uploadFile")+File.separator+module;	//module文件存储的模块名称
    	
//    	System.out.println("上传工艺流程图路径1："+uploadPath);
//    	System.out.println("上传工艺流程图路径2：filePath="+anlianConfig.filePath+"	craftProcessPath="+anlianConfig.craftProcessPath);

    	//获取上传文件的路径
//        String path1 = session.getServletContext().getRealPath("photo");
//        System.out.println("session获取上传文件的路径："+path1);
    	
    	if(file.isEmpty()) {
    		return Result.error("上传工艺流程图为空！");
    	}else {
    		String path = null;
            try {
                path = FileUploadUtils.upload(uploadPath, file, MimeTypes.IMAGE_EXTENSION);
                //事例数据	uploadFile/craftProcess/9755686cdeda569eb27c664f82a8fabc.png
                //页面访问时请求地址为http://81.68.89.219/uploadFile/craftProcess/9755686cdeda569eb27c664f82a8fabc.png
                String retPath = "uploadFile/"+module+"/"+path;
//                System.out.println("上传工艺流程图路径返回值："+retPath);
                return Result.ok().put("uploadPath", retPath);
            } catch (IOException e) {
                return Result.error(e.getLocalizedMessage());
            }
    	}
    }
    
    /**
     * 上传文件接口
     */
    @PostMapping("/uploadFile")
    @SysLog("上传文件接口")
    @ApiOperation("上传文件接口，支持图片、文档、压缩文件等")
//    @RequiresPermissions("anlian:uploadFile:file")
    public Result uploadFile(HttpServletRequest request,String module,MultipartFile file){
//    	String uploadPath = FileUtils.getAbsPathOfProject(request);	// Windows获取当前项目路径：D:\\Tomcat9.0\\webapps\\anlian\\
    	//项目部署在tomcat下为anlian的文件夹，此时替换为uploadFile,目的是项目与资源文件分开
    	//比如后台项目访问路径是http://81.68.89.219/anlian，前端请求图片路径为http://81.68.89.219/uploadFile/craftProcess/**.jpg
//    	uploadPath = uploadPath.replace("anlian_zj", "uploadFile")+File.separator+module;	//module文件存储的模块名称

//        String uploadPath = "D:\\new_java\\new_zj_java\\uploadFile"+File.separator+module;  //本地
        String uploadPath = "/opt/module/apache-tomcat-9.0.37/webapps/uploadFile"+File.separator+module;  //线上

    	if(file.isEmpty()) {
    		return Result.error("上传的文件不能为空！");
    	}else {
    		String path = null;
    		try {
    			path = FileUploadUtils.upload(uploadPath, file, MimeTypes.DEFAULT_ALLOWED_EXTENSION);
    			//事例数据	uploadFile/craftProcess/9755686cdeda569eb27c664f82a8fabc.png
    			//页面访问时请求地址为http://81.68.89.219/uploadFile/craftProcess/9755686cdeda569eb27c664f82a8fabc.png
    			String retPath = "uploadFile/"+module+"/"+path;
    			return Result.ok().put("uploadPath", retPath);
    		} catch (IOException e) {
    			return Result.error(e.getLocalizedMessage());
    		}
    	}
    }
    
    /**
     * 上传工艺流程图路径获取测试
     */
    @PostMapping("/getUploadPath")
    @ApiOperation("上传工艺流程图路径获取测试")
    public Result getUploadPath(HttpServletRequest request){
    	String uploadPath ;//= anlianConfig.filePath+anlianConfig.craftProcessPath;//获取工艺流程图上传路径
    	uploadPath = "uploadFile"+System.getProperty("file.separator")+anlianConfig.craftProcessPath;
    	
    	String absPathOfProject = FileUtils.getAbsPathOfProject(request);	// Windows获取当前项目路径：D:\\Tomcat9.0\\webapps\\anlian\\
    	System.out.println("获取当前项目路径getServletPath："+request.getServletPath());	//	/anlian/crafprocess/getUploadPath
    	System.out.println("获取当前项目路径getContextPath："+request.getContextPath());	//	/anlian
    	
    	
    	System.out.println("springboot获取当前项目路径的地址："+System.getProperty("user.dir"));	//	Windows D:\\Tomcat9.0\\bin
    	System.out.println("获取当前项目路径："+absPathOfProject);
    	
   
    	return Result.ok("获取当前项目路径："+absPathOfProject).put("springboot获取当前项目路径的地址：", System.getProperty("user.dir"));
    	
    }

}
