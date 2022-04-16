package br.com.rafael.catalogo.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.models.auth.In;
import springfox.documentation.builders.*;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@EnableSwagger2
public class SwaggerConfig {

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;


    private final Response m200 = simpleMessage(200, "Chamada realizada com sucesso");
    private final Response m201 = simpleMessage(201, "Recurso criado");
    private final Response m204 = simpleMessage(204, "Atualização ok");
    private final Response m401 = simpleMessage(401, "Autorização é requerida");
    private final Response m403 = simpleMessage(403, "Não autorizado");
    private final Response m404 = simpleMessage(404, "Objeto não encontrado");
    private final Response m422 = simpleMessage(422, "Erro de validação");
    private final Response m500 = simpleMessage(500, "Erro inesperado");

    @Bean
    public Docket api() {
        return
//                new Docket(DocumentationType.SWAGGER_2)
//                .useDefaultResponseMessages(false)
//                .globalResponses(HttpMethod.POST, Arrays.asList(m201, m401, m403, m422, m500, m200))
//                .globalResponses(HttpMethod.GET, Arrays.asList(m403, m404, m500))
//                .globalResponses(HttpMethod.PUT, Arrays.asList(m401, m403, m204, m422, m500))
//                .globalResponses(HttpMethod.DELETE, Arrays.asList(m401, m403, m404, m200)).select()
//                .apis(RequestHandlerSelectors.basePackage("br.com.rafael.catalogo.resources"))
//                .paths(PathSelectors.any()).build()
//                .securitySchemes(Arrays.asList(securitySchema()))
//                .securityContexts(Collections.singletonList(securityContext()))
                new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.POST, Arrays.asList(m201, m401, m403, m422, m500, m200))
                .globalResponses(HttpMethod.GET, Arrays.asList(m403, m404, m500))
                .globalResponses(HttpMethod.PUT, Arrays.asList(m401, m403, m204, m422, m500))
                .globalResponses(HttpMethod.DELETE, Arrays.asList(m401, m403, m404, m200)).select()
                .apis(RequestHandlerSelectors.basePackage("br.com.rafael.catalogo.resources"))
                .paths(PathSelectors.any()).build()
                .securityContexts(Collections.singletonList(securityContext()))
                        .securitySchemes(Arrays.asList(
                                OAuth2Scheme.OAUTH2_PASSWORD_FLOW_BUILDER
                                        .name("oauth2schema")
                                        .tokenUrl("/oauth/token")
                                        .scopes(Arrays.asList(getAuthorizationScopes()))
                                        .build()))
                ;

    }

    private Response simpleMessage(int code, String msg) {
        return new ResponseBuilder().code(Integer.toString(code)).description(msg).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Demo Reference with spring/spring cloud").version("1.0")
                .description("Rafael Marinheiro ").build();
    }

    private OAuth securitySchema() {

        List<AuthorizationScope> authorizationScopeList = new ArrayList();

        List<GrantType> grantTypes = new ArrayList();
        GrantType creGrant =
//                new ResourceOwnerPasswordCredentialsGrant("http://localhost:8080/oauth/token");

                new ImplicitGrantBuilder()
                        .loginEndpoint(new LoginEndpoint("/oauth/token"))
                                .build();
        grantTypes.add(creGrant);

        return
//                new OAuth("oauth2schema", authorizationScopeList, grantTypes)
                new OAuthBuilder()
                        .name("oauth2schema")
                        .grantTypes(grantTypes)
                        .scopes(authorizationScopeList)
                        .build()
                ;

    }

    private List<SecurityReference> defaultAuth() {
        final AuthorizationScope[] authorizationScopes = getAuthorizationScopes();
        return Collections.singletonList(new SecurityReference("oauth2schema", authorizationScopes));
    }

    private AuthorizationScope[] getAuthorizationScopes() {
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[3];
        authorizationScopes[0] = new AuthorizationScope("read", "read all");
        authorizationScopes[1] = new AuthorizationScope("trust", "trust all");
        authorizationScopes[2] = new AuthorizationScope("write", "write all");
        return authorizationScopes;
    }

    @SuppressWarnings("deprecation")
    @Bean
    public SecurityConfiguration securityInfo() {
        return new SecurityConfiguration(clientId, clientSecret, "", "", "", ApiKeyVehicle.HEADER, "", "+");
    }

    private SecurityContext securityContext() {
        return
//		 SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.ant("/producties/**"))
//				.forPaths(PathSelectors.ant("/categories/**")).forPaths(PathSelectors.ant("/user/**")).build();
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .operationSelector(operationContext ->
                                (PathSelectors.ant("/user/**")
                                        .or(PathSelectors.ant("/producties/**"))
                                        .or(PathSelectors.ant("/categories/**"))
                                        .test(operationContext.requestMappingPattern()))
                        ).build();
    }

}
