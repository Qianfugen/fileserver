package fun.qianfg.fileservice.service.impl;

import fun.qianfg.fileservice.entity.FileUploadRecord;
import fun.qianfg.fileservice.repository.FileUploadRecordRepository;
import fun.qianfg.fileservice.service.FileUploadRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileUploadRecordServiceImpl implements FileUploadRecordService {

    @Autowired
    private FileUploadRecordRepository fileUploadRecordRepo;


    @Override
    public boolean checkIsExist(String md5) {
        return fileUploadRecordRepo.findByMd5(md5) != null;
    }

    @Override
    public void save(String md5, String fileName) {
        FileUploadRecord entity = new FileUploadRecord();
        entity.setMd5(md5);
        entity.setFileName(fileName);
        fileUploadRecordRepo.save(entity);
    }
}
