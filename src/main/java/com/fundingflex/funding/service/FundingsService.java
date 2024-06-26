package com.fundingflex.funding.service;

import com.fundingflex.category.domain.dto.dto.CategoriesDto;

import groovy.util.logging.Log4j2;
import groovy.util.logging.Slf4j;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fundingflex.category.service.CategoriesService;
import com.fundingflex.common.enums.DeleteFlagEnum;
import com.fundingflex.funding.domain.dto.FundingIdsDTO;
import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.funding.domain.dto.FundingsInfoDTO;
import com.fundingflex.funding.domain.dto.ResponseFundingInfoDTO;
import com.fundingflex.funding.domain.entity.FundingJoin;
import com.fundingflex.funding.domain.entity.Fundings;
import com.fundingflex.funding.domain.entity.Images;
import com.fundingflex.funding.domain.enums.FundingsStatusEnum;
import com.fundingflex.funding.domain.form.FundingsForm;
import com.fundingflex.funding.repository.FundingJoinRepository;
import com.fundingflex.funding.repository.FundingsRepository;
import com.fundingflex.funding.repository.ImagesRepository;
import com.fundingflex.mybatis.mapper.category.CategoriesMapper;
import com.fundingflex.mybatis.mapper.funding.FundingsMapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FundingsService {

   private final FundingsRepository fundingsRepository;
   private final FundingsMapper fundingsMapper;
   private final ImagesRepository imagesRepository;
   private final CategoriesService categoriesService;
   private final CategoriesMapper categoriesRepository;
   private final FundingJoinRepository fundingJoinRepository;
   
   // 사용자별 좋아요 상태를 저장하기 위한 Set (실제 서비스에서는 데이터베이스를 사용)
    private final Set<String> userLikes = new HashSet<>();
   
   // 이미지 파일 경로
   private final Path fundingImgPath = Paths.get("src/main/resources/static/images/fundings");
   
   
   // 저장
   @Transactional
   public FundingIdsDTO saveFundings(FundingsForm fundingsForm, MultipartFile[] images) {
      
      // 폴더 경로 생성
       if (!Files.exists(fundingImgPath)) {
          
           try {
               Files.createDirectories(fundingImgPath);
               
           } catch (IOException ex) {
               throw new RuntimeException("폴더 생성 실패: " + ex.getMessage());
           }
       }
      
      // 카테고리 정보
      CategoriesDto categoriesDto =
         categoriesService.selectCategoriesById(fundingsForm.getCategoryId());
       
       // Fundings 객체 생성
       Fundings newFundings = Fundings.builder()
               .categoryId(fundingsForm.getCategoryId())
               .categoryName(categoriesDto.getCategoryName())
               .title(fundingsForm.getTitle())
               .content(fundingsForm.getContent())
               .goalAmount(fundingsForm.getGoalAmount())
               .statusFlag(FundingsStatusEnum.WAITING)
               .isDeleted(DeleteFlagEnum.N)
               .build();
      
       
       // 이미지 처리 및 Images 객체 생성
       int idx = 1;      // 파일 순서
       List<Images> imageList = new ArrayList<>();
       try {
           for (MultipartFile file : images) {
              
               if (!file.isEmpty()) {
                   String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                   Files.copy(file.getInputStream(), this.fundingImgPath.resolve(fileName));

                   Images newImage = Images.builder()
                           .imageUrl(fileName)
                           .fundings(newFundings)
                           .seq(idx++)
                           .isDeleted(DeleteFlagEnum.N)
                           .build();

                   imageList.add(newImage);
               }
           }
           
       } catch (IOException ex) {
           throw new RuntimeException("이미지 처리 실패: " + ex.getMessage());
       }
      
       
       // Fundings 객체에 이미지 리스트 설정
       newFundings.setImageList(imageList);

       // Fundings 객체와 Images 객체 저장
       fundingsRepository.save(newFundings);
       imagesRepository.saveAll(imageList);
       
       return FundingIdsDTO.builder()
             .categoryId(newFundings.getCategoryId())
             .fundingsId(newFundings.getFundingsId())
             .build();
   }

   
   // 상세 정보 전체 조회
   public ResponseFundingInfoDTO selectFundinsInfo(Long categoryId, Long fundingId) throws Exception {

      // fundingId, categoryId 확인
      if(categoriesRepository.existsById(categoryId) <= 0) {
         throw new NotFoundException("해당 카테고리가 존재하지 않습니다.");
      }

      if(!fundingsRepository.existsById(fundingId)) {
         throw new NotFoundException("해당 펀딩이 존재하지 않습니다.");
      }


      // 이미지 조회
      List<Images> imagesList = imagesRepository.findByFundings_FundingsIdOrderBySeqAsc(fundingId)
         .orElseThrow(() -> new NotFoundException("이미지가 존재하지 않습니다."));

      FundingsInfoDTO fundingsInfoDto =
         fundingsMapper.selectFundingInfo(categoryId, fundingId);

      return ResponseFundingInfoDTO.builder()
         .fundingsInfoDto(fundingsInfoDto)
         .imagesList(imagesList)
         .build();
   }
   

   // fundingId 기준 조회
   public Fundings findById(Long fundingId) {
      return fundingsRepository.findById(fundingId)
            .orElseThrow(() -> new EntityNotFoundException("펀딩 데이터가 존재하지 않습니다."));
   }
   
   
   // 펀딩 리스트 조회
   @Transactional
   public List<FundingsDTO> getAllFundings() {
       List<Fundings> fundingsList = fundingsRepository.findAll();
       
       return fundingsList.stream().map(funding -> {
          
           FundingsDTO dto = new FundingsDTO();
           dto.setFundingsId(funding.getFundingsId());
           dto.setTitle(funding.getTitle());
           dto.setContent(funding.getContent());
           dto.setStatusFlag(funding.getStatusFlag()); // 추가된 부분
           dto.setLikeCount(funding.getLikeCount());
           dto.setGoalAmount(funding.getGoalAmount());
           
           int currentAmount = fundingJoinRepository.findByFundingsId(funding.getFundingsId())
               .stream().mapToInt(FundingJoin::getFundingAmount).sum();
           dto.setCurrentAmount(currentAmount);
           
           List<String> imageUrls = funding.getImageList().stream()
               .map(Images::getImageUrl).collect(Collectors.toList());
           dto.setImageUrls(imageUrls);
           
           return dto;
           
       }).collect(Collectors.toList());
   }
   
   // 좋아요 기능
   public boolean likeFunding(Long fundingsId) {
        // 예시 사용자 ID (실제 구현에서는 세션이나 인증 정보를 통해 사용자 ID를 가져와야 함)
        String userId = "exampleUserId";
        String userFundingsKey = userId + "-" + fundingsId;
        
        if (userLikes.contains(userFundingsKey)) {
            return false; // 이미 좋아요를 누른 경우
        }

        userLikes.add(userFundingsKey);

        Fundings fundings = fundingsRepository.findById(fundingsId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid fundingsId"));
        
        fundings.setLikeCount(fundings.getLikeCount() + 1);
        fundingsRepository.save(fundings);
        return true;
    }
}
