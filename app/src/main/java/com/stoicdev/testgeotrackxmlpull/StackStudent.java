package com.stoicdev.testgeotrackxmlpull;

/**
 * Created by bp on 11/17/2015.
 */
public class StackStudent {

    private String firstName;  //name
    private String lastName;   //link
    private String subjectID;  //about
    private String eventTitle; //imgUrl
    private String eventLocation;
    private String eventDateTime;

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getSubjectID() {
        return subjectID;
    }
    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }
    public String getEventTitle() {
        return eventTitle;
    }
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
    public String getEventLocation() {
        return eventLocation;
    }
    public void setEventLocation(String eventLocation) {this.eventLocation = eventLocation;}
    public String getEventDateTime(){return eventDateTime;}
    public void setEventDateTime(String eventDateTime) {this.eventDateTime = eventDateTime;}

    @Override
    public String toString() {
        return "StackStudent [firstName=" + firstName + ", lastName=" + lastName + ", subjectID="
                + subjectID + ", eventTitle=" + eventTitle + ", eventLocation" + eventLocation + "]";
    }
}
