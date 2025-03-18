import { Company } from "@/interfaces/ICompany";
import renderCompanyPage from "@/pages/companyPage";
import { mockCompanies } from "@/dataMocked/companiesMock";
import { validateCEP, validateCNPJ, validateCompanyName, validateCountry, validateDescription, validateEmail, validateSkills, validateState } from "@/utils/validations";
import { setupInputValidation, validateAllFields } from "@/utils/validateSubmit";

export default function renderFormsCompany(nameButtonSubmit: string): HTMLFormElement {
    const form = document.createElement("form");
    form.id = "form";
  
    form.innerHTML = /*html*/`
          <label class="name company">Nome da Empresa: 
              <input type="text" name="company_name" placeholder="Digite o nome da empresa" required />
              <span class="error-message">Nome inválido. Deve conter pelo menos 2 caracteres.</span>
          </label>
          <label class="email">Email: 
              <input type="email" name="email" placeholder="Digite o email da empresa" required />
              <span class="error-message">Email inválido.</span>
          </label>
          <label>Competências (separadas por vírgula): 
              <input type="text" name="skills" placeholder="Ex: JavaScript, Marketing, Design" required />
              <span class="error-message">Competências inválidas.</span>
          </label>
          <label>Descrição da Empresa: 
              <textarea name="company_description" placeholder="Descreva a empresa brevemente" required></textarea>
              <span class="error-message">Descrição inválida. Deve conter entre 10 e 500 caracteres.</span>
          </label>
          <div class="inline-group-company">
              <label>CNPJ: 
                  <input type="text" name="cnpj" placeholder="00.000.000/0000-00" required />
                  <span class="error-message">CNPJ inválido. Use o formato 00.000.000/0000-00</span>
              </label>
              <label>CEP: 
                  <input type="text" name="cep" placeholder="00000-000" required />
                  <span class="error-message">CEP inválido. Deve conter exatamente 8 dígitos.</span>
              </label>
          </div>
          <div class="inline-group-company">
              <label>País: 
                  <input type="text" name="country" placeholder="Digite o país" required />
                  <span class="error-message">País inválido. Deve conter entre 2 e 50 caracteres.</span>
              </label>
              <label>Estado: 
                  <input type="text" name="state" placeholder="Digite o estado" required />
                  <span class="error-message">Estado inválido. Deve conter entre 2 e 50 caracteres.</span>
              </label>
          </div>
          <div class="form-actions">
              <button class="submit" type="submit">${nameButtonSubmit}</button>
          </div>
    `;

    const validationRules = [
        { name: 'company_name', validator: validateCompanyName, errorMsg: "O nome da empresa deve ter pelo menos 2 caracteres!", tag: 'input' },
        { name: 'email', validator: validateEmail, errorMsg: "Email inválido", tag: 'input' },
        { name: 'skills', validator: validateSkills, errorMsg: "As competências devem ser separadas por vírgula!", tag: 'input' },
        { name: 'company_description', validator: validateDescription, errorMsg: "A descrição deve conter entre 10 e 500 caracteres!", tag: 'textarea' },
        { name: 'cnpj', validator: validateCNPJ, errorMsg: "O CNPJ deve seguir o formato 00.000.000/0000-00", tag: 'input' },
        { name: 'cep', validator: validateCEP, errorMsg: "O CEP deve conter exatamente 8 dígitos!", tag: 'input' },
        { name: 'country', validator: validateCountry, errorMsg: "O país deve ter entre 2 e 50 caracteres!", tag: 'input' },
        { name: 'state', validator: validateState, errorMsg: "O estado deve ter entre 2 e 50 caracteres!", tag: 'input' }
    ];
    
    setupInputValidation(form, validationRules);

    form.addEventListener("submit", (event) => {
        event.preventDefault();
        
        if (!validateAllFields(form, validationRules)) {
            return;
        }

        const formData = new FormData(form);
        const companyData: Record<string, string> = {};

        formData.forEach((value, key) => {
            companyData[key] = value.toString();
        });

        const company: Company = {
            id: Date.now(), // Gerar um ID único
            name: companyData.company_name,
            email: companyData.email,
            cnpj: companyData.cnpj,
            cep: companyData.cep,
            expectedSkills: companyData.skills.split(",").map((skill) => skill.trim()),
            state: companyData.state,
            country: companyData.country,
            description: companyData.company_description,
            vacancies: [],
        };

        mockCompanies.push(company);

        renderCompanyPage();
    });
  
    return form;
}
