package may.yuntian;

import may.yuntian.common.exception.RRException;
import may.yuntian.untils.FreemarkerDocxUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) throws UnknownHostException {
//        //获取当前文件所在的路径
//        URL localPath = FreemarkerDocxUtil.class.getResource("");
//        System.out.println("1AppMain.class.getResource(\"\").getPath() = " + (localPath == null ? "NullPointerException" : localPath.getPath()));
//
//        //获取当前文件所在的路径
//        URL localPath2 = FreemarkerDocxUtil.class.getResource("DeclareIndex.ftl");
//        System.out.println("2AppMain.class.getResource(\"a.json\").getPath() = "  + (localPath2 == null ? "NullPointerException" : localPath2.getPath()));
//
//        //获取项目根目录
//        URL rootPath = FreemarkerDocxUtil.class.getResource("/");
//        System.out.println("3AppMain.class.getResource(\"/\").getPath() = "  + (rootPath == null ? "NullPointerException" : rootPath.getPath()));
//
//        URL rootPath2 = FreemarkerDocxUtil.class.getResource("/DeclareIndex.ftl");
//        System.out.println("4AppMain.class.getResource(\"/a.json\").getPath() = "  + (rootPath2 == null ? "NullPointerException" : rootPath2.getPath()));
//
//
//        URL cl = FreemarkerDocxUtil.class.getClassLoader().getResource("");
//
//        URL cl2 = FreemarkerDocxUtil.class.getClassLoader().getResource("DeclareIndex.ftl");
//
//        URL cl3 = FreemarkerDocxUtil.class.getClassLoader().getResource("/");
//
//        System.out.println("5AppMain.class.getClassLoader().getResource(\"\").getPath() = " + (cl == null ? "NullPointerException" : cl.getPath()));
//        System.out.println("6AppMain.class.getClassLoader().getResource(\"a.json\").getPath() = " + (cl2 == null ? "NullPointerException" : cl2.getPath()));
//        System.out.println("7AppMain.class.getClassLoader().getResource(\"/\").getPath() = " + (cl3 == null ? "NullPointerException" : cl3.getPath()));
//
//
//
//        String f = new File("").getPath();
//        System.out.println("8new File(\"\").getPath() = " + f);
//
//        //当前工程的绝对路径
//        String absolutePath1 = new File("").getAbsolutePath();
//        System.out.println("9new File(\"\").getAbsolutePath() = " + absolutePath1);
//
//        String absolutePath2 = new File("DeclareIndex.ftl").getAbsolutePath();
//        System.out.println("10new File(\"a.json\").getAbsolutePath() = " + absolutePath2);
//
//
//        String absolutePath3 = new File("/").getAbsolutePath();
//        System.out.println("11new File(\"/\").getAbsolutePath() = " + absolutePath3);
//
//        String absolutePath4 = new File("/DeclareIndex.ftl").getAbsolutePath();
//        System.out.println("12new File(\"/a.json\").getAbsolutePath() = " + absolutePath4);
//
//        //获取工程目录
//        File abc = new File("a.json");
//        System.out.println("13new File(\"a.json\").getPath() = " + abc.getPath());
//
//        //获取绝对路径
//        String absolutePath = abc.getAbsolutePath();
//        System.out.println("14new File(\"a.json\").getAbsolutePath() = " + absolutePath);
//
//
//        //获取相对路径
//        String path = abc.getPath();
//        System.out.println("15new File(\"a.json\").getPath() = " + path);
//
//        System.out.println("16new File(\"a.json\").exists() = " + abc.exists());
//
//        // 获取工程路径
//        String sp1 = System.getProperty("user.dir");
//        System.out.println("17System.getProperty(\"user.dir\") = " + sp1);


//        InetAddress ip = InetAddress.getLocalHost();
//        System.out.println(ip.getHostAddress());

        String templateRootPath="templates";
        try{
            Resource resource = new ClassPathResource(templateRootPath);
            URL url = resource.getURL();
            String path = url.getPath();
            System.out.println("url = " + url);
            System.out.println("path = " + path);
        }catch (Exception e){
//            log.error("获取路径失败："+e);
//            new RRException("获取模板失败");
        }

//        String[] a = new String[]{"a"};
//        String[] b = new String[]{"A","a"};
//        boolean b1 = new ArrayList<>(Arrays.asList(a)).removeAll(new ArrayList<>(Arrays.asList(b)));
//        System.out.println("b1 = " + b1);
//        System.out.println("a = " + a.toString());
//        System.out.println("b = " + b.toString());
    }




}
