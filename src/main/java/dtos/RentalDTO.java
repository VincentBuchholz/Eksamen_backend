package dtos;

import entities.Rental;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RentalDTO {
    private int id;
    private String start;
    private String end;
    private double price;
    private double deposit;
    private String contact;
    private int houseID;
    private int tenantID;


    List<TenantDTO> tenants = new ArrayList<>();

    public RentalDTO(Rental rental) {
        this.id = rental.getId();
        this.start = rental.getStartDate();
        this.end = rental.getEndDate();
        this.price = rental.getPriceAnnual();
        this.deposit = rental.getDeposit();
        this.contact = rental.getContactPerson();
    }

    public RentalDTO(String start, String end, double price, double deposit, String contact, int houseID, int tenantID) {
        this.start = start;
        this.end = end;
        this.price = price;
        this.deposit = deposit;
        this.contact = contact;

        if(houseID > 0){
        this.houseID = houseID;
        }
        if(tenantID >0){
        this.tenantID = tenantID;
        }
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public int getTenantID() {
        return tenantID;
    }

    public void setTenantID(int tenantID) {
        this.tenantID = tenantID;
    }

    public List<TenantDTO> getTenants() {
        return tenants;
    }

    public void addTenant(TenantDTO tenantDTO){
        this.tenants.add(tenantDTO);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentalDTO rentalDTO = (RentalDTO) o;
        return id == rentalDTO.id && Double.compare(rentalDTO.price, price) == 0 && Double.compare(rentalDTO.deposit, deposit) == 0 && Objects.equals(start, rentalDTO.start) && Objects.equals(end, rentalDTO.end) && Objects.equals(contact, rentalDTO.contact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end, price, deposit, contact);
    }
}
