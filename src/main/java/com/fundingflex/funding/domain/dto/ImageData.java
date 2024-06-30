package com.fundingflex.funding.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageData {
    private String type; // 'existing' or 'new'
    private String url; // for existing images
    private String filename; // for new images
    private String base64; // Base64 encoded image data for new images
    private int order;
}
