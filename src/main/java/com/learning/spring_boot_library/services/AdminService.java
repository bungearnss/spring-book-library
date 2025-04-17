package com.learning.spring_boot_library.services;

import com.learning.spring_boot_library.models.entity.Book;
import com.learning.spring_boot_library.models.request.AddBookRequest;
import com.learning.spring_boot_library.repositories.BookRepository;
import com.learning.spring_boot_library.repositories.CheckoutRepository;
import com.learning.spring_boot_library.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AdminService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CheckoutRepository checkoutRepository;

    public void increaseBookQuantity(Long bookId) throws Exception {

        Optional<Book> book = bookRepository.findById(bookId);

        if (book.isEmpty()) {
            throw new Exception("Book not found");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        book.get().setCopies(book.get().getCopies() + 1);

        bookRepository.save(book.get());
    }

    public void decreaseBookQuantity(Long bookId) throws Exception {

        Optional<Book> book = bookRepository.findById(bookId);

        if (book.isEmpty() || book.get().getCopiesAvailable() <= 0 || book.get().getCopies() <= 0) {
            throw new Exception("Book not found or quantity locked");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        book.get().setCopies(book.get().getCopies() - 1);

        bookRepository.save(book.get());
    }

    public void postBook(AddBookRequest addBookRequest) {
        Book book = new Book()
                .setTitle(addBookRequest.getTitle())
                .setAuthor(addBookRequest.getAuthor())
                .setDescription(addBookRequest.getDescription())
                .setCopies(addBookRequest.getCopies())
                .setCopiesAvailable(addBookRequest.getCopies())
                .setCategory(addBookRequest.getCategory())
                .setImg(addBookRequest.getImg());
        bookRepository.save(book);
    }

    public void deleteBook(Long bookId) throws Exception {

        Optional<Book> book = bookRepository.findById(bookId);

        if (book.isEmpty()) {
            throw new Exception("Book not found");
        }

        bookRepository.delete(book.get());
        checkoutRepository.deleteAllByBookId(bookId);
        reviewRepository.deleteAllByBookId(bookId);
    }
}
