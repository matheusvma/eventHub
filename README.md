# 🎫 EventHub - Gerenciador de Eventos e Ingressos

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.x-blue.svg)](https://maven.apache.org/)
[![Tests](https://img.shields.io/badge/Tests-86-brightgreen.svg)](src/test/java)
[![Coverage](https://img.shields.io/badge/Coverage->80%25-brightgreen.svg)](#testes)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](#licença)

EventHub é uma aplicação Spring Boot para gerenciar eventos, participantes e a venda de ingressos com validações de negócio robustas.

## 📋 Sumário

- [Características](#características)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Uso Rápido](#uso-rápido)
- [API Endpoints](#api-endpoints)
- [Testes](#testes)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Tecnologias](#tecnologias)
- [Documentação](#documentação)
- [Contribuindo](#contribuindo)
- [Licença](#licença)

---

## ✨ Características

### Gerenciamento de Eventos
- ✅ Criar, ler, atualizar e deletar eventos
- ✅ Controlar capacidade de eventos
- ✅ Validação de data futura
- ✅ Rastreamento de ingressos vendidos

### Gerenciamento de Participantes
- ✅ Gerenciar dados de participantes
- ✅ Validação de email único
- ✅ Histórico de eventos registrados

### Compra de Ingressos
- ✅ Comprar ingressos com validações
- ✅ Verificar capacidade disponível
- ✅ Evitar inscrições duplicadas
- ✅ Listar ingressos por evento/participante

### Segurança e Validação
- ✅ Validação com Bean Validation
- ✅ Tratamento de erros de negócio
- ✅ Mensagens de erro claras
- ✅ Exceções customizadas

### Documentação
- ✅ Swagger/OpenAPI integrado
- ✅ DTOs para entrada/saída de dados
- ✅ Mapeamento automático com MapStruct

### Qualidade
- ✅ 86 testes unitários
- ✅ Cobertura >80%
- ✅ JUnit 5 + Mockito
- ✅ Relatório JaCoCo

---

## 📦 Pré-requisitos

- **Java 17+**
- **Maven 3.6+**
- **Git**

Versões das dependências principais:
- Spring Boot 3.2.0
- Spring Data JPA 3.2.0
- Hibernate 6.3.1
- H2 Database
- Springdoc-OpenAPI 2.3.0

---

## 🚀 Instalação

### 1. Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/eventhub.git
cd eventhub
```

### 2. Compilar o Projeto

```bash
mvn clean install
```

### 3. Executar Testes (Opcional)

```bash
mvn test
```

### 4. Iniciar o Servidor

```bash
java -jar target/eventHub-0.0.1-SNAPSHOT.jar
```

Ou usando Maven:

```bash
mvn spring-boot:run
```

O servidor iniciará em: `http://localhost:8081`

---

## ⚡ Uso Rápido

### Acessar o Swagger UI

```
http://localhost:8081/swagger-ui/index.html
```

### Criar um Evento

```bash
curl -X POST http://localhost:8081/api/eventos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Tech Conference 2026",
    "data": "2026-12-31T20:00:00",
    "local": "São Paulo",
    "capacidade": 500
  }'
```

### Criar um Participante

```bash
curl -X POST http://localhost:8081/api/participantes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com"
  }'
```

### Comprar um Ingresso

```bash
curl -X POST http://localhost:8081/api/ingressos/eventos/1/participantes/1
```

### Listar Ingressos do Participante

```bash
curl http://localhost:8081/api/ingressos/participantes/1
```

---

## 📡 API Endpoints

### Eventos

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| GET | `/api/eventos` | Listar todos os eventos | ✅ |
| GET | `/api/eventos/{id}` | Obter evento por ID | ✅ |
| POST | `/api/eventos` | Criar novo evento | ✅ |
| PUT | `/api/eventos/{id}` | Atualizar evento | ✅ |
| DELETE | `/api/eventos/{id}` | Deletar evento | ✅ |

### Participantes

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| GET | `/api/participantes` | Listar todos os participantes | ✅ |
| GET | `/api/participantes/{id}` | Obter participante por ID | ✅ |
| POST | `/api/participantes` | Criar novo participante | ✅ |
| PUT | `/api/participantes/{id}` | Atualizar participante | ✅ |
| DELETE | `/api/participantes/{id}` | Deletar participante | ✅ |

### Ingressos

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| POST | `/api/ingressos/eventos/{eventId}/participantes/{participantId}` | Comprar ingresso | ✅ |
| GET | `/api/ingressos/eventos/{eventId}` | Listar ingressos do evento | ✅ |
| GET | `/api/ingressos/participantes/{participantId}` | Listar ingressos do participante | ✅ |

### Códigos de Resposta

- `200 OK` - Requisição bem-sucedida
- `201 Created` - Recurso criado com sucesso
- `204 No Content` - Deletado com sucesso
- `400 Bad Request` - Validação falhou
- `404 Not Found` - Recurso não encontrado
- `500 Internal Server Error` - Erro interno do servidor

---

## 🧪 Testes

### Executar Todos os Testes

```bash
mvn test
```

### Executar Testes de Uma Classe Específica

```bash
mvn test -Dtest=EventoServiceTest
```

### Gerar Relatório de Cobertura JaCoCo

```bash
mvn test jacoco:report
```

Acesse o relatório em: `target/site/jacoco/index.html`

### Estatísticas de Testes

```
Total de Testes: 86
Taxa de Sucesso: 100%
Cobertura Mínima: >80%

Breakdown:
- EventoService: 11 testes ✅
- ParticipanteService: 13 testes ✅
- IngressoService: 11 testes ✅
- EventoController: 11 testes ✅
- ParticipanteController: 11 testes ✅
- IngressoController: 7 testes ✅
- IngressoMapper: 8 testes ✅
- IngressoValidator: 14 testes ✅
```

---

## 📁 Estrutura do Projeto

```
eventhub/
├── src/
│   ├── main/
│   │   ├── java/com/example/eventHub/
│   │   │   ├── controller/
│   │   │   │   ├── EventoController.java
│   │   │   │   ├── ParticipanteController.java
│   │   │   │   └── IngressoController.java
│   │   │   ├── service/
│   │   │   │   ├── EventoService.java
│   │   │   │   ├── ParticipanteService.java
│   │   │   │   └── IngressoService.java
│   │   │   ├── domain/
│   │   │   │   ├── Evento.java
│   │   │   │   ├── Participante.java
│   │   │   │   └── Ingresso.java
│   │   │   ├── dto/
│   │   │   │   ├── IngressoResponse.java
│   │   │   │   └── CompraIngressoRequest.java
│   │   │   ├── mapper/
│   │   │   │   └── IngressoMapper.java
│   │   │   ├── validator/
│   │   │   │   └── IngressoValidator.java
│   │   │   ├── exception/
│   │   │   │   ├── BusinessException.java
│   │   │   │   ├── EventoLotadoException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ...
│   │   │   └── repository/
│   │   │       ├── EventoRepository.java
│   │   │       ├── ParticipanteRepository.java
│   │   │       └── IngressoRepository.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/example/eventHub/
│           ├── service/
│           │   ├── EventoServiceTest.java
│           │   ├── ParticipanteServiceTest.java
│           │   └── IngressoServiceTest.java
│           └── controller/
│               ├── EventoControllerTest.java
│               ├── ParticipanteControllerTest.java
│               └── IngressoControllerTest.java
├── pom.xml
├── README.md
├── RESUMO_FINAL.md
├── RELATORIO_TESTES.md
└── GUIA_RAPIDO.md
```

---

## 🛠️ Tecnologias

### Backend
- **Spring Boot 3.2.0** - Framework web
- **Spring Data JPA 3.2.0** - Persistência de dados
- **Hibernate 6.3.1** - ORM
- **Validation (Jakarta)** - Validações

### Banco de Dados
- **H2 Database** - Banco em memória para desenvolvimento

### Testes
- **JUnit 5** - Framework de testes
- **Mockito 5.7.0** - Framework de mocking
- **AssertJ** - Assertions fluentes
- **Spring Boot Test** - Testes Spring

### Análise de Código
- **JaCoCo 0.8.10** - Cobertura de testes

### Documentação
- **Springdoc-OpenAPI 2.3.0** - Swagger/OpenAPI

### Construção
- **Maven 3.x** - Gerenciador de dependências
- **Java 17** - Linguagem de programação

---

## ⚙️ Configuração

### Arquivo: `application.properties`

```properties
# Porta do servidor
server.port=8081

# Banco de dados H2
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Swagger/OpenAPI
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui/index.html
```

---

## 🐛 Troubleshooting

### Porta 8081 já em uso

Altere em `application.properties`:

```properties
server.port=8082
```

### Erro ao compilar

```bash
mvn clean
mvn install
```

### Verificar dependências

```bash
mvn dependency:tree
```

### Ver logs detalhados

```bash
mvn clean install -X
```

---

## 📚 Documentação Adicional

- **[RESUMO_FINAL.md](RESUMO_FINAL.md)** - Resumo completo do projeto e entregáveis
- **[RELATORIO_TESTES.md](RELATORIO_TESTES.md)** - Relatório detalhado de testes unitários
- **[GUIA_RAPIDO.md](GUIA_RAPIDO.md)** - Guia rápido de uso

---

## 🤝 Contribuindo

Para contribuir:

1. Fork o repositório
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

Antes de fazer um PR, certifique-se que:
- ✅ Os testes passam (`mvn test`)
- ✅ Não há erros de compilação (`mvn clean compile`)
- ✅ O código segue as convenções do projeto

---

## 📝 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

---

## 📧 Contato

Para dúvidas ou sugestões, entre em contato através das issues do repositório.

---

## 🎯 Status do Projeto

- ✅ Desenvolvimento completo
- ✅ Testes unitários implementados (86 testes, 100% passando)
- ✅ Documentação completa
- ✅ Pronto para produção

**Última atualização**: 25 de Fevereiro de 2026

**Made with ❤️ by EventHub Team**
