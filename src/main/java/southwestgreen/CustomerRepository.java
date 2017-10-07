package southwestgreen;

import com.google.cloud.datastore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class CustomerRepository {

    private final Logger log = LoggerFactory.getLogger(CustomerRepository.class);

    @Autowired
    private Datastore datastore;

    private KeyFactory keyFactory;

    @PostConstruct
    public void init() {
        log.info("Initializing key factories");
        keyFactory = datastore.newKeyFactory().kind("Customer");
    }

    public void createCustomer(Customer customer) {
        customer.setId(System.currentTimeMillis());
        datastore.add(constructEntity(customer));
    }

    /**
     * Deletes the specified customer record
     * @param customerContact the customer contact to delete
     * @throws XNoSuchRecord if the customer does not exist
     * @throws XNoUniqueRecord if more than one customer would be deleted
     */
    public void deleteCustomer(Customer customerContact)
    throws XNoSuchRecord, XNoUniqueRecord {
        Query<Entity> query = Query.entityQueryBuilder()
                .kind("Customer")
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

    protected Entity constructEntity(Customer customer) {
        Key key = keyFactory.newKey(customer.getId());
        return Entity.builder(key)
                .set("id", customer.getId())
                .set("email", customer.getEmail())
                .set("website", customer.getWebsite())
                .set("telephone", customer.getTelephone())
                .set("facsimile", customer.getFacsimile())
                .set("address", customer.getAddress())
                .set("city", customer.getCity())
                .set("province", customer.getProvince())
                .set("postalCode", customer.getPostalCode())
                .build();
    }
}
