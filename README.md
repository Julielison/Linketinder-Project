# Linketinder

## Descrição
Este projeto é um sistema simples desenvolvido em Groovy para gerenciar candidatos e empresas. Ele permite listar candidatos e empresas cadastradas por meio de um menu interativo no terminal.

## Tecnologias Utilizadas
- **Linguagem:** Groovy
- **Paradigma:** Orientação a Objetos

## Estrutura do Projeto
O projeto é dividido em três camadas principais:

### 1. **Controller**
- `MainController.groovy`: Controla a interação do usuário com o sistema e gerencia o fluxo do programa.

### 2. **Model**
- `Pessoa.groovy`: Classe abstrata que representa uma pessoa.
- `Candidato.groovy`: Representa um candidato com informações como nome, email, CPF, idade, estado, CEP, descrição e competências.
- `Empresa.groovy`: Representa uma empresa com informações como nome, email, CNPJ, país, estado, CEP, descrição e competências.
- `Dados.groovy`: Armazena dados fictícios de candidatos e empresas.
- `PessoaInterface.groovy`: Interface para garantir a implementação dos métodos `getNome()` e `getEmail()`.

### 3. **View**
- `MenuView.groovy`: Gerencia a interface do usuário, exibindo o menu e as listas de candidatos e empresas.

## Como Executar
### Requisitos
- Ter o **Groovy** instalado no sistema.

### Passos
1. Clone este repositório:
   ```sh
   git clone https://github.com/Julielison/Linketinder-Project.git
   cd Linketinder-Project
   ```
2. Execute o sistema com o seguinte comando:
   ```sh
   groovy MainController.groovy
   ```

## Funcionalidades
- Listagem de candidatos cadastrados.
- Listagem de empresas cadastradas.
- Interação via terminal com menu interativo.

## Exemplo de Uso
```sh
=== Menu ===
1. Listar Candidatos
2. Listar Empresas
3. Sair
Escolha uma opção: 1

--- Lista de Candidatos ---
Candidato: Carlos Silva | Email: carlos@gmail.com | CPF: 12345678900 | Idade: 30 | Estado: SP | CEP: 01000-000 | Descrição: Desenvolvedor Full Stack | Competências: Java, Spring, Angular
...
```

## Devs
- Julielison Lima
