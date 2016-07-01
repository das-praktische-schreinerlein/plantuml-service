/** 
 * software for diagram-converter
 * 
 * @FeatureDomain                Converter
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     diagram-services
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package de.yaio.services.plantuml.server.configuration;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

/** 
 * secure the plantuml-service
 */
@EnableWebSecurity
@Component
public class PlantumlWebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger logger = Logger.getLogger(PlantumlWebSecurityConfig.class);
    
    /**
     * configure API-Configuration for plantuml-service
     */
    @EnableWebSecurity
    @Configuration
    @Order(1)
    public static class PlantumlServiceSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Value("${yaio-plantuml-service.baseurl}")
        protected String plantumlBaseUrl;

        @Value("${yaio-plantuml-service.security.apiusers.filelocation}")
        protected String usersFile;

        @Value("${yaio-plantuml-service.security.useown}")
        protected Boolean flgSecureByMyOwn;

        private CsrfTokenRepository csrfTokenRepository() {
            HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
            repository.setHeaderName("X-XSRF-TOKEN");
            return repository;
        }        

        @Autowired
        public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
            if (! flgSecureByMyOwn) {
                return;
            }
            
            // secure it by my own
            Properties users = Configurator.readProperties(usersFile);
            InMemoryUserDetailsManager im = new InMemoryUserDetailsManager(users);
            auth.userDetailsService(im);
        }

        protected void configure(final HttpSecurity http) throws Exception {
            if (! flgSecureByMyOwn) {
                return;
            }
            
            // secure it by my own
            http
                    // authentification
                    .httpBasic()
                .and()
                    // secure path
                    .requestMatcher(new AntPathRequestMatcher(plantumlBaseUrl + "/**", "POST"))
                        .authorizeRequests()
                    // secure API webservice
                    .anyRequest()
                        .hasRole("PLANTUML")
                .and()
                   // disable csrf-protection
                   .csrf().disable();
        }
    }    
}