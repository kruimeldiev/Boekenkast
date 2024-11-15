package nl.casperdaris.boekenkast.book;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nl.casperdaris.boekenkast.common.BaseEntity;
import nl.casperdaris.boekenkast.feedback.Feedback;
import nl.casperdaris.boekenkast.history.BookTransactionHistory;
import nl.casperdaris.boekenkast.user.User;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
@Entity
public class Book extends BaseEntity {

    private String title;
    private String author;
    private String isbn;
    private String synopsis;
    private String publisher;
    private String coverUrl;
    private boolean isArchived;
    private boolean isSharable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedback;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> transactionHistory;
}
