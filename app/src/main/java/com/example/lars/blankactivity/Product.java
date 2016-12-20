package com.example.lars.blankactivity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lars on 10-11-2016.
 */

public class Product implements Parcelable {

    private String name;
    private int quantity;
    private String comment;

    public Product(String name, int quantity, String comment){

        this.quantity = quantity;
        this.comment = comment;
        this.name=name;


    }
    public Product(){}
    // get and set method

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(comment);
        dest.writeInt(quantity);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    // "De-parcel object
    public Product(Parcel in) {
        name = in.readString();
        comment = in.readString();
        quantity = in.readInt();
    }

    @Override
    public String toString() {
        return this.quantity+" x "+name+"\n"+comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {

        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getComment() {
        return comment;
    }
}
