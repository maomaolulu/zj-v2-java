package may.yuntian.minio.utils;


import cn.hutool.core.io.FastByteArrayOutputStream;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.modules.sys_v2.utils.SpringUtils;
import may.yuntian.modules.sys_v2.utils.StringUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MinioUtil {
    private static final MinioClient minioClient = SpringUtils.getBean(MinioClient.class);
    private static final String bucketName = SpringUtils.getRequiredProperty("minio.bucketName");

    /**
     * 查看存储bucket是否存在
     *
     * @return boolean
     */
    public static Boolean bucketExists(String bucketName) {
        Boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e+"===========");
            return false;
        }
        return found;
    }

    /**
     * 创建存储bucket
     *
     * @return Boolean
     */
    public static Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除存储bucket
     *
     * @return Boolean
     */
    public static Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取全部bucket
     */
    public static List<Bucket> getAllBuckets() {
        try {
            List<Bucket> buckets = minioClient.listBuckets();
            return buckets;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    /**
     *
     * @param file 文件
     * @param type 存储路径类型  user/(id)94/(type)test
     * @return
     */
    public static String upload(MultipartFile file,String type) {
//        String bucketName = prop.getBucketName();
        System.out.println("bucketName = " + bucketName);
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new RuntimeException();
        }
        String fileName = IdUtils.fastUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
//        String now1 ;
//        if (minioDto.getUserId()!=null){
//            now1 = "user/"+minioDto.getUserId();
//        }else if (minioDto.getProjectId()!=null){
//            now1 = "project/"+minioDto.getProjectId();
//        }else {
//            now1 = "other";
//        }
        String objectName = type + "/" + fileName;
        try {
            InputStream in = file.getInputStream();
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(bucketName).object(objectName)
                    .stream(in, file.getSize(), -1).contentType(file.getContentType()).build();
            boolean exist = bucketExists(bucketName);
            if (!exist) {
                makeBucket(bucketName);
            }
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return bucketName + "/" + objectName;
    }

    /**
     * 预览图片
     *
     * @param fileName
     * @return
     */
    public static String preview(String fileName) {
//        String bucketName = prop.getBucketName();
        // 查看文件地址
        GetPresignedObjectUrlArgs build = new GetPresignedObjectUrlArgs.Builder().bucket(bucketName).object(fileName).method(Method.GET).build();
        String url = "";
        try {
            url = minioClient.getPresignedObjectUrl(build);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @param res      response
     * @return Boolean
     */
    public static void download(String fileName, HttpServletResponse res) {
//        String bucketName = prop.getBucketName();
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(bucketName)
                .object(fileName).build();
        try (InputStream response = minioClient.getObject(objectArgs)) {
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = response.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                res.setCharacterEncoding("utf-8");
                // 设置强制下载不打开
                // res.setContentType("application/force-download");
                res.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
                try (ServletOutputStream stream = res.getOutputStream()) {
                    stream.write(bytes);
                    stream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看文件对象
     *
     * @return 存储bucket内文件对象信息
     */
    public List<Item> listObjects() {
//        String bucketName = prop.getBucketName();
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).build());
        List<Item> items = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return items;
    }

    /**
     * 删除
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public static boolean remove(String fileName) {
//        String bucketName = prop.getBucketName();
        String objName = fileName.substring(fileName.lastIndexOf(bucketName + "/") + bucketName.length() + 1);
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objName).build());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 通过文件路径获取MultipartFile
     * @param path
     * @return
     */
    public static MultipartFile createMfileByPath(String path) {
        MultipartFile mFile = null;
        try {
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file);
            String fileName = file.getName();
            fileName = fileName.substring((fileName.lastIndexOf("/") + 1));
            mFile = new MockMultipartFile(fileName, fileName,
                    "application/sql", fileInputStream);
        } catch (Exception e) {
            log.error("封装文件出现错误：{}", e);
            //e.printStackTrace();
        }
        return mFile;
    }

}