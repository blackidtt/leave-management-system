package com.example.leave.filter;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageFilter {
    private Integer page;
    private Integer size;
    private String sortBy = "id";
    private String direction = "DESC";
}
