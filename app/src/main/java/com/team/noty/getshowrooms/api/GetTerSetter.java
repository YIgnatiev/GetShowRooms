package com.team.noty.getshowrooms.api;


import com.google.gson.annotations.SerializedName;

/**
 * Created by o_cure on 1/23/16.
 */
public class GetTerSetter {

    @SerializedName("id")
    String id;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("coord_x")
    String lat;
    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }

    @SerializedName("coord_y")
    String lon;
    public String getLon() {
        return lon;
    }
    public void setLon(String lon) {
        this.lon = lon;
    }

    @SerializedName("name")
    String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("rating")
    String rating;
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }

    @SerializedName("reviews")
    String reviews;
    public String getReviews() {
        return reviews;
    }
    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    @SerializedName("price")
    String price;
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    @SerializedName("instagram")
    String instagram;
    public String getInstagram() {
        return instagram;
    }
    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    @SerializedName("facebook")
    String facebook;
    public String getFacebook() {
        return facebook;
    }
    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    @SerializedName("address")
    String address;
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    @SerializedName("metro")
    String metro;
    public String getMetro() {
        return metro;
    }
    public void setMetro(String metro) {
        this.metro = metro;
    }

    @SerializedName("worktime")
    String worktime;
    public String getWorktime() {
        return worktime;
    }
    public void setWorktime(String worktime) {
        this.worktime = worktime;
    }

    @SerializedName("description")
    String description;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @SerializedName("comment")
    String comment;
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    @SerializedName("best")
    String best;
    public String getBest() {
        return best;
    }
    public void setBest(String best) {
        this.best = best;
    }


    @SerializedName("city")
    String city;
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }


    @SerializedName("district")
    String district;
    public String getDistrict() {
        return district;
    }
    public void setDistrict(String city) {
        this.district = district;
    }

    @SerializedName("telephone")
    String telephone;
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String city) {
        this.telephone = telephone;
    }

}