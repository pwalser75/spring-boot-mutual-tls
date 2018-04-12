package ch.frostnova.spring.boot.mutual.tls.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Configuration test
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigTest {

    @Autowired
    private ExampleConfiguration config;

    @Test
    public void testConfiguration() {

        Assert.assertNotNull(config);
        Assert.assertEquals("Example text", config.getText());
        Assert.assertEquals(12345, config.getNumber());
        Assert.assertNotNull(config.getOptions());
        Assert.assertTrue(config.getOptions().contains("one"));
        Assert.assertTrue(config.getOptions().contains("two"));
        Assert.assertTrue(config.getOptions().contains("three"));
        Assert.assertFalse(config.getOptions().contains("four"));
        Assert.assertNotNull(config.getList());
        Assert.assertEquals(3, config.getList().size());
        Assert.assertEquals("first", config.getList().get(0));
        Assert.assertEquals("second", config.getList().get(1));
        Assert.assertEquals("third", config.getList().get(2));
        Assert.assertTrue(config.getOptions().contains("two"));
        Assert.assertTrue(config.getOptions().contains("three"));
        Assert.assertFalse(config.getOptions().contains("four"));
        Assert.assertNotNull(config.getProperties());
        Assert.assertEquals("bla", config.getProperties().get("foo"));
        Assert.assertEquals("3.14159265", config.getProperties().get("pi"));
    }
}
