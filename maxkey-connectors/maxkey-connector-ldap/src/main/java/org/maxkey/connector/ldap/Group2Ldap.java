package org.maxkey.connector.ldap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.maxkey.connector.GroupConnector;
import org.maxkey.domain.GroupMember;
import org.maxkey.domain.Groups;
import org.maxkey.persistence.ldap.LdapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(value = "groupConnector")
public class Group2Ldap extends GroupConnector {
	private final static Logger logger = LoggerFactory.getLogger(Group2Ldap.class);

	LdapUtils  ldapUtils;
	public Group2Ldap() {

	}

	@Override
	public boolean create(Groups group)  throws Exception{
		logger.info("create");
		try {
			Attributes attributes = new BasicAttributes();
			attributes.put(new BasicAttribute("objectClass","groupOfUniqueNames"));
			attributes.put(new BasicAttribute("cn",group.getName()));
			attributes.put(new BasicAttribute("uniqueMember","uid=dummy"));
			
			String dn="cn="+group.getName()+",dc=groups,"+ldapUtils.getBaseDN();
		
			ldapUtils.getCtx().createSubcontext(dn, attributes);
			ldapUtils.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean update(Groups group)  throws Exception{
		logger.info("update");
		try {
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(ldapUtils.getSearchScope());
			NamingEnumeration<SearchResult> results = ldapUtils.getConnection()
					.search(ldapUtils.getBaseDN(), "(cn="+group.getName()+")", constraints);
			String oldDn="";
			String rdn="";
			if (results == null || !results.hasMore()) {
				return create(group);
			}else{
				SearchResult sr = (SearchResult) results.next();
				oldDn =sr.getNameInNamespace();
				String[] dnSplit=oldDn.split(",");
				rdn=oldDn.substring(oldDn.indexOf(","), oldDn.length());
				
				String groupName=dnSplit[0].split("=")[1];
				if(group.getName()!=groupName){
					String newDn="cn="+group.getName()+","+rdn;
					ldapUtils.getCtx().rename(oldDn, newDn);
					ModificationItem[] modificationItems = new ModificationItem[1];
					modificationItems[0]=new ModificationItem(DirContext.REMOVE_ATTRIBUTE,new BasicAttribute("cn",groupName));
					ldapUtils.getCtx().modifyAttributes(newDn, modificationItems);
				}
			}
			
			ldapUtils.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean delete(Groups group) throws Exception {
		logger.info("delete");
		try {
			String dn="cn="+group.getName()+",dc=groups,"+ldapUtils.getBaseDN();
			ldapUtils.getCtx().destroySubcontext(dn);
			ldapUtils.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean addMember(GroupMember groupMember) throws Exception {
		try {
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(ldapUtils.getSearchScope());
			NamingEnumeration<SearchResult> results = ldapUtils.getConnection()
					.search(ldapUtils.getBaseDN(), "(cn="+groupMember.getGroupName()+")", constraints);
			if (results == null || !results.hasMore()) {
				Groups  group =new Groups();
				group.setName(groupMember.getGroupName());
				create(group);
			}
			
			String uniqueMember="uid="+groupMember.getMemberName()+",dc=users,"+ldapUtils.getBaseDN();
			ModificationItem[] modificationItems = new ModificationItem[1];
			modificationItems[0]=new ModificationItem(DirContext.ADD_ATTRIBUTE,new BasicAttribute("uniqueMember",uniqueMember));
			
			String dn="cn="+groupMember.getGroupName()+",dc=groups,"+ldapUtils.getBaseDN();
			logger.debug("dn : "+dn);
			logger.debug("uniqueMember : "+uniqueMember);
			ldapUtils.getCtx().modifyAttributes(dn, modificationItems);
			ldapUtils.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean deleteMember(GroupMember groupMember)  throws Exception{
		try {
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(ldapUtils.getSearchScope());
			NamingEnumeration<SearchResult> results = ldapUtils.getConnection()
					.search(ldapUtils.getBaseDN(), "(cn="+groupMember.getGroupName()+")", constraints);
			if (results == null || !results.hasMore()) {
				return true;
			}
			
			String uniqueMember="uid="+groupMember.getMemberName()+",dc=users,"+ldapUtils.getBaseDN();
			ModificationItem[] modificationItems = new ModificationItem[1];
			modificationItems[0]=new ModificationItem(DirContext.REMOVE_ATTRIBUTE,new BasicAttribute("uniqueMember",uniqueMember));
			
			String dn="cn="+groupMember.getGroupName()+",dc=groups,"+ldapUtils.getBaseDN();
			logger.debug("dn : "+dn);
			logger.debug("uniqueMember : "+uniqueMember);
			ldapUtils.getCtx().modifyAttributes(dn, modificationItems);
			ldapUtils.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return true;
	}
	


}
