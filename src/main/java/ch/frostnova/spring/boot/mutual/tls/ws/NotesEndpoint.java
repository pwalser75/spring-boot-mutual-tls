package ch.frostnova.spring.boot.mutual.tls.ws;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ch.frostnova.spring.boot.mutual.tls.api.model.Note;
import ch.frostnova.spring.boot.mutual.tls.api.service.NoteService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Notes web service endpoint <p>
 * Full local path: <a href="https://localhost/api/notes">https://localhost/api/notes</a>
 */
@Component
@Path("notes")
@Api(value = "Notes resource", produces = "application/json")
public class NotesEndpoint {

    @Autowired
    private NoteService noteService;

    /**
     * List notes
     *
     * @return list of notes (never null)
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Lists all notes", response = Note.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful")
    })
    public List<Note> list() {
        return noteService.list();
    }

    /**
     * Get a record by id. If the record was not found, a NoSuchElementException will be thrown (resulting in a 404 NOT FOUND).
     *
     * @param id id of the record
     * @return record
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Note get(@PathParam("id") long id) {
        Note result = noteService.get(id);
        if (result != null) {
            return result;
        }
        throw new NoSuchElementException();
    }

    /**
     * Create a new record (or updates an existing record, when the id is set).
     *
     * @param note record to create
     * @return created record
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Note save(Note note) {
        return noteService.save(note);
    }

    /**
     * Update a record
     *
     * @param id   id of the record to update
     * @param note new data to set
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@PathParam("id") long id, Note note) {
        note.setId(id);
        noteService.save(note);
    }

    /**
     * Delete a record
     *
     * @param id id of the record
     */
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") long id) {
        noteService.delete(id);
    }
}
