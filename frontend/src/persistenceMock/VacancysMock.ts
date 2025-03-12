import { Vacancy } from "../interfaces/Vacancy";

export const mockJobs: Vacancy[] = [
  {
    id: 1,
    title: "Desenvolvedor Full Stack",
    companyName: "Tech Solutions",
    location: "Remoto",
    skills: ["JavaScript", "React", "Node.js", "MongoDB"],
    likedByCandidatesId: [1], // O candidato de id 1 curtiu essa vaga
    idCompany: 1
  },
  {
    id: 2,
    title: "Engenheiro de Software Backend",
    companyName: "Inovação Digital",
    location: "São Paulo",
    skills: ["Java", "Spring Boot", "PostgreSQL", "Docker"],
    likedByCandidatesId: [],
    idCompany: 2
  },
  {
    id: 3,
    title: "Desenvolvedor Frontend",
    companyName: "WebDev Masters",
    location: "Remoto",
    skills: ["HTML", "CSS", "JavaScript", "Vue.js"],
    likedByCandidatesId: [],
    idCompany: 3
  },
  {
    id: 4,
    title: "DevOps Engineer",
    companyName: "Cloud Systems",
    location: "Rio de Janeiro",
    skills: ["AWS", "Kubernetes", "Docker", "CI/CD", "Terraform"],
    likedByCandidatesId: [],
    idCompany: 4
  },
  {
    id: 5,
    title: "Analista de Dados",
    companyName: "Data Insights",
    location: "Belo Horizonte",
    skills: ["Python", "SQL", "Power BI", "Excel"],
    likedByCandidatesId: [],
    idCompany: 5
  },
  {
    id: 6,
    title: "UX/UI Designer",
    companyName: "Creative Solutions",
    location: "Remoto",
    skills: ["Figma", "Adobe XD", "Sketch", "Pesquisa de usuário"],
    likedByCandidatesId: [],
    idCompany: 6
  },
  {
    id: 7,
    title: "Analista de Segurança da Informação",
    companyName: "SecureTech",
    location: "Brasília",
    skills: ["Pentest", "OWASP", "Gestão de riscos", "SOC"],
    likedByCandidatesId: [],
    idCompany: 7
  },
  {
    id: 8,
    title: "Arquiteto de Software",
    companyName: "Sistemas Corporativos",
    location: "São Paulo",
    skills: ["Microserviços", "Cloud Architecture", "Java", "AWS"],
    likedByCandidatesId: [],
    idCompany: 8
  },
  {
    id: 9,
    title: "Desenvolvedor Mobile",
    companyName: "AppFactory",
    location: "Florianópolis",
    skills: ["React Native", "Kotlin", "Swift", "Firebase"],
    likedByCandidatesId: [],
    idCompany: 9
  },
  {
    id: 10,
    title: "QA Tester",
    companyName: "Quality Systems",
    location: "Porto Alegre",
    skills: ["Testes automatizados", "Selenium", "Cypress", "JIRA"],
    likedByCandidatesId: [],
    idCompany: 10
  }
];