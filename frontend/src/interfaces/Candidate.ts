export interface Candidate {
    id: number;
    name: string;
    email: string;
    cpf: string;
    age: number;
    skills: string[];
    formation: {
        educationalInstitution: string,
        startDate: Date,
        endDate: Date
    };
    description: string;
    isMatched: boolean;
  }
  