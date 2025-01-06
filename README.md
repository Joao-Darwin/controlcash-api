<div align="center">
  <img width="200" height="200" src="https://img.icons8.com/plasticine/200/cash--v2.png" alt="star-wars"/>
  <h1 align="center" id="titulo">Control Cash API </h1> 
</div>

<div align="center" id="badges">

  [![GitHub License](https://img.shields.io/github/license/Joao-Darwin/controlcash-api?style=for-the-badge)](https://github.com/Joao-Darwin/parking-api/blob/main/LICENSE)
  ![Badge em Desenvolvimento](http://img.shields.io/static/v1?label=STATUS&message=ONGOING&color=GREEN&style=for-the-badge)
  [![GitHub closed issues](https://img.shields.io/github/issues-closed-raw/Joao-Darwin/controlcash-api?style=for-the-badge&color=purple)](https://github.com/Joao-Darwin/parking-api/issues?q=is%3Aissue+is%3Aclosed)
</div>

# Índice 
* [Badges](#badges)
* [Índice](#índice)
* [Sobre o Projeto](#sobreProjeto)
* [Técnologias Usadas](#techs)
* [Executar o Projeto](#execute)
* [Autor](#author)

<h1 id="sobreProjeto">Sobre o Projeto</h1>

Controll Cash API é a solução backend do meu projeto de gerenciamento financeiro, que possui uma interface tanto em aplicativo quanto em site.

Este projeto foi desenvolvido seguindo todas as boas práticas para criação de uma API RESTful, atingindo um alto nível de maturidade e eficiência.

Principais funcionalidades implementadas:

- Content Negotiation: Suporte a diferentes formatos de resposta.
- CORS (Cross-Origin Resource Sharing): Controle de acessos entre domínios.
- Paginação: Gerenciamento eficiente de endpoints com grandes volumes de dados.
- Autenticação e Autorização: Garantindo segurança no acesso às informações.
- Documentação Dinâmica com Swagger: Facilita o consumo e entendimento da API.
- Testes Unitários e de Integração: Garantia de qualidade e confiabilidade do código.
- CRUD de todas a entidades: Permite realizar operações com todas as entidades.
- Docker e docker-compose: Permite executar mais facilmente em qualquer ambiente.

Esta API foi projetada para ser escalável, segura e fácil de integrar, atendendo tanto aplicações web quanto mobile.

## Camadas lógicas
![Modelo Conceitual](https://github.com/Joao-Darwin/repoImgs/blob/main/Imgs%20-%20Web%20Service%20SpringBoot/camadasLogicas.png)

<h1 id="techs">Técnologias Usadas</h1>

### API
  
  ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
  
  ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
  
  ![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
  
  ![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
  
### Database
  
  ![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)

<h1 id="execute">🚀 Executar o Projeto</h1>
Pré-requisitos: Java 17 e Maven

### Clonar repositório
```bash
git clone https://github.com/Joao-Darwin/controlcash-api ControlCashAPI
```

### Entrar na pasta do projeto
```bash
cd ControlCashAPI
```
### Instalar o Maven
```bash
sudo apt-get install maven
```
### Instalar depedências do projeto
```bash
sudo mvn clean install
```
### Executar o projeto
```bash
mvn spring-boot:run
```
<h2>Docker</h2>

### Clonar repositório
```bash
git clone https://github.com/Joao-Darwin/controlcash-api ControlCashAPI
```
### Entrar na pasta do projeto
```bash
cd ControlCashAPI
```
### Clone o arquivo com as variáveis de ambiente
```bash
cp .env.example .env
```
### Configure as variáveis de ambiente
```bash
DATABASE_USER=
DATABASE_PASSWORD=
DATABASE_NAME=
DATABASE_URL=jdbc:postgresql://cca_postgres:5432/database_name?useTimezone=true&serverTimezone=UTC
```
### Executa docker compose
```bash
docker compose up
```

<div>
  <h2 id="author">Autor</h2>
  <h3>João Darwin</h3>
  <a href="https://www.linkedin.com/in/joao-darwin/" target="_blank"><img src="https://img.shields.io/badge/-LinkedIn-%230077B5?style=for-the-badge&logo=linkedin&logoColor=white" style="border-radius: 30px"></a>
  <a href="https://github.com/Joao-Darwin" target="_blank"><img src="https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white" style="border-radius: 30px"></a>
</div>
