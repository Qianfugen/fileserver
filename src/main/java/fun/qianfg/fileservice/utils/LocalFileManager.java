package fun.qianfg.fileservice.utils;

import fun.qianfg.fileservice.cst.PlatformCst;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class LocalFileManager {

    public static String upload(MultipartFile attach, String fileLocalPath) throws IOException {
        return upload(attach, fileLocalPath, false);
    }

    public static String upload(MultipartFile attach, String fileLocalPath, boolean secret) throws IOException {

        String ext = attach.getOriginalFilename()
                .substring(attach.getOriginalFilename().lastIndexOf(PlatformCst.DOT) + 1);
        InputStream inputStream = attach.getInputStream();
        String fileName = UUID.randomUUID() + PlatformCst.DOT + ext;
        String pathPrefix = secret ? PlatformCst.SECURE_UPLOAD_PATH : PlatformCst.UPLOAD_PATH;
        String path = pathPrefix + PlatformCst.SEPARATOR + fileLocalPath;

        FileUtils.makeDir(path);

        FileUtils.saveFile(inputStream, fileName, path);

        return PlatformCst.SEPARATOR + fileLocalPath + PlatformCst.SEPARATOR + fileName;
    }

    public static void deleteFile(String path) {
        File f = new File(path);
        f.delete();
    }

    /**
     * 递归删除整个文件夹
     */
    public static void deleteDir(File file) {
        File[] fs = file.listFiles();
        for (int i = 0; i < fs.length; i++) {
            File f = fs[i];
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteDir(f);
                f.delete();
            }
        }
        file.delete();

    }
}