package edu.depaul.cdm.se452.fall2023group1.libraryproject.bookreservations;

import edu.depaul.cdm.se452.fall2023group1.bookreservations.BookReservation;
import edu.depaul.cdm.se452.fall2023group1.bookreservations.BookReservationRepository;
import edu.depaul.cdm.se452.fall2023group1.bookreservations.BookReservationService;
import edu.depaul.cdm.se452.fall2023group1.bookreservations.ReservationType;
import edu.depaul.cdm.se452.fall2023group1.books.Book;
import edu.depaul.cdm.se452.fall2023group1.books.BookRepository;
import edu.depaul.cdm.se452.fall2023group1.user.User;
import edu.depaul.cdm.se452.fall2023group1.user.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class BookReservationRepositoryTest {

    @Autowired
    private BookReservationRepository bookReservationRepository;

    @Autowired
    private BookReservationService service;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(0)
    public void addReservation() {
        var beforeCount = (int) bookReservationRepository.count();
        Optional<Book> book = bookRepository.findById(1L);
        Optional<User> user = userRepository.findById(2L);
        BookReservation reservation = new BookReservation();
        reservation.setBook(book.get());
        reservation.setType(ReservationType.DIGITAL);
        reservation.setUser(user.get());
        reservation.setBorrowDate(new java.sql.Date( new java.util.Date().getTime() ));
        reservation.setReturnDate(new java.sql.Date( new java.util.Date().getTime() + 24*60*60*1000));
        bookReservationRepository.save(reservation);
        var afterCount = (int) bookReservationRepository.count();
        assertEquals(afterCount , beforeCount + 1);
    }

    @Test
    @Order(2)
    public void testGetReservationByUserId() {
        var beforeCount = (int) bookReservationRepository.count();
        List<BookReservation> reservation = bookReservationRepository.findByUserId(2);
        assertEquals(reservation.size(), 2);
    }


    @Test
    @Order(5)
    public void testUpdateReservation() {
        Optional<Book> book = bookRepository.findById(1L);
        Optional<User> user = userRepository.findById(2L);
        BookReservation reservation = new BookReservation();
        reservation.setBook(book.get());
        reservation.setType(ReservationType.DIGITAL);
        reservation.setUser(user.get());
        reservation.setBorrowDate(new java.sql.Date( new java.util.Date().getTime() ));
        reservation.setReturnDate(new java.sql.Date( new java.util.Date().getTime() + 24*60*60*1000));
        bookReservationRepository.save(reservation);
        Long savedId = reservation.getReservationId();
        assertThat(savedId).isNotNull(); // Ensure the ID is generated
        java.sql.Date now = new java.sql.Date( new java.util.Date().getTime() );
        java.sql.Date tomorrow= new java.sql.Date( now.getTime() + 12*60*60*1000);
        reservation.setBorrowDate(tomorrow);
        bookReservationRepository.save(reservation);
        Optional<BookReservation> found = bookReservationRepository.findById(savedId);
        assertThat(found.get().getBorrowDate()).isNotNull();
    }


    @Test
    @Order(6)
    public void testDeleteReservation() {
        Optional<Book> book = bookRepository.findById(1L);
        Optional<User> user = userRepository.findById(2L);
        BookReservation reservation = new BookReservation();
        reservation.setBook(book.get());
        reservation.setType(ReservationType.DIGITAL);
        reservation.setUser(user.get());
        reservation.setBorrowDate(new java.sql.Date( new java.util.Date().getTime() ));
        reservation.setReturnDate(new java.sql.Date( new java.util.Date().getTime() + 24*60*60*1000));
        bookReservationRepository.save(reservation);
        assertNotNull(reservation.getReservationId());
        // Delete the book and verify it was deleted
        bookReservationRepository.deleteById(reservation.getReservationId());
        Optional<BookReservation> found = bookReservationRepository.findById(reservation.getReservationId());
        assertThat(found).isEmpty();
    }

}
