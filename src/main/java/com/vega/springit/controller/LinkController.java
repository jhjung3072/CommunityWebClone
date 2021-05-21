package com.vega.springit.controller;

import com.vega.springit.domain.Link;
import com.vega.springit.repository.LinkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;


@Controller
public class LinkController {

    private static final Logger logger = LoggerFactory.getLogger(LinkController.class);

    private LinkRepository linkRepository;

    public LinkController(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @GetMapping("/")
    public String list(Model model){
        model.addAttribute("links", linkRepository.findAll());
        return "link/list";
    }

    @GetMapping("/link/{id}")
    public String read(@PathVariable Long id, Model model) {
        Optional<Link> link = linkRepository.findById(id);
        if (link.isPresent()){
            model.addAttribute("link",link.get());
            model.addAttribute("success",model.containsAttribute("success"));
            return "link/view";
        }else{
            return "redirect:/";
        }
    }

    @GetMapping("/link/submit")
    public String newLinkForm(Model model) {
        model.addAttribute("link",new Link());
        return "link/submit";
    }

    @PostMapping("/link/submit")
    public String createLink(@Valid Link link, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            logger.info("링크 저장중에 에러가 발생했습니다.");
            model.addAttribute("link", link);
            return "link/submit";
        }else {
            linkRepository.save(link);
            logger.info("링크 저장에 성공했습니다.");
            redirectAttributes.addAttribute("id", link.getId()).addFlashAttribute("success", true);
            return "redirect:/link/{id}";
        }

    }

}
