import { Vacancy } from "../interfaces/Vacancy";

export const mockJobs: Vacancy[] = [
  {
    id: 1,
    title: "Desenvolvedor Full Stack",
    companyName: "Tech Solutions",
    location: "Remoto",
    skills: ["JavaScript", "React", "Node.js", "MongoDB"],
    match: {
      candidateId: null,
      companyId: 1
    }
  },
  {
    id: 2,
    title: "Engenheiro de Software Backend",
    companyName: "Inovação Digital",
    location: "São Paulo",
    skills: ["Java", "Spring Boot", "PostgreSQL", "Docker"],
    match: {
      candidateId: null,
      companyId: 2
    }
  },
  {
    id: 3,
    title: "Desenvolvedor Frontend",
    companyName: "WebDev Masters",
    location: "Remoto",
    skills: ["HTML", "CSS", "JavaScript", "Vue.js"],
    match: {
      candidateId: 3,
      companyId: 3
    }
  },
  {
    id: 4,
    title: "DevOps Engineer",
    companyName: "Cloud Systems",
    location: "Rio de Janeiro",
    skills: ["AWS", "Kubernetes", "Docker", "CI/CD", "Terraform"],
    match: {
      candidateId: null,
      companyId: 4
    }
  },
  {
    id: 5,
    title: "Analista de Dados",
    companyName: "Data Insights",
    location: "Belo Horizonte",
    skills: ["Python", "SQL", "Power BI", "Excel"],
    match: {
      candidateId: null,
      companyId: 5
    }
  },
  {
    id: 6,
    title: "UX/UI Designer",
    companyName: "Creative Solutions",
    location: "Remoto",
    skills: ["Figma", "Adobe XD", "Sketch", "Pesquisa de usuário"],
    match: {
      candidateId: 6,
      companyId: 6
    }
  },
  {
    id: 7,
    title: "Analista de Segurança da Informação",
    companyName: "SecureTech",
    location: "Brasília",
    skills: ["Pentest", "OWASP", "Gestão de riscos", "SOC"],
    match: {
      candidateId: null,
      companyId: 7
    }
  },
  {
    id: 8,
    title: "Arquiteto de Software",
    companyName: "Sistemas Corporativos",
    location: "São Paulo",
    skills: ["Microserviços", "Cloud Architecture", "Java", "AWS"],
    match: {
      candidateId: 8,
      companyId: 8
    }
  },
  {
    id: 9,
    title: "Desenvolvedor Mobile",
    companyName: "AppFactory",
    location: "Florianópolis",
    skills: ["React Native", "Kotlin", "Swift", "Firebase"],
    match: {
      candidateId: null,
      companyId: 9
    }
  },
  {
    id: 10,
    title: "QA Tester",
    companyName: "Quality Systems",
    location: "Porto Alegre",
    skills: ["Testes automatizados", "Selenium", "Cypress", "JIRA"],
    match: {
      candidateId: 10,
      companyId: 10
    }
  }
];