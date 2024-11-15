package nl.casperdaris.boekenkast.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/// DTO voor het response object van een boek.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private String synopsis;
    private boolean isSharable;
    private boolean isArchived;
    private String owner;
    private byte[] coverUrl;
    private double rating;

}
