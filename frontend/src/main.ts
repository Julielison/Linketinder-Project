import renderFormsCandidate from "@/components/formsCandidate";
import renderFormsCompany from "@/components/formsCompany";
import renderFormsStruture from "./components/formsStruture";
import renderToggleSection from "./components/toggleSelection";

document.addEventListener("DOMContentLoaded", () => {
    const app = document.getElementById("app");

    if (app) {
        app.innerHTML = renderFormsStruture("Registre-se");

        const container = document.getElementById("container");

        if (container) {
            container.insertAdjacentElement('afterbegin', renderToggleSection());

            const containerForm = document.createElement("div");
            containerForm.id = "containerForm";
            container.appendChild(containerForm);

            renderSelectedForm("candidate");
        }
    }

    const candidateBtn = document.getElementById("candidate");
    const businessBtn = document.getElementById("business");

    function toggleSelection(selected: HTMLElement, unselected: HTMLElement) {
        selected.classList.add("selected");
        unselected.classList.remove("selected");
    }

    function renderSelectedForm(type: "candidate" | "business") {
        const containerForm = document.getElementById("containerForm");
        if (!containerForm) return;

        // Remove apenas o último formulário, se existir
        if (containerForm.lastElementChild) {
            containerForm.removeChild(containerForm.lastElementChild);
        }

        // Adiciona o novo formulário
        if (type === "candidate") {
            containerForm.appendChild(renderFormsCandidate("Registrar"));
        } else {
            containerForm.appendChild(renderFormsCompany("Registrar"));
        }
    }

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
});
