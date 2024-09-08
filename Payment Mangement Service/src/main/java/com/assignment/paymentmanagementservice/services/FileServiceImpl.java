package com.assignment.paymentmanagementservice.services;

import com.assignment.paymentmanagementservice.constants.Constants;
import com.assignment.paymentmanagementservice.exceptions.CustomGenericException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {


    private final Path root = Paths.get("build/resources/main");

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (Exception e) {
            throw new CustomGenericException(Constants.INTERNAL_SERVER_ERROR_CODE, Constants.FILE_PATH_NOT_CREATED);
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (Exception e) {
            throw new CustomGenericException(Constants.INTERNAL_SERVER_ERROR_CODE, Constants.FILE_NOT_CREATED + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path path = root.resolve(filename);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new CustomGenericException(Constants.INTERNAL_SERVER_ERROR_CODE, Constants.FILE_NOT_READ);
            }
        } catch (Exception e) {
            throw new CustomGenericException(Constants.INTERNAL_SERVER_ERROR_CODE, Constants.GENERAL + e.getMessage());
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (Exception e) {
            throw new CustomGenericException(Constants.INTERNAL_SERVER_ERROR_CODE, Constants.FILE_NOT_LOADED);
        }
    }
}
