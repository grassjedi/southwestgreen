package southwestgreen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import southwestgreen.repository.CompanyContactRepository;
import southwestgreen.repository.CompanyRepository;

import java.util.Map;

@Controller
@RequestMapping(path = "/~maintenance")
public class MaintenanceController {

    private CompanyRepository companyRepository;
    private CompanyContactRepository companyContactRepository;

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index() {
        return "~maintenance/index";
    }

    @RequestMapping(value = "/bulk-add-companies", method = RequestMethod.POST)
    public String importCompanies(
            @RequestParam("data-file") MultipartFile file,
            Map<String, Object> model) {
        try {
            companyRepository.importCsv(file.getInputStream());
            model.put("message", "Successfully imported companies from: " + file.getOriginalFilename());
        }
        catch(Exception e) {
            model.put("message", "Failed to import companies from: " + file.getOriginalFilename());
        }
        return "/~maintenance/index";
    }

    @RequestMapping(value = "/bulk-add-contacts", method = RequestMethod.POST)
    public String importContacts(
            @RequestParam("data-file") MultipartFile file,
            Map<String, Object> model) {
        try {
            companyContactRepository.importCsv(file.getInputStream());
            model.put("message", "Successfully imported companies from: " + file.getOriginalFilename());
        }
        catch(Exception e) {
            model.put("message", "Failed to import companies from: " + file.getOriginalFilename());
        }
        return "/~maintenance/index";
    }

    public CompanyRepository getCompanyRepository() {
        return companyRepository;
    }

    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyContactRepository getCompanyContactRepository() {
        return companyContactRepository;
    }

    public void setCompanyContactRepository(CompanyContactRepository companyContactRepository) {
        this.companyContactRepository = companyContactRepository;
    }

}
