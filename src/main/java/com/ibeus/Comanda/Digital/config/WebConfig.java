package com.ibeus.Comanda.Digital.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Define o nome da subpasta onde os arquivos serão salvos (DEVE ser o mesmo no StorageService)
    private final String SUB_FOLDER = "comanda-digital-uploads/images";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Encontra o caminho absoluto do diretório de uploads
        Path uploadDir = Paths.get(System.getProperty("user.dir"), SUB_FOLDER);
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        // Mapeia URLs que começam com /images/ para o diretório físico no disco
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}