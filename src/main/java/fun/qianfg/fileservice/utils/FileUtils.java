package fun.qianfg.fileservice.utils;

import fun.qianfg.fileservice.cst.PlatformCst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.*;

@Slf4j
public final class FileUtils {

    /**
     * 读取classpath下的文件内容 例：classpath:db/data.sql
     *
     * @param filename 文件名
     * @return 文件内容
     */
    public static String readClasspathFileContent(String filename) {
        return readClasspathFileContent(filename, null);
    }

    /**
     * @param filename         文件名
     * @param ignoreLinePrefix 如果行开头是该字符串，忽略
     * @return 文件内容
     */
    public static String readClasspathFileContent(String filename, String ignoreLinePrefix) {
        StringBuffer contentBuf = new StringBuffer();
        boolean prefixable = ignoreLinePrefix != null && ignoreLinePrefix.trim().isEmpty() == false;
        String prefix = null;
        if (prefixable) {
            prefix = ignoreLinePrefix.trim();
        }

        try {
            File file = ResourceUtils.getFile(filename);
            try (BufferedReader reader = new BufferedReader(new FileReader(file));) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (prefixable == false || line.startsWith(prefix) == false) {
                        contentBuf.append(line).append(PlatformCst.NEW_LINE);
                    } else {
                        log.info("Abort line: " + line);
                    }
                }
            }
        } catch (Exception e) {
            log.error("读取classpath文件错误.filename: " + filename, e);
        }

        return contentBuf.toString();
    }

    /**
     * SQL脚本以分号“;”隔开，注释以--开头
     *
     * @param filename 数据库脚本文件名
     * @return SQL脚本数组, 一行一个命令
     */
    public static String[] readSqlData(String filename) {
        return readClasspathFileContent(filename, "--").split(";");
    }

    /**
     * 建文件夹
     *
     * @param dirPath
     */
    public static void makeDir(String dirPath) {

        File folder = new File(dirPath);
        if (folder.exists() && folder.isDirectory()) {
            return;
        } else {
            folder.mkdirs();
        }

    }

    /**
     * 保存文件
     *
     * @param inputStream
     * @param fileName
     * @param path
     */
    public static void saveFile(InputStream inputStream, String fileName, String path) {

        OutputStream os = null;
        String filePath = null;
        try {
            // 2、保存到临时文件
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件

            File tempFile = new File(path);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            filePath = tempFile.getPath() + File.separator + fileName;
            os = new FileOutputStream(filePath);
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 保存文件
     *
     * @param bytes
     * @param fileName
     * @param path
     */
    public static void saveFile(byte[] bytes, String fileName, String path) {
        OutputStream os = null;
        try {
            File tempFile = FileUtils.createFile2(path, fileName);
            os = new FileOutputStream(tempFile);
            // 开始读取
            os.write(bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static File createFile(String path, String fileName) {
        String absolutePath = FileUtils.class.getResource("/").getPath() + path;
        FileUtils.makeDir(absolutePath);
        File tempFile = new File(absolutePath + File.separator + fileName);
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    public static File createFile2(String path, String fileName) {
        FileUtils.makeDir(path);
        File tempFile = new File(path + File.separator + fileName);
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    /**
     * 将文件转换成Byte数组
     */
    public static byte[] getBytesByFile(String fileUrl, String pathPrefix) {
        try {
            File file = ResourceUtils.getFile(pathPrefix + File.separator + fileUrl);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            log.error("读取classpath文件错误.filename: " + fileUrl, e);
        }
        return new byte[0];
    }

    /**
     * 将文件转换成Byte数组
     */
    public static byte[] getBytesByFile(String fileUrl) {
        try {
            File file = ResourceUtils.getFile(fileUrl);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            log.error("读取classpath文件错误.filename: " + fileUrl, e);
        }
        return new byte[0];
    }

    public static boolean deleteFile(String path, String fileName) {
        File tempFile = new File(path + File.separator + fileName);
        return tempFile.delete();
    }
}
