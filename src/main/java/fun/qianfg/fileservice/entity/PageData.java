package fun.qianfg.fileservice.entity;

import lombok.Data;

import java.util.List;

@Data
public class PageData<T> {

    private static final long serialVersionUID = 1L;

    private List<T> list;

    private Integer pageNumber;

    private Integer pageSize;

    private Integer totalPages;

    private Integer totalElements;

    public static final int PAGESIZE = 30;

    public static final int PAGE_NUBMER = 1;

    public PageData() {
    }

    public PageData(List<T> datas, Integer pageNumber, Integer pageSize,
                    Integer totalElements) {
        this.list = datas;
        this.pageNumber = pageNumber != null ? pageNumber : PAGE_NUBMER;
        this.pageSize = pageSize != null ? pageSize : PAGESIZE;
        this.totalElements = totalElements;
        if (totalElements % this.pageSize != 0) {
            this.totalPages = ((totalElements / this.pageSize) + 1);
        } else {
            this.totalPages = totalElements / this.pageSize;
        }
    }

}
