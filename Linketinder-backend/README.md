# Linketinder
## Dev
- Julielison Lima

## Descrição
Este projeto é um sistema simples desenvolvido em Groovy para gerenciar candidatos, empresas e vagas. Ele permite listar candidatos, empresas e vagas cadastradas, além de possibilitar interações como "curtidas" entre candidatos e empresas, por meio de um menu interativo no terminal.

## Tecnologias Utilizadas
- **Linguagem:** Groovy
- **Paradigma:** Orientação a Objetos
- **Banco de Dados:** PostgreSQL

#Vou atualizar a seção "Estrutura do Projeto" do README do backend com base na nova estrutura de pastas e arquivos que você forneceu. Aqui está o conteúdo atualizado:

## Estrutura do Projeto
O projeto está dividido da seguinte forma:

### 1. **Controller**
- Contém os controladores que recebem requisições HTTP e gerenciam o fluxo do programa

### 2. **Model**
- Contém as classes que representam as entidades do sistema

### 3. **DAO (Data Access Object)**
- **interfaces**: Define os contratos para acesso a dados
- **impl**: Implementações das interfaces de acesso a dados
- **connection**: Gerencia a conexão com o banco de dados PostgreSQL

### 4. **Service**
- Contém a lógica de negócio do sistema

### 5. **View**
- Gerencia a interface do usuário via terminal

### 6. **Config**
- Contém classes de configuração do sistema

### 7. **Util**
- Contém classes utilitárias

### 8. **Enums**
- `MenuOption.groovy`: Enumeração para as opções do menu do sistema

### 9. **Main**
- `Main.groovy`: Ponto de entrada da aplicação
- `System.groovy`: Gerencia o fluxo principal do sistema
****
## Modelagem do Banco de Dados

### Modelo Entidade-Relacionamento (MER)
![Modelo Entidade-Relacionamento](docs/dataBase/ModeloConceitual/MER_LinkeTinder.png)

### Modelo Lógico (ML)
![Modelo Lógico](docs/dataBase/ModeloLógico/ML_LinkeTinder.png)

### Modelo Físico (MF)
O banco de dados foi implementado com base no modelo físico abaixo:

[Modelo Físico](docs/dataBase/Modelo_Físico_LinkeTinder.sql)

### Principais Tabelas
- **CANDIDATO:** Armazena informações sobre os candidatos, como nome, sobrenome, data de nascimento, email, CPF, descrição pessoal, endereço e competências.
- **EMPRESA:** Armazena informações sobre as empresas, como nome, CNPJ, email corporativo, descrição e endereço.
- **VAGA:** Armazena informações sobre as vagas de emprego, como nome, descrição, local e empresa responsável.
- **COMPETENCIA:** Lista as competências que podem ser associadas a candidatos e exigidas por vagas.
- **FORMACAO:** Armazena informações sobre formações acadêmicas ou profissionais dos candidatos.
- **Tabelas Associativas:** 
  - `CANDIDATO_CURTE_VAGA`: Relaciona candidatos que curtiram vagas.
  - `EMPRESA_CURTE_CANDIDATO`: Relaciona empresas que curtiram candidatos.
  - `CANDIDATO_COMPETENCIA`: Relaciona candidatos com suas competências.
  - `VAGA_COMPETENCIA`: Relaciona vagas com as competências exigidas.
  - `FORMACAO_CANDIDATO`: Relaciona candidatos com suas formações.


## Como Executar
### Requisitos
- **Java 17**
- **Groovy 4.0.14**

### Passos
1. Clone este repositório:
   ```sh
   git clone https://github.com/Julielison/Linketinder-Project.git
   cd Linketinder-Project/backend/src
   ```
2. Criar o banco:
    - Abra o pgAdmin e crie um database com o código do modelo físico.
   
3. Crie um arquivo .env na pasta resources com seus dados de conexão com o banco, por exemplo:
    ```txt
    DB_URL=jdbc:postgresql://localhost:5432/linketinder
    DB_USER=postgres
    DB_PASSWORD=teste
    DB_DRIVER=org.postgresql.Driver
    ```

4. Execute o sistema com a task run do plugin do gradle no intellij

## Endpoints disponíneis
### Root: localhost:8080
### Candidatos
1. **GET** `/candidates`: Listagem de candidatos cadastrados.
2. **DELETE**`/candidates/{id}`: remove um candidato
3. **POST** `/candidates`: adiciona um candidato com base no conteúdo do body
   - Exemplo:
   ```json
   {
    "firstName": "João",
    "lastName": "Silva",
    "email": "112111qqaq1w1çé2.1silva@email.com",
    "cpf": "10310521210",
    "dateBirth": "1990-05-10",
    "zipCode": "12345678",
    "description": "Desenvolvedor de software com experiência em Java e Spring.",
    "skills": [
    "Java",
    "Spring Boot",
    "REST APIs",
    "SQL"
    ],
    "password": "senha123",
    "country": "Brasil",
    "formations": [
    {
    "name": "Engenharia de Software",
    "institution": "Universidade de São Paulo",
    "dateStart": "2010-02-15",
    "dateEnd": "2014-12-15"
    },
    {
    "name": "Machine Learning",
    "institution": "Coursera",
    "dateStart": "2020-01-01",
    "dateEnd": "2020-06-01"
    }
    ]
    }
   ```
### Empresas
1. **GET**`/companies`: lista os dados de todas as empresas
2. **DELETE**`/companies/{id}`: remove uma empresa com base no id
3. **POST**`/companies`: adcione uma empresa com base no conteúdo do body
   - Exemplo:
   ```json
   {
    "name": "Tech Solutions Brasil",
    "email": "contsaqt2o2@techsolutionsbrasil.com.br",
    "cnpj": "12325631909234",
    "country": "Brasil",
    "zipCode": "01234567",
    "description": "Empresa especializada em soluções de tecnologia para o mercado de trabalho",
    "password": "senhaSegura123"
    }
   ```
4. **POST** `/companies/{id}/jobs`: adiciona uma vaga com base no conteúdo do body
   - Exemplo
   ```json
    {
        "name": "Desenvolvedor Backend",
        "description": "Responsável por desenvolver e manter APIs e serviços backend.",
        "local": "São Paulo, SP",
        "skills": [
            "Java",
            "Spring Boot",
            "SQL",
            "Git",
            "Docker"
        ]
    }
    ```

### Vagas
1. **GET**`/jobs`: lista os dados de todas as vagas
2. **DELETE**`/jobs/{id}`: remove uma vaga com base no id