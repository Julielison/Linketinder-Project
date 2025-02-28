package repository

import model.Empresa

class EmpresaRepository {
    static private List<Empresa> empresas = []
    static {
        empresas = [
                new Empresa("Arroz-Gostoso", "contato@arrozgostoso.com", "12345678000199", "Brasil", "SP", "01001-000", "Empresa de alimentos", ["Python", "Java"]),
                new Empresa("Império do Boliche", "rh@imperiodoboliche.com", "98765432000155", "Brasil", "RJ", "20001-000", "Empresa de entretenimento", ["Angular", "Spring"]),
                new Empresa("Tech Solutions", "contato@techsolutions.com", "11223344000177", "Brasil", "MG", "30001-000", "Empresa de tecnologia", ["Groovy", "Java"]),
                new Empresa("Innovatech", "rh@innovatech.com", "22334455000188", "Brasil", "RS", "90001-000", "Startup de inovação", ["Python", "Django"]),
                new Empresa("FutureWorks", "info@futureworks.com", "33445566000199", "Brasil", "SC", "88001-000", "Consultoria de TI", ["AWS", "Docker"])
        ]
    }

    static void addEmpresa(Empresa empresa) {
        empresas.add(empresa)
    }

    static List<Empresa> getEmpresas(){
        return empresas
    }

}
