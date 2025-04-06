package com.learning.spring_boot_library.services;

import com.learning.spring_boot_library.models.entity.Book;
import com.learning.spring_boot_library.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Page<Book> getAllBooks(Pageable pageable){
        return bookRepository.findAll(pageable);
    }
}