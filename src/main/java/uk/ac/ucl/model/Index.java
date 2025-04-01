package uk.ac.ucl.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;
import java.util.function.Function;

public class Index {
    private final UUID id;
    private String name;
    private ArrayList<UUID> elements;
    private UUID parent;


    public Index(UUID id, String name, ArrayList<UUID> elements, UUID parent) {
        this.id = id;
        this.name = name;
        this.elements = elements;
        this.parent = parent;
    }


    public Index(String name, ArrayList<UUID> elements, UUID parent) {
        this(UUID.randomUUID(), name, elements, parent);
    }


    public Index(String name, UUID parent) {
        this(name, new ArrayList<>(), parent);
    }


    public Index(String name) {
        this(name, null);
    }


    public Index() {
        this("Untitled Index");
    }


    public UUID getID() {
        return id;
    }


    public String getName() {
        return name;
    }


    public ArrayList<UUID> getElements() {
        return elements;
    }


    public ArrayList<UUID> getElements(SortMode sortMode) {
        IndexHandler indexHandler = ModelFactory.getModel().getIndexHandler();
        NoteHandler noteHandler = ModelFactory.getModel().getNoteHandler();

        return switch(sortMode) {
            case NONE -> getElements();
            case NAME_ASCENDING -> getSortedElementsByNameAscending(indexHandler, noteHandler);
            case NAME_DESCENDING -> getSortedElementsByNameDescending(indexHandler, noteHandler);
            case TYPE_ASCENDING -> getSortedElementsByTypeAscending(indexHandler, noteHandler);
            case TYPE_DESCENDING -> getSortedElementsByTypeDescending(indexHandler, noteHandler);
            case DATE_ASCENDING -> getSortedElementsByDateAscending(indexHandler, noteHandler, Note::getLastEditDate);
            case DATE_DESCENDING -> getSortedElementsByDateDescending(indexHandler, noteHandler, Note::getLastEditDate);
        };
    }


    private ArrayList<UUID> getSortedElementsByNameAscending(IndexHandler indexHandler, NoteHandler noteHandler) {
        return getSortedElements(generateNameAscendingComparator(indexHandler, noteHandler));
    }


    private ArrayList<UUID> getSortedElementsByNameDescending(IndexHandler indexHandler, NoteHandler noteHandler) {
        return getSortedElements(generateNameAscendingComparator(indexHandler, noteHandler).reversed());
    }


    private Comparator<UUID> generateNameAscendingComparator(IndexHandler indexHandler, NoteHandler noteHandler) {
        return Comparator.comparing(id -> {
            String name = indexHandler.isIndex(id) ? indexHandler.getContentFromID(id).getName() : noteHandler.getContentFromID(id).getTitle();
            return name.toLowerCase();
        });
    }


    private ArrayList<UUID> getSortedElementsByTypeAscending(IndexHandler indexHandler, NoteHandler noteHandler) {
        return getSortedElements(generateTypeAscendingComparator(indexHandler, noteHandler));
    }


    private ArrayList<UUID> getSortedElementsByTypeDescending(IndexHandler indexHandler, NoteHandler noteHandler) {
        return getSortedElements(generateTypeAscendingComparator(indexHandler, noteHandler).reversed());
    }


    private Comparator<UUID> generateTypeAscendingComparator(IndexHandler indexHandler, NoteHandler noteHandler) {
        return Comparator.comparing(id -> indexHandler.isIndex(id) ? 0 : 1);
    }


    public ArrayList<UUID> getSortedElementsByDateAscending(IndexHandler indexHandler, NoteHandler noteHandler, Function<Note, String> dateGetter) {
        return getSortedElements(generateDateComparator(indexHandler, noteHandler, dateGetter, true));
    }


    public ArrayList<UUID> getSortedElementsByDateDescending(IndexHandler indexHandler, NoteHandler noteHandler, Function<Note, String> dateGetter) {
        return getSortedElements(generateDateComparator(indexHandler, noteHandler, dateGetter, false));
    }


    private Comparator<UUID> generateDateComparator(IndexHandler indexHandler, NoteHandler noteHandler, Function<Note, String> dateGetter, boolean ascending) {
        Comparator<LocalDateTime> dateComparator = ascending
                ? Comparator.naturalOrder()
                : Comparator.reverseOrder();
        return Comparator.comparing((UUID id) -> indexHandler.isIndex(id) ? 0 : 1).thenComparing((UUID id) -> {
            if(indexHandler.isIndex(id)) return null;
            String dateStr = dateGetter.apply(noteHandler.getContentFromID(id));
            try {
                return LocalDateTime.parse(dateStr, Note.dateFormat);
            } catch(DateTimeParseException e) {
                return LocalDateTime.MIN;
            }
        }, Comparator.nullsFirst(dateComparator));
    }


    private ArrayList<UUID> getSortedElements(Comparator<UUID> comparator) {
        return new ArrayList<>(elements.stream().sorted(comparator).toList());
    }


    public UUID getParent() {
        return parent;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setElements(ArrayList<UUID> elements) {
        this.elements = elements;
    }


    public void setParent(UUID parent) {
        this.parent = parent;
    }


    public void addElement(UUID element) {
        elements.add(element);
    }


    public void removeElement(UUID element) {
        elements.remove(element);
    }


    public Index cloneIndex() {
        return new Index(name + " (Copy)", parent);
    }


    public Index cloneIndex(boolean withoutDuplicateAnnotation) {
        return withoutDuplicateAnnotation ? new Index(name, parent) : cloneIndex();
    }


    public boolean hasNote(UUID noteID) {
        return elements.contains(noteID);
    }
}
