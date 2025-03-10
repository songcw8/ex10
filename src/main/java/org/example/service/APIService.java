package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.example.model.APIParam;
import org.example.model.ModelResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.example.model.ModelPlatform.GROQ;
import static org.example.model.ModelPlatform.TOGETHER;

public class APIService {
    private static final APIService instance = new APIService();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Dotenv dotenv = Dotenv.load();
    private final String groqToken;
    private final String togetherToken;

    public static APIService getInstance() {
        return instance;
    }

    private APIService() {
        groqToken = dotenv.get("GROQ_KEY");
        togetherToken = dotenv.get("TOGETHER_KEY");
    }

    public String callAPI(APIParam apiParam) throws Exception {
        String url;
        String token;
        String instruction;
        switch (apiParam.model().platform) {
            case GROQ -> {
                url = "https://api.groq.com/openai/v1/chat/completions";
                token = groqToken;
                instruction = "간곡하게 말하오니 한글 쓰세요";
            }
            case TOGETHER -> {
                url = "https://api.together.xyz/v1/chat/completions";
                token = togetherToken;
                instruction = "제발 제발 한글 쓰세요";
            }
            default -> throw new Exception("Unsupported platform");
        }

        String body = """
                {
                         "messages": [
                           {
                             "role": "system",
                             "content": "%s"
                           },
                           {
                             "role": "user",
                             "content": "%s"
                           }
                         ],
                         "model": "%s"
                       }
                """.formatted(instruction, apiParam.prompt(), apiParam.model().name);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Authorization", "Bearer %s".formatted(token))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        ModelResponse modelResponse = objectMapper.readValue(responseBody, ModelResponse.class);
        String content = modelResponse.choices().get(0).message().content();
        return """
            {
                "content": "%s"
            }
        """.formatted(content);
    }
}
