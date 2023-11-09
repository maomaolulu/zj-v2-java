package may.yuntian.untils;

public class FilePathUtil {
    public FilePathUtil() {
    }

    public static String getJarRootPath() {
        String path = FilePathUtil.class.getResource("/").getPath();
        System.err.println("path = " + path);
        int i2 = path.indexOf("/");
        int i = path.indexOf(".jar!");
        if (i > 0) {
            path = path.substring(i2 + 1, i);
        }

        int i3 = path.lastIndexOf("/");
        path = path.substring(0, i3 + 1);
        int i1 = path.indexOf(":");
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        if (path.contains(".war!")) {
            path = path.replace("/file:", "");
            path = path.substring(0, path.indexOf(".war!"));
            path = path.substring(0, path.lastIndexOf("/"));
        }

        return path;
    }

//    public static void main(String[] args) {
//        String path = "/file:/D:/workspace/RuoYi-mysql/ruoyi-admin/target/ruoyi-admin.war!/WEB-INF/classes!";
//        path = path.replace("/file:", "");
//        path = path.substring(0, path.indexOf(".war!"));
//        path = path.substring(0, path.lastIndexOf("/"));
//        System.out.println(path);
//    }
}
