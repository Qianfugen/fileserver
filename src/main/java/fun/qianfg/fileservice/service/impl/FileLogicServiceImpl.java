package fun.qianfg.fileservice.service.impl;

import fun.qianfg.fileservice.cst.PlatformCst;
import fun.qianfg.fileservice.cst.PlatformErrorCodeCst;
import fun.qianfg.fileservice.exception.ErrorInfoException;
import fun.qianfg.fileservice.service.FileLogicService;
import fun.qianfg.fileservice.service.FileUploadRecordService;
import fun.qianfg.fileservice.utils.FileUtils;
import fun.qianfg.fileservice.utils.LocalFileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;

@Slf4j
@Service
public class FileLogicServiceImpl implements FileLogicService {

    @Autowired
    private FileUploadRecordService fileUploadRecordService;

    private static final String TEMP_FILE_SUFFIX = ".tmp";
    private static final String TEMP_FOLDER = "/temp";

    private static final String BIG_FILE_FOLDER = "/bigfile";

    @Override
    public ResponseEntity<byte[]> download(String filePath) {
        filePath = "." + filePath;
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

        HttpHeaders headers = new HttpHeaders();

        headers.add("Access-Control-Expose-Headers", "filename");
        try {
            headers.add("filename", URLEncoder.encode(fileName, "utf-8"));
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileName, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }

        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(FileUtils.getBytesByFile(filePath), headers, HttpStatus.CREATED);
    }

    @Override
    public String upload(MultipartFile file, String folderName) {
        try {
            return PlatformCst.UPLOAD_PATH.replace(".", "") + LocalFileManager.upload(file, folderName);
        } catch (IOException e) {
            throw new ErrorInfoException(PlatformErrorCodeCst.FILE_SAVE_WRONG);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public boolean checkChunk(String md5, Integer chunk) {
        Boolean exist = false;
        String path = getTempPath(md5);
        String chunkName = chunk + TEMP_FILE_SUFFIX;
        File file = new File(path + chunkName);
        if (file.exists()) {
            exist = true;
        }
        return exist;
    }

    @Override
    public void uploadBigFile(MultipartFile file, String md5, Integer chunk) {
        String path = getTempPath(md5);
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        String chunkName;
        if (chunk == null) {
            chunkName = "0" + TEMP_FILE_SUFFIX;
        } else {
            chunkName = chunk + TEMP_FILE_SUFFIX;
        }
        String filePath = path + chunkName;
        File saveFile = new File(filePath);
        try {
            if (!saveFile.exists()) {
                saveFile.createNewFile();
            }
            String absolutePathOfSaveFile = saveFile.getAbsolutePath();
            file.transferTo(new File(absolutePathOfSaveFile));
        } catch (IOException e) {
            throw new ErrorInfoException(PlatformErrorCodeCst.FILE_SAVE_WRONG);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String mergeBigFile(Integer chunks, String md5, String fileName) {
        File bigFilePath = new File(PlatformCst.UPLOAD_PATH + BIG_FILE_FOLDER);
        if (bigFilePath.exists() == false) {
            bigFilePath.mkdir();
        }

        String mergeFilePath = generateMergeFilePath(fileName);
        File mergeFile = new File(mergeFilePath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(mergeFile)) {
            doMerge(chunks, md5, fileOutputStream);
            LocalFileManager.deleteDir(new File(getTempPath(md5)));
            fileUploadRecordService.save(md5, fileName);
        } catch (FileNotFoundException e) {
            throw new ErrorInfoException(PlatformErrorCodeCst.FILE_PATH_NOT_FOUND, e.getMessage());
        } catch (IOException e) {
            throw new ErrorInfoException(PlatformErrorCodeCst.FILE_SAVE_WRONG);
        } catch (Exception e) {
            throw e;
        }

        return mergeFilePath.replaceFirst(".", "");
    }

    /**
     * 创建合成的文件，会判断路径上是否有同名文件，有就像windows一样在后面加(1)
     *
     * @param fileName
     * @return
     */
    private String generateMergeFilePath(String fileName) {
        File file = new File(getPathOfMergeFile(fileName));
        StringBuilder sb = new StringBuilder(getPathOfMergeFile(fileName));
        Integer count = 1;
        while (file.exists()) {
            if (sb.toString().contains("(") == false) {
                int commaIndex = sb.lastIndexOf(".");
                sb.insert(commaIndex, "(" + count + ")");
            } else {
                sb.replace(sb.lastIndexOf("(") + 1, sb.lastIndexOf(")"), count.toString());
            }
            file = new File(sb.toString());
            count++;
        }

        return sb.toString();
    }

    /**
     * 使用文件流实现合并操作
     */
    private void doMerge(Integer chunks, String md5, FileOutputStream fileOutputStream) throws IOException {
        byte[] buf = new byte[1024];
        for (long i = 0; i < chunks; i++) {
            String chunkFile = i + TEMP_FILE_SUFFIX;
            File file = new File(getTempPath(md5) + chunkFile);
            InputStream inputStream = new FileInputStream(file);
            int len = 0;
            while ((len = inputStream.read(buf)) != -1) {
                fileOutputStream.write(buf, 0, len);
            }
            inputStream.close();
        }
    }

    /**
     * 根据md5获取临时文件夹路径
     */
    private String getTempPath(String md5) {
        return PlatformCst.UPLOAD_PATH + TEMP_FOLDER + PlatformCst.SEPARATOR + md5 + PlatformCst.SEPARATOR;
    }

    /**
     * 根据文件名获取合成后的文件路径
     */
    private String getPathOfMergeFile(String fileName) {
        return PlatformCst.UPLOAD_PATH + BIG_FILE_FOLDER + PlatformCst.SEPARATOR + fileName;
    }
}
