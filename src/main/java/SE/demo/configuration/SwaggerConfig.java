package SE.demo.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "MoaBom API 명세서",
                description = "모아봄(MoaBom) 서비스의 API 명세서입니다.",
                version = "v1.0.0"),
        tags = {
                @Tag(name = "User", description = "사용자 인증 및 정보"),
                @Tag(name = "OTT", description = "OTT 정보"),
                @Tag(name = "Program", description = "프로그램 검색 및 상세 정보"),
                @Tag(name = "Wishlist", description = "위시리스트"),
                @Tag(name = "Subscribe", description = "구독 정보"),
                @Tag(name = "Recommendation", description = "추천")
        }
)

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        // JWT 인증 스키마 정의
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme));
    }

    @Bean
    public OperationCustomizer addDefaultProduces() {
        return (operation, handlerMethod) -> {
            operation.getResponses().forEach((code, apiResponse) -> {
                if (code.startsWith("2") && apiResponse.getContent() != null) {
                    Content content = apiResponse.getContent();
                    if (content.size() == 1 && content.containsKey("*/*")) {
                        MediaType mediaType = content.remove("*/*");
                        content.addMediaType("application/json", mediaType);
                    }
                }
            });
            return operation;
        };
    }

    @Bean
    public OperationCustomizer addUnauthorizedResponseForSecuredOperations() {
        return (operation, handlerMethod) -> {
            if (operation.getSecurity() != null && !operation.getSecurity().isEmpty()) {
                if (!operation.getResponses().containsKey("401")) {
                    operation.getResponses().addApiResponse("401",
                            new ApiResponse().description("Unauthorized"));
                }
            }
            return operation;
        };
    }
}
