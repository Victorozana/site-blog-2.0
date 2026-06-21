package com.blog.bo;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@ApplicationScoped
public class ImageStorageBO {
    private final Path uploadDirectory = Paths.get("uploads/images");

    public String save(FileUpload file) {
        try {
            // Cria a pasta se ela não existir
            if (!Files.exists(uploadDirectory)) {
                Files.createDirectories(uploadDirectory);
            }

            // Gera um nome único para evitar que uma foto substitua outra com o mesmo nome
            String uniqueFileName = UUID.randomUUID().toString() + "-" + file.fileName();
            Path targetPath = uploadDirectory.resolve(uniqueFileName);

            // Move o arquivo temporário do Quarkus para a nossa pasta final
            Files.copy(file.filePath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return "/user/uploads/images/" + uniqueFileName;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar a imagem no servidor", e);
        }
    }

}
