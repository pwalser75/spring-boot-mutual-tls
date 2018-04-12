package ch.frostnova.spring.boot.mutual.tls.persistence;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Note Entity
 */
@Entity
@Table(name = "note")
public class NoteEntity {

    private static final long serialVersionUID = -1166461789114073327L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "text", length = 2048, nullable = false)
    private String text;

    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return getId() + ": " + getText();
    }

    public boolean isPersistent() {
        return id != null;
    }

    @PrePersist
    private void setAuditDates() {
        lastModifiedDate = LocalDateTime.now();
        if (creationDate == null) {
            creationDate = lastModifiedDate;
        }
    }
}
