package br.com.locadora.services;

import br.com.locadora.dtos.FilmeDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// Classe auxiliar para ler o JSON da API
class OmdbResponse {
    public String Title;
    public String Director;
    public String Year;
    public String Plot;   // Sinopse
    public String Poster; // Imagem
    public String Rated;  // Classificação
}

@Service
public class OmdbService {


    private final String API_KEY = "c914095f";
    // ------------------------------

    public FilmeDto buscarFilmePeloTitulo(String titulo) {
        try {
            // 1. Prepara a URL (troca espaços por +)
            String url = "http://www.omdbapi.com/?t=" + titulo.replace(" ", "+") + "&apikey=" + API_KEY;

            // 2. Faz a requisição na internet
            RestTemplate restTemplate = new RestTemplate();
            OmdbResponse resposta = restTemplate.getForObject(url, OmdbResponse.class);

            // 3. Se não achou nada, retorna null
            if (resposta == null || resposta.Title == null) {
                return null;
            }

            // 4. Converte a resposta da API para o DTO
            FilmeDto dto = new FilmeDto();
            dto.setTitulo(resposta.Title);
            dto.setDiretor(resposta.Director);
            dto.setSinopse(resposta.Plot);
            dto.setImagemUrl(resposta.Poster);

            // Tratamento simples para o Ano (A API manda ex: "1999", removemos caracteres estranhos)
            try {
                String anoLimpo = resposta.Year.replaceAll("[^0-9]", "");
                dto.setAnoLancamento(Integer.parseInt(anoLimpo));
            } catch (Exception e) {
                dto.setAnoLancamento(2000); // Valor padrão se der erro no ano
            }

            // Traduz a classificação (A API vem em inglês)
            dto.setClassificacao(traduzirClassificacao(resposta.Rated));

            return dto;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Pequeno tradutor para ficar bonito no Brasil
    private String traduzirClassificacao(String rated) {
        if (rated == null) return "Livre";
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