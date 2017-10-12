package southwestgreen.entity;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class CompanyContact {
    @Id public Long id;
    public String surname;
    public String forenames;
    public String jobTitle;
    public String businessTelephone;
    public String homeTelephone;
    public String mobile;
    public String facsimile;
    public String website;
    public String email;
    public String address;
    public String city;
    public String province;
    public String postalCode;
    public String country;
    public String notes;
    public Ref<Company> companyRef;

    public void setCompany(Company company) {
        companyRef = Ref.create(company);
    }

    public Company getCompany() {
        return companyRef.get();
    }
}
