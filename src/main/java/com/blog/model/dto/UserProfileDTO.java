package com.blog.model.dto;

public class UserProfileDTO {
    private Long id;
    private String name;
    private String bio;
    private String profilePictureUrl;

    public UserProfileDTO() {
    }

    public UserProfileDTO(Long id, String name, String bio, String profilePictureUrl) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
