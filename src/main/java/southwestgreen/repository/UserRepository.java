package southwestgreen.repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import southwestgreen.XDuplicateRecord;
import southwestgreen.XNoSuchRecord;
import southwestgreen.XNoUniqueRecord;
import southwestgreen.entity.User;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Service
public class UserRepository implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserRepository.class);

    @PostConstruct
    public void init() {
        log.info(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));
        log.info("Initializing User key factories");
        log.info("Creating admin user");
        ObjectifyService.run(new VoidWork() {
            @Override
            public void vrun() {
                try {
                    createAdminUser("professor.bokdrol@gmail.com", "$2a$06$aS0VjZ8IT0F6gOSsb7.Z2u/4mHcRvLGSG7vlB4jDQW0DBCm7UjU9i");
                }
                catch(XDuplicateRecord e) {
                    throw new IllegalStateException("Failed to create admin user");
                }
            }
        });
    }

    public void createAdminUser(String email, String password)
    throws XDuplicateRecord {
        User user = new User();
        user.email = email;
        user.password = password;
        user.admin = true;
        ofy().save().entity(user).now();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<User> user = ofy().load().type(User.class).filterKey(Key.create(User.class, email)).list();
        if(user.size() != 1) {
            throw new UsernameNotFoundException("user not found");
        }
        return user.get(0);
    }

    public void createUser(User user)
    throws XDuplicateRecord {
        ofy().save().entity(user);
    }

    public void deleteUser(User user)
    throws XNoSuchRecord, XNoUniqueRecord {
        ofy().delete().entity(user).now();
    }

    static {
        ObjectifyService.register(User.class);
    }
}
