package org.maxkey.connector;

import org.maxkey.constants.ConstantsProperties;
import org.maxkey.persistence.ldap.LdapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(ConstantsProperties.applicationPropertySource)
@SpringBootApplication
@ComponentScan(basePackages = {
        "org.maxkey.connector",
        "org.maxkey.connector.receiver",
        "org.maxkey.connector.ldap"
    })
public class LdapConsumerApplication {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    ConfigurableApplicationContext context = SpringApplication.run(LdapConsumerApplication.class, args);
		
	}
	
	//@Bean(name = "ldapUtils")
	public LdapUtils getLdapConnection(
			@Value("${config.connector.ldap.providerUrl}") String providerUrl,
			@Value("${config.connector.ldap.principal}") String principal,
			@Value("${config.connector.ldap.credentials}") String credentials,
			@Value("${config.connector.ldap.baseDN}") String baseDn
			)throws Exception{
		
		LdapUtils ldapUtils=new LdapUtils(
				providerUrl,
				principal,
				credentials,
				baseDn);
		if(ldapUtils.openConnection()==null){
			 throw new Exception("connection to Ldap Error.");   
		}
		return ldapUtils;
	}
	
	
	   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	        return application.sources(LdapConsumerApplication.class);
	    }

}
