package ch.frostnova.spring.boot.mutual.tls.persistence;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring JPA repository for Notes
 */
public interface NoteRepository extends CrudRepository<NoteEntity, Long> {
}
