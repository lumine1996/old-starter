package com.cgm.starter.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

/**
 * @author cgm
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final String TOKEN_NAME = "X-Token";
    private static final String PASS_AS = "header";
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // apiInfo指定测试文档基本信息，这部分将在页面展示
                .apiInfo(apiInfo())
                .select()
                // apis() 控制哪些接口暴露给swagger，
                // RequestHandlerSelectors.any() 所有都暴露
                // RequestHandlerSelectors.basePackage("com.info.*")  指定包位置
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(securityContexts())
                .securitySchemes(Collections.singletonList(new ApiKey(TOKEN_NAME, TOKEN_NAME, PASS_AS)));
    }

    private List<SecurityContext> securityContexts() {
        SecurityContext securityContext = SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
        return Collections.singletonList(securityContext);
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = {authorizationScope};
        SecurityReference securityReference = new SecurityReference(TOKEN_NAME, authorizationScopes);
        return Collections.singletonList(securityReference);
    }

    /**
     * 基本信息，将在页面展示
     *
     * @return 基本信息
     */
    private ApiInfo apiInfo() {
        String localhost = "localhost";
        try {
            localhost = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return new ApiInfoBuilder()
                .title("临时项目")
                .description("快速原型项目。\n为前端快速提供接口，为其他项目提供参考，为新想法提供基础运行环境。")
                .contact(
                        new Contact("cgm", "http://" + localhost + ":9528/", "675056544@qq.com")
                )
                //版本号
                .version("1.0.0-SNAPSHOT")
                .build();
    }
}
