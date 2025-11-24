package cn.fzu.edu.furever_home.storage.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String upload(String bucket, MultipartFile file, String objectName);

    String getUrl(String bucket, String objectName, Integer expirySeconds);

    void delete(String bucket, String objectName);

    cn.fzu.edu.furever_home.storage.dto.FileObject getFile(String bucket, String objectName);

    cn.fzu.edu.furever_home.storage.dto.StreamObject getStream(String bucket, String objectName, Long start,
            Long length);
}