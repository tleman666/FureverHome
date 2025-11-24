package cn.fzu.edu.furever_home.storage.dto;

import lombok.Data;

@Data
public class FileObject {
    private String contentType;
    private byte[] data;
}