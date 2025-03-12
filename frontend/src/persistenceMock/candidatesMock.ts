import { Candidate } from "../interfaces/Candidate";

export const mockCandidates: Candidate[] = [
  {
    id: 1,
    name: "João Silva",
    email: "joao.silva@email.com",
    cpf: "123.456.789-00",
    age: 28,
    skills: ["JavaScript", "React", "Node.js"],
    formation: {
      educationalInstitution: "Universidade de São Paulo - Ciência da Computação",
      startDate: new Date("2015-03-01"),
      endDate: new Date("2019-12-15")
    },
    description: "Desenvolvedor Full Stack com experiência em aplicações web modernas e APIs RESTful.",
    isMatched: false
  },
  {
    id: 2,
    name: "Maria Oliveira",
    email: "maria.oliveira@email.com",
    cpf: "987.654.321-00",
    age: 25,
    skills: ["Java", "Spring", "PostgreSQL", "AWS"],
    formation: {
      educationalInstitution: "PUC-Rio - Engenharia de Software",
      startDate: new Date("2016-02-15"),
      endDate: new Date("2020-11-30")
    },
    description: "Engenheira de software apaixonada por arquitetura e desenvolvimento backend.",
    isMatched: false
  },
  {
    id: 3,
    name: "Pedro Santos",
    email: "pedro.santos@email.com",
    cpf: "456.789.123-00",
    age: 31,
    skills: ["HTML", "CSS", "JavaScript", "Vue.js", "UI/UX"],
    formation: {
      educationalInstitution: "UFMG - Design Digital",
      startDate: new Date("2012-03-10"),
      endDate: new Date("2016-12-20")
    },
    description: "Frontend developer com foco em experiência do usuário e interfaces responsivas.",
    isMatched: true
  },
  {
    id: 4,
    name: "Ana Costa",
    email: "ana.costa@email.com",
    cpf: "321.654.987-00",
    age: 29,
    skills: ["Python", "Django", "SQL", "Data Science"],
    formation: {
      educationalInstitution: "UFRJ - Ciência de Dados",
      startDate: new Date("2014-03-05"),
      endDate: new Date("2018-07-10")
    },
    description: "Cientista de dados com experiência em análise e visualização de dados.",
    isMatched: false
  },
  {
    id: 5,
    name: "Carlos Mendes",
    email: "carlos.mendes@email.com",
    cpf: "789.123.456-00",
    age: 35,
    skills: ["DevOps", "AWS", "Docker", "Kubernetes", "Terraform"],
    formation: {
      educationalInstitution: "Unicamp - Engenharia da Computação",
      startDate: new Date("2010-02-20"),
      endDate: new Date("2015-06-15")
    },
    description: "Especialista em infraestrutura cloud e CI/CD com foco em automação.",
    isMatched: false
  },
  {
    id: 6,
    name: "Juliana Lima",
    email: "juliana.lima@email.com",
    cpf: "654.321.987-00",
    age: 27,
    skills: ["UX Research", "UI Design", "Figma", "Adobe XD"],
    formation: {
      educationalInstitution: "Mackenzie - Design de Interfaces",
      startDate: new Date("2016-03-10"),
      endDate: new Date("2020-12-05")
    },
    description: "Designer de UX/UI com foco em pesquisa de usuários e prototipação.",
    isMatched: true
  },
  {
    id: 7,
    name: "Rafael Almeida",
    email: "rafael.almeida@email.com",
    cpf: "234.567.890-00",
    age: 33,
    skills: ["C#", ".NET", "SQL Server", "Azure"],
    formation: {
      educationalInstitution: "UFPE - Sistemas de Informação",
      startDate: new Date("2011-02-15"),
      endDate: new Date("2015-12-20")
    },
    description: "Desenvolvedor .NET com experiência em aplicações corporativas e cloud.",
    isMatched: false
  },
  {
    id: 8,
    name: "Fernanda Souza",
    email: "fernanda.souza@email.com",
    cpf: "876.543.210-00",
    age: 30,
    skills: ["Java", "Microserviços", "Spring Cloud", "Kafka"],
    formation: {
      educationalInstitution: "USP - Engenharia de Computação",
      startDate: new Date("2013-03-01"),
      endDate: new Date("2018-12-15")
    },
    description: "Arquiteta de software especializada em sistemas distribuídos e alta disponibilidade.",
    isMatched: true
  },
  {
    id: 9,
    name: "Bruno Oliveira",
    email: "bruno.oliveira@email.com",
    cpf: "345.678.901-00",
    age: 26,
    skills: ["React Native", "Flutter", "Kotlin", "Swift"],
    formation: {
      educationalInstitution: "UFSC - Ciência da Computação",
      startDate: new Date("2015-02-20"),
      endDate: new Date("2019-12-10")
    },
    description: "Desenvolvedor mobile multiplataforma com experiência em apps nativos e híbridos.",
    isMatched: false
  },
  {
    id: 10,
    name: "Camila Ferreira",
    email: "camila.ferreira@email.com",
    cpf: "567.890.123-00",
    age: 28,
    skills: ["Testes automatizados", "Selenium", "Cypress", "JUnit"],
    formation: {
      educationalInstitution: "UFRGS - Engenharia de Software",
      startDate: new Date("2014-03-15"),
      endDate: new Date("2019-07-20")
    },
    description: "Engenheira de qualidade especializada em automação de testes e garantia de qualidade.",
    isMatched: true
  }
];