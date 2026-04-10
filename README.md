 # Sistema de Notificações Multicanal

API REST para envio de notificações por múltiplos canais (email e webhook) com processamento assíncrono via filas de mensagens.

## Sobre o projeto

O sistema recebe pedidos de envio de notificação, enfileira no RabbitMQ e processa de forma assíncrona. 
Suporta retry automático com até 3 tentativas e dead letter queue para mensagens que falharam, garantindo resiliência no envio.

### Principais funcionalidades

- Cadastro e gerenciamento de templates de notificação com placeholders dinâmicos
- Envio assíncrono via RabbitMQ com status tracking (PENDING → SENT / FAILED)
- Strategy Pattern para canais de envio (email e webhook), extensível para novos canais
- Retry automático (3 tentativas) com dead letter queue
- Registro de logs a cada tentativa de envio
- Endpoints de monitoramento: métricas, histórico de tentativas e dead letter

## Arquitetura

```
Cliente → API REST → RabbitMQ (fila principal)
                         ↓
                    Consumer processa
                     ↙          ↘
              Email (Mailtrap)   Webhook (HTTP POST)
                     ↓               ↓
                  Sucesso → SENT
                  Falha → Retry (3x) → Dead Letter Queue
```

## Tecnologias

- **Java 21** + **Spring Boot**
- **RabbitMQ** — filas de mensagens com retry e dead letter queue
- **PostgreSQL 16** — banco de dados relacional
- **Flyway** — versionamento de migrations
- **Mailtrap** — serviço de email para testes
- **Spring AMQP** — integração com RabbitMQ
- **Spring Actuator** — health checks e métricas
- **Docker Compose** — orquestração dos serviços
- **Testcontainers** — testes de integração

## Como executar

### Pré-requisitos

- Java 21
- Docker e Docker Compose
- Conta no [Mailtrap](https://mailtrap.io) (gratuita)

### 1. Clone o repositório

```bash
git clone https://github.com/xxxgo-stak/notifications.git
cd notifications
```

### 2. Suba os containers

```bash
docker-compose up -d
```

Isso inicia o PostgreSQL (porta 5432) e o RabbitMQ (porta 5672, painel em http://localhost:15672).

### 3. Configure as variáveis de ambiente

Crie um arquivo `.env` ou configure no `application.yml`:

| Variável | Padrão | Descrição |
|---|---|---|
| DB_URL | jdbc:postgresql://localhost:5432/notifications | URL do banco |
| DB_USER | admin | Usuário do banco |
| DB_PASSWORD | admin123 | Senha do banco |
| RABBITMQ_HOST | localhost | Host do RabbitMQ |
| RABBITMQ_PORT | 5672 | Porta do RabbitMQ |
| RABBITMQ_USER | guest | Usuário do RabbitMQ |
| RABBITMQ_PASS | guest | Senha do RabbitMQ |

As credenciais do Mailtrap devem ser configuradas diretamente no `application.yml`.

### 4. Execute a aplicação

```bash
./mvnw spring-boot:run
```

## Endpoints

### Templates

| Método | Endpoint | Descrição |
|---|---|---|
| POST | /api/templates | Cadastra um novo template |
| GET | /api/templates | Lista todos os templates |
| GET | /api/templates/{id} | Busca template por id |
| PUT | /api/templates/{id} | Atualiza um template |
| DELETE | /api/templates/{id} | Remove um template |

### Notificações

| Método | Endpoint | Descrição |
|---|---|---|
| POST | /api/notifications | Solicita envio de notificação |
| GET | /api/notifications/{id} | Consulta status da notificação |
| GET | /api/notifications/{id}/logs | Histórico de tentativas |
| GET | /api/notifications/stats | Métricas (enviadas, falhas, pendentes) |
| GET | /api/notifications/dead-letter | Lista notificações que falharam |

### Monitoramento

| Método | Endpoint | Descrição |
|---|---|---|
| GET | /actuator/health | Health check da aplicação |

## Exemplos de uso

### Criar um template

```bash
POST /api/templates
```
```json
{
    "name": "bem-vindo",
    "subject": "Bem-vindo à plataforma!",
    "body": "Olá {{nome}}, seja bem-vindo!",
    "channel": "EMAIL"
}
```

### Enviar uma notificação

```bash
POST /api/notifications
```
```json
{
    "templateId": 1,
    "recipient": "fulano@email.com",
    "channel": "EMAIL",
    "payload": "{\"nome\": \"Luiz\"}"
}
```

**Response (202 Accepted):**
```json
{
    "id": 1,
    "status": "PENDING",
    "message": "Notificação enfileirada para envio"
}
```

### Consultar métricas

```bash
GET /api/notifications/stats
```
```json
{
    "sent": 10,
    "failed": 2,
    "pending": 0
}
```

## Design Patterns

### Strategy Pattern

Cada canal de envio implementa a interface `NotificationSender`. O consumer identifica o canal da notificação e delega para o sender correto.
Para adicionar um novo canal (ex: SMS), basta criar uma nova classe que implementa a interface — sem alterar o consumer.

```
NotificationSender (interface)
├── EmailSender
└── WebhookSender
```

## Estrutura do projeto

```
dev.luiz.notifications
├── config              — Configurações (RabbitMQ)
├── controller          — Controllers REST
├── dtos                — Request e Response DTOs
├── entity              — Entidades JPA
├── messaging
│   ├── producer        — Publica mensagens na fila
│   └── consumer        — Consome mensagens e processa envios
├── repository          — Repositórios JPA
├── sender              — Strategy Pattern (canais de envio)
└── service             — Lógica de negócio
```
## Melhorias futuras

- Autenticação e autorização com Spring Security (JWT)
- Rate limiting nos endpoints
- Canal SMS com Twilio
- Dashboard frontend para monitoramento
