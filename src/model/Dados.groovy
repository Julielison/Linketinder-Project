package model

class Dados {
    static List<Candidato> candidatos = []
    static List<Empresa> empresas = []

    static {
        candidatos = [
                new Candidato("Carlos Silva", "carlos@gmail.com", "12345678900", 30, "SP", "01000-000", "Desenvolvedor Full Stack", ["Java", "Spring", "Angular"]),
                new Candidato("Ana Souza", "ana@gmail.com", "09876543211", 25, "RJ", "20000-000", "Analista de Sistemas", ["Python", "Django"]),
                new Candidato("Mariana Costa", "mari@gmail.com", "11122233344", 28, "MG", "30000-000", "Engenheira de Software", ["Java", "Groovy", "Angular"]),
                new Candidato("João Pereira", "joao@gmail.com", "55566677788", 35, "RS", "90000-000", "Arquiteto de Soluções", ["Python", "Flask", "JavaScript"]),
                new Candidato("Fernanda Lima", "fernanda@gmail.com", "99988877766", 32, "SC", "88000-000", "DevOps Engineer", ["Docker", "Kubernetes", "AWS"])
        ]

        empresas = [
                new Empresa("Arroz-Gostoso", "contato@arrozgostoso.com", "12345678000199", "Brasil", "SP", "01001-000", "Empresa de alimentos", ["Python", "Java"]),
                new Empresa("Império do Boliche", "rh@imperiodoboliche.com", "98765432000155", "Brasil", "RJ", "20001-000", "Empresa de entretenimento", ["Angular", "Spring"]),
                new Empresa("Tech Solutions", "contato@techsolutions.com", "11223344000177", "Brasil", "MG", "30001-000", "Empresa de tecnologia", ["Groovy", "Java"]),
                new Empresa("Innovatech", "rh@innovatech.com", "22334455000188", "Brasil", "RS", "90001-000", "Startup de inovação", ["Python", "Django"]),
                new Empresa("FutureWorks", "info@futureworks.com", "33445566000199", "Brasil", "SC", "88001-000", "Consultoria de TI", ["AWS", "Docker"])
        ]
    }
}
