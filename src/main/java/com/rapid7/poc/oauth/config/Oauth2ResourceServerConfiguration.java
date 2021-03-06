package com.rapid7.poc.oauth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

public class Oauth2ResourceServerConfiguration {

    private static final String RESOURCE_SERVER_RESOURCE_ID = "mjs-poc-resource-server";

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
        
        
        @Value("${oauth.clientid}")
        private String oauthClientId;

        @Value("${oauth.clientsecret}")
        private String oauthClientSecret;

        @Value("${oauth.check.token.endpoint}")
        private String oauthClientTokenEndpoint;
        
        
        /**
         * Use the remote token services in the Authorisation Server.
         * @return
         */
        @Bean
        public ResourceServerTokenServices tokenService() {
           RemoteTokenServices tokenServices = new RemoteTokenServices();
           tokenServices.setClientId(oauthClientId);
           tokenServices.setClientSecret(oauthClientSecret);
           tokenServices.setCheckTokenEndpointUrl(oauthClientTokenEndpoint);
           
           return tokenServices;
        }        
        
        
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            // @formatter:off
            resources
                .resourceId(RESOURCE_SERVER_RESOURCE_ID)
                .tokenServices(tokenService())
                ;
            // @formatter:on
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                .authorizeRequests()
                    .antMatchers("/users").hasRole("ADMIN")
                    .antMatchers("/greeting").authenticated();
            // @formatter:on
        }

    }    
    

}
