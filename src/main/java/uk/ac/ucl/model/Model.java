package uk.ac.ucl.model;

import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Model
{
    private NoteHandler noteHandler;
    private CategoryHandler categoryHandler;
    private ImageHandler imageHandler;
    private IndexHandler indexHandler;


    public void initialiseModel() {
        noteHandler = new NoteHandler();
        categoryHandler = new CategoryHandler();
        imageHandler = new ImageHandler();
        indexHandler = new IndexHandler();
        noteHandler.checkCategoriesExist();
        categoryHandler.checkNotesExist();
    }


    public NoteHandler getNoteHandler() {
        return noteHandler;
    }


    public CategoryHandler getCategoryHandler() {
        return categoryHandler;
    }


    public ImageHandler getImageHandler() { return imageHandler; }


    public IndexHandler getIndexHandler() { return indexHandler; }


    public void shutdown() {
        noteHandler.saveContents();
        categoryHandler.saveContents();
        indexHandler.saveContents();
    }
}
