package com.nexus.unify.ModelClasses;

import java.io.Serializable;

public class BugReport implements Serializable {

    private String id;
    private String name;
    private String contact;
    private String bugDescription;

    public BugReport() {
        // Default constructor required for calls to DataSnapshot.getValue(BugReport.class)
    }

    public BugReport(String id,String name,String contact, String bugDescription) {
        this.id = id;
        this.bugDescription = bugDescription;
        this.name = name;
        this.contact = contact;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBugDescription() {
        return bugDescription;
    }

    public void setBugDescription(String bugDescription) {
        this.bugDescription = bugDescription;
    }
}
