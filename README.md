# Salão de Beleza - API de Administração

Este projeto é uma API de administração para um salão de beleza, construída utilizando Spring Boot, Docker e Maven. Ele permite gerenciar administradores, autenticação, serviços e mais. A API utiliza o padrão RESTful e possui documentação interativa gerada pelo Swagger.

## Tecnologias Utilizadas

- **Java**: JDK 23
- **Spring Boot**: Framework para desenvolvimento de APIs RESTful
- **Docker**: Para orquestração de containers e ambiente de backend
- **Maven**: Gerenciador de dependências e build
- **Swagger**: Documentação interativa da API
- **Spring Security**: Autenticação e autorização

## Pré-requisitos

Antes de rodar a aplicação, certifique-se de ter as seguintes ferramentas instaladas:

- **Docker**: [Documentação de instalação do Docker](https://docs.docker.com/get-docker/)
- **Docker Compose**: [Documentação de instalação do Docker Compose](https://docs.docker.com/compose/install/)
- **JDK 23**: [Download JDK 23](https://jdk.java.net/23/)
- **Apache Maven 3.9.9**: [Download Maven 3.9.9](https://maven.apache.org/download.cgi)

## Como Executar o Projeto

### Passo 1: Subir o Docker

Primeiro, você precisa subir o backend com o Docker. No terminal, dentro do diretório do seu projeto, execute o seguinte comando:

```bash
docker-compose up -d
```
Esse comando irá iniciar os containers necessários para o funcionamento do backend.

### Passo 2: Configurar o Ambiente
Certifique-se de que o JDK 23 e o Maven 3.9.9 estão instalados corretamente no seu sistema.

### Passo 3: Compilar e Executar o Projeto

Abra o terminal e execute o seguinte comando para compilar o projeto:
```bash
mvn clean install
```
Após a compilação bem-sucedida, execute o projeto com:
```bash
mvn spring-boot:run
```

Agora, a aplicação estará em execução no endereço http://localhost:8080

### Passo 4: Testar a API

Você pode testar a API através da documentação interativa gerada pelo Swagger, acessível no seguinte link:

```bash
http://localhost:8080/swagger-ui/index.html#/
```
Aqui você poderá visualizar todos os endpoints da API e testá-los diretamente na interface.

# Essa Aplicação Precisa de Melhorias na Logica e nos  Endpoints.
## Licença

[MIT](https://choosealicense.com/licenses/mit/)

