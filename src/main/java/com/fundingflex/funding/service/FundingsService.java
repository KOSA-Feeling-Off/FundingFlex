package com.fundingflex.funding.service;


import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fundingflex.category.domain.entity.Categories;
import com.fundingflex.category.service.CategoriesService;
import com.fundingflex.common.enums.DeleteFlagEnum;
import com.fundingflex.funding.domain.dto.FundingIdsDTO;
import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.funding.domain.dto.FundingsInfoDTO;
import com.fundingflex.funding.domain.dto.ImageData;
import com.fundingflex.funding.domain.dto.ResponseFundingInfoDTO;
import com.fundingflex.funding.domain.entity.FundingJoin;
import com.fundingflex.funding.domain.entity.Fundings;
import com.fundingflex.funding.domain.entity.Images;
import com.fundingflex.funding.domain.form.FundingsForm;
import com.fundingflex.member.domain.entity.Members;
import com.fundingflex.mybatis.mapper.category.CategoriesMapper;
import com.fundingflex.mybatis.mapper.funding.FundingsMapper;
import com.fundingflex.mybatis.mapper.member.MembersMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FundingsService {

	private static final Logger logger = LoggerFactory.getLogger(FundingsService.class);
	
	private final FundingsMapper fundingsMapper;
	private final ImageService imageService;

	private final CategoriesService categoriesService;
	private final CategoriesMapper categoriesMapper;
	private final MembersMapper membersMapper;
	

	// 사용자별 좋아요 상태를 저장하기 위한 Set (실제 서비스에서는 데이터베이스를 사용)
	private final Set<String> userLikes = new HashSet<>();
	

	// 저장
	@Transactional
	public FundingIdsDTO saveFundings(FundingsForm fundingsForm, MultipartFile[] images, Long userId) {

		try {
            // 유저 정보 가져오기
			Members member = membersMapper.findById(userId);

            // 폴더 경로 생성
            imageService.createDirectoriesIfNotExists();

            // 카테고리 정보
            Categories categoriesDto =
            		categoriesService.selectCategoriesById(fundingsForm.getCategoryId());

            // Fundings 객체 저장
            Fundings newFundings =
            		Fundings.of(fundingsForm, userId, categoriesDto.getCategoryName(), member.getNickname());
            fundingsMapper.insertFundings(newFundings);

            // 저장 후 id 받아옴
            Long fundingsId = newFundings.getFundingsId();


            // funding_conditions 저장
            fundingsMapper.insertFundingConditions(fundingsId, newFundings.getGoalAmount());


            // 이미지 처리 및 Images 객체 생성
            List<Images> imageList = imageService.processImages(images, fundingsId);


            // Images 객체 저장
            imageService.saveImages(imageList);


            return FundingIdsDTO.of(categoriesDto.getCategoryId(), fundingsId);

        } catch (Exception ex) {
        	log.error("=>>>>>> 펀딩 저장 실패: {}" , ex.getMessage());
            throw new RuntimeException("펀딩 저장 실패: " + ex.getMessage(), ex);
        }
	}


	// 펀딩 수정 폼 조회
	public FundingsForm selectFundingsForm(Long categoryId, Long fundingsId) {

		try {
			// fundingId, categoryId 확인
			if (categoriesMapper.existsById(categoryId) <= 0) {
				throw new NotFoundException("해당 카테고리가 존재하지 않습니다.");
			}

			if (fundingsMapper.existsById(fundingsId) <= 0) {
				throw new NotFoundException("해당 펀딩이 존재하지 않습니다.");
			}

			return fundingsMapper.selectFundingsForm(categoryId, fundingsId);

		} catch(Exception ex) {
			log.error("=>>>>>> 펀딩 수정 정보 조회 실패: {}" , ex.getMessage());
			throw new RuntimeException("펀딩 수정 정보 조회 실패: " + ex.getMessage(), ex);
		}
	}


    // 펀딩 수정
    @Transactional
    public FundingIdsDTO updateFunding(FundingsForm fundingsForm, List<ImageData> images,
        Long categoryId, Long fundingId) {

        try {

            // 펀딩 정보 조회
            Fundings fundings = fundingsMapper.findById(fundingId)
                .orElseThrow(() -> new NotFoundException("해당 펀딩 정보가 없습니다."));

            // 펀딩 정보 수정
            if(fundingsMapper.updateFunding(fundingsForm, categoryId, fundingId) <= 0) {
                throw new SQLException("펀딩 수정에 실패했습니다.");
            }

            // 펀딩 자금조달 목표금액 수정
            if(fundings.getGoalAmount() != fundingsForm.getGoalAmount()) {
                fundingsMapper.updateFundingConditionsAmount(fundingId, fundingsForm.getGoalAmount());
            }

            // 이미지 수정
            // 이전 이미지 isDelete 값 수정
            if(imageService.updateImageIsDelete(fundingId, DeleteFlagEnum.Y) <= 0) {
                throw new SQLException("이미지 수정에 실패했습니다.");
            }

            // 이미지 저장
            imageService.updateImageAll(images, fundingId);

            return FundingIdsDTO.of(categoryId, fundingId);

        } catch (Exception ex) {
            log.error("=>>>>>> 펀딩 수정 실패: {}" , ex.getMessage());
            throw new RuntimeException("펀딩 수정 실패: " + ex.getMessage(), ex);
        }
    }


	// 상세 정보 전체 조회
	public ResponseFundingInfoDTO selectFundinsInfo(Long categoryId, Long fundingId) {

		try {
			// fundingId, categoryId 확인
			if (categoriesMapper.existsById(categoryId) <= 0) {
				throw new NotFoundException("해당 카테고리가 존재하지 않습니다.");
			}

			if (fundingsMapper.existsById(fundingId) <= 0) {
				throw new NotFoundException("해당 펀딩이 존재하지 않습니다.");
			}

			// 이미지 조회
			List<Images> imagesList = imageService.selectImagesByFundingId(fundingId);


			// 펀딩 정보 조회
			FundingsInfoDTO fundingsInfoDto =
					fundingsMapper.selectFundingInfo(categoryId, fundingId);


			return ResponseFundingInfoDTO.builder()
					.fundingsInfoDto(fundingsInfoDto)
					.imagesList(imagesList)
					.build();

		} catch(Exception ex) {
			log.error("=>>>>>> 펀딩 상세 조회 실패: {}" , ex.getMessage());
            throw new RuntimeException("펀딩 상세 조회 실패: " + ex.getMessage(), ex);
		}
	}


    // 펀딩 리스트 조회
    @Transactional
    public List<FundingsDTO> getAllFundings(String sortBy, Long userId) {
        List<FundingsDTO> fundingsList = fundingsMapper.getAllFundings(sortBy, userId);
        return fundingsList.stream().map(funding -> {
            FundingsDTO dto = new FundingsDTO();
            dto.setFundingsId(funding.getFundingsId());
            dto.setTitle(funding.getTitle());
            dto.setContent(funding.getContent());
            dto.setStatusFlag(funding.getStatusFlag());
            dto.setLikeCount(funding.getLikeCount());
            dto.setGoalAmount(funding.getGoalAmount());
            dto.setCategoryId(funding.getCategoryId()); // 추가
            dto.setCategoryId(funding.getCategoryId()); // 추가
            dto.setExistsFlag(funding.getExistsFlag()); // 좋아요 상태 설정

            int currentAmount = fundingsMapper.findFundingJoinsByFundingsId(funding.getFundingsId()).stream()
                .mapToInt(FundingJoin::getFundingAmount).sum();
            dto.setCurrentAmount(currentAmount);

            List<String> imageUrls = funding.getImageUrls();
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

        Fundings fundings = fundingsMapper.findById(fundingsId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid fundingsId"));

        fundings.setLikeCount(fundings.getLikeCount() + 1);
        fundingsMapper.updateLikeCount(fundingsId, fundings.getLikeCount());
        return true;
    }


    // 카테고리별 목록
    public List<FundingsDTO> getFundingsByCategory(Long categoryId, String sortBy) {
        logger.debug("Getting fundings for category ID: {} with sort by: {}", categoryId, sortBy);
        List<FundingsDTO> fundingsList = fundingsMapper.getFundingsByCategory(categoryId, sortBy);
        logger.debug("Fundings list retrieved: {}", fundingsList);
        return fundingsList.stream().map(funding -> {
            FundingsDTO dto = new FundingsDTO();
            dto.setFundingsId(funding.getFundingsId());
            dto.setTitle(funding.getTitle());
            dto.setContent(funding.getContent());
            dto.setStatusFlag(funding.getStatusFlag());
            dto.setLikeCount(funding.getLikeCount());
            dto.setGoalAmount(funding.getGoalAmount());
            dto.setCategoryId(funding.getCategoryId());

            int currentAmount = fundingsMapper.findFundingJoinsByFundingsId(funding.getFundingsId()).stream()
                .mapToInt(FundingJoin::getFundingAmount).sum();
            dto.setCurrentAmount(currentAmount);

            List<String> imageUrls = funding.getImageUrls();
            dto.setImageUrls(imageUrls);

            return dto;
        }).collect(Collectors.toList());
    }

   
    // 진행 중인 펀딩 목록 조회 (카테고리별)
    public List<FundingsDTO> getInProgressFundingsByCategory(Long categoryId) {
        List<FundingsDTO> fundingsList = fundingsMapper.getInProgressFundingsByCategory(categoryId);
        return mapToFundingsDTO(fundingsList);
    }
    
    // 공통 메서드로 DTO 매핑
    private List<FundingsDTO> mapToFundingsDTO(List<FundingsDTO> fundingsList) {
        return fundingsList.stream().map(funding -> {
            FundingsDTO dto = new FundingsDTO();
            dto.setFundingsId(funding.getFundingsId());
            dto.setTitle(funding.getTitle());
            dto.setContent(funding.getContent());
            dto.setStatusFlag(funding.getStatusFlag());
            dto.setLikeCount(funding.getLikeCount());
            dto.setGoalAmount(funding.getGoalAmount());
            dto.setCategoryId(funding.getCategoryId());
            int currentAmount = fundingsMapper.findFundingJoinsByFundingsId(funding.getFundingsId()).stream()
                .mapToInt(FundingJoin::getFundingAmount).sum();
            dto.setCurrentAmount(currentAmount);
            List<String> imageUrls = funding.getImageUrls();
            dto.setImageUrls(imageUrls);
            return dto;
        }).collect(Collectors.toList());
    }
    
    
    // 진행 중인 펀딩 목록 조회
    public List<FundingsDTO> getInProgressFundings(String sortBy, Long userId) {
        List<FundingsDTO> fundingsList = fundingsMapper.getInProgressFundings(sortBy, userId);
        return fundingsList.stream().map(funding -> {
            FundingsDTO dto = new FundingsDTO();
            dto.setFundingsId(funding.getFundingsId());
            dto.setTitle(funding.getTitle());
            dto.setContent(funding.getContent());
            dto.setStatusFlag(funding.getStatusFlag());
            dto.setLikeCount(funding.getLikeCount());
            dto.setGoalAmount(funding.getGoalAmount());
            dto.setCategoryId(funding.getCategoryId());

            int currentAmount = fundingsMapper.findFundingJoinsByFundingsId(funding.getFundingsId()).stream()
                .mapToInt(FundingJoin::getFundingAmount).sum();
            dto.setCurrentAmount(currentAmount);

            List<String> imageUrls = funding.getImageUrls();
            dto.setImageUrls(imageUrls);

            return dto;
        }).collect(Collectors.toList());
    }
}
