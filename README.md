<img src="docs/banner.png" alt="User Management — Spring Boot" width="100%">

# User Management

API REST de cadastro e autenticação de usuários, com senhas protegidas por BCrypt.

`Java` · `Spring Boot` · `Spring Security` · `Spring Data JPA` · `MySQL`

---

## O que esta API faz

Três operações: registrar um usuário, autenticar com e-mail e senha, e acessar uma área restrita enquanto a sessão estiver ativa. As senhas nunca são guardadas como texto — passam por `BCryptPasswordEncoder` antes de ir para o banco, e a verificação no login compara o hash.

## Endpoints

| Método | Rota | Corpo / Cabeçalho | Descrição |
|---|---|---|---|
| `POST` | `/api/register` | `{ "email", "password" }` | Registra um usuário |
| `POST` | `/api/login` | `{ "email", "password" }` | Autentica e abre a sessão |
| `GET` | `/api/dashboard` | cabeçalho `email` | Área restrita, exige sessão ativa |

## Como rodar

**Pré-requisitos:** Java 17 ou superior e MySQL 8.

```bash
# 1. Clone
git clone https://github.com/BernardoMonnerat/UserManagement.git
cd UserManagement

# 2. Crie o banco
mysql -u root -p -e "CREATE DATABASE users;"

# 3. Informe as credenciais
export DB_USERNAME=root
export DB_PASSWORD=sua_senha

# 4. Suba a aplicação
./mvnw spring-boot:run
```

## Exemplo de uso

```bash
# Registrar
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{"email":"bernardo@exemplo.com","password":"minhasenha"}'
# → User has been registered Successfully!

# Autenticar
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"email":"bernardo@exemplo.com","password":"minhasenha"}'
# → Logged successfully!

# Acessar a area restrita
curl http://localhost:8080/api/dashboard -H "email: bernardo@exemplo.com"
# → Welcome to Dashboard, you session is active!
```

## Estrutura

```
src/main/java/com/user_api/ebac1/
├── Configuration/   SecurityConfig (BCrypt), DbConfiguration
├── Controller/      UserController — registro, login, dashboard
├── Service/         UserService — regras de negócio
├── Model/           entidade User
├── Request/         UserRequest — contrato de entrada
├── Utils/           LoggedUser — controle de sessão
└── Exceptions/      UserExceptions
```

## Roadmap

- [ ] Substituir o controle de sessão em memória por JWT
- [ ] Deixar de identificar o usuário por cabeçalho `email` no `/dashboard`
- [ ] Validação de formato de e-mail e força de senha no `UserRequest`
- [ ] Respostas em JSON estruturado em vez de texto
- [ ] Testes do fluxo de registro e autenticação

## Licença

MIT — veja [LICENSE](LICENSE).
