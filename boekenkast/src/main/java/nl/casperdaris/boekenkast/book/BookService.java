package nl.casperdaris.boekenkast.book;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nl.casperdaris.boekenkast.common.PaginationResponse;
import nl.casperdaris.boekenkast.exceptions.ForbiddenException;
import nl.casperdaris.boekenkast.file.FileStorageService;
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
    private final FileStorageService fileStorageService;

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

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id " + bookId));
        if (book.isArchived() || !book.isSharable()) {
            throw new ForbiddenException("Book is not available for borrowing");
        }
        User user = ((User) connectedUser.getPrincipal());
        if (book.getOwner().getId() == user.getId()) {
            throw new ForbiddenException("Not allowed to borrow your own book");
        }
        final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowed(bookId, user.getId());
        if (isAlreadyBorrowed) {
            throw new ForbiddenException("The rquested book is already borrowed");
        }
        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApprovedByOwner(false)
                .build();
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id " + bookId));
        if (book.isArchived() || !book.isSharable()) {
            throw new ForbiddenException("Book is not available for return");
        }
        User user = ((User) connectedUser.getPrincipal());
        if (book.getOwner().getId() == user.getId()) {
            throw new ForbiddenException("User cannot return their own book");
        }
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndUserId(bookId,
                user.getId())
                .orElseThrow(() -> new ForbiddenException(
                        "No book transaction history found for book " + bookId + " and user " + user.getId()));
        bookTransactionHistory.setReturned(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer approveBookReturn(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id " + bookId));
        if (book.isArchived() || !book.isSharable()) {
            throw new ForbiddenException("Book is not available for return");
        }
        User user = ((User) connectedUser.getPrincipal());
        if (book.getOwner().getId() != user.getId()) {
            throw new ForbiddenException("User cannot approve return of book they do not own");
        }
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId,
                user.getId())
                .orElseThrow(() -> new ForbiddenException(
                        "Book is not yet returned"));
        bookTransactionHistory.setReturnApprovedByOwner(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public void uploadBookCoverImage(MultipartFile file, Authentication connectedUser, Integer bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id " + bookId));
        User user = ((User) connectedUser.getPrincipal());
        var bookCoverImage = fileStorageService.uploadBookCoverImage(file, user.getId());
        book.setCoverUrl(bookCoverImage);
        bookRepository.save(book);
    }
}
