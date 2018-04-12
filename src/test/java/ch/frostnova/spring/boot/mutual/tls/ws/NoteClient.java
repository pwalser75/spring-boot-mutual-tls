package ch.frostnova.spring.boot.mutual.tls.ws;

import ch.frostnova.spring.boot.mutual.tls.api.model.Note;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.logging.LoggingFeature;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.List;

/**
 * Note client API
 */
public class NoteClient implements AutoCloseable {

    private final String baseURL;
    private final Client client;

    public NoteClient(String baseURL) {
        this.baseURL = baseURL;
        ClientBuilder clientBuilder = createClientBuilder();
        client = clientBuilder.build();
    }

    public void close() {
        client.close();
    }

    private static ClientBuilder createClientBuilder() {

        try (InputStream in = NoteClient.class.getResourceAsStream("/client-truststore.jks")) {
            KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
            truststore.load(in, "truststore".toCharArray());

            return ClientBuilder.newBuilder()
                    .trustStore(truststore)
                    .property(ClientProperties.CONNECT_TIMEOUT, 500)
                    .property(ClientProperties.READ_TIMEOUT, 5000)
					.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY)
					.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "WARNING")
                    .hostnameVerifier((hostname, sslSession) -> "localhost".equals(hostname));
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load client truststore", ex);
        }
    }

    /**
     * List all notes
     *
     * @return list of notes (never null)
     */
    public List<Note> list() {
        Invocation invocation = client
                .target(baseURL)
                .request()
                .buildGet();

        Response response = ResponseExceptionMapper.check(invocation.invoke(), 200);
        return response.readEntity(new GenericType<List<Note>>() {
        });
    }

    /**
     * Get a note by id. Throws a {@link javax.ws.rs.NotFoundException} if the note wasn't found.
     *
     * @param id id
     * @return note.
     */
    public Note get(long id) {
        Invocation invocation = client
                .target(baseURL + "/" + id)
                .request()
                .buildGet();

        Response response = ResponseExceptionMapper.check(invocation.invoke(), 200);
        return response.readEntity(Note.class);
    }

    /**
     * Create a new note with the provided data
     *
     * @param note data
     * @return created note
     */
    public Note create(Note note) {
        Invocation invocation = client
                .target(baseURL)
                .request()
                .buildPost(Entity.json(note));

        Response response = ResponseExceptionMapper.check(invocation.invoke(), 200);
        return response.readEntity(Note.class);
    }

    /**
     * Update a note
     *
     * @param note note (whose id is required)
     */
    public void save(Note note) {

        if (note.getId() == null) {
            throw new IllegalArgumentException("Not yet persisted, use the create() method instead");
        }

        Invocation invocation = client
                .target(baseURL + "/" + note.getId())
                .request()
                .buildPut(Entity.json(note));

        ResponseExceptionMapper.check(invocation.invoke(), 204);
    }

    /**
     * Delete the note with the given id, if it exists (no error thrown otherwise).
     *
     * @param id id of the record
     */
    public void delete(long id) {

        Invocation invocation = client
                .target(baseURL + "/" + id)
                .request()
                .buildDelete();

        ResponseExceptionMapper.check(invocation.invoke(), 204);
    }

}
