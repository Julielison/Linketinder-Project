package repository

import model.Candidato

class CandidatoRepository {
    static private List<Candidato> candidatos = []
    static {
        candidatos = [
                new Candidato("Carlos Silva", "carlos@gmail.com", "12345678900", 30, "SP", "01000-000", "Desenvolvedor Full Stack", ["Java", "Spring", "Angular"]),
                new Candidato("Ana Souza", "ana@gmail.com", "09876543211", 25, "RJ", "20000-000", "Analista de Sistemas", ["Python", "Django"]),
                new Candidato("Mariana Costa", "mari@gmail.com", "11122233344", 28, "MG", "30000-000", "Engenheira de Software", ["Java", "Groovy", "Angular"]),
                new Candidato("João Pereira", "joao@gmail.com", "55566677788", 35, "RS", "90000-000", "Arquiteto de Soluções", ["Python", "Flask", "JavaScript"]),
                new Candidato("Fernanda Lima", "fernanda@gmail.com", "99988877766", 32, "SC", "88000-000", "DevOps Engineer", ["Docker", "Kubernetes", "AWS"])
        ]
    }

    static void addCandidato(Candidato candidato) {
        candidatos.add(candidato)
    }

    static List<Candidato> getCandidatos(){
        return candidatos
    }
}
