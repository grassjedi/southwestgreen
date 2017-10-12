package southwestgreen.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Company {
    @Id public Long id;
    @Index public String name;
    public String email;
    public String website;
    public String telephone;
    public String facsimile;
    public String address;
    public String city;
    public String province;
    public String country;
    public String postalCode;
    public String notes;
}
