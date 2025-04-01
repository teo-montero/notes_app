package uk.ac.ucl.model;

import jakarta.servlet.http.Part;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class NoteHandler extends AbstractHandler<Note> {
    private static final String SPECIFIC_NOTES_DIRECTORY = "notes";
    private static final String NOTES_METADATA_FILENAME = "INFO_notesMetadataList.txt";


    public NoteHandler() {
        super(SPECIFIC_NOTES_DIRECTORY, NOTES_METADATA_FILENAME);
    }


    @Override
    Note addActualContentToSystem(File contentFile) throws IOException {
        return objectMapper.readValue(contentFile, Note.class);
    }


    @Override
    public void saveContent(Note content) {
        File noteFile = new File(specificDirectory, content.getID() + ".json");
        try {
            objectMapper.writeValue(noteFile, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateNote(UUID noteID, String updatedTitle, ArrayList<String> updatedText, Part updatedImage, String imageAction) {
        Note note = contents.get(noteID);
        note.setTitle(updatedTitle);
        note.setTextContent(updatedText);
        addImageToNote(updatedImage, imageAction, noteID);
        note.updateLastEditDate();
        saveContent(note);
    }


    public UUID cloneNote(UUID noteID) {
        Note clonedNote = contents.get(noteID).cloneNote(true);
        addContentToSystem(clonedNote.getID(), clonedNote);
        return clonedNote.getID();
    }


    public void addImageToNote(Part image, String imageAction, UUID noteID) {
        ImageHandler imageHandler = ModelFactory.getModel().getImageHandler();
        Note note = contents.get(noteID);
        if("remove".equals(imageAction)) {
            note.setImage("");
        } else if("upload".equals(imageAction)) {
            if(!image.getSubmittedFileName().isEmpty()) {
                String imagePath = imageHandler.saveImage(image);
                note.setImage(imagePath);
            }
        }
    }


    public boolean isNote(UUID id) {
        return contents.containsKey(id);
    }


    public void checkCategoriesExist() {
        contents.values().forEach(this::checkCategoriesInNote);
    }


    public void checkCategoriesInNote(Note note) {
        note.getCategories().removeIf(categoryID -> !ModelFactory.getModel().getCategoryHandler().isCategory(categoryID));
    }


    public ArrayList<UUID> searchQueryInNotes(String query) {
        return getSortedMatches(getMatchingNotes(query));
    }


    private ArrayList<Map.Entry<UUID, Double>> getMatchingNotes(String query) {
        ArrayList<Map.Entry<UUID, Double>> scoredNotes = new ArrayList<>();
        for (Map.Entry<UUID, Note> entry : contents.entrySet()) {
            double score = entry.getValue().doesNoteContainQuery(query);
            if (score > 0) {
                scoredNotes.add(new AbstractMap.SimpleEntry<>(entry.getKey(), score));
            }
        }
        return scoredNotes;
    }


    private ArrayList<UUID> getSortedMatches(ArrayList<Map.Entry<UUID, Double>> matches) {
        sortMatches(matches);
        return matches.stream().map(Map.Entry::getKey).collect(Collectors.toCollection(ArrayList::new));
    }


    private void sortMatches(ArrayList<Map.Entry<UUID, Double>> matches) {
        matches.sort((id, match) -> Double.compare(match.getValue(), id.getValue()));
    }
}
