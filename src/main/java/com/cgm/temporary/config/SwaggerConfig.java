package com.cgm.temporary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author cgm
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                //apiInfo指定测试文档基本信息，这部分将在页面展示
                .apiInfo(apiInfo())
                .select()
                //apis() 控制哪些接口暴露给swagger，
                // RequestHandlerSelectors.any() 所有都暴露
                // RequestHandlerSelectors.basePackage("com.info.*")  指定包位置
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 基本信息，将在页面展示
     *
     * @return 基本信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("临时项目")
                .description("快速原型项目。\n为前端快速提供接口，为其他项目提供参考，为新想法提供基础运行环境。")
                .contact(
                        new Contact("cgm", "www.baidu.com", "chen.guangming@diligentfirst.com")
                )
                //版本号
                .version("1.0.0-SNAPSHOT")
                .build();
    }
}
