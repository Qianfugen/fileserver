package fun.qianfg.fileservice.repository;

import fun.qianfg.fileservice.entity.FileUploadRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadRecordRepository extends JpaRepository<FileUploadRecord, Long> {
    FileUploadRecord findByMd5(String md5);
}
