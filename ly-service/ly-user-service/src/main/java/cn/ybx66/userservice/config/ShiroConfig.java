//package cn.ybx66.userservice.config;
//
//
//import cn.ybx66.userservice.shiro.CustomRealm;
//import lombok.extern.log4j.Log4j2;
//import org.apache.shiro.mgt.SecurityManager;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
//import org.apache.shiro.spring.LifecycleBeanPostProcessor;
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.apache.shiro.web.servlet.SimpleCookie;
//import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
//import org.crazycake.shiro.RedisCacheManager;
//import org.crazycake.shiro.RedisManager;
//import org.crazycake.shiro.RedisSessionDAO;
//import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * shiro配置中心
// */
//@Log4j2
//@Configuration
//public class ShiroConfig {
//
//    //取redis连接配置
//    @Value("${spring.redis.host}")
//    private String host;
//    @Value("${spring.redis.port}")
//    private int port;
//    @Value("${spring.redis.password}")
//    private String password;
//    @Value("${spring.redis.timeout}")
//    private int timeout;
//    @Value("${spring.redis.isRedisCache}")
//    private int isRedisCache;
//
////    @Bean
////    public EhCacheManager getEhCacheManager() {
////        EhCacheManager em = new EhCacheManager();
////        em.setCacheManagerConfigFile("classpath:config/ehcache.xml");
////        return em;
////    }
//
//    @Bean
//    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
//        return new LifecycleBeanPostProcessor();
//    }
//    /**
//     * 配置shiro redisManager
//     *
//     * @return
//     */
//    @Bean
//    public RedisManager redisManager() {
//        RedisManager redisManager = new RedisManager();
//        redisManager.setHost(host);
//        redisManager.setPort(port);
//        redisManager.setPassword(password);
//        redisManager.setExpire(18000);// 配置过期时间
//        log.info("配置redis连接设置##########" + host + ":::" + port);
//        return redisManager;
//    }
//    @Bean
//    public JavaUuidSessionIdGenerator sessionIdGenerator(){
//        return new JavaUuidSessionIdGenerator();
//    }
//
//    @Bean
//    public RedisSessionDAO sessionDAO(){
//        RedisSessionDAO sessionDAO = new RedisSessionDAO(); // crazycake 实现
////        sessionDAO.setSessionInMemoryEnabled(false);
//        sessionDAO.setRedisManager(redisManager());
//        sessionDAO.setSessionIdGenerator(sessionIdGenerator()); //  Session ID 生成器
//        return sessionDAO;
//    }
//    @Bean
//    public SimpleCookie cookie(){
//        SimpleCookie cookie = new SimpleCookie("SHAREJSESSIONID"); //  cookie的name,对应的默认是 JSESSIONID
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");        //  path为 / 用于多个系统共享JSESSIONID
//        return cookie;
//    }
//
//    @Bean
//    public DefaultWebSessionManager sessionManager(){
//        //更改
//        CommonWebSessionManager sessionManager = new CommonWebSessionManager();
//        sessionManager.setGlobalSessionTimeout(-1000L);    // 设置session超时
//        sessionManager.setDeleteInvalidSessions(true);      // 删除无效session
//        sessionManager.setSessionIdCookie(cookie());            // 设置JSESSIONID
//        sessionManager.setSessionDAO(sessionDAO());         // 设置sessionDAO
//        return sessionManager;
//    }
//
//    @Bean
//    public RedisCacheManager redisCacheManager(){
//        RedisCacheManager cacheManager = new RedisCacheManager();   // crazycake 实现
//        cacheManager.setRedisManager(redisManager());
//        return cacheManager;
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
//        defaultAAP.setProxyTargetClass(true);
//        return defaultAAP;
//    }
//
//    //将自己的验证方式加入容器
//    @Bean
//    public AuthorizingRealm myShiroRealm() {
//        return new CustomRealm();
//    }
//
//    //权限管理，配置主要是Realm的管理认证
//    @Bean
//    public SecurityManager securityManager() {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(myShiroRealm());
//        //关闭shiro自带的session 不创建session会话
////        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
////        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
////        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
////        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
////        securityManager.setSubjectDAO(subjectDAO);
//
//        // 自定义缓存实现 使用redis
//        securityManager.setCacheManager(redisCacheManager());
//        // 自定义session管理 使用redis
//        securityManager.setSessionManager(sessionManager());
//        return securityManager;
//    }
//
//    //Filter工厂，设置对应的过滤条件和跳转条件
//    @Bean
//    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
////        shiroFilterFactoryBean.setFilters();
//        Map<String, String> map = new HashMap<>();
//        map.put("/**/*.js", "anon");
//        map.put("/**/*.png", "anon");
//        map.put("/**/*.ico", "anon");
//        map.put("/**/*.css", "anon");
//        map.put("/**/ui/**", "anon");
//        map.put("/user/getUser/**","anon");
//        //登出
//        map.put("/logout", "logout");
//        //对所有用户认证
//        map.put("/**", "authc");
//
//        //登录
//        shiroFilterFactoryBean.setLoginUrl("/login");
//        //首页
//        shiroFilterFactoryBean.setSuccessUrl("/index");
//        //错误页面，认证不通过跳转
//        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
//
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
//
//
//        return shiroFilterFactoryBean;
//    }
//
//    //注入权限管理
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//        return authorizationAttributeSourceAdvisor;
//    }
////    @Bean
////    public RedisManager redisManager(){
////        RedisManager redisManager = new RedisManager();     // crazycake 实现
//////        RedisClusterManager redisClusterManager = new RedisClusterManager();
////        redisManager.setHost(host);
////        redisManager.setPort(port);
////        redisManager.setTimeout(180000);
////        return redisManager;
////    }
////
////    @Bean
////    public JavaUuidSessionIdGenerator sessionIdGenerator(){
////        return new JavaUuidSessionIdGenerator();
////    }
////
////    @Bean
////    public RedisSessionDAO sessionDAO(){
////        RedisSessionDAO sessionDAO = new RedisSessionDAO(); // crazycake 实现
////        sessionDAO.setRedisManager(redisManager());
////        sessionDAO.setSessionIdGenerator(sessionIdGenerator()); //  Session ID 生成器
////        return sessionDAO;
////    }
////
////    @Bean
////    public SimpleCookie cookie(){
////        SimpleCookie cookie = new SimpleCookie("SHAREJSESSIONID"); //  cookie的name,对应的默认是 JSESSIONID
////        cookie.setHttpOnly(true);
////        cookie.setPath("/");        //  path为 / 用于多个系统共享JSESSIONID
////        return cookie;
////    }
////
////    @Bean
////    public DefaultWebSessionManager sessionManager(){
////        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
////        sessionManager.setGlobalSessionTimeout(-1000L);    // 设置session超时
////        sessionManager.setDeleteInvalidSessions(true);      // 删除无效session
////        sessionManager.setSessionIdCookie(cookie());            // 设置JSESSIONID
////        sessionManager.setSessionDAO(sessionDAO());         // 设置sessionDAO
////        return sessionManager;
////    }
////
////    /**
////     * 1. 配置SecurityManager
////     * @return
////     */
////    @Bean
////    public SecurityManager  securityManager(){
////        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
////        securityManager.setRealm(realm());  // 设置realm
////        securityManager.setSessionManager(sessionManager());    // 设置sessionManager
////        securityManager.setCacheManager(redisCacheManager()); // 配置缓存的话，退出登录的时候crazycake会报错，要求放在session里面的实体类必须有个id标识
////        return securityManager;
////    }
////
////
////
////    @Bean
////    public RedisCacheManager redisCacheManager(){
////        RedisCacheManager cacheManager = new RedisCacheManager();   // crazycake 实现
////        cacheManager.setRedisManager(redisManager());
////        return cacheManager;
////    }
////
////    /**
////     * 3. 配置Realm
////     * @return
////     */
////    @Bean
////    public AuthorizingRealm realm(){
////        return new CustomRealm();
////    }
////
////    /**
////     * 4. 配置LifecycleBeanPostProcessor，可以来自动的调用配置在Spring IOC容器中 Shiro Bean 的生命周期方法
////     * @return
////     */
////    @Bean
////    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
////        return new LifecycleBeanPostProcessor();
////    }
////
////    /**
////     * 5. 启用IOC容器中使用Shiro的注解，但是必须配置第四步才可以使用
////     * @return
////     */
////    @Bean
////    @DependsOn("lifecycleBeanPostProcessor")
////    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
////        DefaultAdvisorAutoProxyCreator defaultAAP=new DefaultAdvisorAutoProxyCreator();
////        defaultAAP.setProxyTargetClass(true);
////        return new DefaultAdvisorAutoProxyCreator();
////    }
////
////    /**
////     * 6. 配置ShiroFilter
////     * @return
////     */
////    @Bean("shiroFilter")
////    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager){
////        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
////        shiroFilterFactoryBean.setSecurityManager(securityManager);
////        Map<String, String> map = new HashMap<>();
////        //登出
////        map.put("/logout", "logout");
////        //对所有用户认证
////        map.put("/**", "authc");
////        //登录
////        shiroFilterFactoryBean.setLoginUrl("/login");
////        //首页
////        shiroFilterFactoryBean.setSuccessUrl("/index");
////        //错误页面，认证不通过跳转
////        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
////        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
////        return shiroFilterFactoryBean;
////    }
////        //注入权限管理
////    @Bean
////    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor( SecurityManager securityManager) {
////        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
////        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
////        return authorizationAttributeSourceAdvisor;
////    }
//}
//
