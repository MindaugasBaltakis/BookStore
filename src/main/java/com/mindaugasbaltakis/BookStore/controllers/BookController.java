package com.mindaugasbaltakis.BookStore.controllers;

import com.mindaugasbaltakis.BookStore.entities.Book;
import com.mindaugasbaltakis.BookStore.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BookController {

    private final BookService bookService;
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("/books")
    public List<Book> findAllBooks() {
        return bookService.findAllBooks();
    }

    @GetMapping("/book/{barcode}")
    public ResponseEntity<?> findBook(@PathVariable Long barcode) {
        Optional<Book> book = bookService.findBook(barcode);
        //checking if book with given barcode exists
        if(book.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(book);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book with this barcode does not exist.");
        }
    }

    @GetMapping("/book/price/{barcode}")
    public ResponseEntity<?> calculatePrice(@PathVariable Long barcode) {
        Optional<Book> book = bookService.findBook(barcode);
        //checking if book with given barcode exists
        if(book.isPresent()){
            //checking if book is antique or not
            if(book.get().getReleaseYear()!=null){
                Double totalPrice = book.get().getPrice() * book.get().getQuantity() *
                        (Calendar.getInstance().get(Calendar.YEAR) - book.get().getReleaseYear())/50;
                return ResponseEntity.status(HttpStatus.OK).body(totalPrice);
            } else{
                Double totalPrice = book.get().getPrice() * book.get().getQuantity();
                return ResponseEntity.status(HttpStatus.OK).body(totalPrice);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book with this barcode does not exist.");
        }
    }


    @PostMapping("/book")
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        //checking for dublicate barcodes
        if(bookService.findBook(book.getBarcode()).isPresent()){
            return ResponseEntity.badRequest().body("Book with this barcode already exists.");
        } else {
            bookService.addBook(book);
            return ResponseEntity.status(HttpStatus.OK).body("Book was added.");
        }

    }

    @PutMapping("/book/{barcode}")
    public ResponseEntity<?> updateBook(@PathVariable Long barcode, @RequestBody Book book) {
        Optional<Book> bookToUpdate = bookService.findBook(barcode);
        if(bookToUpdate.isPresent()){
            bookService.updateBook(book, bookToUpdate.get().getId());
            return ResponseEntity.status(HttpStatus.OK).body("Book was updated.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book with this barcode does not exist.");
        }




    }

}
