package pl.lodz.p.whoborrowedthat.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserRelation implements Serializable {

    @SerializedName("id")
    private long id;
    @SerializedName("related_user")
    private User relatedUser;
    @SerializedName("relating_user")
    private User relatingUser;

    public UserRelation(long id, User relatedUser, User relatingUser) {
        this.id = id;
        this.relatedUser = relatedUser;
        this.relatingUser = relatingUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getRelatedUser() {
        return relatedUser;
    }

    public void setRelatedUser(User relatedUser) {
        this.relatedUser = relatedUser;
    }

    public User getRelatingUser() {
        return relatingUser;
    }

    public void setRelatingUser(User relatingUser) {
        this.relatingUser = relatingUser;
    }
}
