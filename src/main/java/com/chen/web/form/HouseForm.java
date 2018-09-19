package com.chen.web.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by ccong
 */
public class HouseForm {
    private Long id;

    @NotNull(message = "Titile should not be null!")
    @Size(min = 1, max = 30, message = "Message length should between 1 and 30")
    private String title;

    @NotNull(message = "Must choose the city")
    @Size(min = 1, message = "City not exist")
    private String cityEnName;

    @NotNull(message = "Must choose the region")
    @Size(min = 1, message = "Region not exist")
    private String regionEnName;

    @NotNull(message = "Must choose the street")
    @Size(min = 1, message = "Street not exist")
    private String street;

    @NotNull(message = "Must choose the district")
    private String district;

    @NotNull(message = "Must input the detailAddress!")
    @Size(min = 1, max = 30, message = "Length should between 1 and 30")
    private String detailAddress;

    @NotNull(message = "Must input the room number")
    @Min(value = 1, message = "Not good number")
    private Integer room;

    private int parlour;

    @NotNull(message = "Must input the floor")
    private Integer floor;

    @NotNull(message = "Must input the total floor")
    private Integer totalFloor;

    @NotNull(message = "Must input the direction")
    private Integer direction;

    @NotNull(message = "Must input the build year")
    @Min(value = 1900, message = "Not good")
    private Integer buildYear;

    @NotNull(message = "Must input the area")
    @Min(value = 1)
    private Integer area;

    @NotNull(message = "Must input the price")
    @Min(value = 1)
    private Integer price;

    @NotNull(message = "Must choose the rent way")
    @Min(value = 0)
    @Max(value = 1)
    private Integer rentWay;

    private Long subwayLineId;

    private Long subwayStationId;

    private int distanceToSubway = -1;

    private String layoutDesc;

    private String roundService;

    private String traffic;

    @Size(max = 255)
    private String description;

    //FIXME
    private String cover="house.jpg";

    private List<String> tags;

    private List<PhotoForm> photos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCityEnName() {
        return cityEnName;
    }

    public void setCityEnName(String cityEnName) {
        this.cityEnName = cityEnName;
    }

    public String getRegionEnName() {
        return regionEnName;
    }

    public void setRegionEnName(String regionEnName) {
        this.regionEnName = regionEnName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public int getParlour() {
        return parlour;
    }

    public void setParlour(int parlour) {
        this.parlour = parlour;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getTotalFloor() {
        return totalFloor;
    }

    public void setTotalFloor(Integer totalFloor) {
        this.totalFloor = totalFloor;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Integer getBuildYear() {
        return buildYear;
    }

    public void setBuildYear(Integer buildYear) {
        this.buildYear = buildYear;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getRentWay() {
        return rentWay;
    }

    public void setRentWay(Integer rentWay) {
        this.rentWay = rentWay;
    }

    public Long getSubwayLineId() {
        return subwayLineId;
    }

    public void setSubwayLineId(Long subwayLineId) {
        this.subwayLineId = subwayLineId;
    }

    public Long getSubwayStationId() {
        return subwayStationId;
    }

    public void setSubwayStationId(Long subwayStationId) {
        this.subwayStationId = subwayStationId;
    }

    public int getDistanceToSubway() {
        return distanceToSubway;
    }

    public void setDistanceToSubway(int distanceToSubway) {
        this.distanceToSubway = distanceToSubway;
    }

    public String getLayoutDesc() {
        return layoutDesc;
    }

    public void setLayoutDesc(String layoutDesc) {
        this.layoutDesc = layoutDesc;
    }

    public String getRoundService() {
        return roundService;
    }

    public void setRoundService(String roundService) {
        this.roundService = roundService;
    }

    public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<PhotoForm> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoForm> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "HouseForm{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", cityEnName='" + cityEnName + '\'' +
                ", regionEnName='" + regionEnName + '\'' +
                ", district='" + district + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                ", room=" + room +
                ", parlour=" + parlour +
                ", floor=" + floor +
                ", totalFloor=" + totalFloor +
                ", direction=" + direction +
                ", buildYear=" + buildYear +
                ", area=" + area +
                ", price=" + price +
                ", rentWay=" + rentWay +
                ", subwayLineId=" + subwayLineId +
                ", subwayStationId=" + subwayStationId +
                ", distanceToSubway=" + distanceToSubway +
                ", layoutDesc='" + layoutDesc + '\'' +
                ", roundService='" + roundService + '\'' +
                ", traffic='" + traffic + '\'' +
                ", description='" + description + '\'' +
                ", cover='" + cover + '\'' +
                ", photos=" + photos +
                '}';
    }
}
