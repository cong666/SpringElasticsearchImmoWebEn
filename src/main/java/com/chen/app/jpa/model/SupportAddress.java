package com.chen.app.jpa.model;

import javax.persistence.*;

/**
 * Created by: ccong
 * Date: 18/8/26 下午7:33
 */
@Entity
@Table(name="support_address")
public class SupportAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //Parent Level
    @Column(name = "belong_to")
    private String belongTo;

    @Column(name = "en_name")
    private String enName;
    @Column(name="cn_name")
    private String cnName;
    //city or region
    private String level;

    public enum Level {
        CITY("city"),
        REGION("region");

        private String value;

        Level(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @Column(name="baidu_map_longitude")
    private double baiduMapLongitude;

    @Column(name="baidu_map_latitude")
    private double baiduMapLatitude;

    public static Level of(String value) {
        for(Level level : Level.values()) {
            if(level.getValue().equals(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public double getBaiduMapLongitude() {
        return baiduMapLongitude;
    }

    public void setBaiduMapLongitude(double baiduMapLongitude) {
        this.baiduMapLongitude = baiduMapLongitude;
    }

    public double getBaiduMapLatitude() {
        return baiduMapLatitude;
    }

    public void setBaiduMapLatitude(double baiduMapLatitude) {
        this.baiduMapLatitude = baiduMapLatitude;
    }
}
