package fun.qianfg.fileservice.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 大文件上传记录
 */
@Data
@Entity
@Table(name = "FND_FileUploadRecord", uniqueConstraints = @UniqueConstraint(columnNames = "MD5"))
public class FileUploadRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", length = 20)
    private Long id;

    /**
     * 文件名
     */
    @Column(name = "FileName", nullable = false, length = 30)
    private String fileName;

    /**
     * 文件的MD5，用文件名+文件size生成
     */
    @Column(name = "MD5", nullable = false, length = 32)
    private String md5;
}
