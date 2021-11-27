package ru.project.photoblog;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.boot.Banner;

import java.io.PrintStream;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@SpringBootApplication
public class PhotoBlogApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication( PhotoBlogApplication.class);
	/*	app.setBanner(new Banner() {
			@Override
			public void printBanner(Environment environment, Class<?>
					sourceClass, PrintStream out) {
				out.print( "PhotoBlog"  );
			}
		});
	 */
		app.run(args);
	}

}
