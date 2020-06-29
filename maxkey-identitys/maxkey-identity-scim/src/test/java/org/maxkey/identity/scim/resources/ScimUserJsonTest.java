package org.maxkey.identity.scim.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.maxkey.pretty.impl.JsonPretty;
import org.maxkey.util.JsonUtils;

public class ScimUserJsonTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        User u = new User();
        u.setUserName("UserName");
        u.setExternalId("UserName");
        u.setId("1111111111111");
        
        Meta meta = new Meta();
        meta.setVersion("W\\/\"f250dd84f0671c3\"");
        meta.setCreated(new Date());
        meta.setLocation("https://example.com/v2/Users/2819c223...");
        meta.setResourceType("User");
        meta.setLastModified(new Date());
        u.setMeta(meta);
        
        UserName un=new UserName();
        un.setFamilyName("Jensen");
        un.setFormatted("Ms. Barbara J Jensen, III");
        un.setGivenName("Barbara");
        un.setHonorificPrefix("Ms.");
        un.setHonorificSuffix("III");
        un.setMiddleName("Jane");
        u.setName(un);
        
        List<UserPhoneNumber> UserPhoneNumberList = new ArrayList<UserPhoneNumber>();
        UserPhoneNumber pn =new UserPhoneNumber();
        pn.setValue("555-555-8377");
        pn.setType(UserPhoneNumber.UserPhoneNumberType.WORK);
        
        UserPhoneNumber pnh =new UserPhoneNumber();
        pnh.setValue("555-555-8377");
        pnh.setType(UserPhoneNumber.UserPhoneNumberType.HOME);
        UserPhoneNumberList.add(pnh);
        
        UserPhoneNumberList.add(pn);
        
        u.setPhoneNumbers(UserPhoneNumberList);
        
        List<UserEmail> ueList = new ArrayList<UserEmail>();
        UserEmail ue =new UserEmail();
        ue.setValue("bjensen@example.com");
        ue.setType(UserEmail.UserEmailType.WORK);
        ueList.add(ue);
        u.setEmails(ueList);
        
        System.out.println(
                (new JsonPretty()).format(JsonUtils.object2Json(u)));
    }

}
