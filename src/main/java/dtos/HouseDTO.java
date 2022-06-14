package dtos;

import entities.House;

import java.util.Objects;

public class HouseDTO {
    private int id;
    private String address;
    private String city;
    private int rooms;
    private String img;

    public HouseDTO(House house) {
        this.id = house.getId();
        this.address = house.getAddress();
        this.city = house.getCity();
        this.rooms = house.getNumberOfRooms();
        this.img = house.getImage();
    }

    public HouseDTO(String address, String city, int rooms) {
        this.address = address;
        this.city = city;
        this.rooms = rooms;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public int getRooms() {
        return rooms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HouseDTO houseDTO = (HouseDTO) o;
        return id == houseDTO.id && rooms == houseDTO.rooms && Objects.equals(address, houseDTO.address) && Objects.equals(city, houseDTO.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, city, rooms);
    }
}
