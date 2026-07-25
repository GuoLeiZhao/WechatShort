package com.sqx.config;

import com.sqx.modules.sys.oauth2.OAuth2Filter;
import com.sqx.modules.sys.oauth2.OAuth2Realm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置
 *
 */
@Configuration
public class ShiroConfig {

    @Bean("securityManager")
    public SecurityManager securityManager(OAuth2Realm oAuth2Realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);
        securityManager.setRememberMeManager(null);
        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        //oauth过滤
        Map<String, Filter> filters = new HashMap<>();
        filters.put("oauth2", new OAuth2Filter());
        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/course/synCourse", "anon");
        filterMap.put("/wechat/office_account/**", "anon");
        filterMap.put("/kuaishou/watch/**", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/app/**", "anon");
        filterMap.put("/wx/**", "anon");
        filterMap.put("/activity/**", "anon");
        filterMap.put("/banner/**", "anon");
        filterMap.put("/courseClassification/selectCourseClassification", "anon");
        filterMap.put("/sys/login", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/alioss/**", "anon");
        filterMap.put("/enterpriseWechat/tempMediaUpload/**", "anon");
        filterMap.put("/enterpriseWechat/getTempMedia/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/captcha.jpg", "anon");
        filterMap.put("/aaa.txt", "anon");
        filterMap.put("/search/**", "anon");
        filterMap.put("/short_url/**", "anon");
        filterMap.put("/admin/shortUrl/change_bak/**", "anon");
        filterMap.put("/ad_link/webhook/**", "anon");
        filterMap.put("/ad_link/webhook_qq/**", "anon");
        filterMap.put("/ad_dy_tp/component_ticket", "anon");
        filterMap.put("/ad_play/notify_callback/**", "anon");
        filterMap.put("/ocean/callback/**", "anon");
        filterMap.put("/actuator/**", "anon");
        filterMap.put("/user_event/**", "anon");
        filterMap.put("/app_log/**", "anon");
        filterMap.put("/mini_app/**", "anon");
        filterMap.put("/**", "oauth2");

        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
