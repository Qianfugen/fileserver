package fun.qianfg.fileservice.controller;

import fun.qianfg.fileservice.entity.BaseDataResult;
import fun.qianfg.fileservice.service.FileLogicService;
import fun.qianfg.fileservice.service.FileUploadRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 提供统一的文件下载、上传接口
 */
@Api(tags = "文件上传")
@RestController
@RequestMapping("/file")
public class FileController extends BaseController {

    @Autowired
    private FileUploadRecordService fileUploadRecordService;

    @Autowired
    private FileLogicService fileLogicService;


    @GetMapping("download.do")
    @ApiOperation("下载文件")
    public ResponseEntity<byte[]> download(String filePath) {
        return fileLogicService.download(filePath);
    }

    @PostMapping("checkBigFile.do")
    @ApiOperation("检查大文件存在与否")
    public BaseDataResult<Boolean> checkBigFile(String md5) {
        return data(fileUploadRecordService.checkIsExist(md5));
    }

    @PostMapping("checkChunk.do")
    @ApiOperation("检查大文件分片存在与否")
    public BaseDataResult<Boolean> checkChunk(String md5, Integer chunk) {
        return data(fileLogicService.checkChunk(md5, chunk));
    }

    @PostMapping("upload.do")
    @ApiOperation("上传普通文件")
    public BaseDataResult<String> upload(MultipartFile file, String folderName) {
        return data(fileLogicService.upload(file, folderName));
    }

    @PostMapping("uploadBigFile.do")
    @ApiOperation("大文件上传，这里根据文件md5值生成目录，并将分片文件放到该目录下")
    public BaseResult uploadBigFile(MultipartFile file, String md5, Integer chunk) {
        fileLogicService.uploadBigFile(file, md5, chunk);
        return new BaseResult();
    }

    @PostMapping("mergeBigFile.do")
    @ApiOperation("将分片合成为整个大文件")
    public BaseDataResult<String> mergeBigFile(Integer chunks, String md5, String fileName) {
        return data(fileLogicService.mergeBigFile(chunks, md5, fileName));
    }

}
