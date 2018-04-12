package ch.frostnova.spring.boot.mutual.tls.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import ch.frostnova.spring.boot.mutual.tls.api.model.Note;
import ch.frostnova.spring.boot.mutual.tls.api.service.NoteService;
import ch.frostnova.spring.boot.mutual.tls.persistence.NoteEntity;
import ch.frostnova.spring.boot.mutual.tls.persistence.NoteRepository;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@EnableTransactionManagement
@Transactional(readOnly = true)
/**
 * Implementation of the NoteService
 */
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository repository;

    @Override
    public Note get(long id) {
        return convert(repository.findOne(id));
    }

    @Override
    @Transactional(readOnly = false)
    public Note save(Note note) {
        NoteEntity entity = new NoteEntity();
        if (note.getId() != null) {
            entity = repository.findOne(note.getId());
        }
        return convert(repository.save(update(entity, note)));
    }

    @Override
    public List<Note> list() {
        Spliterator<NoteEntity> spliterator = repository.findAll().spliterator();
        Stream<NoteEntity> stream = StreamSupport.stream(spliterator, false);
        return stream.map(this::convert).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(long id) {
        if (repository.exists(id)) {
            repository.delete(id);
        }
    }

    private Note convert(NoteEntity entity) {
        if (entity == null) {
            return null;
        }
        Note dto = new Note();
        dto.setId(entity.getId());
        dto.setText(entity.getText());
        dto.setCreationDate(entity.getCreationDate());
        dto.setModificationDate(entity.getLastModifiedDate());
        return dto;
    }

    private NoteEntity update(NoteEntity entity, Note dto) {
        if (dto == null) {
            return null;
        }
        entity.setId(dto.getId());
        entity.setText(dto.getText());
        return entity;
    }
}
