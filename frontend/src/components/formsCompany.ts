export default function renderFormsCompany(nameButtonSubmit: string): HTMLFormElement {
  const form = document.createElement("form");
  form.id = "form";

  form.innerHTML = /*html*/`
      <div class="inline-group">
          <label class="name">Nome da Empresa: <input type="text" name="company_name" required /></label>
          <label>CNPJ: <input type="text" name="cnpj" required /></label>
      </div>
      <label class="email">Email: <input type="email" name="email" required /></label>
      <label>Descrição da Empresa: <textarea name="company_description" required></textarea></label>
      <label>Competências (separadas por vírgula): <input type="text" name="skills" required /></label>
      <fieldset>
          <legend>Endereço da Empresa</legend>
          <div class="inline-group">
              <label>CEP: <input type="text" name="cep" required /></label>
              <label>País: <input type="text" name="country" required /></label>
              <label>Estado: <input type="text" name="state" required /></label>
              <label>Cidade: <input type="text" name="city" required /></label>
              <label>Rua: <input type="text" name="street" required /></label>
          </div>
      </fieldset>
      <div class="form-actions">
          <button class="submit" type="submit">${nameButtonSubmit}</button>
      </div>
  `;

  return form;
}
