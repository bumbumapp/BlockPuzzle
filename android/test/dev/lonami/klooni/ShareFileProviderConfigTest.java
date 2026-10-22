package dev.lonami.klooni;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.junit.Test;

/**
 * Regression test for the share crash on Android 7+ (FileUriExposedException): sharing must go
 * through a FileProvider that exposes the external cache dir used by AndroidShareChallenge.
 */
public class ShareFileProviderConfigTest {

    private static String read(String path) throws Exception {
        File f = new File(path);
        if (!f.exists()) f = new File("android", path); // run from root or module dir
        return new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
    }

    @Test
    public void manifestDeclaresFileProviderWithMatchingAuthority() throws Exception {
        String manifest = read("AndroidManifest.xml");
        assertTrue(manifest.contains("androidx.core.content.FileProvider"));
        assertTrue(manifest.contains("${applicationId}.fileprovider"));
        assertTrue(manifest.contains("@xml/file_paths"));
    }

    @Test
    public void filePathsExposeExternalCacheDir() throws Exception {
        String paths = read("res/xml/file_paths.xml");
        assertTrue(paths.contains("<external-cache-path"));
    }
}
