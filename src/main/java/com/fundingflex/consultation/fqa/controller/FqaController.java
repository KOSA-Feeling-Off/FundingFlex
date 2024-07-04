package com.fundingflex.consultation.fqa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.fundingflex.consultation.fqa.domain.dto.FqaDTO;
import com.fundingflex.consultation.fqa.domain.form.FqaForm;
import com.fundingflex.consultation.fqa.service.FqaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qa/fqa")
public class FqaController {

    private final FqaService fqaService;

    @GetMapping("")
    public String fqaList(Model model) {
        List<FqaDTO> fqas = fqaService.getAllFqas();
        model.addAttribute("fqas", fqas);
        return "qa/fqa/frequent";
    }

    @GetMapping("/add")
    public String addFqaForm() {
        return "qa/fqa/add-frequent";
    }
    
    @PostMapping("/save")
    public String saveFqa(@Valid FqaForm fqaForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "qa/add";
        }
        //임시
		Long testAdminId = 0L;
        this.fqaService.create(testAdminId, fqaForm.getTitle(), fqaForm.getContent(), fqaForm.getReply());
        return "redirect:/qa/fqa";
    }

    @GetMapping("/detail/{id}")
    public String fqaDetail(@PathVariable("id") Long fqaId, Model model) {
        FqaDTO fqa = fqaService.getFqaById(fqaId);
        model.addAttribute("fqa", fqa);
        return "/qa/fqa/frequent-detail";
    }
    
    @GetMapping("/search")
    public String searchFqas(@RequestParam("searchType") String searchType,
                             @RequestParam("keyword") String keyword, Model model) {
        List<FqaDTO> fqas;
        switch (searchType) {
            case "title":
                fqas = fqaService.findByTitle(keyword);
                break;
            case "content":
                fqas = fqaService.findByContent(keyword);
                break;
            case "titleContent":
                fqas = fqaService.findByTitleOrContent(keyword);
                break;
            default:
                fqas = fqaService.findAll();
                break;
        }
        model.addAttribute("fqas", fqas);
        return "qa/fqa/frequent";
    }
}