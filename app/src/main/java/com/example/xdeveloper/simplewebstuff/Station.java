package com.example.xdeveloper.simplewebstuff;

/**
 * Created by nissy on 23/10/2016.
 */

public class Station {

    public final String name_en;
    public final String name_tc;
    public final String packing_no;
    public final String image_link;
    public final double distance;
    public final double lat;
    public final double lng;
    public final int id;

    public final String type;
    public final String provider;
    public final String address_en;
    public final String address_tc;


    public Station(int id, String name_tc, String name_en, String packing_no, double lat, double lng, String image_link) {
        this.id = id;
        this.name_en = name_en;
        this.name_tc = name_tc;
        this.packing_no = packing_no;
        this.lat = lat;
        this.lng = lng;
        this.image_link = image_link;
        this.distance = 0;
        this.type = "";
        this.provider = "";
        this.address_en = "";
        this.address_tc = "";
    }

    public Station(int id, String name_tc, String name_en, String packing_no, double lat, double lng, String image_link, double distance) {
        this.id = id;
        this.name_en = name_en;
        this.name_tc = name_tc;
        this.packing_no = packing_no;
        this.lat = lat;
        this.lng = lng;
        this.image_link = image_link;
        this.distance = distance;
        this.type = "";
        this.provider = "";
        this.address_en = "";
        this.address_tc = "";
    }

    public Station(int id, String name_tc, String name_en, String packing_no, double lat, double lng, String image_link, double distance, String type, String provider, String address_en ,String address_tc) {
        this.id = id;
        this.name_en = name_en;
        this.name_tc = name_tc;
        this.packing_no = packing_no;
        this.lat = lat;
        this.lng = lng;
        this.image_link = image_link;
        this.distance = distance;
        this.type = type;
        this.provider = provider;
        this.address_en = address_en;
        this.address_tc = address_tc;
    }

    public String get_name_en() {
        return this.name_en;
    }
    public String get_name_tc() {
        return this.name_tc;
    }

    public String get_type() {
        return this.type;
    }
    public String get_provider() {
        return this.provider;
    }
    public String get_address_en() {
        return this.address_en;
    }

    public String get_address_tc() {
        return this.address_tc;
    }

    public String get_image() {
        return "https://www.clp.com.hk/" + this.image_link;
    }

    public int get_id() {
        return this.id;
    }

    public String get_string_id(){
        return new Integer(this.id).toString();
    }

    public double get_lat() {
        return this.lat;
    }

    public String get_packing_no() {
        return this.packing_no;
    }

    public double get_lng() {
        return this.lng;
    }

    public double get_m_distance() {
        return this.distance;
    }

    public int get_km_distance() {
        return (int)this.distance/1000;
    }

    @Override
    public String toString() {
        return this.name_en + " " + this.packing_no + " " + this.lat + " " + this.lng;
    }
}