package cc.catman.workbench.service.core.common.page;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class VPage<T> {
    @Getter
    private long size;
    @Getter
    private long pages;
    @Getter
    private long current;
    @Getter
    private long total;
    private boolean isFirst;
    private boolean isLast;
    private boolean hasPre;
    private boolean hasNext;
    private List<T> records;
    private Object ex;

    public VPage<T> done() {
        if (size == 0) {
            throw new RuntimeException("The page size can not be zero.");
        }
        if (current == 0) {
            current = 1;
        }
        return this.setPages(total % size == 0 ? total / size : (total / size) + 1)
                .setCurrent(pages > 0 ? current : 1)
                .setFirst(current == 1)
                .setLast(current == pages)
                .setNext(current < pages)
                .setPre(current > 1);
    }

    public VPage(long current, long size) {
        this.current = current;
        this.size = size;
    }

    public VPage<T> setSize(long size) {
        this.size = size;
        return this;
    }

    public VPage<T> setPages(long pages) {
        this.pages = pages;
        return this;
    }

    public VPage<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    public VPage<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public VPage<T> setFirst(boolean first) {
        this.isFirst = first;
        return this;
    }

    public boolean isLast() {
        return isLast;
    }

    public VPage<T> setLast(boolean last) {
        this.isLast = last;
        return this;
    }


    public VPage<T> setPre(boolean hasPre) {
        this.hasPre = hasPre;
        return this;
    }


    public VPage<T> setNext(boolean hasNext) {
        this.hasNext = hasNext;
        return this;
    }

    public List<T> getRecords() {
        return records;
    }

    public VPage<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    public boolean isHasPre() {
        return hasPre;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public Object getEx() {
        return ex;
    }

    public void setEx(Object ex) {
        this.ex = ex;
    }

    public <S> VPage<S> convert(PageConverter<T, S> converter) {
        VPage<S> page = new VPage<>(current, size);
        page.setCurrent(current);
        page.setSize(size);
        page.setFirst(isFirst);
        page.setLast(isLast);
        page.setNext(hasNext);
        page.setPre(hasPre);
        page.setPages(pages);
        page.setTotal(total);
        page.setEx(ex);
        page.setRecords(converter.convert(records));
        return page;

    }

    @FunctionalInterface
    public interface PageConverter<S, T> {
        List<T> convert(List<S> s);
    }
}
