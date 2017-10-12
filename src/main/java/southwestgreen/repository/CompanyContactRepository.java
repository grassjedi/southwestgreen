package southwestgreen.repository;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import southwestgreen.XImportFailure;
import southwestgreen.XNoSuchRecord;
import southwestgreen.XNoUniqueRecord;
import southwestgreen.entity.CompanyContact;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Service
public class CompanyContactRepository {

    private final Logger log = LoggerFactory.getLogger(CompanyContactRepository.class);

    private CompanyRepository companyRepository;

    public void createCompanyContact(CompanyContact contact) {
        ofy().save().entity(contact).now();
    }

    public void deleteCustomer(CompanyContact companyContact)
    throws XNoSuchRecord, XNoUniqueRecord {
        ofy().delete().entity(companyContact).now();
    }

    public void importCsv(InputStream csvStream)
    throws IOException {
        Iterable<CSVRecord> contactRecords =
                CSVFormat.POSTGRESQL_CSV.withHeader(
                        "companyname",
                        "surname",
                        "firstname",
                        "jobtitle",
                        "businessphone",
                        "homephone",
                        "mobilephone",
                        "faxnumber",
                        "website",
                        "email",
                        "address",
                        "city",
                        "postalcode",
                        "province",
                        "country",
                        "notes").parse(new InputStreamReader(csvStream));
        int rowNumber = 0;
        List<CompanyContact> companyContacts = new LinkedList<>();
        for(CSVRecord record: contactRecords) {
            rowNumber++;
            String companyName = record.get("companyname");
            if(companyName == null) {
                throw new IOException("Row #" + rowNumber + ": Company name may not be null");
            }
            CompanyContact companyContact = new CompanyContact();
            companyContact.setCompany(companyRepository.findByName(companyName));
            companyContact.surname = record.get("surname");
            companyContact.forenames = record.get("firstname");
            companyContact.jobTitle = record.get("jobtitle");
            companyContact.businessTelephone = record.get("businessphone");
            companyContact.homeTelephone = record.get("homephone");
            companyContact.mobile = record.get("mobilephone");
            companyContact.facsimile = record.get("faxnumber");
            companyContact.website = record.get("website");
            companyContact.email = record.get("email");
            companyContact.address = record.get("address");
            companyContact.city = record.get("city");
            companyContact.postalCode = record.get("postalcode");
            companyContact.province = record.get("province");
            companyContact.country = record.get("country");
            companyContact.notes = record.get("notes");
            companyContacts.add(companyContact);
        }
        ofy().save().entities(companyContacts).now();
    }

    public CompanyRepository getCompanyRepository() {
        return companyRepository;
    }

    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    static {
        ObjectifyService.register(CompanyContact.class);
    }
}
