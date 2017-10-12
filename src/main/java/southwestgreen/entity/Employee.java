package southwestgreen.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Employee {

    @Id public Long id;
    public String foreNames;
    public String surname;
    public String email;
    public String jobTitle;
    public String businessTelephone;
    public String homeTelephone;
    public String mobile;
    public String address;
    public String city;
    public String province;
    public String postalCode;
    public String website;
    private String notes;

}
