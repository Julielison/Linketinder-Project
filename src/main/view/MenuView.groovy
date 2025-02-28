package main.view

class MenuView {
    static void showMenu() {
        println "\n=== Menu ==="
        println "1. Listar Candidatos"
        println "2. Listar Empresas"
        println "3. Adicionar Empresa"
        println "4. Adicionar Candidato"
        println "0. Sair"
        print "Escolha uma opção: "
    }

    static void showPessoas(List pessoas, String tipoPessoa) {
        println "\n--- Lista de ${tipoPessoa} ---"
        if (pessoas.isEmpty()) {
            println "Nenhum ${tipoPessoa} cadastrado."
        } else {
            pessoas.each { println it }
        }
    }

    static int getUserInput() {
        try {
            return System.in.newReader().readLine() as int
        } catch (Exception ignored) {
            return -1
        }
    }

    static void showExitMessage() {
        println "\nEncerrando o sistema..."
    }

    static void showInvalidOption() {
        println "\nOpção inválida. Tente novamente."
    }
    static void showFeedbackInsercao(String tipoPessoa){
        println "${tipoPessoa} cadastrado(a) com sucesso "
    }

    static Map<String, ?> getEmpresaInput() {
        println "\n--- Cadastro de Empresa ---"
        print "Nome: "
        String nome = System.in.newReader().readLine()

        print "Email: "
        String email = System.in.newReader().readLine()

        print "CNPJ: "
        String cnpj = System.in.newReader().readLine()

        print "País: "
        String pais = System.in.newReader().readLine()

        print "Estado: "
        String estado = System.in.newReader().readLine()

        print "CEP: "
        String cep = System.in.newReader().readLine()

        print "Descrição: "
        String descricao = System.in.newReader().readLine()

        print "Competências (separadas por vírgula): "
        List<String> competencias = System.in.newReader().readLine().split(",").collect { it.trim() }

        return [
                nome: nome,
                email: email,
                cnpj: cnpj,
                pais: pais,
                estado: estado,
                cep: cep,
                descricao: descricao,
                competencias: competencias
        ]
    }

    static Map<String, ?> getCandidatoInput() {
        println "\n--- Cadastro de Candidato ---"
        print "Nome: "
        String nome = System.in.newReader().readLine()

        print "Email: "
        String email = System.in.newReader().readLine()

        print "Cpf: "
        String cpf = System.in.newReader().readLine()

        String idade
        while (true) {
            print "Idade: "
            try {
                idade = System.in.newReader().readLine()
                if (idade.isInteger()) {
                    break
                } else {
                    println "Por favor, insira um número válido."
                }
            } catch (Exception e) {
                println "Erro ao ler a idade: ${e.message}"
            }
        }

        print "Estado: "
        String estado = System.in.newReader().readLine()

        print "Cep: "
        String cep = System.in.newReader().readLine()

        print "Descrição: "
        String descricao = System.in.newReader().readLine()

        print "Competências (separadas por vírgula): "
        List<String> competencias = System.in.newReader().readLine().split(",").collect { it.trim() }

        return [
                nome: nome,
                email: email,
                competencias: competencias,
                cpf: cpf,
                descricao: descricao,
                cep: cep,
                idade: idade,
                estado: estado
        ]
    }
}
