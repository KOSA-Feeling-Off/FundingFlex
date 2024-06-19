package com.fundingflex.qa;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/qa")
@RequiredArgsConstructor
public class QaController {

	private final QaService qaService;
	
    @GetMapping("")
    public String qaHome() {
        return "qa/main";
    }

    @GetMapping("/frequent")
    public String frequentQuestions(Model model) {
        // 자주 문의한 내역 데이터를 모델에 추가
        return "qa/frequent";
    }

    @GetMapping("/myquestions")
    public String myQuestions(Model model) {
        // 내가 문의한 내역 데이터를 모델에 추가
        return "qa/myquestions";
    }

    @GetMapping("/ask")
    public String askQuestion() {
        return "qa/ask";
    }

    @PostMapping("/ask")
    public String submitQuestion(@Valid QaForm qaForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "qa/ask";
        }
        this.qaService.create(qaForm.getSubject(), qaForm.getContent());
        return "redirect:/qa";
    }
	
    @GetMapping("/chat")
    public String liveChat() {
        return "qa/chat";
    }
    
    @GetMapping("/chat-window")
    public String liveChatWindow() {
        return "qa/chat-window";
    }
    
    @GetMapping("/chat-reply-window")
    public String liveChatReplyWindow() {
        return "qa/chat-reply-window";
    }
}
