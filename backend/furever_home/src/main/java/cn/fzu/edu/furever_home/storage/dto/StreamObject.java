package cn.fzu.edu.furever_home.storage.dto;

import lombok.Data;

import java.io.InputStream;

@Data
public class StreamObject {
    private String contentType;
    private long totalLength;
    private long start;
    private long contentLength;
    private InputStream inputStream;
}