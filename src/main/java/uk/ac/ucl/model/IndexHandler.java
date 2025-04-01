package uk.ac.ucl.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;


public class IndexHandler extends AbstractHandler<Index> {
    private static final String SPECIFIC_INDEX_DIRECTORY = "index";
    private static final String INDEX_METADATA_FILENAME = "INFO_indexMetadataList.txt";

    private UUID home;

    private SortMode sortingMode;

    public IndexHandler() {
        super(SPECIFIC_INDEX_DIRECTORY, INDEX_METADATA_FILENAME);
        if(contents.isEmpty() || contents.get(home) == null) {
            resetHandler();
        }
        sortingMode = SortMode.NONE;
    }


    public SortMode getSortingMode() {
        return sortingMode;
    }


    public void setSortingMode(SortMode sortingMode) {
        this.sortingMode = sortingMode;
    }


    public UUID createIndex(String indexName, UUID parentID) {
        Index newIndex = new Index(indexName, parentID);
        Index parentIndex = contents.get(parentID);
        parentIndex.addElement(newIndex.getID());
        saveContent(parentIndex);
        addContentToSystem(newIndex.getID(), newIndex);
        return newIndex.getID();
    }


    @Override
    Index addActualContentToSystem(File contentFile) throws IOException {
        Index newIndex = objectMapper.readValue(contentFile, Index.class);
        if(newIndex.getParent() == null) {
            home = newIndex.getID();
        }
        return newIndex;
    }


    @Override
    public void saveContent(Index content) {
        File indexFile = new File(specificDirectory, content.getID() + ".json");
        try {
            objectMapper.writeValue(indexFile, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void resetHandler() {
        super.resetHandler();
        Index defaultIndex = new Index("Home");
        home = defaultIndex.getID();
        addContentToSystem(defaultIndex.getID(), defaultIndex);
        addAllNotesToDefaultIndex(defaultIndex);
        saveContents();
    }


    private void addAllNotesToDefaultIndex(Index defaultIndex) {
        for(UUID noteID : ModelFactory.getModel().getNoteHandler().getContentIDs()) {
            defaultIndex.addElement(noteID);
        }
    }


    public void addContentToIndex(UUID contentID, UUID indexID) {
        contents.get(indexID).addElement(contentID);
        saveContent(contents.get(indexID));
    }


    public void removeContextFromIndex(UUID contentID, UUID indexID) {
        contents.get(indexID).removeElement(contentID);
        saveContent(contents.get(indexID));
    }


    public UUID getHomeIndex() {
        return home;
    }


    public boolean isIndex(UUID contentID) {
        return contents.containsKey(contentID);
    }


    public UUID duplicateIndex(UUID indexID, UUID parentID) {
        Index parentIndex = contents.get(parentID);
        Index originalIndex = contents.get(indexID);
        Index duplicatedIndex = originalIndex.cloneIndex(true);
        duplicatedIndex.setName(duplicatedIndex.getName() + " (Copy)");
        parentIndex.addElement(duplicatedIndex.getID());
        saveContent(parentIndex);
        addContentToSystem(duplicatedIndex.getID(), duplicatedIndex);
        for(UUID elementID : originalIndex.getElements()) {
            if(isIndex(elementID)) {
                duplicateIndex(elementID, duplicatedIndex.getID());
            } else {
                NoteHandler noteHandler = ModelFactory.getModel().getNoteHandler();
                duplicatedIndex.addElement(noteHandler.cloneNote(elementID));
            }
        }
        saveContent(duplicatedIndex);
        return duplicatedIndex.getID();
    }


    public void removeIndex(UUID indexID) {
        Index index = contents.get(indexID);
        for(UUID elementID : index.getElements()) {
            if(isIndex(elementID)) {
                removeIndex(elementID);
            } else {
                NoteHandler noteHandler = ModelFactory.getModel().getNoteHandler();
                noteHandler.removeContentFromSystem(elementID);
            }
        }
        removeContentFromSystem(indexID);
    }


    public ArrayList<Index> getIndexPath(UUID indexID) {
        Index index = contents.get(indexID);
        ArrayList<Index> path = index.getParent() == null ? new ArrayList<>() : getIndexPath(index.getParent());
        path.add(index);
        return path;
    }


    public UUID getIndexToWhichNoteBelongs(UUID noteID) {
        for(Map.Entry<UUID, Index> entry : contents.entrySet()) {
            if(entry.getValue().hasNote(noteID)) {
                return entry.getKey();
            }
        }
        return home;
    }
}
