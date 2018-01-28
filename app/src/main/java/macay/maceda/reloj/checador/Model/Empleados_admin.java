package macay.maceda.reloj.checador.Model;


import macay.maceda.reloj.checador.Adapters.User_detail_admin;

/**
 * Created by Vlover on 28/01/2018.
 */

public class Empleados_admin {
    private long id;
    private String name;
    private String lastname;
    private String number_phone;
    private String occupation;
    private String area;
    private String email;
    private String birthday;
    private String address;
    private String datework;
    private String image;


    public Empleados_admin() {
    }

    public Empleados_admin(String name,String lastname, String number_phone, String occupation,
                           String area,String email,String birthday,String address,String datework,String image) {
        this.name = name;
        this.lastname = lastname;
        this.number_phone = number_phone;
        this.occupation = occupation;
        this.area = area;
        this.email = email;
        this.birthday = birthday;
        this.address = address;
        this.datework = datework;
        this.image = image;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getNumber_phone() {
        return number_phone;
    }

    public void setNumber_phone(String number_phone) {
        this.number_phone = number_phone;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getDatework() {
        return datework;
    }

    public void setDatework(String datework) {
        this.datework = datework;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
