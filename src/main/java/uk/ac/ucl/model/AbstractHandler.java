package uk.ac.ucl.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.util.*;


public abstract class AbstractHandler<T> extends Model {
    private static final String MAIN_PATH = "data";

    final File metadataFile;
    final File specificDirectory;

    final ObjectMapper objectMapper = new ObjectMapper();

    Map<UUID,T> contents;


    public AbstractHandler(String specificDirectoryName, String metadataFilename) {
        metadataFile = new File(MAIN_PATH + File.separator + specificDirectoryName + File.separator + metadataFilename);
        specificDirectory = new File(MAIN_PATH + File.separator + specificDirectoryName);
        contents = new HashMap<>();
        readContents();
    }


    public T getContentFromID(UUID contentID) {
        return contents.get(contentID);
    }


    public ArrayList<UUID> getContentIDs() {
        return new ArrayList<UUID>(contents.keySet());
    }


    public ArrayList<T> getContents() {
        return new ArrayList<T>(contents.values());
    }


    public void readContents() {
        if(ensureCorrectDirectoryAndFileSetup()) {
            try(BufferedReader reader = new BufferedReader(new FileReader(metadataFile))) {
                String contentID;
                while((contentID = reader.readLine()) != null) {
                    readContent(contentID);
                }
            } catch (IOException e) {
                resetHandler();
            }
        }
    }


    private boolean ensureCorrectDirectoryAndFileSetup() {
        try {
            return ensureSpecificDirectoryExists() && ensureMetadataFileExists();
        } catch(IOException e) {
            return false;
        }
    }


    private boolean ensureSpecificDirectoryExists() throws IOException {
        return specificDirectory.exists() || specificDirectory.mkdirs();
    }


    private boolean ensureMetadataFileExists() throws IOException {
        return metadataFile.exists() || metadataFile.createNewFile();
    }


    private void readContent(String id) {
        File contentFile = new File(specificDirectory, id + ".json");
        UUID contentID = UUID.fromString(id);
        try {
            T content = contentFile.exists() ? addActualContentToSystem(contentFile) : null;
            if(content != null) {
                addContentToSystem(contentID, content);
            }
        } catch (Exception e) {
            return;
        }
    }


    abstract T addActualContentToSystem(File contentFile) throws IOException;


    public void addContentToSystem(UUID contentID, T content) {
        T hadPrevious = contents.put(contentID, content);
        if(hadPrevious == null) {
            addContentIDToMetadataFile(contentID);
        }
        saveContent(content);
    }


    public boolean isIDInMetadataFile(UUID contentID) throws IOException {
        return Files.readAllLines(metadataFile.toPath()).stream().anyMatch(line -> line.contains(contentID.toString()));
    }


    void addContentIDToMetadataFile(UUID contentID) {
        try(FileWriter writer = new FileWriter(metadataFile, true)) {
            if(!isIDInMetadataFile(contentID)) {
                writer.write(contentID + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void saveContents() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(metadataFile))) {
            for (Map.Entry<UUID, T> entry : contents.entrySet()) {
                saveContent(entry.getValue());
                writer.write(entry.getKey() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        updateContentsMetadata();
    }


    public abstract void saveContent(T content);


    public void updateContentsMetadata() {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(metadataFile))) {
            for(UUID contentID : contents.keySet()) {
                writer.write(contentID + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void removeContentFromSystem(UUID contentID) {
        File contentFile = new File(specificDirectory, contentID + ".json");
        if(contentFile.delete()) {
            contents.remove(contentID);
        }
        updateContentsMetadata();
    }


    public void resetHandler() {
        removeDirectory();
        makeDirectory();
        createMetadataFile();
        resetHandlerState();
    }


    private void removeDirectory() {
        removeFilesInDirectory();
        removeFile(specificDirectory);
    }


    private void removeFilesInDirectory() {
        Arrays.asList(Objects.requireNonNull(specificDirectory.listFiles())).forEach(this::removeFile);
    }


    private void removeFile(File file) {
        if(!file.delete()) {
            throw new RuntimeException("Error: Files have not been deleted.");
        }
    }


    private void makeDirectory() {
        if(!specificDirectory.mkdirs()) {
            throw new RuntimeException("Error: Directory has not been created.");
        }
    }


    private void createMetadataFile() {
        try {
            if(!metadataFile.createNewFile()) {
                throw new IOException();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error: Directory has not been created.");
        }
    }


    protected void resetHandlerState() {
        contents = new HashMap<>();
    }
}
