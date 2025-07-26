package com.nasor.postsapi.config.infraestructure;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class ApplicationConfig {
}
