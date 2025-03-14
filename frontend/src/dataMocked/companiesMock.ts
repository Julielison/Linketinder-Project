import { Company } from "../interfaces/ICompany";
import { Vacancy } from "../interfaces/IVacancy";

// Array vazio de vagas para uso inicial
const emptyVacancies: Vacancy[] = [];


export const mockCompanies: Company[] = [
  {
    id: 101,
    name: "Tech Solutions",
    email: "contato@techsolutions.com.br",
    cnpj: "12.345.678/0001-90",
    cep: "01310-100",
    expectedSkills: ["JavaScript", "React", "Node.js", "MongoDB"],
    description: "Empresa especializada em soluções web e mobile para startups e empresas de médio porte.",
    state: "SP",
    country: "Brasil",
    vacancies: emptyVacancies
  },
  {
    id: 2,
    name: "Inovação Digital",
    email: "rh@inovacaodigital.com.br",
    cnpj: "23.456.789/0001-01",
    cep: "22031-000",
    expectedSkills: ["Java", "Spring Boot", "PostgreSQL", "Docker"],
    description: "Desenvolvemos sistemas corporativos escaláveis com foco em performance e segurança.",
    state: "RJ",
    country: "Brasil",
    vacancies: emptyVacancies
  },
  {
    id: 3,
    name: "WebDev Masters",
    email: "contato@webdevmasters.com",
    cnpj: "34.567.890/0001-12",
    cep: "30130-110",
    expectedSkills: ["HTML", "CSS", "JavaScript", "Vue.js"],
    description: "Agência especializada em desenvolvimento de interfaces web de alta qualidade e UX impecável.",
    state: "MG",
    country: "Brasil",
    vacancies: emptyVacancies
  },
  {
    id: 4,
    name: "Cloud Systems",
    email: "jobs@cloudsystems.com.br",
    cnpj: "45.678.901/0001-23",
    cep: "91020-000",
    expectedSkills: ["AWS", "Kubernetes", "Docker", "Terraform"],
    description: "Especialistas em infraestrutura cloud, migração e gerenciamento de ambientes complexos.",
    state: "RS",
    country: "Brasil",
    vacancies: emptyVacancies
  },
  {
    id: 5,
    name: "Data Insights",
    email: "contato@datainsights.com.br",
    cnpj: "56.789.012/0001-34",
    cep: "80240-000",
    expectedSkills: ["Python", "SQL", "Power BI", "Excel"],
    description: "Fornecemos insights através de análise de dados para auxiliar na tomada de decisões estratégicas.",
    state: "PR",
    country: "Brasil",
    vacancies: emptyVacancies
  },
  {
    id: 6,
    name: "Creative Solutions",
    email: "contato@creativesolutions.com.br",
    cnpj: "67.890.123/0001-45",
    cep: "88015-000",
    expectedSkills: ["Figma", "Adobe XD", "Sketch", "UI/UX"],
    description: "Design de interfaces e experiência do usuário para aplicações mobile e web.",
    state: "SC",
    country: "Brasil",
    vacancies: emptyVacancies
  },
  {
    id: 7,
    name: "SecureTech",
    email: "contato@securetech.com.br",
    cnpj: "78.901.234/0001-56",
    cep: "70070-010",
    expectedSkills: ["Pentest", "OWASP", "Gestão de riscos", "SOC"],
    description: "Especialistas em segurança da informação, proteção de dados e compliance.",
    state: "DF",
    country: "Brasil",
    vacancies: emptyVacancies
  },
  {
    id: 8,
    name: "Sistemas Corporativos",
    email: "contato@sistemascorporativos.com.br",
    cnpj: "89.012.345/0001-67",
    cep: "04543-000",
    expectedSkills: ["Microserviços", "Cloud Architecture", "Java", "AWS"],
    description: "Desenvolvimento de sistemas empresariais de alta performance e escalabilidade.",
    state: "SP",
    country: "Brasil",
    vacancies: emptyVacancies
  },
  {
    id: 9,
    name: "AppFactory",
    email: "contato@appfactory.com.br",
    cnpj: "90.123.456/0001-78",
    cep: "88015-200",
    expectedSkills: ["React Native", "Kotlin", "Swift", "Firebase"],
    description: "Especialistas em desenvolvimento de aplicativos mobile nativos e multiplataforma.",
    state: "SC",
    country: "Brasil",
    vacancies: emptyVacancies
  },
  {
    id: 10,
    name: "Quality Systems",
    email: "contato@qualitysystems.com.br",
    cnpj: "01.234.567/0001-89",
    cep: "90430-000",
    expectedSkills: ["Testes automatizados", "Selenium", "Cypress", "JIRA"],
    description: "Serviços de qualidade de software, testes e garantia de qualidade para empresas de todos os portes.",
    state: "RS",
    country: "Brasil",
    vacancies: emptyVacancies
  }
];