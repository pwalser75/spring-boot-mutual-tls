package ch.frostnova.spring.boot.mutual.tls;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class FileSystemTest {

    @Test
    void testFileSystemPerformance() throws IOException {
        var time = System.nanoTime();
        var count = new AtomicInteger();
        visit(new File("/media/piwi/Walserfamily"), Integer.MAX_VALUE, count);
        time = System.nanoTime() - time;
        System.out.printf("Total: %s files, %.3f sec, %.3f ms per file\n", count, time * 0.000000001f, time * 0.000001f / count.get());
    }

    private void visit(File file, int depth, AtomicInteger count) throws IOException {
        if (!file.exists()) {
            return;
        }
        count.incrementAndGet();
        var attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);

        String info = file.getAbsolutePath() +
                ": isFile: " + file.isFile() +
                ", isDirectory: " + file.isDirectory() +
                ", canRead: " + file.canRead() +
                ", canWrite: " + file.canWrite() +
                ", length: " + file.length() +
                ", isDirectory: " + attributes.isDirectory() +
                ", isRegularFile: " + attributes.isRegularFile() +
                ", creationTime: " + attributes.creationTime() +
                ", lastAccessTime: " + attributes.lastAccessTime() +
                ", lastModifiedTime: " + attributes.lastModifiedTime();

        System.out.println(info);

        if (info.length()>0 && depth > 0 && file.isDirectory()) {
            var files = file.list();
            if (files != null) {
                for (var f : files) {
                    visit(new File(file, f), depth - 1, count);
                }
            }
        }

    }
}
