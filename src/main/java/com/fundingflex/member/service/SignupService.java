package com.fundingflex.member.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fundingflex.member.domain.entity.Members;
import com.fundingflex.member.domain.form.MemberResisterForm;
import com.fundingflex.mybatis.mapper.member.MembersMapper;

@Service
public class SignupService {

	private final PasswordEncoder passwordEncoder;
	private final MembersMapper membersMapper;

	@Autowired
	public SignupService(MembersMapper membersMapper, PasswordEncoder passwordEncoder) {
		this.membersMapper = membersMapper;
		this.passwordEncoder = passwordEncoder;
	}

	// 이미지 파일 경로
	private final Path fundingImgPath = Paths.get("src/main/resources/static/images/members");

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

	@Transactional
	public Members signup(MemberResisterForm resisterForm, MultipartFile profileImage) {
	    String encodedPassword = passwordEncoder.encode(resisterForm.getPassword1());

	    String profileUrl = null;
	    if (profileImage != null && !profileImage.isEmpty()) {
	        profileUrl = saveProfileImage(profileImage, resisterForm.getEmail());
	    }

	    Members newMember = Members.builder()
	            .email(resisterForm.getEmail())
	            .nickname(resisterForm.getNickname())
	            .password(encodedPassword)
	            .profileUrl(profileUrl) // 이미지 URL 바로 할당
	            .createdAt(new Date())
	            .createdBy(resisterForm.getNickname())
	            .role("USER")
	            .build();

	    membersMapper.insertMember(newMember);
	    return newMember;
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
	
	private String saveProfileImage(MultipartFile file, String userEmail) {
	    try {
	        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replace(" ", "_");
	        Path targetLocation = this.fundingImgPath.resolve(fileName);
	        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
	        return targetLocation.toString();
	    } catch (IOException ex) {
	        throw new RuntimeException("이미지 파일 저장 실패: " + ex.getMessage(), ex);
	    }
	}
	
	public boolean existsByEmail(String email) {
		return membersMapper.existsByEmail(email);
	}

	public boolean existsByNickname(String nickname) {
		return membersMapper.existsByNickname(nickname);
	}
}
