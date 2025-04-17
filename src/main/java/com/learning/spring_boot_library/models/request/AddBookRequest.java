package com.learning.spring_boot_library.models.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AddBookRequest {

    private String title;

    private String author;

    private String description;

    private int copies;

    private String category;

    private String img;

}