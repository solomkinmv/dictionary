package in.solomk.dictionary.config;

import in.solomk.dictionary.api.mapper.UserWordsWebApiMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappersConfiguration {

    @Bean
    public UserWordsWebApiMapper userWordsWebApiMapper() {
        return UserWordsWebApiMapper.INSTANCE;
    }
}
