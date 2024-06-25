package com.fundingflex.qa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/qa")
@RequiredArgsConstructor
public class QaController {

	private final QaService qaService;
	
    @GetMapping("")
    public String qaHome() {
        return "redirect:qa/fqa";
    }

//		FQA쪽으로 이동
//    @GetMapping("/frequent")
//    public String frequentQuestions(Model model) {
//        // 자주 문의한 내역 데이터를 모델에 추가
//        return "qa/frequent";
//    }

//    @GetMapping("/myquestions")
//    public String myQuestions(Model model) {
//        // 내가 문의한 내역 데이터를 모델에 추가
//        return "qa/myquestions";
//    }
    
//    @GetMapping("/qa/myquestions")
//    public String getUserQuestions(@RequestParam("userId") Long userId, 
//                                   @RequestParam(value = "page", defaultValue = "0") int page, 
//                                   @RequestParam(value = "size", defaultValue = "10") int size, 
//                                   Model model) {
//        Page<QaDTO> questionsPage = qaService.getUserQuestions(userId, PageRequest.of(page, size));
//        model.addAttribute("questionsPage", questionsPage);
//        model.addAttribute("currentPage", page);
//        return "qa/myquestions";
//    }
    
    @GetMapping("/myquestions")
//    public String getUserQuestions(@RequestParam("userId") Long userId, 
//                                   @RequestParam(value = "page", defaultValue = "0") int page, 
//                                   @RequestParam(value = "size", defaultValue = "10") int size, 
//                                   Model model) {
    public String getUserQuestions(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                   Model model) {
        Long userId = 10L; // 유저 ID를 10으로 고정
        Pageable pageable = PageRequest.of(page, size);
        Page<QaDTO> questionsPage = qaService.findByMembersUserIdOrderByCounsulIdDesc(userId, pageable);

        model.addAttribute("questionsPage", questionsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", questionsPage.getTotalPages());

        return "qa/myquestions";
    }

    @GetMapping("/detail/{id}")
    public String getQuestionDetail(@PathVariable("id") Long id, Model model) {
        QaDTO question = qaService.getQuestionDetail(id);
        model.addAttribute("question", question);
        return "qa/questions-detail"; // 상세 페이지로 연결
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
        this.qaService.qaCreate(qaForm.getTitle(), qaForm.getContent(),images);
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
    		@RequestParam(value="content") String content) {

        Long adminId = 0L;
    	this.qaService.replyCreate(id, adminId, content);
    	
        QaDTO question = qaService.getQuestionDetail(id);
        model.addAttribute("question", question);
        return "qa/questions-detail"; // 상세 페이지로 연결
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
