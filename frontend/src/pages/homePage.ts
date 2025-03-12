import renderFormsCandidate from "@/components/formsCandidate";
import renderFormsCompany from "@/components/formsCompany";
import renderFormsStruture from "@/components/formsStruture";
import renderToggleSection from "@/components/toggleSelection";


export function renderHomePage() {
    initializePageStructure();
    
    setupInitialForm();
    
    setupEventListeners();
}

/**
 * Inicializa a estrutura básica da página
 */
function initializePageStructure() {
    const app = document.getElementById("app");
    
    if (app) {
        app.innerHTML = renderFormsStruture("Registre-se");
        
        const container = document.getElementById("container");
        
        if (container) {
            container.insertAdjacentElement('afterbegin', renderToggleSection());
            
            const containerForm = document.createElement("div");
            containerForm.id = "containerForm";
            container.appendChild(containerForm);
        }
    }
}

/**
 * Configura o formulário inicial (candidato por padrão)
 */
function setupInitialForm() {
    renderSelectedForm("candidate");
}

/**
 * Configura os ouvintes de eventos para os botões de alternância
 */
function setupEventListeners() {
    const candidateBtn = document.getElementById("candidate");
    const businessBtn = document.getElementById("business");
    
    candidateBtn?.addEventListener("click", () => {
        if (candidateBtn && businessBtn) {
            toggleSelection(candidateBtn, businessBtn);
            renderSelectedForm("candidate");
        }
    });
    
    businessBtn?.addEventListener("click", () => {
        if (candidateBtn && businessBtn) {
            toggleSelection(businessBtn, candidateBtn);
            renderSelectedForm("business");
        }
    });
}

/**
 * Alterna a seleção visual entre os botões
 */
function toggleSelection(selected: HTMLElement, unselected: HTMLElement) {
    selected.classList.add("selected");
    unselected.classList.remove("selected");
}

/**
 * Renderiza o formulário selecionado
 */
function renderSelectedForm(type: "candidate" | "business") {
    const containerForm = document.getElementById("containerForm");
    if (!containerForm) return;
    
    clearCurrentForm(containerForm);
    renderForm(containerForm, type);
}

/**
 * Limpa o formulário atual
 */
function clearCurrentForm(container: HTMLElement) {
    if (container.lastElementChild) {
        container.removeChild(container.lastElementChild);
    }
}

/**
 * Renderiza o formulário apropriado com base no tipo
 */
function renderForm(container: HTMLElement, type: "candidate" | "business") {
    if (type === "candidate") {
        container.appendChild(renderFormsCandidate("Registrar"));
    } else {
        container.appendChild(renderFormsCompany("Registrar"));
    }
}