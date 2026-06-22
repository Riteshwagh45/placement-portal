package com.placement.placementportal.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private GeminiService geminiService;

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {

        String answer = geminiService.askGemini(request.getPrompt());

        return new ChatResponse(answer);
    }
}