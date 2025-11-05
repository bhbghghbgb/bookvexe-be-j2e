package com.bookvexe.notificationservice.dto.base;

import lombok.Data;

@Data
public class BasePageableQuery {
    private Integer page = 0;
    private Integer size = 20;
    private String sortBy = "createdDate";
    private String sortDirection = "DESC";
}
