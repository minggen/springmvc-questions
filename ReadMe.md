Hello, I found it didn't make sense to intercept `swagger` when I use `javaConfig` on interceptor configuration.

When xml and javaConfig are used in the same interceptor, javaConfig didn't work on swagger /v2/api-doc.

Also, I create a demo about this issue [https://github.com/minggen/springmvc-questions], and the issue could be reproduced when modifying  `com.example.demo.config.AutoConfig` .

- When using xml configuration, there is no return value after calling http://localhost:8080/v2/api-docs .

- When using annotation configuration, the right return value would not be intercepted properly.

When I try to figure out the reason, in [this line of code](https://github.com/spring-projects/spring-framework/blob/376434eb7545c39dcc7f8f87a28eeb9f26bbfd6e/spring-webmvc/src/main/java/org/springframework/web/servlet/config/annotation/InterceptorRegistration.java#L130), I  found `InterceptorRegistration` will wrap the custom interceptor into an instance of the `MappedInterceptor` class, and then put the instance into `interceptors`, but not into the `applicationContext`,  so that `adaptedInterceptors` cannot be added for ` PropertySourcedRequestMappingHandlerMapping`. As a result,  the interceptor will not take effect.

```java
mappedInterceptors.addAll(
 BeanFactoryUtils.beansOfTypeIncludingAncestors(
  obtainApplicationContext(), MappedInterceptor.class, true, false).values());
```


