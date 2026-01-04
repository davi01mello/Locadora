package br.com.locadora.configs;

import br.com.locadora.services.LocadoraAgent;
import br.com.locadora.services.LocadoraTools;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String modelName;

    @Bean
    public LocadoraAgent locadoraAgent(LocadoraTools tools) {
        // Configura o modelo usando as propriedades do application.properties
        GoogleAiGeminiChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(0.3) // Foco em precisão
                .build();

        // Cria o Serviço com Memória de 10 mensagens
        return AiServices.builder(LocadoraAgent.class)
                .chatModel(model)
                .tools(tools)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }
}