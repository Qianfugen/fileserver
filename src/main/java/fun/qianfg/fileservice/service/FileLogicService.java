package fun.qianfg.fileservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件逻辑服务层
 */
public interface FileLogicService {

    ResponseEntity<byte[]> download(String filePath);

    String upload(MultipartFile file, String folderName);

    /**
     * 检查分片存在与否
     *
     * @return
     */
    boolean checkChunk(String md5, Integer chunk);

    /**
     * 上传，这里根据文件md5值生成目录，并将分片文件放到该目录下
     */
    void uploadBigFile(MultipartFile file, String md5, Integer chunk);

    /**
     * 将分片合成为整个大文件
     */
    String mergeBigFile(Integer chunks, String md, String fileName);
}
