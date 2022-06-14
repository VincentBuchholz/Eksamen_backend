package dtos;

import entities.Rental;

public class RentalDTO {
    private int id;
    private String start;
    private String end;
    private double price;
    private double deposit;
    private String contact;

    public RentalDTO(Rental rental) {
        this.id = rental.getId();
        this.start = rental.getStartDate();
        this.end = rental.getEndDate();
        this.price = rental.getPriceAnnual();
        this.deposit = rental.getDeposit();
        this.contact = rental.getContactPerson();
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
}
