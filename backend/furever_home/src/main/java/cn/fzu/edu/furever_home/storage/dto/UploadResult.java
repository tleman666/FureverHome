package cn.fzu.edu.furever_home.storage.dto;

import lombok.Data;

@Data
public class UploadResult {
    private String bucket;
    private String objectName;
    private String url;
}