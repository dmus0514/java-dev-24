package ru.otus.web.helpers;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@SuppressWarnings({"squid:S112"})
public final class FileSystemHelper {

    private FileSystemHelper() {}

    public static String localFileNameOrResourceNameToFullPath(String fileOrResourceName) {
        String path = null;
        File file = new File(String.format("./%s", fileOrResourceName));
        if (file.exists()) {
            path = URLDecoder.decode(file.toURI().getPath(), StandardCharsets.UTF_8);
        }

        if (path == null) {
            URL url = FileSystemHelper.class.getClassLoader().getResource(fileOrResourceName);
            if (url == null) {
                throw new RuntimeException(String.format("File \"%s\" not found", fileOrResourceName));
            }
            path = url.toExternalForm();
        }
        return path;
    }
}
