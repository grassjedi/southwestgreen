package southwestgreen;

import com.google.cloud.datastore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class CustomerContactRepository {

    private final Logger log = LoggerFactory.getLogger(CustomerContactRepository.class);

    @Autowired
    private Datastore datastore;

    private KeyFactory keyFactory;

    @PostConstruct
    public void init() {
        log.info("Initializing key factories");
        keyFactory = datastore.newKeyFactory().kind("CustomerContact");
    }

    public void createCustomer(CustomerContact customerContact) {
        customerContact.setId(System.currentTimeMillis());
        datastore.add(constructEntity(customerContact));
    }

    /**
     * Deletes the specified customer record
     * @param customerContact the customer contact to delete
     * @throws XNoSuchRecord if the customer does not exist
     * @throws XNoUniqueRecord if more than one customer would be deleted
     */
    public void deleteCustomer(CustomerContact customerContact)
    throws XNoSuchRecord, XNoUniqueRecord {
        Query<Entity> query = Query.entityQueryBuilder()
                .kind("CustomerContact")
                .filter(StructuredQuery.PropertyFilter.eq("id", customerContact.getId())).build();
        QueryResults<Entity> results = datastore.run(query);
        if(!results.hasNext()) {
            throw new XNoSuchRecord();
        }
        Key key = results.next().key();
        if(results.hasNext()) {
            throw new XNoUniqueRecord();
        }
        datastore.delete(key);
    }

    protected Entity constructEntity(CustomerContact customerContact) {
        Key key = keyFactory.newKey(customerContact.getId());
        return Entity.builder(key)
                .set("id", customerContact.getId())
                .set("companyId", customerContact.getCustomerId())
                .set("email", customerContact.getEmail())
                .set("website", customerContact.getWebsite())
                .set("telephone", customerContact.getTelephone())
                .set("foreNames", customerContact.getForeNames())
                .set("surname", customerContact.getSurname())
                .set("jobTitle", customerContact.getJobTitle())
                .set("businessTelephone", customerContact.getBusinessTelephone())
                .set("homeTelephone", customerContact.getHomeTelephone())
                .set("mobile", customerContact.getMobile())
                .set("facsimile", customerContact.getFacsimile())
                .set("address", customerContact.getAddress())
                .set("city", customerContact.getCity())
                .set("province", customerContact.getProvince())
                .set("postalCode", customerContact.getPostalCode())
                .build();
    }
}
