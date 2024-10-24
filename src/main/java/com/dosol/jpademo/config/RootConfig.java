package com.dosol.jpademo.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {
        @Bean
        public ModelMapper getMapper() {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration()
                    .setFieldMatchingEnabled(true)
                    .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                    .setMatchingStrategy(MatchingStrategies.LOOSE);
            //이게 LOOSE로 해야하는게 보드랑 리플라이랑 긁을때 리플라이에는 bno가 없어서 이렇게 맞춰줘야함.
            return modelMapper;
    }
}
//디비설정같은거 넣는거임