export default function renderFormsCandidate(nameButtonSubmit: string): HTMLElement {
  const form = document.createElement('form');
    
  form.id = 'form';
  
  form.innerHTML = /*html*/`
          <div class="inline-group">
            <label class="name">Nome: <input type="text" name="name" required /></label>
            <label>Idade: <input type="number" name="age" id="age" /></label>
          </div>
          <label class="email">Email: <input type="email" name="email" required /></label>
          <label>Competências (separadas por vírgula): <input type="text" name="skills" required /></label>
          <label>Descrição Pessoal: <textarea name="description" required></textarea></label>
          <fieldset>
            <legend>Formação Acadêmica</legend>
            <label>Curso - Instituição <input type="text" name="formation" required /></label>
            <div class="inline-group date">
              <label>Data de Início: <input type="date" name="start_date" required /></label>
              <label>Data de Fim: <input type="date" name="end_date" required /></label>
            </div>
          </fieldset>
          <label>CPF: <input type="text" name="cpf" required /></label>
          <div class="inline-group">
            <label>CEP: <input type="text" name="cep" required /></label>
            <label>País: <input type="text" name="country" required /></label>
            <label>Estado: <input type="text" name="state" required /></label>
          </div>
          <div class="form-actions">
          <button class="submit" type="submit">${nameButtonSubmit}</button>
          </div>
    `;

    return form;
}

