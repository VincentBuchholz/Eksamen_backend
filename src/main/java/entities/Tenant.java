package entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tenant")
@NamedQuery(name = "Tenant.deleteAllRows", query = "DELETE from Tenant ")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "job")
    private String job;

    @ManyToMany(mappedBy = "tenants")
    private Set<Rental> rentals = new HashSet<>();

    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public Tenant() {
    }

    public void removeUser(User user){
        this.user = null;
        if(user.getTenant().equals(this)){
            user.removeTenant();
        }
    }

    public void removeRental(Rental rental){
        this.rentals.remove(rental);
        if (rental.getTenants().contains(this)){
            rental.removeTenant(this);
        }
    }

    public Tenant(String name, String phone, String job) {
        this.name = name;
        this.phone = phone;
        this.job = job;
    }

    public void addRental(Rental rental){
        this.rentals.add(rental);
        if(!rental.getTenants().contains(this)){
            rental.getTenants().add(this);
        }
    }

    public Set<Rental> getRentals() {
        return rentals;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if(user.getTenant() != this){
            user.setTenant(this);
        }
    }
}
