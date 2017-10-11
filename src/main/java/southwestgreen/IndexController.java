package southwestgreen;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Map;

@Controller
public class IndexController {

    @RequestMapping(value = {"/index*", "/"}, method = RequestMethod.GET)
    public String login() {
        return "index";
    }

    @RequestMapping(value = "/denied", method = RequestMethod.GET)
    public String denied(){
        return "denied";
    }

}
