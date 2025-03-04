package kr.co.fitzcode.common.config;

import kr.co.fitzcode.admin.filter.VisitorLoggingFilter;
import kr.co.fitzcode.admin.mapper.DashboardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private DashboardMapper dashboardMapper;

    // 필터 url
    @Bean
    public FilterRegistrationBean<VisitorLoggingFilter> visitorLoggingFilterRegistration() {
        FilterRegistrationBean<VisitorLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new VisitorLoggingFilter(dashboardMapper));
        registrationBean.addUrlPatterns("/admin/*"); // admin 하위 모든 URL
        registrationBean.setOrder(1); // 필터 순서
        return registrationBean;
    }
}