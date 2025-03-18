import { Company } from "@/interfaces/ICompany";
import renderCompanyPage from "@/pages/companyPage";
import { mockCompanies } from "@/dataMocked/companiesMock";
import { validateCEP, validateCNPJ, validateCompanyName, validateCountry, validateDescription, validateEmail, validateSkills, validateState } from "@/utils/validations";
import { removeFeedbackError, showFeedbackError } from "@/utils/errorFeedback";

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

    let formValid: boolean = false;
    const setupInputValidation = (form: HTMLFormElement) => {
        const setupValidation = (
            nameInput: string, 
            validationFn: (value: any) => boolean, 
            errorMessage: string,
            tag: string = 'input'
        ) => {
            const input = form.querySelector(`${tag}[name="${nameInput}"]`) as HTMLInputElement | HTMLTextAreaElement;
            input?.addEventListener('input', () => {
                const value = input.value;
                
                if (!validationFn(value)) {
                    showFeedbackError(input, errorMessage);
                    formValid = false;
                } else {
                    removeFeedbackError(input);
                    formValid = true;
                }
            });
        };

        setupValidation('company_name', validateCompanyName, "O nome da empresa deve ter pelo menos 2 caracteres!");
        setupValidation('email', validateEmail, "Email inválido");
        setupValidation('skills', validateSkills, "As competências devem ser separadas por vírgula!");
        setupValidation('company_description', validateDescription, "A descrição deve conter entre 10 e 500 caracteres!", "textarea");
        setupValidation('cnpj', validateCNPJ, "O CNPJ deve seguir o formato 00.000.000/0000-00");
        setupValidation('cep', validateCEP, "O CEP deve conter exatamente 8 dígitos!");
        setupValidation('country', validateCountry, "O país deve ter entre 2 e 50 caracteres!");
        setupValidation('state', validateState, "O estado deve ter entre 2 e 50 caracteres!");

        const requiredInputs = form.querySelectorAll('input[required], textarea[required]');
        requiredInputs.forEach(input => {
            input.addEventListener('invalid', (event) => {
                const element = event.target as HTMLInputElement;
                if (element.validity.valueMissing) {
                    element.setCustomValidity(`Este campo é obrigatório.`);
                }
            });
            
            input.addEventListener('input', (event) => {
                const element = event.target as HTMLInputElement;
                element.setCustomValidity('');
            });
        });
    };
    
    setupInputValidation(form);

    form.addEventListener("submit", (event) => {
        event.preventDefault();
        if (!formValid) {
            return;
        }

        // Capturar os dados do formulário
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
