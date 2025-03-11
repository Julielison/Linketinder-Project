import renderCandidateProfile from "./profileCandidate";

export default function renderFormsCandidate(nameButtonSubmit: string): HTMLElement {
  const form = document.createElement('form');
  form.id = 'form';

  form.innerHTML = /*html*/`
      <div class="inline-group">
        <label class="name">Nome: 
            <input type="text" name="name" placeholder="Digite seu nome completo" required />
        </label>
        <label>Idade: 
            <input type="number" name="age" id="age" placeholder="Ex: 25" required />
        </label>
      </div>
      <label class="email">Email: 
          <input type="email" name="email" placeholder="Digite seu email" required />
      </label>
      <label>Competências (separadas por vírgula): 
          <input type="text" name="skills" placeholder="Ex: Python, React, Inglês fluente" required />
      </label>
      <label>Descrição Pessoal: 
          <textarea name="description" placeholder="Fale um pouco sobre você" required></textarea>
      </label>
      <fieldset>
        <legend>Formação Acadêmica</legend>
        <label>Curso - Instituição 
            <input type="text" name="formation" placeholder="Ex: Ciência da Computação - UFSP" required />
        </label>
        <div class="inline-group date">
          <label>Data de Início: 
              <input type="date" name="start_date" required />
          </label>
          <label>Data de Fim: 
              <input type="date" name="end_date" required />
          </label>
        </div>
      </fieldset>
      <label>CPF: 
          <input type="text" name="cpf" placeholder="000.000.000-00" required />
      </label>
      <div class="inline-group">
        <label>CEP: 
            <input type="text" name="cep" placeholder="00000-000" required />
        </label>
        <label>País: 
            <input type="text" name="country" placeholder="Digite seu país" required />
        </label>
        <label>Estado: 
            <input type="text" name="state" placeholder="Digite seu estado" required />
        </label>
      </div>
      <div class="form-actions">
      <button class="submit" type="submit">${nameButtonSubmit}</button>
      </div>
  `;

  form.addEventListener("submit", (event) => {
      event.preventDefault();

      // Capturar os dados do formulário
      const formData = new FormData(form);
      const userData: Record<string, string> = {};

      formData.forEach((value, key) => {
          userData[key] = value.toString();
      });

      // Salvar os dados no localStorage
      localStorage.setItem("userData", JSON.stringify(userData));

      // Atualizar a URL sem recarregar a página
      history.pushState({}, "", "?page=perfilCandidato");

      const main = document.getElementById("app")

      if (main) {
        main.innerHTML = '';
        main.appendChild(renderCandidateProfile());
      }

  });

  return form;
}
