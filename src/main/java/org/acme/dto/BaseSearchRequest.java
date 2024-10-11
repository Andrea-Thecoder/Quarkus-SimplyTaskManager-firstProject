package org.acme.dto;

import io.quarkus.panache.common.Page;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BaseSearchRequest {

    @QueryParam("page")
    @DefaultValue("0")
    @PositiveOrZero
    int page;

    @QueryParam("size")
    @DefaultValue("20")
    @Positive
    int size;

    @QueryParam("sort")
    String sort;

    public Page toPage(){
        return Page.of(page, size);
    }
}
