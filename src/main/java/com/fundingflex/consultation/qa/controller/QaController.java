package com.fundingflex.consultation.qa.controller;

import com.fundingflex.consultation.qa.domain.dto.QaDTO;
import com.fundingflex.consultation.qa.domain.form.QaForm;
import com.fundingflex.consultation.qa.service.QaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                   Model model) {
        Long userId = 10L; 
        int totalElements = qaService.countUserQuestions(userId);
        int totalPages = (totalElements + size - 1) / size;

        List<QaDTO> questions = qaService.getUserQuestions(userId, page, size);

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
                                    @RequestParam("images") MultipartFile[] images) {
        if (bindingResult.hasErrors()) {
            return "qa/ask";
        }
        qaService.qaCreate(qaForm.getTitle(), qaForm.getContent(), images);
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

    @GetMapping("/chat")
    public String liveChat() {
        return "qa/chat/chat";
    }

    @GetMapping("/chat-window")
    public String liveChatWindow() {
        return "qa/chat/chat-window";
    }

    @GetMapping("/chat-reply-window")
    public String liveChatReplyWindow() {
        return "qa/chat/chat-reply-window";
    }
}
