package com.ase.aat_android.data;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Wrapper class for datastore GroupPojo.
 *
 * Created by anahitik on 02.02.17.
 */

public class GroupPojo implements Serializable {
    private Long parentID;
    private Long ID;
    private String name;

    public GroupPojo(Long parentID, Long ID, String name) {
        this.parentID = parentID;
        this.ID = ID;
        this.name = name;
    }

    public GroupPojo(Long parentID, LinkedHashMap<String, Object> entry) {
        this.parentID = parentID;
        this.ID = (Long) entry.get("id");
        this.name = (String) entry.get("name");
    }

    public Long getParentID() {
        return parentID;
    }

    public Long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        // Not always the case
        com.aat.datastore.Group g = (com.aat.datastore.Group) obj;
        return g.getId() == ID && g.getName() == name;
    }
}
