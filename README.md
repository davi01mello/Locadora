# 🎬 Locadora System

> Sistema de gestão de locadora de filmes desenvolvido com Java, Spring Boot e Inteligência Artificial.

![Dashboard Preview](prints/dashboard-preview.jpg)
*(Ainda vou adiiconar fotos)*

## 💡 Sobre o Projeto

Este projeto é uma aplicação web completa para gerenciamento de acervos de filmes. O objetivo foi criar uma solução robusta que resolva problemas reais, como o cadastro manual de dados, utilizando APIs externas para automação.

Destaque para o uso de IA (OMDb API) que busca automaticamente capas, sinopses e dados técnicos dos filmes apenas pelo título.

## ✨ Funcionalidades

- **Dashboard Interativo:** Visão geral do negócio com métricas em tempo real.
- **Cadastro Inteligente (IA):** Integração com API externa para preenchimento automático de dados de filmes.
- **Gestão de Locações:** Controle de datas, cálculo automático de multas e status de devolução.
- **Segurança:** Sistema de Login/Logout com Spring Security.
- **Banco de Dados Real:** Persistência de dados utilizando MySQL.

## 🛠️ Tecnologias Utilizadas

- **Back-end:** Java 17, Spring Boot 3.
- **Banco de Dados:** MySQL, H2 (para testes), Spring Data JPA.
- **Front-end:** Thymeleaf, HTML5, Bootstrap 5.
- **Segurança:** Spring Security.
- **Integração:** OMDb API (REST Client).

## 🚀 Como Executar

### Pré-requisitos
- Java 17 instalado.
- MySQL rodando na porta 3306 (ou configure no `application.properties`).

### Passos
1. Clone o repositório:
   ```bash
   git clone [https://github.com/](https://github.com/)[SEU-USUARIO]/locadora-filmes.git
Configure o banco de dados no arquivo application.properties.

Execute a aplicação via IntelliJ ou Maven:

Bash

mvn spring-boot:run
Acesse no navegador: http://localhost:8080

👤 Autor
Desenvolvido por Davi Mello.
