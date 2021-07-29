package com.example01.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;


@Data
@AllArgsConstructor
public class PaginateResult {
    private int totalItems;
    private Page<?> paginate;
}
