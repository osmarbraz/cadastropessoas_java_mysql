# Cadastro de Pessoas – Java + MySQL

## 1. Descrição do Projeto

Este projeto demonstra a integração entre a linguagem Java e o banco de dados relacional MySQL, utilizando o Maven para gerenciamento de dependências.

A aplicação implementa um sistema simples de cadastro de pessoas, permitindo armazenar o nome de cada pessoa e realizar operações de inclusão, consulta, atualização e exclusão de registros no MySQL.

O objetivo é demonstrar na prática as operações CRUD (*Create, Read, Update e Delete*) utilizando JDBC (*Java Database Connectivity*) para estabelecer a comunicação entre a aplicação Java e o banco de dados.

## 2. Tecnologias Utilizadas

- **Java:** linguagem de programação utilizada no desenvolvimento.
- **Maven:** ferramenta para gerenciamento de dependências e construção do projeto.
- **MySQL:** sistema gerenciador de banco de dados relacional.
- **JDBC:** API utilizada para estabelecer a comunicação entre Java e o banco de dados.
- **MySQL Connector/J:** driver JDBC utilizado para conectar a aplicação Java ao MySQL.

## 3. Pré-requisitos

Antes de executar o projeto, é necessário possuir:

- JDK instalado (recomendado Java 8 ou superior).
- Apache Maven instalado.
- MySQL Server instalado e em execução.
- Usuário e senha configurados para acesso ao MySQL.
- MySQL configurado para aceitar conexões em `localhost:3306`.

## 4. Dependência Maven

O projeto utiliza o driver oficial MySQL Connector/J para Java.

Adicione a seguinte dependência ao arquivo `pom.xml`:

```
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.3.0</version>
</dependency>
```

## 5. Estrutura do Projeto

A organização recomendada para o projeto Maven é:

```
CadastroMySQL/
│
├── pom.xml
├── .gitignore
├── README.md
├── LICENSE
└── src/
    └── main/
        └── java/
            └── Principal.java
```

## 6. Configuração da Conexão

A aplicação estabelece conexão com o MySQL utilizando JDBC.

As configurações de conexão utilizadas são:

```
private static final String URL_SERVIDOR = "jdbc:mysql://localhost:3306/";
private static final String URL_BANCO = "jdbc:mysql://localhost:3306/pessoadb";
private static final String USUARIO = "root";
private static final String SENHA = "root";
```

O banco de dados e a tabela utilizados são:

```
CREATE DATABASE IF NOT EXISTS pessoadb;

USE pessoadb;

CREATE TABLE IF NOT EXISTS pessoa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL
);
```

- Banco de dados: `pessoadb`
- Tabela: `pessoa`
- Chave primária: `id`
- Campo de armazenamento: `nome`

O identificador `id` é gerado automaticamente pelo MySQL por meio da propriedade `AUTO_INCREMENT`.

A aplicação possui um método responsável por criar o banco de dados e a tabela caso ainda não existam.

**Observação:** os valores de usuário e senha devem ser ajustados de acordo com a configuração do MySQL instalado no ambiente.

## 7. Funcionalidades do Sistema

A aplicação apresenta um menu interativo no terminal com as seguintes operações:

| Opção | Funcionalidade          | Operação SQL       |
| ----- | ----------------------- | ------------------ |
| 1     | Cadastrar pessoa        | `INSERT`           |
| 2     | Listar pessoas          | `SELECT`           |
| 3     | Consultar pessoa por ID | `SELECT ... WHERE` |
| 4     | Atualizar pessoa        | `UPDATE`           |
| 5     | Excluir pessoa          | `DELETE`           |
| 9     | Encerrar programa       | —                  |

### 7.1. Cadastrar Pessoa

Permite inserir uma nova pessoa na tabela `pessoa`.

```
INSERT INTO pessoa (nome) 
VALUES (?);
```

A aplicação utiliza `PreparedStatement` para executar o comando, substituindo o parâmetro `?` pelo nome informado pelo usuário.

O MySQL gera automaticamente o identificador da pessoa por meio do campo `id`.

### 7.2. Listar Pessoas

Consulta todos os registros cadastrados na tabela `pessoa`, apresentando os resultados em ordem crescente pelo campo `nome`.

```
SELECT id, nome
FROM pessoa
ORDER BY nome ASC;
```

A aplicação utiliza `ResultSet` para percorrer os registros retornados pela consulta.

### 7.3. Consultar Pessoa por ID

Permite localizar um registro utilizando seu identificador `id`.

```
SELECT id, nome
FROM pessoa
WHERE id = ?;
```

O comando `WHERE` restringe a consulta ao registro que possui o identificador informado pelo usuário.

### 7.4. Atualizar Pessoa

Permite modificar o nome de uma pessoa já cadastrada.

```
UPDATE pessoa
SET nome = ?
WHERE id = ?;
```

A atualização utiliza o comando SQL `UPDATE`, modificando somente o campo `nome` do registro correspondente ao identificador informado.

### 7.5. Excluir Pessoa

Permite excluir um registro pelo identificador `id`, solicitando confirmação do usuário antes da operação.

```
DELETE FROM pessoa
WHERE id = ?;
```

A aplicação utiliza `PreparedStatement` para executar a exclusão e verificar a quantidade de registros afetados.

## 8. Executando o Projeto

**Passo 1 – Iniciar o MySQL**

Verifique se o servidor MySQL está em execução e disponível na porta `3306`.

**Passo 2 – Configurar o acesso ao banco**

Confira as configurações de conexão no código-fonte:

- URL do servidor;
- URL do banco de dados;
- Usuário;
- Senha.

Ajuste os valores conforme a configuração do MySQL instalado.

**Passo 3 – Abrir o projeto**

Abra o projeto em uma IDE.

**Passo 4 – Compilar o projeto**

No terminal, dentro do diretório do projeto, execute:

```
mvn clean compile
```

**Passo 5 – Executar o programa**

Caso o projeto esteja configurado com o plugin Maven Exec, execute:

```
mvn exec:java -Dexec.mainClass="Principal"
```

Na primeira execução, a aplicação verificará a existência do banco de dados e da tabela, criando-os caso necessário.

## 9. Exemplo de Utilização

Ao executar o programa, será apresentado o seguinte menu:

```
=== MENU CADASTRO DE PESSOAS ===
1 - Cadastrar pessoa
2 - Listar pessoas
3 - Consultar pessoa por ID
4 - Atualizar pessoa
5 - Excluir pessoa
9 - Sair

Escolha uma opcao:
```

Após cadastrar uma pessoa, o MySQL armazenará um registro semelhante a:

| id | nome |
| -- | ---- |
| 1  | Joao |

O campo `id` é gerado automaticamente pelo MySQL e utilizado para identificar cada registro da tabela.

## 10. Conceitos Trabalhados

Com este projeto, são explorados os seguintes conceitos:

- Integração entre Java e MySQL.
- Gerenciamento de dependências com Maven.
- Banco de dados relacional.
- Criação de bancos de dados e tabelas com SQL.
- Chaves primárias e propriedade `AUTO_INCREMENT`.
- Conexão com banco de dados utilizando JDBC.
- Operações CRUD.
- Inserção e consulta de registros.
- Ordenação de resultados com `ORDER BY`.
- Filtragem de registros com `WHERE`.
- Atualização de registros com `UPDATE`.
- Exclusão de registros com `DELETE`.
- Utilização de `PreparedStatement`.
- Recuperação de resultados com `ResultSet`.
- Tratamento de exceções com `SQLException`.

## 11. Considerações Finais

O projeto apresenta uma aplicação prática de integração entre Java e MySQL, permitindo compreender como uma aplicação pode realizar operações de persistência de dados em um banco de dados relacional.

A implementação demonstra as principais operações de manipulação de dados utilizando JDBC e comandos SQL, servindo como ponto de partida para o desenvolvimento de aplicações mais completas com Java, Maven e MySQL.
