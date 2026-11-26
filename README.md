# NextEra
 
> **Connecting support. Simplifying operations.**
 
Plataforma de **gerenciamento de chamados**, **diagnóstico técnico**, **processamento assíncrono** e **automação de operações**.
 
![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.2.0-6DB33F)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-database-336791)
![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)
 
---
 
## Sumário
 
- [Sobre o projeto](#sobre-o-projeto)
- [Status atual](#status-atual)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Modelo de domínio](#modelo-de-domínio)
- [Regras de negócio](#regras-de-negócio)
- [Como executar](#como-executar)
- [Testes](#testes)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Roadmap](#roadmap)
- [Como contribuir](#como-contribuir)
---
 
## Sobre o projeto
 
O NextEra conecta clientes e analistas de suporte em torno de **tickets**. Cada ticket tem prioridade, ciclo de vida controlado e histórico de auditoria. A prioridade é calculada considerando a **gravidade do problema** e a **proximidade de obrigações fiscais** do cliente, de modo que chamados com prazo legal próximo sejam atendidos primeiro.
 
As regras de negócio vivem em classes de domínio em Java puro, independentes de framework, banco de dados ou camada web.
 
## Status atual
 
O projeto está em fase inicial. A tabela mostra o que já existe e o que ainda será construído.
 
| Componente | Situação |
|---|---|
| Entidades de domínio (`Ticket`, `TicketHistorico`, `Cliente`, `Analista`, `ObrigacaoFiscal`) | Implementado |
| Máquina de estados do ticket | Implementado |
| Cálculo de prioridade (`PriorizacaoTicketService`) | Implementado |
| Testes unitários do domínio | Implementado |
| Persistência (JPA, repositórios, migrações Flyway) | Dependências configuradas, código pendente |
| API REST (controllers, DTOs, validação) | Dependências configuradas, código pendente |
| Segurança (Spring Security) | Dependência configurada, regras pendentes |
| Processamento assíncrono (Redis) | Dependência configurada, código pendente |
| Automação de operações (`AUTO_RESOLVIDO`) | Status previsto, regra pendente |
| Diagnóstico técnico | Planejado |
 
## Tecnologias
 
| Área | Tecnologia |
|---|---|
| Linguagem | Java 25 |
| Framework | Spring Boot 4.2.0 |
| Build | Maven (com Maven Wrapper) |
| API REST | Spring Web MVC |
| Persistência | Spring Data JPA |
| Banco de dados | PostgreSQL |
| Migrações | Flyway |
| Cache e mensageria | Spring Data Redis |
| Segurança | Spring Security |
| Validação | Bean Validation |
| Observabilidade | Spring Boot Actuator |
| Testes | JUnit, Spring Boot Test, Testcontainers |
 
## Arquitetura
 
O projeto é organizado com princípios de **DDD**, mantendo o domínio isolado do restante da aplicação. Hoje existe apenas a camada de domínio:
 
```
com.nextera
├── NexteraApplication        # Ponto de entrada Spring Boot
└── domain
    ├── model                 # Entidades, enums e objetos de domínio
    └── service               # Serviços de domínio (regras que envolvem mais de uma entidade)
```
 
À medida que o projeto evoluir, a ideia é adicionar camadas de **aplicação** (casos de uso) e **infraestrutura** (persistência, fila, API REST), sempre com dependências apontando para o domínio e nunca o contrário. Nomes de pacotes e classes dessas camadas serão definidos durante a implementação.
 
## Modelo de domínio
 
### Entidades
 
| Classe | Responsabilidade |
|---|---|
| `Ticket` | Chamado de suporte. Controla status, prioridade, analista responsável, título, descrição e carimbos de tempo. |
| `TicketHistorico` | Registro **imutável** de um evento ocorrido no ticket, com valor anterior e valor atual. |
| `Cliente` | Quem abre os chamados. Nome e e-mail validados. |
| `Analista` | Quem atende os chamados. Pode ser ativado e desativado. |
| `ObrigacaoFiscal` | Descrição, data de vencimento e tipo de uma obrigação fiscal do cliente. |
 
### Enums
 
| Enum | Valores |
|---|---|
| `TicketStatus` | `ABERTO`, `EM_ANDAMENTO`, `AGUARDANDO_CLIENTE`, `AGUARDANDO_ANALISTA`, `RESOLVIDO`, `AUTO_RESOLVIDO`, `FECHADO` |
| `TicketPrioridade` | `BAIXA`, `MEDIA`, `ALTA`, `CRITICA` |
| `GravidadeProblema` | `BAIXA`, `MEDIA`, `ALTA`, `CRITICA` |
| `TipoAutor` | `CLIENTE`, `ANALISTA`, `ADMINISTRADOR`, `SISTEMA` |
| `TipoEventoTicket` | `TICKET_CRIADO`, `STATUS_ALTERADO`, `PRIORIDADE_ALTERADA`, `TITULO_ALTERADO`, `DESCRICAO_ALTERADA`, `ANALISTA_ATRIBUIDO`, `ANALISTA_REMOVIDO`, `TICKET_RESOLVIDO`, `TICKET_FECHADO`, `AUTOMACAO_EXECUTADA` |
 
### Ciclo de vida do ticket
 
```
                 iniciarAtendimento()
   ABERTO ─────────────────────────────▶ EM_ANDAMENTO
                                            │   ▲
              aguardarCliente()             │   │  retomarAtendimento()
              aguardarAnalista()            ▼   │
                                  AGUARDANDO_CLIENTE
                                  AGUARDANDO_ANALISTA
 
   EM_ANDAMENTO ── resolver() ──▶ RESOLVIDO ── fecharTicket() ──▶ FECHADO
                                  AUTO_RESOLVIDO ─ fecharTicket() ─▶ FECHADO
```
 
`AUTO_RESOLVIDO` já é aceito pelas regras de fechamento, mas ainda não existe uma operação que leve um ticket até esse status. Ele está reservado para a futura automação.
 
## Regras de negócio
 
### Ticket
 
- Título, descrição, prioridade e cliente são obrigatórios na criação.
- Todo ticket nasce com status `ABERTO`.
- Só é possível iniciar o atendimento de um ticket `ABERTO`.
- Só é possível aguardar cliente, aguardar analista ou resolver a partir de `EM_ANDAMENTO`.
- Só é possível retomar o atendimento a partir de `AGUARDANDO_CLIENTE` ou `AGUARDANDO_ANALISTA`.
- Só é possível fechar um ticket `RESOLVIDO` ou `AUTO_RESOLVIDO`.
- Tickets `FECHADO`, `RESOLVIDO` ou `AUTO_RESOLVIDO` **não podem** ter título, descrição, prioridade ou analista alterados.
- Um ticket tem no máximo um analista. Para trocar, é preciso removê-lo primeiro.
- Alterar título, descrição ou prioridade para o mesmo valor atual é rejeitado.
### Histórico
 
- `ticketId`, `tipoEvento`, `descricao` e `tipoAutor` são obrigatórios.
- O `autorId` só pode ser nulo quando o autor é `SISTEMA`.
- Os registros são imutáveis (todos os campos são `final`).
### Cliente e analista
 
- Nome não pode ser nulo ou vazio.
- E-mail é obrigatório e deve ter formato válido.
- Um analista não pode ser ativado se já está ativo, nem desativado se já está inativo.
### Priorização
 
O `PriorizacaoTicketService` calcula a prioridade do ticket:
 
1. A gravidade do problema é convertida para a prioridade equivalente.
2. Se existe uma obrigação fiscal com vencimento em até **3 dias** (incluindo datas já vencidas), a prioridade sobe um nível.
3. A prioridade nunca passa de `CRITICA`.
| Gravidade | Sem prazo próximo | Com prazo fiscal próximo |
|---|---|---|
| `BAIXA` | `BAIXA` | `MEDIA` |
| `MEDIA` | `MEDIA` | `ALTA` |
| `ALTA` | `ALTA` | `CRITICA` |
| `CRITICA` | `CRITICA` | `CRITICA` |
 
## Como executar
 
### Pré-requisitos
 
- **JDK 25**
- **Docker** (necessário para os testes com Testcontainers e para rodar PostgreSQL e Redis localmente)
- Maven não precisa estar instalado: o projeto inclui o Maven Wrapper.
### Clonar o repositório
 
```bash
git clone https://github.com/PedrorCastro/NextEra.git
cd NextEra/nextera
```
 
### Configurar o ambiente
 
O arquivo `src/main/resources/application.properties` ainda contém apenas o nome da aplicação. Como o projeto já inclui JPA, Flyway e Redis, a aplicação precisa de configuração de banco para iniciar. Exemplo de propriedades (ajuste conforme seu ambiente):
 
```properties
spring.application.name=nextera
 
spring.datasource.url=jdbc:postgresql://localhost:5432/nextera
spring.datasource.username=nextera
spring.datasource.password=nextera
 
spring.data.redis.host=localhost
spring.data.redis.port=6379
```
 
Para subir os serviços de apoio rapidamente com Docker:
 
```bash
docker run -d --name nextera-postgres \
  -e POSTGRES_DB=nextera -e POSTGRES_USER=nextera -e POSTGRES_PASSWORD=nextera \
  -p 5432:5432 postgres:17
 
docker run -d --name nextera-redis -p 6379:6379 redis:7
```
 
> **Atenção:** nunca versione senhas reais. Use variáveis de ambiente ou perfis do Spring para credenciais de produção.
 
### Executar a aplicação
 
```bash
# Linux / macOS
./mvnw spring-boot:run
 
# Windows
mvnw.cmd spring-boot:run
```
 
### Gerar o pacote
 
```bash
./mvnw clean package
```
 
## Testes
 
```bash
./mvnw test
```
 
Os testes unitários de domínio rodam sem dependências externas. Os testes de integração usam **Testcontainers** e exigem o Docker em execução.
 
| Teste | O que cobre |
|---|---|
| `TicketTest` | Transições de status e regras de alteração do ticket |
| `TicketHistoricoTest` | Validações e imutabilidade do histórico |
| `AnalistaTest` | Validação de dados e ativação/desativação |
| `ClienteTest` | Validação de nome e e-mail |
| `PriorizacaoTicketServiceTest` | Cálculo de prioridade com e sem prazo fiscal |
| `NexteraApplicationTests` | Carregamento do contexto Spring |
 
## Estrutura do projeto
 
```
NextEra
├── README.md
├── .gitignore
└── nextera
    ├── pom.xml
    ├── mvnw / mvnw.cmd
    └── src
        ├── main
        │   ├── java/com/nextera
        │   │   ├── NexteraApplication.java
        │   │   └── domain
        │   │       ├── model
        │   │       │   ├── Ticket.java
        │   │       │   ├── TicketHistorico.java
        │   │       │   ├── Cliente.java
        │   │       │   ├── Analista.java
        │   │       │   ├── ObrigacaoFiscal.java
        │   │       │   └── (enums: TicketStatus, TicketPrioridade,
        │   │       │        GravidadeProblema, TipoAutor, TipoEventoTicket)
        │   │       └── service
        │   │           └── PriorizacaoTicketService.java
        │   └── resources
        │       └── application.properties
        └── test
            └── java/com/nextera
                ├── NexteraApplicationTests.java
                ├── TestNexteraApplication.java
                ├── TestcontainersConfiguration.java
                └── domain
                    ├── model
                    └── service
```
 
## Roadmap
 
- [ ] Configuração de datasource, Flyway e primeira migração (tabelas de tickets, histórico, clientes e analistas)
- [ ] Mapeamento de persistência e repositórios
- [ ] Camada de aplicação com casos de uso (abrir ticket, atribuir analista, resolver, fechar)
- [ ] Geração automática de `TicketHistorico` a cada mudança no ticket
- [ ] API REST com DTOs, validação e tratamento padronizado de erros
- [ ] Autenticação e autorização por perfil (cliente, analista, administrador)
- [ ] Processamento assíncrono com Redis
- [ ] Automação de operações e regra de `AUTO_RESOLVIDO`
- [ ] Módulo de diagnóstico técnico
- [ ] Documentação da API (OpenAPI)
- [ ] Pipeline de CI
## Como contribuir
 
1. Faça um fork do repositório.
2. Crie uma branch para sua alteração: `git checkout -b feature/minha-feature`
3. Escreva ou ajuste testes para cobrir a mudança.
4. Faça commit das alterações: `git commit -m "feat: descrição da mudança"`
5. Envie a branch: `git push origin feature/minha-feature`
6. Abra um Pull Request descrevendo o que mudou e por quê.
## Licença
 
Ainda não definida. Adicione um arquivo `LICENSE` para esclarecer os termos de uso do projeto.
 
---
 
Desenvolvido por [Pedro Castro](https://github.com/PedrorCastro).
 
