package dtos;

import entities.Tenant;

public class TenantDTO {
    private int id;
    private String name;
    private String phone;
    private String job;
    private int userID;
    private String username;
    private String password;

    public TenantDTO(Tenant tenant) {
        this.id = tenant.getId();
        this.name = tenant.getName();
        this.phone = tenant.getPhone();
        this.job = tenant.getJob();
        this.userID = tenant.getUser().getId();
    }

    public TenantDTO(String name,String phone, String job, String username, String password) {
        this.name = name;
        this.job = job;
        this.phone = phone;
        this.username = username;
        this.password = password;
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

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "TenantDTO{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", job='" + job + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
