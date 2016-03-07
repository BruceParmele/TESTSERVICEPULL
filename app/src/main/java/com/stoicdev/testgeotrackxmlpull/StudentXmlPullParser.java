package com.stoicdev.testgeotrackxmlpull;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;

public class StudentXmlPullParser {

    static final String KEY_SITE = "CallOut";
    static final String KEY_FIRST_NAME = "FirstName";
    static final String KEY_LAST_NAME = "LastName";
    static final String KEY_SUBJECT_ID = "SubjectId";
    static final String KEY_EVENT_TITLE = "Title";
    static final String KEY_EVENT_LOCATION = "Location";
    static final String KEY_EVENT_DATE_TIME = "StartDt";

    public static List<StackStudent> getStackStudentFromFile(Context ctx) {

        // List of StackSites that we will return
        List<StackStudent> stackStudent;
        stackStudent = new ArrayList<StackStudent>();

        // temp holder for current StackSite while parsing
        StackStudent curStackStudent = null;
        // temp holder for current text value while parsing
        String curText = "";

        try {
            // Get our factory and PullParser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            // Open up InputStream and Reader of our file.
            FileInputStream fis = ctx.openFileInput("StackStudents.xml");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            // point the parser to our file.
            xpp.setInput(reader);

            // get initial eventType
            int eventType = xpp.getEventType();

            // Loop through pull events until we reach END_DOCUMENT
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Get the current tag
                String tagname = xpp.getName();

                // React to different event types appropriately
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase(KEY_SITE)) {
                            // If we are starting a new <site> block we need
                            //a new StackSite object to represent it
                            curStackStudent = new StackStudent();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        //grab the current text so we can use it in END_TAG event
                        curText = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase(KEY_SITE)) {
                            // if </site> then we are done with current Site
                            // add it to the list.
                            stackStudent.add(curStackStudent);
                        } else if (tagname.equalsIgnoreCase(KEY_FIRST_NAME)) {
                            // if </name> use setFirstName() on curSite
                            curStackStudent.setFirstName(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_LAST_NAME)) {
                            // if </link> use setLastName() on curSite
                            curStackStudent.setLastName(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_SUBJECT_ID)) {
                            // if </about> use setSubjectID() on curSite
                            curStackStudent.setSubjectID(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_EVENT_TITLE)) {
                            // if </image> use setEventTitle() on curSite
                            curStackStudent.setEventTitle(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_EVENT_LOCATION)){
                            curStackStudent.setEventLocation(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_EVENT_DATE_TIME)){
                            curStackStudent.setEventDateTime(curText);
                        }

                        break;

                    default:
                        break;
                }
                //move on to next iteration
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return the populated list.
        return stackStudent;
    }
}
