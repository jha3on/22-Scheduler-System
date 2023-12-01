package system.share;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import system.share.base.converter.TypeConverter;
import system.share.base.converter.TypeDeserializer;
import system.share.base.converter.TypeSerializer;

@Slf4j
@Configuration
public class SystemConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new TypeConverter.SortConverter());
        registry.addConverter(new TypeConverter.UserSortConverter());

        registry.addConverter(new TypeConverter.JobClassConverter());
        registry.addConverter(new TypeConverter.JobSortConverter());

        registry.addConverter(new TypeConverter.TriggerPolicyConverter());
        registry.addConverter(new TypeConverter.TriggerClassConverter());
        registry.addConverter(new TypeConverter.TriggerSortConverter());
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        JavaTimeModule module = new JavaTimeModule();

        return builder -> {
            builder.serializers(TypeSerializer.localDate());
            builder.serializers(TypeSerializer.localDateTime());
            builder.deserializers(TypeDeserializer.localDate());
            builder.deserializers(TypeDeserializer.localDateTime());
            builder.modules(module);
        };
    }
}