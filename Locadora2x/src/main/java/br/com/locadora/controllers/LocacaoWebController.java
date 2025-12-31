package br.com.locadora.controllers;

import br.com.locadora.dtos.LocacaoDto;
import br.com.locadora.models.Filme;
import br.com.locadora.models.Locacao;
import br.com.locadora.repositories.CategoriaRepository; // <--- NOVO IMPORT
import br.com.locadora.services.ClienteService;
import br.com.locadora.services.FilmeService;
import br.com.locadora.services.LocacaoService;
import br.com.locadora.services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/locacoes")
public class LocacaoWebController {

    @Autowired
    private LocacaoService service;

    @Autowired
    private FilmeService filmeService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PdfService pdfService;

    // --- INJEÇÃO DO REPOSITÓRIO DE CATEGORIAS ---
    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("locacoes", service.buscarTodos());
        return "locacoes";
    }

    @GetMapping("/novo")
    public String formularioNovo(Model model) {
        model.addAttribute("locacaoDto", new LocacaoDto());
        model.addAttribute("clientes", clienteService.buscarTodos());

        // --- MANDANDO FILMES E CATEGORIAS PARA O HTML ---
        model.addAttribute("filmes", filmeService.buscarTodos());
        model.addAttribute("categorias", categoriaRepository.findAll()); // <--- NOVO

        return "formulario-locacao";
    }

    // --- SALVAR COM LÓGICA DE ESTOQUE 🧠 ---
    @PostMapping("/novo")
    public String salvar(@Valid @ModelAttribute LocacaoDto dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.buscarTodos());
            model.addAttribute("filmes", filmeService.buscarTodos());
            model.addAttribute("categorias", categoriaRepository.findAll()); // <--- NOVO (Para não sumir se der erro)
            return "formulario-locacao";
        }

        // 1. Busca o filme para checar o estoque
        Filme filme = filmeService.buscarPorId(dto.getFilmeId());

        // 2. Se o estoque for 0 ou menor, BLOQUEIA!
        if (filme.getEstoque() <= 0) {
            result.rejectValue("filmeId", "error.filme", "Este filme está esgotado! 🚫");
            model.addAttribute("clientes", clienteService.buscarTodos());
            model.addAttribute("filmes", filmeService.buscarTodos());
            model.addAttribute("categorias", categoriaRepository.findAll()); // <--- NOVO
            return "formulario-locacao";
        }

        // 3. Se tem estoque, diminui 1
        filme.setEstoque(filme.getEstoque() - 1);
        filmeService.salvar(filme); // Atualiza o filme no banco

        // 4. Salva a locação
        service.cadastrar(dto);
        return "redirect:/locacoes";
    }

    // --- DEVOLVER COM REPOSIÇÃO DE ESTOQUE 🔄 ---
    @PostMapping("/devolver/{id}")
    public String devolver(@PathVariable Long id) {
        // 1. Busca a locação para saber qual filme é
        Locacao locacao = service.buscarPorId(id);

        // Só repõe estoque se ainda não foi devolvido
        if (locacao.getDataDevolucaoReal() == null) {
            Filme filme = locacao.getFilme();
            filme.setEstoque(filme.getEstoque() + 1); // Aumenta 1
            filmeService.salvar(filme);
        }

        // 2. Realiza a devolução normal
        service.devolver(id);
        return "redirect:/locacoes";
    }

    @GetMapping("/comprovante/{id}")
    public ResponseEntity<byte[]> baixarComprovante(@PathVariable Long id) {
        Locacao locacao = service.buscarPorId(id);
        byte[] pdfBytes = pdfService.gerarComprovanteLocacao(locacao);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comprovante_locacao_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}