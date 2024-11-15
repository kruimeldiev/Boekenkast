package nl.casperdaris.boekenkast.history;

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
import nl.casperdaris.boekenkast.user.User;

/// Deze class representeerd de many-to-many relatie tussen het uitlenen van een Book naar een User
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BookTransactionHistory extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private boolean returned;
    private boolean returnApprovedByOwner;
}
