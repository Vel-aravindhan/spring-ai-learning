package com.example.spring_ai_demo.controller;

import com.example.spring_ai_demo.StudentInfo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @PostMapping("/chat")
    public Map<String, String> chat(
            @RequestBody ChatRequest request) {


        String answer = chatClient.prompt()
                .system("you are a java teacher. Explain concepts in simple English. ")
                .user(request.question())
                .call()
                .content();

        return Map.of("answer", answer);
    }

    public record ChatRequest(String question) {
    }

    @PostMapping("/student")
    public StudentInfo generateStudent(){
        return chatClient.prompt()
                .user("""
                        Generate sample student information.
                        Include the student's name, age, and department.
                        """)
                .call()
                .entity(StudentInfo.class);
    }
    @PostMapping("/explain")
    public Map<String, String> explainTopic(@RequestBody ExplainRequest request) {
        String answer = chatClient.prompt()
                .system("you are a java teacher. Explain concepts in simple English.")
                .user("Explain the topic "+ request.topic() + "in simple english with one example")
                .call()
                .content();
        return Map.of("answer", answer);
    }
    public record ExplainRequest(String topic){}
}