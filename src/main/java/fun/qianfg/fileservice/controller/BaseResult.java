package fun.qianfg.fileservice.controller;

import fun.qianfg.fileservice.cst.PlatformErrorCodeCst;
import lombok.Data;

@Data
public class BaseResult {

    private static final long serialVersionUID = 1L;

    public final static String SUCCESS = PlatformErrorCodeCst.SUCCESS;

    public final static String FAILED = PlatformErrorCodeCst.FAILED;

    private String code;

    private String message;

    public BaseResult() {
        this(PlatformErrorCodeCst.SUCCESS);
    }

    public BaseResult(String code) {
        this.code = code;
    }

}
