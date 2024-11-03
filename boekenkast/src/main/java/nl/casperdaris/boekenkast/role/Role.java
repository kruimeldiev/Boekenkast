package nl.casperdaris.boekenkast.role;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.casperdaris.boekenkast.user.User;

// TODO: Add documentation, do we need all these fields?
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
@EntityListeners(AuditingEntityListener.class)
public class Role {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDate lastModifiedDate;
}
