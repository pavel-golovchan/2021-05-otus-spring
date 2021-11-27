package ru.project.photoblog.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

//@Profile("test")
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        //это отключает не только аутентификацию, но и любые средства защиты, такие как XSS
        web.ignoring().antMatchers("/**"  );
    }
/*  //отключим безопасность на время отладки/разработки основного функционала
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //.and()
                .authorizeRequests().antMatchers("/public").permitAll()
                .and()
                .authorizeRequests().antMatchers("/authenticated", "/success").authenticated()
                .and()
                .authorizeRequests().antMatchers("/user").hasAnyRole( "ADMIN", "USER" )

                .and()
                .authorizeRequests().antMatchers("/admin").hasRole( "ADMIN" )
                .and()
                .formLogin()
                .and()
                .logout().logoutUrl("/logout");
   }
         */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return charSequence.toString().equals(s);
            }
        };
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("admin").roles("ADMIN")
                .and()
                .withUser("user").password("user").roles("USER")
                .and()
                .withUser("manager").password("manager").roles("MANAGER", "USER");
    }
}
