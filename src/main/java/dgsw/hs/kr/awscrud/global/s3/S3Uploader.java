package dgsw.hs.kr.awscrud.global.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    public String upload(MultipartFile file, String directory) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String extension = extractExtension(file.getOriginalFilename());
        String key = directory + "/" + UUID.randomUUID() + extension;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }
    }

    private String extractExtension(String originalFilename) {
        String safeName = Objects.requireNonNullElse(originalFilename, "");
        int dotIndex = safeName.lastIndexOf('.');
        return dotIndex >= 0 ? safeName.substring(dotIndex) : "";
    }
}
