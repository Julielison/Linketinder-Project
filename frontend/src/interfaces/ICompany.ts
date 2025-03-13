import { Vacancy } from "./IVacancy";

export interface Company {
    id: number;
    name: string;
    email: string;
    cnpj: string;
    cep: string;
    expectedSkills: string[];
    state: string;
    country: string;
    description: string;
    vacancies: Vacancy[];
  }
  