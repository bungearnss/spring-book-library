package com.learning.spring_boot_library.models.response;

import com.learning.spring_boot_library.models.entity.Book;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ShelfCurrentLoansResponse {
    private Book book;

    private int daysLeft;
}
