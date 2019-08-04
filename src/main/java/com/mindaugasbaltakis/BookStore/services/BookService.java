package com.mindaugasbaltakis.BookStore.services;

import com.mindaugasbaltakis.BookStore.entities.Book;
import com.mindaugasbaltakis.BookStore.repositories.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public List<Book> findAllBooks() { return bookRepository.findAll(); }

    public Optional<Book> findBook(Long barcode){
        return bookRepository.findByBarcode(barcode);
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Book updatedBook, Integer oldBookId) {
        updatedBook.setId(oldBookId);
        return bookRepository.save(updatedBook);
    }

    }

