package nl.casperdaris.boekenkast.feedback;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nl.casperdaris.boekenkast.book.Book;
import nl.casperdaris.boekenkast.common.BaseEntity;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Feedback extends BaseEntity {

    private Double rating;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

}
