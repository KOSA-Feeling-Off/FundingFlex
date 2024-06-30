package com.fundingflex.funding.controller;


import com.fundingflex.funding.domain.entity.Images;
import com.fundingflex.funding.service.ImageService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImagesController {

    private final ImageService imageService;


    // 이미지 조회
    @GetMapping
    public ResponseEntity<Map<String, List<Images>>> getImages(@RequestParam("fundings-id") Long fundingsId) {
        List<Images> images = imageService.selectImagesByFundingId(fundingsId);
        if (images == null || images.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, List<Images>> response = new HashMap<>();
        response.put("imageList", images);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
