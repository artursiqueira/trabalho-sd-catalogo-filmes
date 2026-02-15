# 🎬 Catálogo de Filmes API

**Trabalho Prático - Sistemas Distribuídos 2025/2**  
**Universidade Federal do Espírito Santo - UFES**

## 📌 Sobre (leia isso primeiro)

Este projeto inclui:

- Uma **collection do Postman pronta** para testar toda a API (GET, autenticação e rotas protegidas com JWT)
- Uma página web simples (`index.html`) que funciona como um **guia de rotas** (tipo um mini-Swagger)

📁 **Postman Collection:** `postman/Catalogo_Filmes_API.postman_collection.json`

📄 **Guia de rotas (mini-Swagger):** ao subir a aplicação, acesse:
- `http://localhost:8080/` (abre o `index.html`)

> **OBS:** a página `index.html` é apenas para consulta/visualização das rotas.  
> Para executar as requisições corretamente (incluindo autenticação e uso do token), use o **Postman** importando a collection do projeto.

### Como testar pelo Postman (recomendado)

1. Abra o Postman
2. Clique em **Import** e selecione o arquivo da pasta `postman/` acima
3. Execute as requisições na ordem:
   - **Auth → Registrar** (`POST /api/auth/registrar`)
   - **Auth → Login** (`POST /api/auth/login`) → a resposta retorna o token
4. Copie o token e use nas rotas protegidas enviando o header:

```
Authorization: Bearer {seu_token}
```

> Na collection, as rotas protegidas já estão organizadas para facilitar o fluxo de testes.

## 👥 Grupo 6
- **Mariana**
- **Artur**

## 📋 Sobre o Projeto

API RESTful completa para gerenciamento de catálogo de filmes, desenvolvida com **Jersey** (framework JAX-RS) e **MySQL**.

## 🚀 Tecnologias Utilizadas

- **Framework REST**: Jersey 3.1.3 (JAX-RS)
- **Banco de Dados**: MySQL 8.0
- **ORM**: Hibernate 6.2.7
- **Autenticação**: JWT (JSON Web Token)
- **Segurança**: BCrypt para hash de senhas
- **Build**: Maven
- **Testes**: JUnit 5, Mockito, REST Assured
- **Servidor**: Jetty (desenvolvimento)

## 📁 Estrutura do Projeto

```
catalogo-filmes-api/
├── src/
│   ├── main/
│   │   ├── java/br/ufes/catalogo/
│   │   │   ├── config/          # Configurações da aplicação
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Exceções e mappers
│   │   │   ├── model/           # Entidades JPA
│   │   │   ├── repository/      # Camada de persistência
│   │   │   ├── resource/        # Endpoints REST
│   │   │   ├── security/        # Autenticação e segurança
│   │   │   └── service/         # Lógica de negócio
│   │   ├── resources/
│   │   │   └── META-INF/
│   │   │       └── persistence.xml
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml
│   │       └── index.html
│   └── test/                    # Testes unitários e integração
├── database/
│   └── schema.sql              # Script SQL
├── postman/
│   └── Catalogo_Filmes_API.postman_collection.json
├── docs/
│   └── Documentacao_API.pdf
├── pom.xml
└── README.md
```

## 🗄️ Modelo de Dados

### Entidades Principais

1. **Filme** (entidade principal)
   - id, titulo, sinopse, dataLancamento, duracao, diretor, avaliacao, posterUrl
   - Relacionamento: ManyToOne com Genero
   - Relacionamento: ManyToMany com Ator

2. **Genero** (entidade relacionada)
   - id, nome, descricao
   - Relacionamento: OneToMany com Filme

3. **Ator** (entidade complementar)
   - id, nome, dataNascimento, nacionalidade, biografia, fotoUrl
   - Relacionamento: ManyToMany com Filme

4. **Usuario** (autenticação)
   - id, username, email, senha, tipo, criadoEm, ultimoAcesso, ativo

## 🔧 Instalação e Configuração

### Pré-requisitos

- Java JDK 17 ou superior
- Maven 3.6+
- MySQL 8.0+
- Git

### Passo 1: Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/catalogo-filmes-api.git
cd catalogo-filmes-api
```

### Passo 2: Configurar o Banco de Dados

1. Criar o banco de dados MySQL:
```bash
mysql -u root -p < database/schema.sql
```

2. Ajustar as credenciais em `src/main/resources/META-INF/persistence.xml`:
```xml
<property name="jakarta.persistence.jdbc.user" value="seu_usuario"/>
<property name="jakarta.persistence.jdbc.password" value="sua_senha"/>
```

### Passo 3: Compilar o Projeto

```bash
mvn clean install
```

### Passo 4: Executar a Aplicação

**Opção 1: Usando Jetty (desenvolvimento)**
```bash
mvn jetty:run
```

**Opção 2: Deploy em servidor Tomcat**
```bash
mvn package
# Copiar catalogo-filmes-api.war para webapps/ do Tomcat
```

A API estará disponível em: `http://localhost:8080/api`

## 📡 Endpoints da API

### Autenticação

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/auth/registrar` | Registra novo usuário | Não |
| POST | `/auth/login` | Realiza login e retorna JWT | Não |

### Filmes

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| GET | `/filmes` | Lista todos os filmes | Não |
| GET | `/filmes?pagina=0&tamanho=10` | Lista com paginação | Não |
| GET | `/filmes?titulo=matrix` | Busca por título | Não |
| GET | `/filmes?generoId=1` | Filtra por gênero | Não |
| GET | `/filmes?diretor=nolan` | Filtra por diretor | Não |
| GET | `/filmes/{id}` | Busca filme por ID | Não |
| POST | `/filmes` | Cria novo filme | **Sim** |
| PUT | `/filmes/{id}` | Atualiza filme completo | **Sim** |
| PATCH | `/filmes/{id}` | Atualiza filme parcial | **Sim** |
| DELETE | `/filmes/{id}` | Remove filme | **Sim** |

### Gêneros

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| GET | `/generos` | Lista todos os gêneros | Não |
| GET | `/generos/{id}` | Busca gênero por ID | Não |
| GET | `/generos/{id}/filmes` | Lista filmes do gênero | Não |
| POST | `/generos` | Cria novo gênero | **Sim** |
| PUT | `/generos/{id}` | Atualiza gênero | **Sim** |
| DELETE | `/generos/{id}` | Remove gênero | **Sim** |

## 🔑 Autenticação

A API utiliza **JWT (JSON Web Token)** para autenticação.

### Como usar:

1. **Registrar um usuário:**
```http
POST /api/auth/registrar
```

```json
{
  "username": "maria",
  "email": "maria@example.com",
  "senha": "senha123"
}
```

2. **Fazer login:**
```http
POST /api/auth/login
```

```json
{
  "username": "maria",
  "senha": "senha123"
}
```

Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "expiracaoEm": 1738876800000
}
```

3. **Usar o token nas requisições protegidas:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 📝 Exemplos de Requisições

### Criar um Filme

```http
POST /api/filmes
Authorization: Bearer {seu_token}
Content-Type: application/json
```

```json
{
  "titulo": "Inception",
  "sinopse": "Um ladrão que rouba segredos através dos sonhos...",
  "dataLancamento": "2010-07-16",
  "duracao": 148,
  "diretor": "Christopher Nolan",
  "avaliacao": 8.8,
  "posterUrl": "https://example.com/inception.jpg",
  "generoId": 4
}
```

> Observação: o atributo `generoId` é obrigatório e deve corresponder a um gênero existente.

### Listar Filmes com Paginação

```http
GET /api/filmes?pagina=0&tamanho=5
```

Resposta (exemplo):
```json
{
  "conteudo": [],
  "paginaAtual": 0,
  "tamanhoPagina": 5,
  "totalElementos": 0,
  "totalPaginas": 0,
  "primeira": true,
  "ultima": true
}
```

### Buscar Filmes por Título

```http
GET /api/filmes?titulo=matrix
```

## 🧪 Executar Testes

```bash
# Todos os testes
mvn test

# Com relatório de cobertura
mvn test jacoco:report
```

**Cobertura de Testes**: >20% conforme especificação

## 📊 Códigos de Status HTTP

| Código | Significado |
|--------|------------|
| 200 | OK - Requisição bem-sucedida |
| 201 | Created - Recurso criado com sucesso |
| 204 | No Content - Recurso deletado com sucesso |
| 400 | Bad Request - Dados inválidos |
| 401 | Unauthorized - Autenticação necessária |
| 404 | Not Found - Recurso não encontrado |
| 500 | Internal Server Error - Erro no servidor |

## 🔒 Segurança

- ✅ Senhas criptografadas com **BCrypt** (12 rounds)
- ✅ Tokens JWT com expiração de 24 horas
- ✅ CORS configurado para permitir requisições de diferentes origens
- ✅ Validação de dados com Bean Validation
- ✅ Proteção contra SQL Injection (JPA/Hibernate)
- ✅ Filtro de autenticação para endpoints protegidos

## 📚 Documentação

- **Documentação PDF**: `docs/Documentacao_API.pdf`
- **Postman Collection**: `postman/Catalogo_Filmes_API.postman_collection.json`

## 🤝 Contribuidores

- **Artur** - Desenvolvimento API, testes unitários e testes locais.
- **Mariana** - Documentação, testes QA e versionamento GitHub.

## 📄 Licença

Este projeto foi desenvolvido como trabalho acadêmico para a disciplina de Sistemas Distribuídos da UFES.

---

**Professor:** Helder de Amorim Mendes  
**Disciplina:** Sistemas Distribuídos 2025/2  
**Instituição:** Universidade Federal do Espírito Santo - **Departamento de Computação** - CCENS/UFES