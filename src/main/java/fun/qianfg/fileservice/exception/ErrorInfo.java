package fun.qianfg.fileservice.exception;

import lombok.Data;

@Data
public class ErrorInfo {

    public String code;

    public Object[] params;

    public ErrorInfo(String code) {
        this.code = code;
    }

    public ErrorInfo(String code, Object... params) {
        this.code = code;
        this.params = params;
    }

    public static void main(String[] args) {

    }
}
