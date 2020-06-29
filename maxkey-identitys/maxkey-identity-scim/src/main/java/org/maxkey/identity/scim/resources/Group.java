package org.maxkey.identity.scim.resources;

import java.util.HashSet;
import java.util.Set;

public class Group extends Resource{
    private static final long serialVersionUID = 404613567384513866L;

    public static final String SCHEMA = "urn:ietf:params:scim:schemas:core:2.0:Group";
    
    private  String displayName;
    private  Set<MemberRef> members;
    
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public Set<MemberRef> getMembers() {
        return members;
    }
    public void setMembers(Set<MemberRef> members) {
        this.members = members;
    }
    
    public Group() {
        schemas =new HashSet<String>();
        schemas.add(SCHEMA);
    }
    
}
