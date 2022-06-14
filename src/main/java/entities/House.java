package entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "house")
@NamedQuery(name = "House.deleteAllRows", query = "DELETE from House")
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "number_of_rooms")
    private int numberOfRooms;

    @Column(name = "img_url")
    private String image;

    @OneToMany(mappedBy = "house")
    private Set<Rental> rentals = new HashSet<>();

    public House() {
    }

    public House(String address, String city, int numberOfRooms, String image) {
        this.address = address;
        this.city = city;
        this.numberOfRooms = numberOfRooms;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(Set<Rental> rentals) {
        this.rentals = rentals;
    }

    public void addRental(Rental rental){
        this.rentals.add(rental);
        if(rental.getHouse() != this){
            rental.setHouse(this);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }
}
