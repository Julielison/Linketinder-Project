import { showFeedbackError, removeFeedbackError } from "@/utils/errorFeedback";

interface ValidationRule {
    name: string;
    validator: (value: any) => boolean;
    errorMsg: string;
    tag?: string;
}


function setupRequiredFieldsValidation(form: HTMLFormElement): void {
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
}


function validateRequiredFields(form: HTMLFormElement, firstInvalidField: HTMLInputElement | HTMLTextAreaElement | null): {
    isValid: boolean;
    firstInvalidField: HTMLInputElement | HTMLTextAreaElement | null;
} {
    let isValid = true;
    let updatedFirstInvalidField = firstInvalidField;
    
    const requiredInputs = form.querySelectorAll('input[required], textarea[required]');
    requiredInputs.forEach(input => {
        const element = input as HTMLInputElement | HTMLTextAreaElement;
        if (!element.value.trim()) {
            showFeedbackError(element, "Este campo é obrigatório.");
            isValid = false;
            
            if (!updatedFirstInvalidField) {
                updatedFirstInvalidField = element;
            }
        }
    });

    return { isValid, firstInvalidField: updatedFirstInvalidField };
}

export function setupInputValidation(form: HTMLFormElement, validationRules: ValidationRule[]): void {
    for (const rule of validationRules) {
        const tag = rule.tag || 'input';
        const input = form.querySelector(`${tag}[name="${rule.name}"]`) as HTMLInputElement | HTMLTextAreaElement;
        
        if (!input) continue;
        
        input.addEventListener('input', () => {
            const value = input.value;
            
            if (!rule.validator(value)) {
                showFeedbackError(input, rule.errorMsg);
            } else {
                removeFeedbackError(input);
            }
        });
    }

    setupRequiredFieldsValidation(form);
}

export function validateAllFields(form: HTMLFormElement, validationRules: ValidationRule[]): boolean {
    let isValid = true;
    let firstInvalidField: HTMLInputElement | HTMLTextAreaElement | null = null;

    for (const rule of validationRules) {
        const tag = rule.tag || 'input';
        const input = form.querySelector(`${tag}[name="${rule.name}"]`) as HTMLInputElement | HTMLTextAreaElement;
        if (!input) continue;

        const value = input.value;
        if (!rule.validator(value)) {
            showFeedbackError(input, rule.errorMsg);
            isValid = false;
            
            if (!firstInvalidField) {
                firstInvalidField = input;
            }
        }
    }

    const requiredFieldsValidation = validateRequiredFields(form, firstInvalidField);
    isValid = isValid && requiredFieldsValidation.isValid;
    firstInvalidField = requiredFieldsValidation.firstInvalidField;

    if (firstInvalidField) {
        firstInvalidField.focus();
        firstInvalidField.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }

    return isValid;
}