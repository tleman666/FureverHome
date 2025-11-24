package cn.fzu.edu.furever_home.storage.service.impl;

import cn.fzu.edu.furever_home.storage.service.StorageService;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.GetObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioStorageService implements StorageService {
    private final MinioClient minioClient;
    private final Environment env;

    private void ensureBucket(String bucket) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String upload(String bucket, MultipartFile file, String objectName) {
        ensureBucket(bucket);
        try {
            String name = objectName;
            if (name == null || name.isEmpty()) {
                String ext = "";
                String original = file.getOriginalFilename();
                if (original != null && original.contains(".")) {
                    ext = original.substring(original.lastIndexOf('.'));
                }
                name = UUID.randomUUID().toString().replace("-", "") + ext;
            }
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(name)
                            .contentType(file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build());
            return name;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getUrl(String bucket, String objectName, Integer expirySeconds) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .method(io.minio.http.Method.GET)
                            .expiry(expirySeconds)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String bucket, String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectName).build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public cn.fzu.edu.furever_home.storage.dto.FileObject getFile(String bucket, String objectName) {
        try {
            StatObjectResponse stat = minioClient
                    .statObject(StatObjectArgs.builder().bucket(bucket).object(objectName).build());
            java.io.InputStream is = minioClient
                    .getObject(GetObjectArgs.builder().bucket(bucket).object(objectName).build());
            byte[] bytes = is.readAllBytes();
            is.close();
            cn.fzu.edu.furever_home.storage.dto.FileObject f = new cn.fzu.edu.furever_home.storage.dto.FileObject();
            f.setContentType(stat.contentType());
            f.setData(bytes);
            return f;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public cn.fzu.edu.furever_home.storage.dto.StreamObject getStream(String bucket, String objectName, Long start,
            Long length) {
        try {
            StatObjectResponse stat = minioClient
                    .statObject(StatObjectArgs.builder().bucket(bucket).object(objectName).build());
            long total = stat.size();
            long s = start == null ? 0 : Math.max(0, Math.min(start, total - 1));
            long l;
            if (length == null) {
                l = total - s;
            } else {
                l = Math.max(0, Math.min(length, total - s));
            }
            java.io.InputStream is = minioClient
                    .getObject(GetObjectArgs.builder().bucket(bucket).object(objectName).offset(s).length(l).build());
            cn.fzu.edu.furever_home.storage.dto.StreamObject so = new cn.fzu.edu.furever_home.storage.dto.StreamObject();
            so.setContentType(stat.contentType());
            so.setTotalLength(total);
            so.setStart(s);
            so.setContentLength(l);
            so.setInputStream(is);
            return so;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}