package br.com.locadora.services;

import br.com.locadora.models.Locacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    @Autowired
    private TemplateEngine templateEngine;

    public byte[] gerarComprovanteLocacao(Locacao locacao) {
        try {
            // 1. Prepara os dados para o HTML (Igual fazemos no Controller)
            Context context = new Context();
            context.setVariable("locacao", locacao);

            // 2. Transforma o HTML em String processada (com os nomes preenchidos)
            String htmlProcessado = templateEngine.process("comprovante", context);

            // 3. Usa o Flying Saucer para criar o PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();

            // Define o HTML que será convertido
            renderer.setDocumentFromString(htmlProcessado);
            renderer.layout();

            // Gera o PDF
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar PDF: " + e.getMessage());
        }
    }
}