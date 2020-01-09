package com.example.flickrbrowser;

import androidx.annotation.NonNull;

import java.io.Serializable;

class Photo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String memberTitle;
    private String memberAuthor;
    private String memberAuthorId;
    private String memberImage;
    private String memberLink;
    private String memberTags;

    public Photo(String memberTitle, String memberAuthor, String memberAuthorId, String memberImage, String memberLink, String memberTags) {
        this.memberTitle = memberTitle;
        this.memberAuthor = memberAuthor;
        this.memberAuthorId = memberAuthorId;
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

    String getMemberAuthorId() {
        return memberAuthorId;
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
                "Author id is " + memberAuthorId + "\n" +
                "Tags is " + memberTags + "\n" +
                "Image is" + memberImage + "\n" +
                "Link is" + memberLink + "\n}\n";
    }
}
