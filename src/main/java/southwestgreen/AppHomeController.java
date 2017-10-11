package southwestgreen;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Map;

@Controller
@RequestMapping(path = "/app")
public class AppHomeController {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String appHome(Map<String, Object> model, Principal principal){
        if(principal == null) {
            return "redirect: /index";
        }
        model.put("email", principal.getName());
        return "app/home";
    }
}
