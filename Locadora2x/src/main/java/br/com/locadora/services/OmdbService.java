package br.com.locadora.services;

import br.com.locadora.dtos.FilmeDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class OmdbService {

    private final String API_KEY = "c914095f"; // Sua chave pública

    public FilmeDto buscarFilme(String titulo) {
        try {
            // 1. Prepara a URL
            String tituloFormatado = titulo.replace(" ", "+");
            String endereco = "https://www.omdbapi.com/?t=" + tituloFormatado + "&apikey=" + API_KEY;

            // 2. Faz a requisição (Modo Java Moderno)
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(endereco)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 3. Lê o JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.body());

            // Se a API retornar erro
            if (json.has("Error")) {
                System.out.println("Erro na API OMDb: " + json.get("Error").asText());
                return null;
            }

            // 4. Preenche o DTO
            FilmeDto dto = new FilmeDto();
            dto.setTitulo(json.path("Title").asText());
            dto.setDiretor(json.path("Director").asText());
            dto.setSinopse(json.path("Plot").asText());

            // Tratamento do Ano
            try {
                String ano = json.path("Year").asText().replaceAll("[^0-9]", "");
                dto.setAnoLancamento(ano.isEmpty() ? 2000 : Integer.parseInt(ano.substring(0, 4)));
            } catch (Exception e) {
                dto.setAnoLancamento(2000);
            }

            // Tratamento da Imagem
            String poster = json.path("Poster").asText();
            dto.setImagemUrl(poster.equals("N/A") ? "" : poster);

            // AQUI ESTÁ A MUDANÇA: Usando o tradutor
            String classificacaoOriginal = json.path("Rated").asText();
            dto.setClassificacao(traduzirClassificacao(classificacaoOriginal));

            dto.setEstoque(3); // Padrão

            return dto;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método auxiliar para traduzir (Copiado do seu código antigo)
    private String traduzirClassificacao(String rated) {
        if (rated == null || rated.equals("N/A")) return "Livre";
        switch (rated) {
            case "G": return "Livre";
            case "PG": return "10 anos";
            case "PG-13": return "12 anos";
            case "R": return "16 anos";
            case "NC-17": return "18 anos";
            default: return rated;
        }
    }
}