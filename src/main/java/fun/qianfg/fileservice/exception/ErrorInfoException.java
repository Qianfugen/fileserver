package fun.qianfg.fileservice.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ErrorInfoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private List<ErrorInfo> errors = new ArrayList<>();

    public ErrorInfoException(String code) {
        this(code, null);
    }

    public ErrorInfoException(String code, Object... params) {
        this(code, (Throwable) null, params);
    }

    public ErrorInfoException(String code, Throwable throwable, Object... parms) {
        super(code, throwable);

        errors.add(new ErrorInfo(code, parms));
    }

    public ErrorInfoException(List<ErrorInfo> errors) {
        if (errors != null && !errors.isEmpty()) {
            this.errors.addAll(errors);
        }
    }
}
