package fun.qianfg.fileservice.entity;

import fun.qianfg.fileservice.controller.BaseResult;
import fun.qianfg.fileservice.cst.PlatformErrorCodeCst;
import lombok.Data;

@Data
public class BaseDataResult<T> extends BaseResult {

    private static final long serialVersionUID = 1L;

    private T data;

    public BaseDataResult(T data) {
        super(PlatformErrorCodeCst.SUCCESS);
        this.data = data;
    }
}
