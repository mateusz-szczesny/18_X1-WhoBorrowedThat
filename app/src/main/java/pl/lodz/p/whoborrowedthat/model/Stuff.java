package pl.lodz.p.whoborrowedthat.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Stuff implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("borrower")
    private User borrower;

    @SerializedName("owner")
    private User owner;

    @SerializedName("name")
    private String name;

    @SerializedName("desc")
    private String desc;

    @SerializedName("photo_uri")
    private String photoUri;

    @SerializedName("rental_date")
    private Date rentalDate;

    @SerializedName("estimated_return_date")
    private Date estimatedReturnDate;

    @SerializedName("return_date")
    private Date returnDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Date getEstimatedReturnDate() {
        return estimatedReturnDate;
    }

    public void setEstimatedReturnDate(Date estimatedReturnDate) {
        this.estimatedReturnDate = estimatedReturnDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Stuff() {
        borrower = new User();
        owner = new User();
        rentalDate = new Date();
        estimatedReturnDate = new Date();
        returnDate = new Date();
    }
}
