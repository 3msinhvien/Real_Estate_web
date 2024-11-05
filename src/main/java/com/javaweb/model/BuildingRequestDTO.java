package com.javaweb.model;

public class BuildingRequestDTO {
    private Long id;
    private String name;
    private String ward;
    private String street;
    private String districtId;
    private Long rentprice;
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
    public String getWard() {
        return ward;
    }
    public void setWard(String ward) {
        this.ward = ward;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getDistrictId() {
        return districtId;
    }
    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }
    public Long getRentprice() {
        return rentprice;
    }
    public void setRentprice(Long rentprice) {
        this.rentprice = rentprice;
    }

    

}
