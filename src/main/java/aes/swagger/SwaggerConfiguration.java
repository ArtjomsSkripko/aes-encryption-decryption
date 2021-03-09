package aes.swagger;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import io.swagger.models.auth.In;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(
            DocumentationType.SWAGGER_2)
            .securitySchemes(Collections.singletonList(getApiKey()))
            .securityContexts(Collections.singletonList(getSecurityContext()))
            .groupName(
                "default")
            .groupName(
                "default")
            .globalOperationParameters(Lists.newArrayList(
                new ParameterBuilder().name("Authorization")
                    .description("JWT token identifying request")
                    .modelRef(new ModelRef("string")).parameterType("header")
                    .required(false).build()))
            .useDefaultResponseMessages(true)
            .directModelSubstitute(LocalDate.class, String.class)
            .directModelSubstitute(ZonedDateTime.class, String.class)
            .directModelSubstitute(LocalDate.class, String.class)
            .directModelSubstitute(LocalTime.class, String.class)
            .directModelSubstitute(Integer.class, String.class).select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build();
    }

    protected ApiKey getApiKey() {
        return new ApiKey("IdentityToken", "Authorization", In.HEADER.name());
    }

    protected SecurityContext getSecurityContext() {
        return SecurityContext.builder().securityReferences(getDefaultAuth())
            .forPaths(PathSelectors.any())
            .build();
    }

    protected List<SecurityReference> getDefaultAuth() {
        return Collections.singletonList(SecurityReference.builder().scopes(new AuthorizationScope[0]).reference("IdentityToken").build());
    }
}