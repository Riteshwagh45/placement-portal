package com.placement.placementportal.ai;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AIPageController {

    @GetMapping("/ai")
    public String aiPage() {
        return "ai-chat";
    }
}