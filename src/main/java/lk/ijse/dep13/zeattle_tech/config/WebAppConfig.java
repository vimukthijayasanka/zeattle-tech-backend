package lk.ijse.dep13.zeattle_tech.config;

import lk.ijse.dep13.zeattle_tech.security.jwt.JwtAuthEntryPoint;
import lk.ijse.dep13.zeattle_tech.service.user.ShopUserDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class WebAppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
