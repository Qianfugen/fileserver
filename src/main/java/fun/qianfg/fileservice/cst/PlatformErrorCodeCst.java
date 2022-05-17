package fun.qianfg.fileservice.cst;

/**
 * 平台通用ErrorCode，以00开头
 */
public interface PlatformErrorCodeCst {
    /**
     * 成功
     */
    String SUCCESS = "000000";

    /**
     * 失败
     */
    String FAILED = "000001";

    /**
     * 文件保存到服务器时发生错误
     */
    String FILE_SAVE_WRONG = "000002";

    /**
     * 未找到文件路径{0}
     */
    String FILE_PATH_NOT_FOUND = "000003";

}
