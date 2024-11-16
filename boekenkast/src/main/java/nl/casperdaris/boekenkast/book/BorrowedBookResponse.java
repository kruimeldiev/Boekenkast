package nl.casperdaris.boekenkast.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/// DTO voor het response object van een geleend boek.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedBookResponse {

    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private double rating;
    private boolean isReturnApprovedByOwner;
    private boolean isReturned;
}
