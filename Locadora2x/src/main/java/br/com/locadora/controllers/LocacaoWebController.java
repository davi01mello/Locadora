package br.com.locadora.controllers;

import br.com.locadora.dtos.LocacaoDto;
import br.com.locadora.models.Locacao; // Importante para o objeto Locacao
import br.com.locadora.services.ClienteService;
import br.com.locadora.services.FilmeService;
import br.com.locadora.services.LocacaoService;
import br.com.locadora.services.PdfService; // Import do novo serviço
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders; // Para configurar o download
import org.springframework.http.MediaType;   // Para dizer que é PDF
import org.springframework.http.ResponseEntity; // Para retornar o arquivo
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/locacoes")
public class LocacaoWebController {

    @Autowired
    private LocacaoService service;

    @Autowired
    private FilmeService filmeService;

    @Autowired
    private ClienteService clienteService;

    // --- INJEÇÃO DO SERVIÇO DE PDF ---
    @Autowired
    private PdfService pdfService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("locacoes", service.buscarTodos());
        return "locacoes";
    }

    @GetMapping("/novo")
    public String formularioNovo(Model model) {
        model.addAttribute("locacaoDto", new LocacaoDto());
        model.addAttribute("clientes", clienteService.buscarTodos());
        model.addAttribute("filmes", filmeService.buscarTodos());
        return "formulario-locacao";
    }

    @PostMapping("/novo")
    public String salvar(@ModelAttribute LocacaoDto dto) {
        service.cadastrar(dto);
        return "redirect:/locacoes";
    }

    // --- RECEBE O CLIQUE DO BOTÃO DEVOLVER ---
    @PostMapping("/devolver/{id}")
    public String devolver(@PathVariable Long id) {
        service.devolver(id);
        return "redirect:/locacoes";
    }

    //  BAIXAR COMPROVANTE PDF ---
    @GetMapping("/comprovante/{id}")
    public ResponseEntity<byte[]> baixarComprovante(@PathVariable Long id) {
        // 1. Busca a locação pelo ID
        // Se der erro vermelho aqui, adicione o método buscarPorId no LocacaoService (veja abaixo)
        Locacao locacao = service.buscarPorId(id);

        // 2. Gera o PDF usando o serviço novo
        byte[] pdfBytes = pdfService.gerarComprovanteLocacao(locacao);

        // 3. Retorna o arquivo configurado para download
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comprovante_locacao_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}