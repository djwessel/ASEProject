package com.ase.aat_android.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Wrapper class for datastore CoursePojo
 * Created by anahitik on 02.02.17.
 */

public class CoursePojo implements Serializable{
    com.aat.datastore.Course course;

    public CoursePojo(com.aat.datastore.Course course) {
        this.course = course;
    }

    public Long getID() {
        return course.getId();
    }

    public String getTitle() {
        return course.getTitle();
    }

    public int getRequiredAttendances() {
        return course.getReqAtten();
    }

    public int getRequiredPresentation() {
        return course.getReqPresent();
    }

    private void writeObject(ObjectOutputStream o) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(o, course);
    }

    private void readObject(ObjectInputStream o) throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        course = (com.aat.datastore.Course) mapper.readValue(o, com.aat.datastore.Course.class);
    }
}
