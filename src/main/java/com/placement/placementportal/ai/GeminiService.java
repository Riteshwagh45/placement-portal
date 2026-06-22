package com.placement.placementportal.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String model;

    private final WebClient webClient = WebClient.builder().build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String askGemini(String prompt) {

        String body = """
                {
                  "contents": [
                    {
                      "parts": [
                        {
                          "text": "%s"
                        }
                      ]
                    }
                  ]
                }
                """.formatted(prompt.replace("\"", "\\\""));

        String url =
                "https://generativelanguage.googleapis.com/v1beta/models/"
                        + model
                        + ":generateContent?key="
                        + apiKey;

        try {

            String response = webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);

            JsonNode candidates = root.path("candidates");

            if (candidates.isArray() && candidates.size() > 0) {

                JsonNode textNode = candidates.get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text");

                return textNode.asText();
            }

            if (root.has("error")) {
                return "Gemini Error : " +
                        root.path("error")
                                .path("message")
                                .asText();
            }

            return "No response received from Gemini.";

        } catch (Exception e) {

            e.printStackTrace();

            return "Unable to connect to Gemini API.\n\n" + e.getMessage();
        }
    }
}