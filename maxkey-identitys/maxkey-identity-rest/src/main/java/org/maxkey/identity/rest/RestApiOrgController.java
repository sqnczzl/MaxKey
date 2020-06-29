package org.maxkey.identity.rest;

import java.io.IOException;

import org.maxkey.domain.Organizations;
import org.maxkey.persistence.service.OrganizationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping(value={"/identity/api/org"})
public class RestApiOrgController {

    @Autowired
    OrganizationsService organizationsService;
    
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Organizations getUser(@PathVariable String id,
                                       @RequestParam(required = false) String attributes) {
        Organizations org = organizationsService.get(id);
        return org;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public Organizations create(@RequestBody  Organizations org,
                                                      @RequestParam(required = false) String attributes,
                                                      UriComponentsBuilder builder) throws IOException {
        Organizations loadOrg = organizationsService.get(org.getId());
        if(loadOrg == null) {
            organizationsService.insert(org);
        }else {
            organizationsService.update(org);
        }
        return org;
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Organizations replace(@PathVariable String id,
                                                       @RequestBody Organizations org,
                                                       @RequestParam(required = false) String attributes)
            throws IOException {
        Organizations loadOrg = organizationsService.get(id);
        if(loadOrg == null) {
            organizationsService.insert(org);
        }else {
            organizationsService.update(org);
        }

        return org;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable final String id) {
        organizationsService.remove(id);
       
    }
}
