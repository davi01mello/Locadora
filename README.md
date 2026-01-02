# 🎬 Locadora System(ainda vou adicionar mais...)

> **Sistema inteligente de gestão de locadora desenvolvido com Java 21, Spring Boot e Google Gemini AI.**

![Status do Projeto](https://img.shields.io/badge/Status-Finalizado-green) ![Java Version](https://img.shields.io/badge/Java-21-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-brightgreen) ![License](https://img.shields.io/badge/License-MIT-blue)

![Dashboard Preview](prints/dashboard-preview.jpg)


---

## 💡 Sobre o Projeto

O **Locadora System** é uma aplicação Fullstack moderna projetada para automatizar e gerenciar o dia a dia de uma locadora de filmes.

Diferente de sistemas comuns, este projeto utiliza **Inteligência Artificial em duas pontas**:
1.  **Na Entrada de Dados:** Busca automática de metadados e capas de filmes via API externa (OMDb), eliminando cadastro manual.
2.  **Na Gestão Estratégica:** Um consultor virtual powered by **Google Gemini AI** que analisa os gráficos de vendas e estoque para fornecer insights de negócio em tempo real para o dono da locadora.

---

## ✨ Funcionalidades Principais

### 🧠 Inteligência Artificial (AI-Powered)
-   **Consultor Virtual (Gemini 1.5):** Analisa dados do banco (faturamento, categorias mais alugadas, inadimplência) e gera dicas de marketing e reposição de estoque com um clique.
-   **Auto-Complete de Filmes:** Ao digitar o nome do filme (ex: "Matrix"), o sistema busca automaticamente a capa, sinopse, diretor, ano e classificação indicativa traduzida.

### 🏢 Gestão Completa
-   **Dashboard Analítico:** Gráficos interativos (Chart.js) mostrando performance por categoria, top clientes e status de devoluções.
-   **Controle de Estoque:** Validação automática que impede aluguel de filmes esgotados.
-   **Fluxo de Locação:** Cálculo automático de multas por atraso (R$ 5,00/dia) e baixa/reposição de estoque em tempo real.

### 📄 Documentação & Segurança
-   **Geração de Comprovantes:** Emissão automática de recibos em **PDF** para o cliente.
-   **Segurança Robusta:** Login e autenticação via Spring Security com proteção de rotas.
-   **Validações:** Regras de negócio fortes (CPF válido, datas coerentes).

---

## 🛠️ Tech Stack (Tecnologias)

O projeto foi construído seguindo as melhores práticas de arquitetura monolítica moderna.

* **Linguagem:** Java 21 (LTS)
* **Framework:** Spring Boot 3.2 (Web, Data JPA, Security, Validation)
* **Banco de Dados:** MySQL 8
* **IA & Integrações:**
    * **Google Gemini API:** Para análise de dados e insights.
    * **OMDb API:** Para catálogo de filmes.
    * **Spring RestClient:** Para consumo de APIs externas.
* **Front-end:** Thymeleaf, Bootstrap 5, SweetAlert2, Chart.js.
* **Relatórios:** Flying Saucer (Geração de PDF).
* **Build Tool:** Maven.

---

## 📸 Screenshots

| Tela de Login | Cadastro com IA |
|:---:|:---:|
| ![Login](prints/login.jpg) | ![Cadastro](prints/cadastro-filme.jpg) |

| Dashboard com Insights | Comprovante PDF |
|:---:|:---:|
| ![Insights](prints/insights-ia.jpg) | ![PDF](prints/pdf-comprovante.jpg) |



---

## 🚀 Como Executar Localmente

### Pré-requisitos
* Java 21 instalado (`java -version`).
* MySQL rodando na porta 3306.
* Chaves de API (Google Gemini e OMDb).

### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/davi01mello/locadora-system.git](https://github.com/davi01mello/locadora-system.git)
    cd locadora-system
    ```

2.  **Configure o Banco e as APIs:**
    Edite o arquivo `src/main/resources/application.properties`:
    ```properties
    # Banco de Dados
    spring.datasource.url=jdbc:mysql://localhost:3306/locadora_db?createDatabaseIfNotExist=true
    spring.datasource.username=root
    spring.datasource.password=...

    # Chaves de API (Obtenha gratuitamente nos sites oficiais)
    omdb.apikey=XXXXXXXXXX
    gemini.api-key=XXXXXXXX
    ```

3.  **Execute o projeto:**
    ```bash
    ./mvnw spring-boot:run
    ```

4.  **Acesse:**
    * Abra: `http://localhost:8080/login`
    * **Usuário:** `admin`
    * **Senha:** `123`

---

## 🤝 Autor

Desenvolvido com dedicação por **Davi Mello**.


[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/davi01mello)

---

**⭐ Se esse projeto te ajudou, dê uma estrela no repositório!**
