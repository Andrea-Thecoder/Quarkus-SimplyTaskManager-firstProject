package org.acme.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PageResult<T> {

    private List<T> results;
    private int page;
    private int size;
    private long total;

    public static <T> PageResult<T> of(List<T> results, int page, int size, long total){
        PageResult<T> result = new PageResult<>();
        result.results = results;
        result.page = page;
        result.size = size;
        result.total = total;
        return result;
    }
}
