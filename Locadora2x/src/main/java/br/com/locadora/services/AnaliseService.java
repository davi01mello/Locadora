package br.com.locadora.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class AnaliseService {

    @Value("${gemini.api-key}")
    private String apiKey;

    private final RestClient restClient;

    public AnaliseService(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public String analisarDados(String dadosDoBanco) {
        // Modelo oficial atualizado (gemini-1.5-flash)
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

        // Debug no Console (para checar se a chave veio)
        System.out.println(">>> Tentando chamar API Gemini...");
        System.out.println(">>> URL: " + url.substring(0, 50) + "...");

        String prompt = """
            Você é um consultor de negócios experiente de uma locadora.
            Analise os dados abaixo e gere 3 insights curtos e estratégicos.
            Use HTML (<b>, <br>) para formatar.
            
            DADOS:
            """ + dadosDoBanco;

        GeminiRequest request = new GeminiRequest(
                List.of(new Content(List.of(new Part(prompt))))
        );

        try {
            GeminiResponse response = restClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON) // Força o envio como JSON
                    .body(request)
                    .retrieve()
                    .body(GeminiResponse.class);

            if (response != null && !response.candidates().isEmpty()) {
                String respostaIa = response.candidates().get(0).content().parts().get(0).text();
                System.out.println(">>> Sucesso! Resposta recebida.");
                return respostaIa;
            }
            return "A IA recebeu os dados mas não gerou resposta.";

        } catch (Exception e) {
            e.printStackTrace(); // Mostra o erro detalhado no terminal
            return "Erro técnico na IA: " + e.getMessage();
        }
    }

    // --- DTOs ---
    public record GeminiRequest(List<Content> contents) {}
    public record Content(List<Part> parts) {}
    public record Part(String text) {}
    public record GeminiResponse(List<Candidate> candidates) {}
    public record Candidate(Content content) {}
}