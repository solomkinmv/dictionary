package in.solomk.dictionary.api;

import in.solomk.dictionary.api.handler.AddWordHandler;
import in.solomk.dictionary.api.handler.GetWordsHandler;
import in.solomk.dictionary.api.handler.ProfileHandler;
import in.solomk.dictionary.api.handler.language.AddLanguageHandler;
import in.solomk.dictionary.api.handler.language.DeleteLanguageHandler;
import in.solomk.dictionary.api.handler.language.GetLanguagesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouteConfiguration {

    @Bean
    RouterFunction<ServerResponse> routerFunction(GetWordsHandler getWordsHandler,
                                                  AddWordHandler addWordHandler,
                                                  ProfileHandler profileHandler,
                                                  GetLanguagesHandler getLanguagesHandler,
                                                  AddLanguageHandler addLanguageHandler,
                                                  DeleteLanguageHandler deleteLanguageHandler) {
        HandlerFunction<ServerResponse> indexPage = (req) -> ServerResponse.ok().bodyValue(new ClassPathResource("public/index.html"));
        return RouterFunctions.route()
                              .GET("/api/languages", getLanguagesHandler)
                              .PUT("/api/languages/{languageCode}", addLanguageHandler)
                              .DELETE("/api/languages/{languageCode}", deleteLanguageHandler)
                              .GET("/api/words", getWordsHandler)
                              .POST("/api/words", addWordHandler)
                              .GET("/api/me", profileHandler)
                              .resources("/**", new ClassPathResource("/public/"))
                              .GET("/**", indexPage)
                              .build();
    }
}
