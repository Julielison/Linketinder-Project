# LinkeTinder - Frontend

## Descrição
Este projeto é a parte frontend do sistema LinkeTinder, desenvolvido em TypeScript utilizando o Vite como bundler. Ele fornece uma interface web interativa para candidatos e empresas se registrarem, visualizarem perfis, vagas e candidatos.

## Tecnologias Utilizadas
- **Linguagem:** TypeScript
- **Build tool:** Vite
- **Bibliotecas:** Chart.js

## Estrutura do Projeto
O projeto está organizado da seguinte forma:

### Componentes
- `formsCandidate.ts`: Formulário para registro de candidatos.
- `formsCompany.ts`: Formulário para registro de empresas.
- `profileCandidate.ts`: Perfil do candidato.
- `profileCompany.ts`: Perfil da empresa.
- `jobComponent.ts`: Componente para exibir vagas de emprego.
- `candidateComponent.ts`: Componente para exibir candidatos.
- `buttonStatistics.ts`: Botão para exibir estatísticas.
- `modalFormsCandidate.ts`: Modal para editar perfil de candidato.
- `modalFormsCompany.ts`: Modal para editar perfil de empresa.
- `modalStatistics.ts`: Modal para exibir estatísticas.

### Páginas
- `homePage.ts`: Página inicial com formulários de registro.
- `candidatePage.ts`: Página de perfil do candidato.
- `companyPage.ts`: Página de perfil da empresa.
- `jobsPage.ts`: Página de listagem de vagas.
- `candidatesPage.ts`: Página de listagem de candidatos.

### Estilos
- `default.css`: Estilos globais.
- `forms.css`: Estilos para formulários.
- `jobs.css`: Estilos para listagem de vagas.
- `pageProfiles.css`: Estilos para perfis.
- `candidates.css`: Estilos para listagem de candidatos.

### Mock de Dados
- `candidatesMock.ts`: Dados fictícios de candidatos.
- `companiesMock.ts`: Dados fictícios de empresas.
- `vacanciesMock.ts`: Dados fictícios de vagas.

## Como Executar
### Requisitos
- **Node.js** (versão 18 ou superior)
- **npm** (versão 9 ou superior)

### Passos
1. Clone este repositório:
   ```sh
   git clone https://github.com/Julielison/Linketinder-Project.git
   cd Linketinder-Project/frontend
    ```
2. Instale as dependências:
    ```bash
    npm install
    ```
3. Execute o projeto:
    ```bash
    npm run dev
    ```
## Funcionalidades
- Registro e listagem de candidatos.
- Registro e listagem de empresas.
- Interface web interativa.
- Exibição de estatísticas de habilidades dos candidatos.