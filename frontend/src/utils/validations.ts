export function validateEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

export function validateCPF(cpf: string): boolean {
    const cpfRegex = /^\d{11}$/;
    return cpfRegex.test(cpf);
}

export function validateCNPJ(cnpj: string): boolean {
    const cnpjRegex = /^\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}$/;
    return cnpjRegex.test(cnpj);
}

export function validateCEP(cep: string): boolean {
    const cepRegex = /^\d{8}$/;
    return cepRegex.test(cep);
}

export function validateAge(age: string): boolean {
    return parseInt(age) >= 16 ? true : false;
}

export function validateNameCandidate(name: string): boolean {
    const nameRegex = /^[a-zA-Z]+ [a-zA-Z]+$/;
    return nameRegex.test(name);
}

export function validateSkills(skills: string): boolean {
    const skillsRegex = /^\w+(, \w+)*$/;
    return skillsRegex.test(skills);
}

export function validateLinkedIn(linkedin: string): boolean {
    const linkedinRegex = /^https:\/\/(www\.)?linkedin\.com\/in\/[a-zA-Z0-9-]+\/?$/;
    return linkedinRegex.test(linkedin);
}

export function validateNumber(number: string): boolean {
    const numberRegex = /^(\d{11})$/;
    return numberRegex.test(number.toString());
}

export function validateDescription(description: string): boolean {
    return description.length >= 10 && description.length <= 500;
}

export function validateCountry(country: string): boolean {
    return country.length >= 2 && country.length <= 50;
}

export function validateState(state: string): boolean {
    return state.length >= 2 && state.length <= 50;
}

export function validateCompanyName(value: string): boolean {
    return value.length >= 2;
}