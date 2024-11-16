package nl.casperdaris.boekenkast.book;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nl.casperdaris.boekenkast.common.PaginationResponse;
import nl.casperdaris.boekenkast.exceptions.ForbiddenException;
import nl.casperdaris.boekenkast.history.BookTransactionHistory;
import nl.casperdaris.boekenkast.history.BookTransactionHistoryRepository;
import nl.casperdaris.boekenkast.user.User;

/// Deze service bevat alle methoden die de boeken aan de database aanroepen.
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final BookMapper bookMapper;

    public Integer createOrUpdateBook(BookRequest request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Book book = bookMapper.toBook(request);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    public BookResponse getBookById(Integer id) {
        return bookRepository.findById(id)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id " + id));
    }

    public PaginationResponse<BookResponse> findAllBooks(Integer page, Integer size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pagable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pagable, user.getId());
        List<BookResponse> bookResponses = books.stream().map(bookMapper::toBookResponse).toList();
        return new PaginationResponse<>(bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast());
    }

    public PaginationResponse<BookResponse> findAllBooksByOwner(Integer page, Integer size,
            Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pagable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerId(user.getId()), pagable);
        List<BookResponse> bookResponses = books.stream().map(bookMapper::toBookResponse).toList();
        return new PaginationResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast());
    }

    public PaginationResponse<BorrowedBookResponse> findAllBorrowedBooks(Integer page, Integer size,
            Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pagable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBooks(pagable,
                user.getId());
        List<BorrowedBookResponse> borrowedBooks = allBorrowedBooks.stream().map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PaginationResponse<>(
                borrowedBooks,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast());
    }

    public PaginationResponse<BorrowedBookResponse> findAllReturnedBooks(Integer page, Integer size,
            Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pagable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BookTransactionHistory> allReturnedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(pagable,
                user.getId());
        List<BorrowedBookResponse> returnedBooks = allReturnedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PaginationResponse<>(
                returnedBooks,
                allReturnedBooks.getNumber(),
                allReturnedBooks.getSize(),
                allReturnedBooks.getTotalElements(),
                allReturnedBooks.getTotalPages(),
                allReturnedBooks.isFirst(),
                allReturnedBooks.isLast());
    }

    public Integer setSharableStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id " + bookId));
        User user = ((User) connectedUser.getPrincipal());
        if (book.getOwner().getId() != user.getId()) {
            throw new ForbiddenException("User is not the owner of this book");
        }
        book.setSharable(!book.isSharable());
        bookRepository.save(book);
        return book.getId();
    }

    public Integer setArchivedStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id " + bookId));
        User user = ((User) connectedUser.getPrincipal());
        if (book.getOwner().getId() != user.getId()) {
            throw new ForbiddenException("User is not the owner of this book");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return book.getId();
    }
}
