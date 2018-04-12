package ch.frostnova.spring.boot.mutual.tls.ws;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ch.frostnova.spring.boot.mutual.tls.api.model.Note;

import javax.ws.rs.NotFoundException;

/**
 * Note endpoint test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteEndpointTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @LocalServerPort
    private int port;

    @Test
    public void testCRUD() {

        final String baseURL = "https://localhost:" + port + "/api/notes";
        log.info("BASE URL: " + baseURL);
        try (final NoteClient noteClient = new NoteClient(baseURL)) {

            // create

            Note note = new Note();
            note.setText("Aloha");

            Note created = noteClient.create(note);
            Assert.assertNotNull(created);
            Assert.assertNotNull(created.getId());
            Assert.assertEquals(note.getText(), created.getText());
            long id = created.getId();
            note = created;

            // read

            Note loaded = noteClient.get(id);
            Assert.assertNotNull(loaded);
            Assert.assertNotNull(loaded.getId());
            Assert.assertEquals(note.getText(), loaded.getText());

            // list

            Assert.assertTrue(noteClient.list().stream().anyMatch(p -> p.getId() == id));

            // update

            note.setText("Lorem ipsum dolor sit amet");
            noteClient.save(note);

            loaded = noteClient.get(id);
            Assert.assertNotNull(loaded);
            Assert.assertEquals(note.getId(), loaded.getId());
            Assert.assertEquals(note.getText(), loaded.getText());

            // delete

            noteClient.delete(id);

            // delete again - must not result in an exception
            noteClient.delete(id);

            // must not be found afterwards
            Assert.assertFalse(noteClient.list().stream().anyMatch(p -> p.getId() == id));

            try {
                noteClient.get(id);
                Assert.fail("Expected: NotFoundException");
            } catch (NotFoundException expected) {
                //
            }
        }
    }
}
