package southwestgreen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import southwestgreen.XEntityValidation;
import southwestgreen.XNoSuchRecord;
import southwestgreen.entity.Company;
import southwestgreen.repository.CompanyContactRepository;
import southwestgreen.repository.CompanyRepository;
import southwestgreen.util.CursorResult;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/app/customer")
public class CompanyController {

    private CompanyRepository companyRepository;
    private CompanyContactRepository companyContactRepository;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String list(
            Map<String, Object> model,
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false) Integer maxResults) {
        if(maxResults == null) {
            maxResults = 200;
        }
        CursorResult<List<Company>> result = companyRepository.list(cursor, maxResults);
        model.put("companies", result.data);
        model.put("cursor", result.cursor);
        model.put("maxResults", maxResults);
        return "/app/customer";
    }

    @RequestMapping(value = "/app/customer/create", method = RequestMethod.GET)
    public String createCompany() {
        return "/app/customer_form";
    }

    @RequestMapping(value = "/app/customer/create", method = RequestMethod.POST)
    public String createCompany(
            @RequestBody MultiValueMap<String, String> record
    ) throws XEntityValidation {
        Company company = new Company();
        company.name = record.getFirst("name");
        company.email = record.getFirst("email");
        company.website = record.getFirst("website");
        company.telephone = record.getFirst("telephone");
        company.facsimile = record.getFirst("facsimile");
        company.address = record.getFirst("address");
        company.city = record.getFirst("city");
        company.province = record.getFirst("province");
        company.postalCode = record.getFirst("postalCode");
        company.country = record.getFirst("country");
        companyRepository.createCompany(company);
        return "redirect: /app/customer/";
    }

    @RequestMapping(value = "/app/customer/update/{companyId}", method = RequestMethod.GET)
    public String updateCompany(
            Map<String, Object> model,
            @PathVariable Long companyId
    ) {
        try {
            Company company = this.companyRepository.getCompanyById(companyId);
            model.put("company", company);
            return "/app/customer_form";
        }
        catch (XNoSuchRecord e) {
            throw new XResourceNotFound("Company not found id: " + companyId, e);
        }
    }

    @RequestMapping(value = "/app/customer/update/{companyId}", method = RequestMethod.POST)
    public String updateCompany(
            @RequestBody MultiValueMap<String, String> record,
            @PathVariable Long companyId
    ) throws XEntityValidation {
        Company company = new Company();
        company.name = record.getFirst("name");
        company.email = record.getFirst("email");
        company.website = record.getFirst("website");
        company.telephone = record.getFirst("telephone");
        company.facsimile = record.getFirst("facsimile");
        company.address = record.getFirst("address");
        company.city = record.getFirst("city");
        company.province = record.getFirst("province");
        company.postalCode = record.getFirst("postalCode");
        company.country = record.getFirst("country");
        companyRepository.createCompany(company);
        return "redirect: /app/customer/";
    }

    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void setCompanyContactRepository(CompanyContactRepository companyContactRepository) {
        this.companyContactRepository = companyContactRepository;
    }
}
