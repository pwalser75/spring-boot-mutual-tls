package ch.frostnova.spring.boot.mutual.tls.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ch.frostnova.spring.boot.mutual.tls.api.converter.LocalDateTimeConverter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for Note
 */
public class Note implements Serializable {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("text")
    private String text;

    @JsonProperty("creation_date")
    @JsonSerialize(using = LocalDateTimeConverter.Serializer.class)
    @JsonDeserialize(using = LocalDateTimeConverter.Deserializer.class)
    private LocalDateTime creationDate;

    @JsonProperty("modification_date")
    @JsonSerialize(using = LocalDateTimeConverter.Serializer.class)
    @JsonDeserialize(using = LocalDateTimeConverter.Deserializer.class)
    private LocalDateTime modificationDate;

    public Note() {
        //
    }

    public Note(String text) {
        this.text = text;
    }

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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "#" + id + ": " + text;
    }
}
