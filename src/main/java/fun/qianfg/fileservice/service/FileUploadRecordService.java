package fun.qianfg.fileservice.service;

/**
 * 大文件上传记录服务
 */
public interface FileUploadRecordService {

    /**
     * 根据md5判断是否文件已上传
     * @param md5
     * @return
     */
    boolean checkIsExist(String md5);

    /**
     * 保存文件上传记录
     * @param md5
     * @param fileName
     */
    void save(String md5, String fileName);
}
