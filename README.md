# 🎉 EventHub - Sistema de Gerenciamento de Eventos

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen)
![Maven](https://img.shields.io/badge/Maven-4.0.0-blue)
![License](https://img.shields.io/badge/License-Apache%202.0-blue)

Sistema completo de gerenciamento de eventos desenvolvido com Spring Boot, incluindo validações, documentação Swagger e API REST completa.

---

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Como Executar](#como-executar)
- [Endpoints da API](#endpoints-da-api)
- [Validações](#validações)
- [Documentação Swagger](#documentação-swagger)
- [Testes](#testes)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Banco de Dados](#banco-de-dados)
- [Contribuindo](#contribuindo)
- [Licença](#licença)
- [Contato](#contato)

---

## 📖 Sobre o Projeto

O **EventHub** é uma API RESTful para gerenciamento de eventos, permitindo criar, listar, buscar, atualizar e deletar eventos. O sistema inclui validações robustas para garantir a integridade dos dados e documentação interativa via Swagger.

### Modelo de Dados

Cada evento possui os seguintes campos:

| Campo | Tipo | Descrição | Obrigatório |
|-------|------|-----------|-------------|
| `id` | Long | Identificador único (gerado automaticamente) | ✅ |
| `nome` | String | Nome do evento | ✅ |
| `data` | LocalDateTime | Data e hora do evento | ✅ |
| `local` | String | Local onde o evento será realizado | ✅ |
| `capacidade` | Integer | Capacidade máxima de participantes | ✅ |

---

## ✨ Funcionalidades

- ✅ **CRUD Completo de Eventos**
  - Criar novos eventos
  - Listar todos os eventos
  - Buscar evento por ID
  - Atualizar eventos existentes
  - Deletar eventos

- ✅ **Validações Bean Validation**
  - Nome não pode ser vazio
  - Data não pode ser no passado
  - Local não pode ser vazio
  - Capacidade deve ser positiva

- ✅ **Documentação Interativa**
  - Swagger UI integrado
  - OpenAPI 3.0
  - Testes direto pelo navegador

- ✅ **Tratamento de Erros**
  - Mensagens customizadas em português
  - Respostas padronizadas em JSON
  - Status HTTP apropriados

- ✅ **Banco de Dados H2**
  - Banco em memória para desenvolvimento
  - Console H2 habilitado
  - Dados persistidos durante execução

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 4.0.3** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Validation** - Validações Bean Validation
- **Lombok** - Redução de código boilerplate

### Documentação
- **SpringDoc OpenAPI 2.3.0** - Geração de documentação
- **Swagger UI** - Interface interativa

### Banco de Dados
- **H2 Database** - Banco de dados em memória

### Build
- **Maven** - Gerenciamento de dependências e build

---

## 📦 Pré-requisitos

Antes de começar, você precisa ter instalado:

- **Java 17** ou superior
- **Maven 3.6+** (ou use o Maven Wrapper incluído)
- **Git** (para clonar o repositório)

---

## 🚀 Instalação

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/eventHub.git
cd eventHub
```

### 2. Compile o projeto

**Windows:**
```bash
mvnw.cmd clean install
```

**Linux/Mac:**
```bash
./mvnw clean install
```

---

## ▶️ Como Executar

### Opção 1: Usando Maven Wrapper (Recomendado)

**Windows:**
```bash
mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

### Opção 2: Usando Maven Instalado

```bash
mvn spring-boot:run
```

### Opção 3: Executando o JAR

```bash
java -jar target/eventHub-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: **http://localhost:8080**

---

## 🌐 Endpoints da API

### Base URL
```
http://localhost:8080/api/events
```

### Endpoints Disponíveis

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| GET | `/api/events` | Lista todos os eventos | 200 OK |
| GET | `/api/events/{id}` | Busca evento por ID | 200 OK / 404 Not Found |
| POST | `/api/events` | Cria um novo evento | 201 Created / 400 Bad Request |
| PUT | `/api/events/{id}` | Atualiza um evento | 200 OK / 404 Not Found |
| DELETE | `/api/events/{id}` | Deleta um evento | 204 No Content / 404 Not Found |

---

## 📝 Exemplos de Uso

### 1. Criar Evento (POST)

**Request:**
```bash
curl --location 'http://localhost:8080/api/events' \
--header 'Content-Type: application/json' \
--data '{
    "nome": "Festival de Música 2026",
    "data": "2026-12-31T20:00:00",
    "local": "Estádio Municipal",
    "capacidade": 5000
}'
```

**Response (201 Created):**
```json
{
    "id": 1,
    "nome": "Festival de Música 2026",
    "data": "2026-12-31T20:00:00",
    "local": "Estádio Municipal",
    "capacidade": 5000
}
```

### 2. Listar Todos os Eventos (GET)

**Request:**
```bash
curl --location 'http://localhost:8080/api/events'
```

**Response (200 OK):**
```json
[
    {
        "id": 1,
        "nome": "Festival de Música 2026",
        "data": "2026-12-31T20:00:00",
        "local": "Estádio Municipal",
        "capacidade": 5000
    },
    {
        "id": 2,
        "nome": "DevConf Brasil 2026",
        "data": "2026-06-15T09:00:00",
        "local": "Centro de Convenções",
        "capacidade": 2000
    }
]
```

### 3. Buscar por ID (GET)

**Request:**
```bash
curl --location 'http://localhost:8080/api/events/1'
```

**Response (200 OK):**
```json
{
    "id": 1,
    "nome": "Festival de Música 2026",
    "data": "2026-12-31T20:00:00",
    "local": "Estádio Municipal",
    "capacidade": 5000
}
```

### 4. Atualizar Evento (PUT)

**Request:**
```bash
curl --location --request PUT 'http://localhost:8080/api/events/1' \
--header 'Content-Type: application/json' \
--data '{
    "nome": "Festival de Música 2026 - ATUALIZADO",
    "data": "2026-12-31T21:00:00",
    "local": "Arena Principal",
    "capacidade": 8000
}'
```

**Response (200 OK):**
```json
{
    "id": 1,
    "nome": "Festival de Música 2026 - ATUALIZADO",
    "data": "2026-12-31T21:00:00",
    "local": "Arena Principal",
    "capacidade": 8000
}
```

### 5. Deletar Evento (DELETE)

**Request:**
```bash
curl --location --request DELETE 'http://localhost:8080/api/events/1'
```

**Response (204 No Content):**
```
(Sem corpo na resposta)
```

---

## ✅ Validações

O sistema implementa validações Bean Validation para garantir a integridade dos dados:

### Regras de Validação

| Campo | Validação | Mensagem de Erro |
|-------|-----------|------------------|
| **nome** | `@NotBlank` | "O nome do evento não pode ser vazio" |
| **data** | `@NotNull` + `@FutureOrPresent` | "A data do evento não pode ser no passado" |
| **local** | `@NotBlank` | "O local do evento não pode ser vazio" |
| **capacidade** | `@NotNull` + `@Positive` | "A capacidade deve ser um número positivo" |

### Exemplo de Erro de Validação

**Request com dados inválidos:**
```bash
curl --location 'http://localhost:8080/api/events' \
--header 'Content-Type: application/json' \
--data '{
    "nome": "",
    "data": "2020-01-01T20:00:00",
    "local": "Estádio",
    "capacidade": -10
}'
```

**Response (400 Bad Request):**
```json
{
    "timestamp": "2026-02-22T11:53:05",
    "status": 400,
    "error": "Erro de validação",
    "errors": {
        "nome": "O nome do evento não pode ser vazio",
        "data": "A data do evento não pode ser no passado",
        "capacidade": "A capacidade deve ser um número positivo"
    }
}
```

---

## 📚 Documentação Swagger

A API possui documentação interativa gerada automaticamente com Swagger/OpenAPI.

### Acessar Swagger UI

Após iniciar a aplicação, acesse:

```
http://localhost:8080/swagger-ui.html
```

ou

```
http://localhost:8080/swagger-ui/index.html
```

### API Docs (JSON)

Documentação em formato JSON:

```
http://localhost:8080/api-docs
```

### Recursos do Swagger

- 📝 Documentação completa de todos os endpoints
- 🧪 Testar endpoints diretamente pelo navegador
- 📊 Visualizar modelos de dados
- 📖 Ver exemplos de requisições e respostas
- ✅ Validar payloads antes de enviar

![Swagger UI](https://via.placeholder.com/800x400.png?text=Swagger+UI+Screenshot)

---

## 🧪 Testes

### Testar com cURL

Veja exemplos completos de cURL no arquivo: **[POSTMAN_CURLS.md](POSTMAN_CURLS.md)**

### Testar com Postman

1. Importe a collection: **EventHub_API.postman_collection.json**
2. Execute as requisições
3. Veja a documentação em: **[POSTMAN_GUIDE.md](POSTMAN_GUIDE.md)**

### Testes de Validação

Veja testes completos de validação em: **[VALIDATIONS.md](VALIDATIONS.md)**

---

## 📁 Estrutura do Projeto

```
eventHub/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── eventHub/
│   │   │               ├── EventHubApplication.java       # Classe principal
│   │   │               ├── config/
│   │   │               │   └── SwaggerConfig.java         # Configuração Swagger
│   │   │               ├── controller/
│   │   │               │   └── EventController.java       # Controller REST
│   │   │               ├── domain/
│   │   │               │   └── Event.java                 # Entity JPA
│   │   │               ├── exception/
│   │   │               │   └── GlobalExceptionHandler.java # Handler de erros
│   │   │               ├── repository/
│   │   │               │   └── EventRepository.java       # Repository JPA
│   │   │               └── service/
│   │   │                   └── EventService.java          # Lógica de negócio
│   │   │
│   │   └── resources/
│   │       └── application.properties                     # Configurações
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── eventHub/
│                       └── EventHubApplicationTests.java  # Testes
│
├── target/                                                 # Arquivos compilados
├── mvnw                                                    # Maven Wrapper (Linux/Mac)
├── mvnw.cmd                                                # Maven Wrapper (Windows)
├── pom.xml                                                 # Dependências Maven
├── README.md                                               # Este arquivo
├── SWAGGER.md                                              # Documentação Swagger
├── VALIDATIONS.md                                          # Documentação Validações
├── POSTMAN_CURLS.md                                        # cURLs para testes
├── POSTMAN_GUIDE.md                                        # Guia Postman
└── EventHub_API.postman_collection.json                    # Collection Postman
```

---

## 💾 Banco de Dados

### H2 Database

O projeto usa H2 Database (banco em memória) para desenvolvimento.

### Configurações (application.properties)

```properties
# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Acessar H2 Console

1. Acesse: **http://localhost:8080/h2-console**
2. Configure:
   - **JDBC URL:** `jdbc:h2:mem:testdb`
   - **Username:** `sa`
   - **Password:** (deixe em branco)
3. Clique em **Connect**

### Consultar Eventos

```sql
SELECT * FROM events;
```

---

## 🎯 Status Codes HTTP

| Status | Código | Quando Ocorre |
|--------|--------|---------------|
| OK | 200 | GET e PUT bem-sucedidos |
| Created | 201 | POST bem-sucedido |
| No Content | 204 | DELETE bem-sucedido |
| Bad Request | 400 | Dados inválidos (falha na validação) |
| Not Found | 404 | Recurso não encontrado |
| Internal Server Error | 500 | Erro no servidor |

---

## 🔧 Configuração

### Alterar Porta do Servidor

Edite `application.properties`:

```properties
server.port=9090
```

### Desabilitar Docker Compose

```properties
spring.docker.compose.enabled=false
```

### Mostrar SQL no Console

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Siga os passos:

1. **Fork** o projeto
2. Crie uma **branch** para sua feature (`git checkout -b feature/NovaFuncionalidade`)
3. **Commit** suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. **Push** para a branch (`git push origin feature/NovaFuncionalidade`)
5. Abra um **Pull Request**

### Padrões de Código

- Use convenções do Spring Boot
- Adicione validações adequadas
- Documente com Swagger
- Escreva testes unitários

---

## 📄 Licença

Este projeto está sob a licença **Apache 2.0**. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 📧 Contato

**EventHub Team**

- 📧 Email: contact@eventhub.com
- 🌐 Website: [www.eventhub.com](http://www.eventhub.com)
- 💼 LinkedIn: [EventHub](https://linkedin.com/company/eventhub)

---

## 🎓 Recursos Adicionais

### Documentação Oficial

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Bean Validation](https://beanvalidation.org/2.0/spec/)
- [SpringDoc OpenAPI](https://springdoc.org/)

### Tutoriais

- [Spring Boot REST API Tutorial](https://spring.io/guides/tutorials/rest/)
- [Bean Validation Guide](https://www.baeldung.com/javax-validation)
- [Swagger with Spring Boot](https://www.baeldung.com/spring-rest-openapi-documentation)

---

## ⭐ Features Futuras

- [ ] Autenticação e Autorização (Spring Security)
- [ ] Paginação e Ordenação
- [ ] Filtros de busca avançados
- [ ] Upload de imagens de eventos
- [ ] Sistema de inscrições
- [ ] Notificações por email
- [ ] Integração com calendário
- [ ] API de localização geográfica
- [ ] Dashboard administrativo
- [ ] Exportação de relatórios (PDF/Excel)

---

## 🏆 Agradecimentos

Agradecimentos especiais a todos que contribuíram para este projeto!

- Spring Team pelo incrível framework
- Comunidade Open Source
- Todos os contribuidores

---

## 📊 Estatísticas do Projeto

![GitHub stars](https://img.shields.io/github/stars/seu-usuario/eventHub)
![GitHub forks](https://img.shields.io/github/forks/seu-usuario/eventHub)
![GitHub issues](https://img.shields.io/github/issues/seu-usuario/eventHub)
![GitHub pull requests](https://img.shields.io/github/issues-pr/seu-usuario/eventHub)

---

<div align="center">

**Feito com ❤️ pela equipe EventHub**

[⬆ Voltar ao topo](#-eventhub---sistema-de-gerenciamento-de-eventos)

</div>

