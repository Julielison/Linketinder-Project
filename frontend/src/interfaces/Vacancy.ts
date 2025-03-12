export interface Vacancy {
    id: number;
    title: string;
    companyName: string;
    location: string;
    skills: string[];
    likedByCandidatesId: number[]
    idCompany: number;
  }