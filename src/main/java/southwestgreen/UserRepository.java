package southwestgreen;

import com.google.cloud.datastore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class UserRepository implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserRepository.class);

    @Autowired
    private Datastore datastore;

    private KeyFactory keyFactory;

    @PostConstruct
    public void init() {
        log.info("Initializing User key factories");
        keyFactory = datastore.newKeyFactory().kind("User");
        try {
            log.info("Creating admin user");
            createAdminUser("professor.bokdrol@gmail.com", "$2a$06$aS0VjZ8IT0F6gOSsb7.Z2u/4mHcRvLGSG7vlB4jDQW0DBCm7UjU9i");
        }
        catch(XDuplicateRecord ignore) {

        }
    }

    public void createAdminUser(String email, String password)
    throws XDuplicateRecord {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setAdmin(true);
        createUser(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Query<Entity> query = Query.entityQueryBuilder()
                .kind("User")
                .filter(StructuredQuery.PropertyFilter.eq("id", email)).build();
        datastore.run(query);
        QueryResults<Entity> results = datastore.run(query);
        if(!results.hasNext()) {
            throw new UsernameNotFoundException("user not found");
        }
        Entity entity = results.next();
        if(results.hasNext()) {
            throw new UsernameNotFoundException("user not found");
        }
        return fromEntity(entity);
    }

    /**
     * Create the specified user record
     * @param user the user record to create
     * @throws XDuplicateRecord if the user record already exists
     */
    public void createUser(User user)
    throws XDuplicateRecord {
        try {
            loadUserByUsername(user.getEmail());
            throw new XDuplicateRecord();
        }
        catch(UsernameNotFoundException e) {
            datastore.add(constructEntity(user));
        }
    }

    /**
     * Deletes the specified user record
     * @param user the user contact to delete
     * @throws XNoSuchRecord if the user does not exist
     * @throws XNoUniqueRecord if more than one user would be deleted
     */
    public void deleteUser(User user)
    throws XNoSuchRecord, XNoUniqueRecord {
        Query<Entity> query = Query.entityQueryBuilder()
                .kind("User")
                .filter(StructuredQuery.PropertyFilter.eq("email", user.getEmail())).build();
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

    protected User fromEntity(Entity entity) {
        User user = new User();
        user.setEmail(entity.getString("email"));
        user.setAdmin(entity.getBoolean("admin"));
        user.setPassword(entity.getString("password"));
        return user;
    }

    protected Entity constructEntity(User user) {
        Key key = keyFactory.newKey(user.getEmail());
        return Entity.builder(key)
                .set("email", user.getEmail())
                .set("admin", user.isAdmin())
                .build();
    }
}
