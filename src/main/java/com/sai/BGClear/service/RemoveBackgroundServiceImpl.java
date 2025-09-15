package com.sai.BGClear.service;

import com.sai.BGClear.client.ClipdropClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RemoveBackgroundServiceImpl implements RemoveBackgroundService {

    @Value("${clipdrop.api-key}")
    private String apiKey;

    private final ClipdropClient clipdropClient;

    public byte[] removeBackground(MultipartFile file) {
        return clipdropClient.removeBackground(file, apiKey);
    }

}
