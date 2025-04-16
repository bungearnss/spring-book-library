package com.learning.spring_boot_library.services;

import com.learning.spring_boot_library.models.entity.Book;
import com.learning.spring_boot_library.models.entity.Checkout;
import com.learning.spring_boot_library.models.entity.History;
import com.learning.spring_boot_library.models.response.ShelfCurrentLoansResponse;
import com.learning.spring_boot_library.repositories.BookRepository;
import com.learning.spring_boot_library.repositories.CheckoutRepository;
import com.learning.spring_boot_library.repositories.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CheckoutRepository checkoutRepository;

    @Autowired
    private HistoryRepository historyRepository;

    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book checkoutBook(String userEmail, Long bookId) throws Exception {
        Book book = bookRepository.findById(bookId).orElse(new Book().setId(-1L));

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (book.getId().equals(-1L) || validateCheckout != null || book.getCopiesAvailable() <= 0) {
            throw new Exception("Book doesn't exist or already checked out by user");
        }

        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        bookRepository.save(book);
        Checkout checkout = new Checkout()
                .setUserEmail(userEmail)
                .setBookId(book.getId())
                .setCheckoutDate(LocalDate.now().toString())
                .setReturnDate(LocalDate.now().plusDays(7).toString());
        checkoutRepository.save(checkout);

        return book;
    }

    public Boolean checkoutBookByUser(String userEmail, Long bookId) {
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        return validateCheckout != null;
    }

    public int currentLoansCount(String userEmail) {
        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {
        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses = new ArrayList<>();

        List<Checkout> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);
        List<Long> bookIdList = new ArrayList<>();

        for (Checkout i : checkoutList) {
            bookIdList.add(i.getBookId());
        }

        List<Book> books = bookRepository.findBooksByBookIds(bookIdList);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (Book book : books) {
            Optional<Checkout> checkout = checkoutList.stream()
                    .filter(x -> Objects.equals(x.getBookId(), book.getId())).findFirst();
            if (checkout.isPresent()) {

                Date d1 = sdf.parse(checkout.get().getReturnDate());
                Date d2 = sdf.parse(LocalDate.now().toString());

                TimeUnit time = TimeUnit.DAYS;

                long differentInTime = time.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);

                shelfCurrentLoansResponses.add(new ShelfCurrentLoansResponse().setBook(book).setDaysLeft((int) differentInTime));
            }
        }
        return shelfCurrentLoansResponses;
    }

    public void returnBook (String userEmail, Long bookId) throws Exception {
        Book book = bookRepository.findById(bookId).orElse(new Book().setId(-1L));

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (book.getId().equals(-1L) || validateCheckout == null){
            throw new Exception("Book doesn't exist or already checked out by user");
        }

        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        bookRepository.save(book);
        checkoutRepository.deleteById(validateCheckout.getId());

        History history = new History()
                .setUserEmail(userEmail)
                .setCheckoutDate(validateCheckout.getCheckoutDate())
                .setReturnedDate(LocalDate.now().toString())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setDescription(book.getDescription())
                .setImg(book.getImg());
        historyRepository.save(history);
    }
}