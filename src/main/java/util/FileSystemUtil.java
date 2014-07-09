package util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Dmitry on 6/29/2014.
 */
public class FileSystemUtil {

    public static InputStream getResourceAsStream(String path) {
        return FileSystemUtil.class.getResourceAsStream(path);
    }

    public static URL getResourceURL(String path) {
        return FileSystemUtil.class.getResource(path);
    }
}
