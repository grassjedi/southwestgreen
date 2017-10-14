package southwestgreen.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import southwestgreen.repository.CompanyContactRepository;
import southwestgreen.repository.CompanyRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping(path = "/~maintenance")
public class MaintenanceController {

    private CompanyRepository companyRepository;
    private CompanyContactRepository companyContactRepository;

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index() {
        return "/~maintenance/index";
    }

    @RequestMapping(value = "/bulk-add-companies", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String importCompanies(
            HttpServletRequest request,
            @RequestParam("data-file") MultipartFile file)
    throws IOException {
        companyRepository.importCsv(file.getInputStream());
        return "redirect:/~maintenance/index";
    }

    @RequestMapping(value = "/bulk-add-contacts", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
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
        return "redirect:/~maintenance/index";
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
