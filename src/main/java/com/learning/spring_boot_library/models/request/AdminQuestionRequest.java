package com.learning.spring_boot_library.models.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AdminQuestionRequest {

    private Long id;

    private String response;
}

