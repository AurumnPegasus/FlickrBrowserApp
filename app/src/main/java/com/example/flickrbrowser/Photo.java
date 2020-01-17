package com.example.flickrbrowser;

import androidx.annotation.NonNull;
import java.io.Serializable;

class Photo implements Serializable {

    //Serializable:
    //Java provides a mechanism, called object serialization where an object can be represented as a sequence of bytes
    // that includes the object's data as well as information about the object's type and the types of data stored in the object.
    //
    //After a serialized object has been written into a file, it can be read from the file and deserialized
    // that is, the type information and bytes that represent the object and its data can be used to recreate the object in memory.
    //https://www.tutorialspoint.com/java/java_serialization.htm
    //
    //Serializability of a class is enabled by the class implementing the java.io.Serializable interface.
    // Classes that do not implement this interface will not have any of their state serialized or deserialized.
    // All subtypes of a serializable class are themselves serializable.
    // The serialization interface has no methods or fields and serves only to identify the semantics of being serializable.
    //https://developer.android.com/reference/java/io/Serializable

    private static final long serialVersionUID = 1L;

    //The serialVersionUID is a universal version identifier for a Serializable class.
    // Deserialization uses this number to ensure that a loaded class corresponds exactly to a serialized object.
    // If no match is found, then an InvalidClassException is thrown.
    //https://stackoverflow.com/questions/14274480/static-final-long-serialversionuid-1l

    private String memberTitle;
    private String memberAuthor;
    private String memberImage;
    private String memberLink;
    private String memberTags;

    public Photo(String memberTitle, String memberAuthor,String memberImage, String memberLink, String memberTags) {
        this.memberTitle = memberTitle;
        this.memberAuthor = memberAuthor;
        this.memberImage = memberImage;
        this.memberLink = memberLink;
        this.memberTags = memberTags;
    }

    String getMemberTitle() {
        return memberTitle;
    }

    String getMemberAuthor() {
        return memberAuthor;
    }

    String getMemberImage() {
        return memberImage;
    }

    String getMemberLink() {
        return memberLink;
    }

    String getMemberTags() {
        return memberTags;
    }

    @NonNull
    @Override
    public String toString() {
        return "Photo{ " +
                "Title is " + memberTitle + "\n" +
                "Author is" + memberAuthor + "\n" +
                "Tags is " + memberTags + "\n" +
                "Image is" + memberImage + "\n" +
                "Link is" + memberLink + "\n}\n";
    }
}
