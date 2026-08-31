# Exams Control

Sistema desenvolvido para controle e acompanhamento do fluxo de solicitacoes de exames e procedimentos no contexto da regulacao de saude municipal.

O projeto foi concebido como um MVP (Minimum Viable Product) com finalidade educacional e pratica, focando na aplicacao de regras de negocio consistentes no backend e na separacao clara de responsabilidades arquiteturais.

---

## Motivacao e Contexto

O projeto nasceu da minha experiencia pratica atuando na regulacao de saude municipal. No dia a dia desse setor, gargalos como controle descentralizado de vagas, dificuldade no monitoramento de filas de espera e risco de agendamentos alem do teto mensal de procedimentos sao desafios frequentes.

A partir dessa vivencia operacional, desenvolvi o **Exams Control** para aplicar conceitos de Engenharia de Software na resolucao desses problemas, traduzindo regras de negocio reais em um backend estruturado e confiavel.

---

## Proposta e Problema de Negocio

Em sistemas municipais de saude, a gestao de exames enfrenta desafios como a falta de visibilidade sobre filas de espera, controle manual de limites mensais e risco de agendamentos acima da capacidade contratada.

O **Exams Control** centraliza essa operacao atraves de regras fundamentais:

* **Controle de Cotas Mensais:** Cada procedimento possui um limite mensal de vagas. O sistema impede agendamentos que excedam a cota do mes selecionado.
* **Fila de Espera sem Bloqueio de Entrada:** A ausencia de cotas nao impede a criacao de novas solicitacoes (`PENDING`). A restricao de cota atua estritamente no momento da transicao para agendado (`SCHEDULED`).
* **Priorizacao Clinica:** A ordenacao das solicitacoes prioriza a gravidade sobre a ordem de chegada (`URGENT > PRIORITY > ELECTIVE`).
* **Visao Operacional (Dashboard):** Fornecimento de metricas consolidadas diretamente pelo backend (total de cotas, utilizadas no mes, disponiveis e total de pacientes na fila).

---

## Arquitetura e Decisoes Tecnicas

A aplicacao adota uma arquitetura em camadas bem delimitadas:

* **Controller:** Recepcao de requisicoes HTTP, validacao de entrada e mapeamento de respostas via DTOs, sem presenca de regras de negocio.
* **Service:** Centralizacao de todas as validacoes, calculos de cotas, transicoes de status e lancamento de excecoes de dominio (como `InvalidSchedulingException`).
* **Repository:** Acesso e persistencia de dados utilizando Spring Data JPA.
* **DTOs Especificos:** Criacao de contratos dedicados por operacao (como DTOs enxutos para `PATCH`), evitando a exposicao desnecessaria de entidades JPA ou campos imutaveis.
* **Frontend Integrado:** Interface minimalista servida como assets estaticos pelo Spring Boot, consumindo a API REST via Fetch API para apresentar os dados sem duplicar logica de negocio no cliente.

---

## Tech Stack

### Backend
* Java 21 LTS
* Spring Boot (Spring Web, Spring Data JPA, Validation)
* PostgreSQL
* Maven

### Frontend
* HTML5 / CSS3
* Bootstrap 5
* JavaScript Vanilla (Fetch API)

---

## Endpoints Principais (API REST)

| Metodo | Rota | Descricao |
| :--- | :--- | :--- |
| `GET` | `/patients` | Listagem de pacientes cadastrados |
| `POST` | `/patients` | Cadastro de paciente (validacao de Cartao SUS unico) |
| `GET` | `/procedures` | Listagem de procedimentos e cotas configuradas |
| `POST` | `/procedures` | Cadastro de novo procedimento |
| `GET` | `/procedures/dashboard` | Metricas consolidadas de cotas, agendamentos e filas |
| `POST` | `/exam-requests` | Criacao de nova solicitacao de exame (status inicial `PENDING`) |
| `PATCH` | `/exam-requests/{id}` | Atualizacao de status e definicao de data de agendamento |