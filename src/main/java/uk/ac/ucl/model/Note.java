package uk.ac.ucl.model;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class Note extends Index {
    private final UUID id;
    private String title;
    private final String creationDate;
    private String lastEditDate;
    private final ArrayList<UUID> categories;
    private ArrayList<String> text;
    private String image;

    public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm");


    public Note(UUID id, String title, ArrayList<UUID> categories, ArrayList<String> text) {
        this.id = id;
        this.title = title;
        creationDate = formatDate(LocalDateTime.now());
        lastEditDate = formatDate(LocalDateTime.now());
        this.categories = categories;
        this.text = text;
        image = "";
    }


    public Note(String title, ArrayList<UUID> categories, ArrayList<String> text) {
        this(UUID.randomUUID(), title, categories, text);
    }


    public Note(String title, ArrayList<String> text) {
        this(title, new ArrayList<>(), text);
    }


    public Note(String title) {
        this(title, new ArrayList<>());
    }


    public Note(UUID id) {
        this(id, "Untitled Note", new ArrayList<>(), new ArrayList<>());
    }


    public Note() {
        this("Untitled Note");
    }


    private String formatDate(LocalDateTime date) {
        return date.format(dateFormat);
    }


    public UUID getID() {
        return id;
    }


    public String getTitle() {
        return title;
    }


    public String getCreationDate() {
        return creationDate;
    }


    public String getLastEditDate() {
        return lastEditDate;
    }


    public ArrayList<UUID> getCategories() {
        return categories;
    }


    public ArrayList<String> getTextContent() {
        return text;
    }


    public String getImage() {
        return image;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public void setTextContent(ArrayList<String> text) {
        this.text = text;
    }


    public void setImage(String image) {
        this.image = image;
    }


    public void updateLastEditDate() {
        lastEditDate = formatDate(LocalDateTime.now());
    }


    public void addCategory(UUID categoryID) {
        categories.add(categoryID);
    }


    public boolean belongsToCategory(UUID categoryID) {
        return categories.contains(categoryID);
    }


    public void removeCategory(UUID categoryID) {
        categories.remove(categoryID);
    }


    public Note cloneNote(boolean withoutDuplicateAnnotation) {
        Note clonedNote = new Note(title, new ArrayList<>(categories), new ArrayList<>(text));
        clonedNote.setImage(image);
        return clonedNote;
    }


    public double doesNoteContainQuery(String query) {
        return 0.7 * doesNoteTitleContainQuery(query) + 0.3 * doesNoteTextContainQuery(query);
    }


    public double doesNoteTitleContainQuery(String query) {
        return doesContentContainQuery(title.toLowerCase(), query);
    }


    public double doesNoteTextContainQuery(String query) {
        return doesContentContainQuery(String.join(" ", text).toLowerCase(), query);
    }


    private double doesContentContainQuery(String content, String query) {
        return (query == null || query.isEmpty()) ? 0 : getMatchPercentage(content, query);
    }


    private double getMatchPercentage(String content, String query) {
        String[] queryWords = query.toLowerCase().split("\\s+");
        return (double) Arrays.stream(queryWords).filter(content::contains).count() / queryWords.length;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(id, note.id);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", creationDate=" + creationDate +
                ", lastEditDate=" + lastEditDate +
                ", categories=" + categories +
                ", text=" + text +
                '}';
    }
}
