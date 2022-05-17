package fun.qianfg.fileservice.controller;

import fun.qianfg.fileservice.cst.PlatformErrorCodeCst;
import fun.qianfg.fileservice.entity.BaseDataResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
public abstract class BaseController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    BaseResult exceptionHandler(Exception ex) {
        log.error("发生异常：{}", ex.getMessage());
        BaseResult rlt = new BaseResult(PlatformErrorCodeCst.FAILED);
        rlt.setMessage(ex.getMessage());
        return rlt;
    }

    /**
     * 将数据封装成BaseDataResult返回
     */
    protected <T> BaseDataResult<T> data(T data) {
        return new BaseDataResult<>(data);
    }

}
