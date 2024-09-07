package com.Optimart.responses;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PagingResponse {
    private int totalPage;
    private int totalCount;
}
