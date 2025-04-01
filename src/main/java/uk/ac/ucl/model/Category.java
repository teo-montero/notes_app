package uk.ac.ucl.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Category {
    private final UUID id;
    private String name;
    private final ArrayList<UUID> notes;


    public Category(UUID id, String name, ArrayList<UUID> notes) {
        this.id = id;
        this.name = name;
        this.notes = notes;
    }


    public Category(UUID id, String name) {
        this(id, name, new ArrayList<>());
    }


    public Category(String name) {
        this(UUID.randomUUID(), name);
    }


    public Category(UUID id) {
        this(id, "New Category");
    }


    public Category() {
        this("New Category");
    }


    public UUID getID() {
        return id;
    }


    public String getName() {
        return name;
    }

    public ArrayList<UUID> getNotes() {
        return notes;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void addNote(Note note) {
        UUID noteID = note.getID();
        if(!notes.contains(noteID)) {
            notes.add(noteID);
            note.addCategory(id);
        }
    }


    public void removeNote(Note note) {
        UUID noteID = note.getID();
        if(notes.remove(noteID)) {
            note.removeCategory(id);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
