package com.fundingflex.funding.service;

import com.fundingflex.common.enums.DeleteFlagEnum;
import com.fundingflex.funding.domain.dto.ImageData;
import com.fundingflex.funding.domain.entity.Images;
import com.fundingflex.mybatis.mapper.funding.ImagesMapper;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImagesMapper imagesMapper;


	// 이미지 파일 경로
	private final Path fundingImgPath = Paths.get("src/main/resources/static/images/fundings");
	
	
	// 파일 경로 생성
	public void createDirectoriesIfNotExists() {
        if (!Files.exists(fundingImgPath)) {
            try {
                Files.createDirectories(fundingImgPath);
            } catch (IOException ex) {
                throw new RuntimeException("폴더 생성 실패: " + ex.getMessage(), ex);
            }
        }
    }
	
	
	// 이미지 url 생성
	public List<Images> processImages(MultipartFile[] images, Long fundingsId) {
		
        List<Images> imageList = new ArrayList<>();
        int idx = 1; // 파일 순서
        try {
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_"
                        + Objects.requireNonNull(
                            file.getOriginalFilename()).replace(" ", "_");
                    Files.copy(file.getInputStream(), fundingImgPath.resolve(fileName));
                    
                    Images newImage =
                    		Images.of(fundingsId, idx++, fileName, DeleteFlagEnum.N);
                    
                    imageList.add(newImage);
                }
            }
            
        } catch (IOException ex) {
            throw new RuntimeException("이미지 처리 실패: " + ex.getMessage(), ex);
        }
        
        return imageList;
    }
	
	
	// 이미지 저장
	public void saveImages(List<Images> imageList) {
        try {
            int insertCount = imagesMapper.insertFundingsImages(imageList);
            if (insertCount <= 0) {
                throw new SQLException("이미지 저장에 실패했습니다.");
            }
            
        } catch (SQLException e) {
            log.error("이미지 저장 오류: {}", e.getMessage(), e);
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }


    // 이미지 조회 (fundingId 기준)
    public List<Images> selectImagesByFundingId(Long fundingId) {
        return imagesMapper.findByfundingsId(fundingId);
    }

    
    // 이미지 IsDelete 수정
    public int updateImageIsDelete(Long fundingId, DeleteFlagEnum isDelete) {
        return imagesMapper.updateImageIsDelete(fundingId, isDelete);
    }

    
    // 이미지 전체 Insert (수정)
    public void updateImageAll(List<ImageData> images, Long fundingId) throws IOException {

        List<Images> updateImageList = new ArrayList<>();
        int idx = 1; // 파일 순서
        for (ImageData file : images) {

            String fileName = "";
            InputStream inputStream = null;

            if (file.getType().equals("new")) {
                if(!file.getFilename().isEmpty()){
                    byte[] decodedBytes =
                        Base64.getDecoder().decode(file.getBase64().split(",")[1]);

                    inputStream = new ByteArrayInputStream(decodedBytes);

                    fileName =
                        UUID.randomUUID() + "_" + file.getFilename().replace(" ", "_");
                }

            } else if(file.getType().equals("existing")) {
                if(!file.getUrl().isEmpty()) {
                    inputStream = getInputStreamFromExistingFile(file.getUrl());
                    fileName = UUID.randomUUID() + "_" + file.getUrl().substring(37);

                } else {
                    byte[] decodedBytes =
                        Base64.getDecoder().decode(file.getBase64().split(",")[1]);

                    inputStream = new ByteArrayInputStream(decodedBytes);

                    fileName =
                        UUID.randomUUID() + "_" + file.getFilename().replace(" ", "_");;
                }

            }

            if (inputStream != null) {
                Files.copy(inputStream, fundingImgPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);
            }

            Images newImage =
                Images.of(fundingId, idx++, fileName, DeleteFlagEnum.N);

            updateImageList.add(newImage);
        }
        
        // 이미지 저장
        saveImages(updateImageList);
    }

    // 파일 읽어오기
    private InputStream getInputStreamFromExistingFile(String url) throws IOException {
        String uuid = extractUUID(url);

        // 파일 목록 조회
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(fundingImgPath)) {
            for (Path path : directoryStream) {
                String fileName = path.getFileName().toString();
                if (fileName.startsWith(uuid)) {
                    return Files.newInputStream(path);
                }
            }
        }

        throw new FileNotFoundException("File with UUID " + uuid+ " not found in " + fundingImgPath);
    }

    private String extractUUID(String fileName) {
        // UUID 형식 추출: 8-4-4-4-12
        String uuidRegex = "^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}";
        Pattern pattern = Pattern.compile(uuidRegex);
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return matcher.group();

        } else {
            throw new IllegalArgumentException("Invalid UUID format in file name: " + fileName);
        }
    }
}
