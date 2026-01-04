package br.com.locadora.controllers;

import br.com.locadora.services.LocadoraAgent;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class AgentController {

    private final LocadoraAgent agent;

    public AgentController(LocadoraAgent agent) {
        this.agent = agent;
    }

    @PostMapping
    public String conversar(@RequestBody String mensagem) {
        // Recebe o texto do usuário e devolve a resposta da IA
        return agent.chat(mensagem).content();
    }
}