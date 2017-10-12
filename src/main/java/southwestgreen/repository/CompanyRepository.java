package southwestgreen.repository;

import com.googlecode.objectify.ObjectifyService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import southwestgreen.XEntityValidation;
import southwestgreen.XImportFailure;
import southwestgreen.XNoSuchRecord;
import southwestgreen.XNoUniqueRecord;
import southwestgreen.entity.Company;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import static com.googlecode.objectify.ObjectifyService.ofy;

@Service
public class CompanyRepository {

    private final Logger log = LoggerFactory.getLogger(CompanyRepository.class);

    public void createCompany(Company company)
    throws XEntityValidation {
        validate(company);
        ofy().save().entity(company);
    }
    
    public void deleteCustomer(Company company)
    throws XNoSuchRecord, XNoUniqueRecord {
        ofy().delete().entity(company);
    }

    public Company findByName(String name) {
        return ofy().load().type(Company.class).filterKey("name =", name).first().now();
    }

    public void importCsv(InputStream csvStream)
    throws IOException {
        Iterable<CSVRecord> companyRecords =
                CSVFormat.POSTGRESQL_CSV.withHeader(
                        "companyname",
                        "email",
                        "website",
                        "businessphone",
                        "faxnumber",
                        "address",
                        "city",
                        "province",
                        "postalcode",
                        "country").parse(new InputStreamReader(csvStream));
        int rowNumber = 0;
        List<Company> companies = new LinkedList<>();
        for(CSVRecord record: companyRecords) {
            rowNumber++;
            if(record.get("companyname") == null) {
                throw new IOException("Row #" + rowNumber + ": Company name may not be null");
            }
            Company company = new Company();
            company.name = record.get("companyname");
            company.email = record.get("email");
            company.website = record.get("website");
            company.telephone = record.get("businessphone");
            company.facsimile = record.get("faxnumber");
            company.facsimile = record.get("address");
            company.facsimile = record.get("city");
            company.facsimile = record.get("province");
            company.facsimile = record.get("postalcode");
            company.facsimile = record.get("country");
            companies.add(company);

        }
        ofy().save().entities(companies);
    }

    public void validate(Company company)
    throws XEntityValidation {
        if (company.name == null || "".equals(company.name.trim())) {
            throw new XEntityValidation("The name of the company must be unique and not empty.");
        }
    }

    static {
        ObjectifyService.register(Company.class);
    }
}
