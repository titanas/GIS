package gis;

import java.io.*;

public class FilesReader {

    public FilesReader() {
    }

    public String [] readFiles (String dir, String ext) {

        File file = new File (dir);
        FilenameFilter ff = new OnlyExt(ext);

        String [] files = file.list(ff);
        for(int i = 0; i < files.length; i++) {
            files[i] = dir.concat(files[i]);
        }
        return files;
    }
}
