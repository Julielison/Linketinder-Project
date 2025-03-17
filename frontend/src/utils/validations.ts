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
    const cepRegex = /^d{8}$/;
    return cepRegex.test(cep);
}

export function validateAge(age: number): boolean {
    return age >= 16 ? true : false;
}

export function validateNameCandidate(name: string): boolean {
    const nameRegex = /^[a-zA-Z]+ [a-zA-Z]+$/;
    return nameRegex.test(name);
}

export function validateSkills(skills: string): boolean {
    const skillsArray = skills.split(',').map(skill => skill.trim());
    return skillsArray.length > 0 && skillsArray.every(skill => skill.length > 0);
}

export function validateLinkedIn(linkedin: string): boolean {
    const linkedinRegex = /^https:\/\/(www\.)?linkedin\.com\/in\/[a-zA-Z0-9-]+\/?$/;
    return linkedinRegex.test(linkedin);
}