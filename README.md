# 🎬 Locadora System

> **Sistema inteligente de gestão de locadora desenvolvido com Java 25, Spring Boot e Spring AI.**

![Status do Projeto](https://img.shields.io/badge/Status-Em_Desenvolvimento-yellow) ![Java Version](https://img.shields.io/badge/Java-25-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-brightgreen) ![License](https://img.shields.io/badge/License-MIT-blue)

![Dashboard Preview](prints/dashboard-preview.png)

---

## 💡 Sobre o Projeto

O **Locadora System** é uma aplicação Fullstack moderna projetada para automatizar e gerenciar o dia a dia de uma locadora de filmes.

Diferente de sistemas comuns, este projeto está integrando **Inteligência Artificial** para modernizar a gestão:
1.  **Na Entrada de Dados:** Busca automática de metadados e capas de filmes via API externa (OMDb).
2.  **Na Gestão Estratégica:** Implementação de um consultor virtual utilizando **Spring AI + Google Gemini** para fornecer insights de negócio.

---

## ✨ Funcionalidades Principais

### 🧠 Inteligência Artificial (AI-Powered)
-   **Consultor Virtual (Em construção):** Integração com o modelo Gemini para analisar dados do banco e gerar dicas de marketing.
-   **Auto-Complete de Filmes:** Ao digitar o nome do filme, o sistema busca automaticamente capa, sinopse, diretor e classificação via OMDb API.

### 🏢 Gestão Completa
-   **Dashboard Analítico:** Gráficos interativos (Chart.js) mostrando performance por categoria e status de devoluções.
-   **Controle de Estoque:** Validação automática que impede aluguel de filmes esgotados.
-   **Fluxo de Locação:** Cálculo automático de multas por atraso e baixa/reposição de estoque em tempo real.

### 📄 Documentação & Segurança
-   **Geração de Comprovantes:** Emissão automática de recibos em **PDF** para o cliente.
-   **Segurança Robusta:** Login e autenticação via Spring Security.
-   **Validações:** Regras de negócio para integridade dos dados (CPF, datas, etc).

---

## 🛠️ Tech Stack (Tecnologias)

O projeto segue as melhores práticas de arquitetura monolítica moderna.

* **Linguagem:** Java 25 (LTS)
* **Framework:** Spring Boot 3.2 (Web, Data JPA, Security, Validation)
* **Banco de Dados:** MySQL 8
* **IA & Integrações:**
    * **Spring AI:** Framework para integração simplificada com LLMs.
    * **Google Gemini API:** Modelo de IA generativa.
    * **OMDb API:** Para catálogo de filmes.
    * **Spring RestClient:** Para consumo de APIs externas.
* **Front-end:** Thymeleaf, Bootstrap 5, SweetAlert2, Chart.js.
* **Relatórios:** Flying Saucer (Geração de PDF).
* **Build Tool:** Maven.

---

## 📸 Screenshots

| Tela de Login | Cadastro com IA |
|:---:|:---:|
| ![Login](prints/login.png) | ![Cadastro](prints/cadastro-filme.png) |

| Dashboard com Insights | Comprovante PDF |
|:---:|:---:|
| ![Insights](prints/insights-ia.png) | ![PDF](prints/pdf-comprovante.png) |

---

## 🚀 Como Executar Localmente

### Pré-requisitos
* Java 25 instalado (`java -version`).
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
    spring.datasource.password=sua_senha

    # Chaves de API
    omdb.apikey=SEU_TOKEN_OMDB
    spring.ai.gemini.api-key=SEU_TOKEN_GEMINI
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
