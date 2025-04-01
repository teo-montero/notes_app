package uk.ac.ucl.model;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;


public class CategoryHandler extends AbstractHandler<Category> {
    private static final String SPECIFIC_CATEGORIES_DIRECTORY = "categories";
    private static final String CATEGORIES_METADATA_FILENAME = "INFO_categoriesMetadataList.txt";

    private final ArrayList<UUID> hiddenCategories;


    public CategoryHandler() {
        super(SPECIFIC_CATEGORIES_DIRECTORY, CATEGORIES_METADATA_FILENAME);
        hiddenCategories = new ArrayList<>();
    }


    @Override
    Category addActualContentToSystem(File contentFile) throws IOException {
        return objectMapper.readValue(contentFile, Category.class);
    }


    @Override
    public void saveContent(Category content) {
        File categoryFile = new File(super.specificDirectory, content.getID() + ".json");
        try {
            objectMapper.writeValue(categoryFile, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void addNoteToCategory(Note note, UUID categoryID) {
        Category category = contents.get(categoryID);
        category.addNote(note);
        saveContent(category);
    }


    public void removeNoteFromCategory(Note note, UUID categoryID) {
        Category category = contents.get(categoryID);
        category.removeNote(note);
        saveContent(category);
    }


    public void hideCategory(UUID categoryID) {
        hiddenCategories.add(categoryID);
    }


    public void showCategory(UUID categoryID) {
        hiddenCategories.remove(categoryID);
    }


    public boolean isCategoryHidden(UUID categoryID) {
        return hiddenCategories.contains(categoryID);
    }


    public boolean isCategory(UUID id) {
        return contents.containsKey(id);
    }


    public void checkNotesExist() {
        contents.values().forEach(this::checkNotesInCategory);
    }


    public void checkNotesInCategory(Category category) {
        category.getNotes().removeIf(noteID -> !ModelFactory.getModel().getNoteHandler().isNote(noteID));
    }
}
