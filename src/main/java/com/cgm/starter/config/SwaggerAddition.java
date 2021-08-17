package com.cgm.starter.config;

import com.cgm.starter.base.ResponseData;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Operation;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 手动添加swagger接口，如登录接口等
 *
 * @author songyinyin
 * @author cgm
 */
@Component
public class SwaggerAddition implements ApiListingScannerPlugin {
    /**
     * Implement this method to manually add ApiDescriptions
     * 实现此方法可手动添加ApiDescriptions
     *
     * @param context - Documentation context that can be used infer documentation context
     * @return List of {@link ApiDescription}
     * @see ApiDescription
     */
    @Override
    public List<ApiDescription> apply(DocumentationContext context) {
        Parameter usernameParam = new ParameterBuilder()
                .description("用户名")
                .type(new TypeResolver().resolve(String.class))
                .name("username")
                .defaultValue("admin")
                .parameterType("query")
                .parameterAccess("access")
                .required(true)
                .modelRef(new ModelRef("string"))
                .build();
        Parameter passwordParam = new ParameterBuilder()
                .description("密码")
                .type(new TypeResolver().resolve(String.class))
                .name("password")
                .defaultValue("123456")
                .parameterType("query")
                .parameterAccess("access")
                .required(true)
                .modelRef(new ModelRef("string"))
                .build();
        ResponseMessage responseMessage = new ResponseMessageBuilder()
                .code(200)
                .message("OK")
                .responseModel(new ModelRef(ResponseData.class.getName()))
                .build();

        Operation usernamePasswordOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .summary("用户名密码登录")
                .notes("username/password登录")
                // 接收参数格式
                .consumes(Sets.newHashSet(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                // 返回参数格式
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                .tags(Sets.newHashSet("登录"))
                .parameters(Arrays.asList(usernameParam, passwordParam))
                .responseMessages(Collections.singleton(responseMessage))
                .build();
        ApiDescription loginApiDescription = new ApiDescription("login", "/api/login",
                "登录接口", Collections.singletonList(usernamePasswordOperation), false);

        return Collections.singletonList(loginApiDescription);
    }

    /**
     * 是否使用此插件
     *
     * @param documentationType swagger文档类型
     * @return true 启用
     */
    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType);
    }
}
