package com.fundingflex.consultation.qa.controller;

import com.fundingflex.consultation.qa.domain.dto.QaDTO;
import com.fundingflex.consultation.qa.domain.form.QaForm;
import com.fundingflex.consultation.qa.service.QaService;
import com.fundingflex.member.domain.dto.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/qa")
@RequiredArgsConstructor
public class QaController {

    private final QaService qaService;

    @GetMapping("")
    public String qaHome() {
        return "redirect:qa/fqa";
    }

    @GetMapping("/myquestions")
    public String getUserQuestions(@RequestParam(value = "page", defaultValue = "0") int page,
                                   Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId(); 
        System.out.print("강훈" + userId);
        int size = 10;
    	int totalElements;
    	List<QaDTO> questions;
        
        String Role = userDetails.getUserRole();
        if(Role.equals("ADMIN")) {
        	questions = qaService.getUQuestions(page, size);
        	totalElements = questions.size();
        }
        else {        	
        	totalElements= qaService.countUserQuestions(userId);
        	questions = qaService.getUserQuestions(userId, page, size);
        }
        int totalPages = (totalElements + size - 1) / size;

        model.addAttribute("questionsPage", questions);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements); // 전체 요소 수

        return "qa/myquestions";
    }

    @GetMapping("/detail/{id}")
    public String getQuestionDetail(@PathVariable("id") Long id, Model model) {
        QaDTO question = qaService.getQuestionDetail(id);
        model.addAttribute("question", question);
        return "qa/questions-detail";
    }

    @GetMapping("/ask")
    public String askQuestion() {
        return "qa/ask";
    }

    @PostMapping("/ask")
    public String askSubmitQuestion(@Valid QaForm qaForm, BindingResult bindingResult,
                                    @RequestParam("images") MultipartFile[] images, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "qa/ask";
        }
        qaService.qaCreate(userDetails.getUserId(), qaForm.getTitle(), qaForm.getContent(), images);
        return "redirect:/qa/myquestions";
    }

    @GetMapping("/reply/{id}")
    public String replyQuestion(@PathVariable("id") Long id, Model model) {
        QaDTO question = qaService.getQuestionDetail(id);
        model.addAttribute("question", question);
        return "qa/reply";
    }

    @PostMapping("/reply/{id}")
    public String replySubmitQuestion(Model model,
                                      @PathVariable("id") Long id,
                                      @RequestParam(value = "content") String content) {
        Long adminId = 0L;
        qaService.replyCreate(id, adminId, content);

        QaDTO question = qaService.getQuestionDetail(id);
        model.addAttribute("question", question);
        return "qa/questions-detail";
    }
}
