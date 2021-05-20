package com.bancoexterior.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*
		http
				.authorizeRequests()
					.antMatchers("/css/**", "/").permitAll()
					.antMatchers("/user/**").hasRole("USER")
					.and()
				.formLogin().loginPage("/login").failureUrl("/login-error").defaultSuccessUrl("/user/index");
				*/
		
		
		http.authorizeRequests()
		// Los recursos estáticos no requieren autenticación
		.antMatchers("/css/**", "/").permitAll()
		.antMatchers(
				"/vendors/**",
				"/img/**",
				"/js/**",
				"/scss/**",
				"/node_modules/**").permitAll()
					//Asignar permisos a URLs por ROLES 
		.antMatchers("/monedas/**").hasAnyAuthority("ROLE_USER") 
		

					
		// Todas las demás URLs de la Aplicación requieren autenticación
		.anyRequest().authenticated()
		// El formulario de Login no requiere autenticacion
		//.and().formLogin().permitAll(); //Asi se usa para usar el por dedecto de spring security
		// El formulario de Login no requiere autenticacion
		//.and().formLogin().loginPage("/login").permitAll()
		.and().formLogin().loginPage("/login").failureUrl("/login-error").defaultSuccessUrl("/").permitAll()
		.and().logout().permitAll();
			
		
	}
	
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
		auth
        .inMemoryAuthentication()
        .withUser("user").password(passwordEncoder().encode("password")).roles("USER")
        .and()
        .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
		/*
		auth
			.inMemoryAuthentication()
				.withUser(User.withDefaultPasswordEncoder().username("user").password("user").roles("USER"));
		auth
			.inMemoryAuthentication()
				.withUser(User.withDefaultPasswordEncoder().username("admin").password("admin").roles("ADMIN"));*/
	}
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
