package org.maxkey.identity.scim.resources;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.maxkey.pretty.impl.JsonPretty;
import org.maxkey.util.JsonUtils;

public class ScimGroupJsonTest {
    public static void main(String[] args) {
        Group g= new Group();
        
        Meta meta = new Meta();
        meta.setVersion("W\\/\"f250dd84f0671c3\"");
        meta.setCreated(new Date());
        meta.setLocation("https://example.com/v2/Users/2819c223...");
        meta.setResourceType("User");
        meta.setLastModified(new Date());
        g.setMeta(meta);
              
        g.setDisplayName("Tour Guides");
        
        Set<MemberRef> mrSet =new HashSet<MemberRef>();
        MemberRef mr1 =new MemberRef();
        mr1.setReference("https://example.com/v2/Users/2819c223-7f76-453a-919d-413861904646");
        mr1.setValue("2819c223-7f76-453a-919d-413861904646");
        mr1.setDisplay("Babs Jensen");
        MemberRef mr2 =new MemberRef();
        mr2.setReference("https://example.com/v2/Users/2819c223-7f76-453a-919d-413861904646");
        mr2.setValue("2819c223-7f76-453a-919d-413861904646");
        mr2.setDisplay("Babs Jensen");
        MemberRef mr3 =new MemberRef();
        mr3.setReference("https://example.com/v2/Users/2819c223-7f76-453a-919d-413861904646");
        mr3.setValue("2819c223-7f76-453a-919d-413861904646");
        mr3.setDisplay("Babs Jensen");
        mrSet.add(mr1);
        mrSet.add(mr2);
        mrSet.add(mr3);
        
        g.setMembers(mrSet);
        
        System.out.println(
                (new JsonPretty()).format(JsonUtils.object2Json(g)));
    }
}
