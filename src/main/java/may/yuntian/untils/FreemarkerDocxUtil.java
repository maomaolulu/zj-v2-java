package may.yuntian.untils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.anlian.utils.FileUtils;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.exception.RRException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@SuppressWarnings("all")
@Slf4j
public class FreemarkerDocxUtil {

//    @Value("${anlian.path.freemarker-path}")
//    private static String templatePath ;

    /**
     * freemarker公用配置
     *
     * @return
     * @throws IOException
     */
    public static Configuration getConfiguration() throws IOException {
        //创建配置实例
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        //设置编码
        configuration.setDefaultEncoding("utf-8");
        configuration.setClassForTemplateLoading(FreemarkerDocxUtil.class,"/templates");
        return configuration;
    }

    /**
     * 根据freemarker生成docx文档--返回生成后的docx地址
     *
     * @param templateRootPath    word模板的根路径 例如：D:/xxx/xxx 最后一个名称只到目录
     * @param xmlTemplateName     word模板的名称（docx的是xml格式）例如：test.xml
//     * @param docxFileName        生成docx文件名称 例如：test.docx
     * @param docxZipName         模板压缩包名称  例如：test.zip
     * @param dataMap             生成文件需要的数据
     * @param itemNameList        需要替换的图片资源地址 例如：word/media/xxx.xxx 去你的zip模板的word/media路径去找
     * @param itemInputStreamList 根据itemNameList中的图片地址替换掉word/media中的图片，这个图片的顺序要和itemNameList中的一致
     * @throws Exception
     */
    public static String freemarkerDocxTest(String xmlTemplateName, String docxZipName, Map<String, Object> dataMap,
                                          List<String> itemNameList, List<InputStream> itemInputStreamList,String xmlFooterTemplateName) throws Exception {

//        String templateRootPath = "D:\\new_java\\new_zj_java\\templates";//模板具体地址  本地
//        String templateRootPath2 = FilePathUtil.getJarRootPath()+"/templates";//模板具体地址     线上
        String templateRootPath = "templates";//模板具体地址     线上
//        System.out.println("systemPath = " + System.getProperty("user.dir"));
//        System.out.println("templateRootPath2 = " + templateRootPath2);
//        String templateRootPath = templatePath;//模板具体地址     线上
        InputStream tempZipinputStream = null;
        try {
            Resource resource2 = new ClassPathResource(templateRootPath+File.separator+docxZipName);
            tempZipinputStream = resource2.getInputStream();
        }catch (Exception e){
            log.error("resource = 路径获取失败：" + e);
            e.printStackTrace();
        }


        String docxFileName = UUID.randomUUID().toString().replace("-", "") +".docx";

        //配置freemarker模板--内容
        Template template = getConfiguration().getTemplate(xmlTemplateName);
        //通过模板生成的xml临时文件 方法结束后删除该临时文件
        String outFilePath =  UUID.randomUUID().toString().replace("-", "") + ".xml";
        //指定输出word xml文件的路径
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFilePath), StandardCharsets.UTF_8));
        template.process(dataMap, out);
        out.close();

        //TODO 调整页眉页脚
        String outFilePath2 = "";
        if (StringUtils.isNotBlank(xmlFooterTemplateName)){
            //配置freemarker模板--内容
            Template template2 = getConfiguration().getTemplate(xmlFooterTemplateName);
            //通过模板生成的xml临时文件 方法结束后删除该临时文件
            outFilePath2 = UUID.randomUUID().toString().replace("-", "") + ".xml";
            //指定输出word xml文件的路径
            Writer out2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFilePath2), StandardCharsets.UTF_8));
            template2.process(dataMap, out2);
            out2.close();
        }

        //包装输入流 模板zip包
        ZipInputStream zipInputStream = new ZipInputStream(tempZipinputStream);
        //包装输出流 输出docx文档流
        String returnFilePath =  docxFileName;
        System.out.println(returnFilePath);
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(returnFilePath));
        //如果为空 创建需要替换zip中的文件集合
        if (itemNameList == null) {
            itemNameList = new ArrayList<>();
        } else {
            itemNameList = itemNameList.stream().map(item -> item.replaceAll("\\\\", "/")).collect(Collectors.toList());
        }

        if (itemInputStreamList == null) {
            itemInputStreamList = new ArrayList<>();
        }
        itemNameList.add(0, "word/document.xml");
        // 创建需要替换的文件资源  根据xml模板生成的xml文件
        itemInputStreamList.add(0, new FileInputStream(outFilePath));

        if (StringUtils.isNotBlank(xmlFooterTemplateName)){//TODO 调整页眉页脚
            itemNameList.add(1, "word/footer1.xml");
            // 创建需要替换的文件资源  根据xml模板生成的xml文件
            itemInputStreamList.add(1, new FileInputStream(outFilePath2));
        }

        replaceItemList(zipInputStream, zipOutputStream, itemNameList, itemInputStreamList);

        if (StringUtils.isNotBlank(outFilePath2)){//TODO 调整页眉页脚
            new File(outFilePath2).delete();
        }
        new File(outFilePath).delete();
        return returnFilePath;
    }

    public static void returnResponse(String fileName,String filePath, HttpServletResponse response){

        File file = null;
        InputStream fin = null;
        ServletOutputStream out = null;
        try {
//            String filePath =  FreemarkerDocxUtil.freemarkerDocxTest("DeclareIndex/", "document.ftl", "DeclareIndex.zip", map, null, null);

            file = new File(filePath);
            fin = new FileInputStream(file);


            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.setHeader("Content-Disposition", "attachment;fileName="
                    .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));

            out = response.getOutputStream();

            // 缓冲区
            byte[] buffer = new byte[512];
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while ((bytesToRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }

        } catch (Exception e ){
            log.error("生成word失败",e);
        }
        finally {
            if(fin!=null){
                try {
                    fin.close();
                } catch (IOException e) {
                    log.error("io异常");
                }
            }
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("io异常");
                }
            }
            if (file != null) {
                file.delete(); // 删除临时文件
            }
        }


    }

    /**
     * @param zipInputStream      zip模板
     * @param zipOutputStream     输出docx文档流
     * @param itemNameList        替换的文件路径
     * @param itemInputStreamList
     */
    public static void replaceItemList(ZipInputStream zipInputStream, ZipOutputStream zipOutputStream, List<String> itemNameList, List<InputStream> itemInputStreamList) {
        if (null == zipInputStream) {
            return;
        }
        if (null == zipOutputStream) {
            return;
        }
        ZipEntry entryIn;
        try {
            while ((entryIn = zipInputStream.getNextEntry()) != null) {
                String entryName = entryIn.getName();
                ZipEntry entryOut = new ZipEntry(entryName);
                // 这个entryName路径结构是什么，解压后的文件结构就是什么，ZipEntry的大概意思就是只设置这个文件在zip中的路径
                // 并不涉及到字节的写入 putNextEntry 设置每一个ZipEntry对象
                zipOutputStream.putNextEntry(entryOut);
                byte[] buf = new byte[8 * 1024];
                int len;

                // 这里的判断意思是如果有需要替换的文件就使用替换文件写入，没有就写入原来的到新的docx文件中
                if (itemNameList.contains(entryName)) {
                    // 使用替换流
                    while ((len = (itemInputStreamList.get(itemNameList.indexOf(entryName)).read(buf))) > 0) {
                        zipOutputStream.write(buf, 0, len);
                    }
                } else {
                    // 输出普通Zip流
                    while ((len = (zipInputStream.read(buf))) > 0) {
                        zipOutputStream.write(buf, 0, len);
                    }
                }
                // 关闭此 entry
                zipOutputStream.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            for (InputStream itemInputStream : itemInputStreamList) {
                close(itemInputStream);
            }
            close(zipInputStream);
            close(zipOutputStream);
        }
    }

    private static void close(InputStream inputStream) {
        if (null != inputStream) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void close(OutputStream outputStream) {
        if (null != outputStream) {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
