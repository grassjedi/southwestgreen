package southwestgreen;


import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;

public class GaeDataStoreManager {

    public Datastore getDatastore() {
        return DatastoreOptions.defaultInstance().service();
    }
}
