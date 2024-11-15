package nl.casperdaris.boekenkast.book;

import org.springframework.stereotype.Service;

/// Deze klasse bevat de mappers voor het omzetten van boeken DTO's van en naar objecten voor de database.
@Service
public class BookMapper {

    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .author(request.author())
                .isbn(request.isbn())
                .synopsis(request.synopsis())
                .isArchived(false)
                .isSharable(request.isSharable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .isSharable(book.isSharable())
                .isArchived(book.isArchived())
                .owner(book.getOwner().getUsername())
                // .coverUrl(book.getCoverUrl())
                .rating(book.getRating())
                .build();
    }
}
