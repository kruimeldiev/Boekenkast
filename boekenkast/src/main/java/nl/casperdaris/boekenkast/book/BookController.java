package nl.casperdaris.boekenkast.book;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.casperdaris.boekenkast.common.PaginationResponse;

/// Deze controller bevat alle endpoints voor het beheer van boeken.
@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book", description = "Book endpoints")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Integer> createBook(
            @Valid @RequestBody BookRequest request,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.createOrUpdateBook(request, connectedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<BookResponse>> getAllBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.findAllBooks(page, size, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PaginationResponse<BookResponse>> getAllBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.findAllBooksByOwner(page, size, connectedUser));
    }

    @GetMapping("/borrowed-books")
    public ResponseEntity<PaginationResponse<BorrowedBookResponse>> getAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.findAllBorrowedBooks(page, size, connectedUser));
    }

    @GetMapping("/returned-books")
    public ResponseEntity<PaginationResponse<BorrowedBookResponse>> getAllReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.findAllReturnedBooks(page, size, connectedUser));
    }

    @PatchMapping("/sharable/{book-id}")
    public ResponseEntity<Integer> setSharableStatus(@PathVariable("book-id") Integer bookId,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.setSharableStatus(bookId, connectedUser));
    }

    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<Integer> setArchivedStatus(@PathVariable("book-id") Integer bookId,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.setArchivedStatus(bookId, connectedUser));
    }

    @PostMapping("/borrow-book/{book-id}")
    public ResponseEntity<Integer> borrowBook(@PathVariable("book-id") Integer bookId,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.borrowBook(bookId, connectedUser));
    }

    @PatchMapping("/return-book/{book-id}")
    public ResponseEntity<Integer> returnBorrowedBook(@PathVariable("book-id") Integer bookId,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.returnBorrowedBook(bookId, connectedUser));
    }

    @PostMapping("/approve-return/{book-id}")
    public ResponseEntity<Integer> approveBookReturn(@PathVariable("book-id") Integer bookId,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.approveBookReturn(bookId, connectedUser));
    }
}
