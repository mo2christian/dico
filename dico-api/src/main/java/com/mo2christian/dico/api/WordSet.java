package com.mo2christian.dico.api;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Iterator;

public class WordSet implements Iterable<String> {

    private String gsLocation;

    public WordSet(String gsLocation){
        this.gsLocation = gsLocation;
    }

    @Override
    public Iterator<String> iterator() {
        try {
            URI uri = new URI(gsLocation);
            Storage storage = StorageOptions.getDefaultInstance().getService();
            BlobId blobId = BlobId.of(uri.getHost(), uri.getPath().substring(1));
            byte[] bytes = storage.readAllBytes(blobId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
            return new WordIterator(reader);
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private class WordIterator implements Iterator<String> {

        private String next;

        private BufferedReader reader;

        public WordIterator(BufferedReader reader){
            this.reader = reader;
        }

        @Override
        public boolean hasNext() {
            try {
                next = reader.readLine();
                if (next == null){
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
            return next != null;
        }

        @Override
        public String next() {
            return next;
        }

    }


}
