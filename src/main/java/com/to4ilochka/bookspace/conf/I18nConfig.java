package com.to4ilochka.bookspace.conf;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.Locale;

@Configuration
public class I18nConfig {

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                String lang = request.getParameter("lang");
                if (lang != null && !lang.isBlank()) {
                    return new Locale(lang);
                }
                return super.resolveLocale(request);
            }
        };

        resolver.setDefaultLocale(Locale.ENGLISH);
        resolver.setSupportedLocales(Arrays.asList(Locale.ENGLISH, new Locale("uk")));

        return resolver;
    }
}
