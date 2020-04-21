您好，我在使用 `javaConfig `配置拦截器时，发现其对`swagger`接口不生效。

xml与javaConfig 配置同一个拦截器 ，javaConfig 对 swagger /v2/api-doc 不生效


我在github 上传了一个 简单工程[ https://github.com/minggen/springmvc-questions ]  ,修改 `com.example.demo.config.AutoConfig` 可以复现

当使用 xml配置时，访问 http://localhost:8080/v2/api-docs 接口是 没有返回的
当打开注释 使用javaConfig时 ,正常返回不会被拦截。

https://github.com/spring-projects/spring-framework/blob/376434eb7545c39dcc7f8f87a28eeb9f26bbfd6e/spring-webmvc/src/main/java/org/springframework/web/servlet/config/annotation/InterceptorRegistration.java#L130

debug代码时，发现`InterceptorRegistration `将`HandlerInterceptor` 包装 `MappedInterceptor `时
 没有把 `MappedInterceptor `实例放入容器。

以至于在 `org.springframework.web.servlet.handler.AbstractHandlerMapping.detectMappedInterceptors`
中 找不到 `MappedInterceptor`的实例，

`org.springframework.web.servlet.handler.AbstractHandlerMapping#initApplicationContext`

故而不能为`PropertySourcedRequestMappingHandlerMapping` 添加 `adaptedInterceptors`，
使得拦截器不拦截`PropertySourcedRequestMappingHandlerMapping` 类型的接口，例如`Swagger`的 `/v2/api-docs` 接口。

```	java
	mappedInterceptors.addAll(
				BeanFactoryUtils.beansOfTypeIncludingAncestors(
						obtainApplicationContext(), MappedInterceptor.class, true, false).values());
	}
```




When i debugging the code, I  found ，
`InterceptorRegistration` will wrap the custom interceptor into an instance of the `MappedInterceptor` class,
and then put the instance into `interceptors`,
but not into the `applicationContext`,

```java
mappedInterceptors.addAll(
	BeanFactoryUtils.beansOfTypeIncludingAncestors(
		obtainApplicationContext(), MappedInterceptor.class, true, false).values());
```

 `adaptedInterceptors` cannot be added for` PropertySourcedRequestMappingHandlerMapping`
As a result,  the interceptor not to take effect
