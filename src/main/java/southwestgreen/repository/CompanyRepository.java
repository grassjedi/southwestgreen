package southwestgreen.repository;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import southwestgreen.XEntityValidation;
import southwestgreen.XNoSuchRecord;
import southwestgreen.XNoUniqueRecord;
import southwestgreen.entity.Company;
import southwestgreen.util.CursorResult;

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
            company.address = record.get("address");
            company.city = record.get("city");
            company.province = record.get("province");
            company.postalCode = record.get("postalcode");
            company.country = record.get("country");
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

    public CursorResult<List<Company>> list(String from, Integer maxResults) {
        CursorResult<List<Company>> result = new CursorResult<>();
        Query<Company> query = ofy().load().type(Company.class).limit(maxResults);
        try {
            if (from != null) query = query.startAt(Cursor.fromWebSafeString(from));
        }
        catch(IllegalArgumentException ignore) {
            // the cursor was invalid start at 0
        }
        QueryResultIterator<Company> i = query.iterator();
        result.data = new LinkedList<>();
        boolean moreResults = false;
        while(i.hasNext()) {
            result.data.add(i.next());
            moreResults = true;
        }
        if(moreResults) {
            result.cursor = i.getCursor().toWebSafeString();
        }
        return result;
    }

    public Company getCompanyById(Long companyId)
    throws XNoSuchRecord {
        Company result = ofy().load().key(Key.create(Company.class, companyId)).now();
        if(result == null) {
            throw new XNoSuchRecord();
        }
        return result;
    }
}
