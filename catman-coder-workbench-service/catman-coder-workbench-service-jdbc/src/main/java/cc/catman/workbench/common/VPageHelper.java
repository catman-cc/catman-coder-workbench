package cc.catman.workbench.common;


import cc.catman.workbench.service.core.common.page.PageParam;
import cc.catman.workbench.service.core.common.page.VPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * 通用Page转换器 ,目前只集成了JPA
 *
 * @author Jpanda [Jpanda@aliyun.com]
 * @version 1.0
 * @since 2020/6/19 16:31:27
 */
public class VPageHelper {

    public static <T> VPage<T> with(Page<T> mpPage) {
        VPage<T> page = new VPage<>(mpPage.getNumber(), mpPage.getSize());
        page.setCurrent(mpPage.getNumber());
        page.setSize(mpPage.getSize());
        page.setFirst(mpPage.isFirst());
        page.setLast(mpPage.isLast());
        page.setNext(mpPage.hasNext());
        page.setPre(mpPage.hasPrevious());
        page.setPages(mpPage.getTotalPages());
        page.setTotal(mpPage.getTotalElements());
        page.setEx(null);
        page.setRecords(mpPage.getContent());
        return page;
    }

    public static PageRequest with(PageParam pageParam) {
        if (pageParam.getSort() != null) {
           return PageRequest.of(pageParam.getStart(), pageParam.getSize(), VSortHelper.with(pageParam.getSort()));
        }
        return  PageRequest.of(pageParam.getStart(), pageParam.getSize());
    }
    public static PageRequest with(PageParam pageParam, Sort sort) {
        if (pageParam.getSort() != null) {
            return PageRequest.of(pageParam.getStart(), pageParam.getSize(), (VSortHelper.with(pageParam.getSort(),sort,true)));
        }
        return  PageRequest.of(pageParam.getStart(), pageParam.getSize(),sort);
    }
}
