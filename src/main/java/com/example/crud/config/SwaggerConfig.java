//package com.example.crud.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//
//@Configuration
//@EnableWebMvc
//public class SwaggerConfig implements WebMvcConfigurer {
//
//    /** swagger */
//    @Bean
//    public Docket ShopApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("Shop API")
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.api.shop.modules.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(this.ShopApiInfo())
//                .tags(   new Tag("AuthController", "Auth API")
//                        , new Tag("MemberController", "Member API")
//                );
//
//    }
//
//    private ApiInfo ShopApiInfo() {
//        return new ApiInfoBuilder()
//                .title("shop API")
//                .description("shop API")
//                .termsOfServiceUrl("http://www.shop-api.com")
//                .version("1.0")
//                .build();
//    }
//}