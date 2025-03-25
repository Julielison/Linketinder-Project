package org.linketinder.view


import org.linketinder.model.Pessoa

import java.text.SimpleDateFormat

class MenuView {
    static void showMenu() {
        println "\n=== Menu ==="
        println "1. Listar dados dos candidatos"
        println "2. Listar dados das Empresas"
        println "3. Adicionar Empresa"
        println "4. Obter dados de uma empresa"
        println "5. Atualizar dados de uma empresa"
        println "6. Remover Empresa"
        println "7. Adicionar Candidato"
        println "8. Remover dados de um candidato"
        println "9. Atualizar candidato"
        println "10. Remover candidato"
        println "11. Listar vagas"
        println "12. Listar todas as competências"
        println "0. Sair"
        print "Escolha uma opção: "
    }

    static void showPessoas(List<Pessoa> pessoas, String tipoPessoa) {
        println "\n--- Lista de ${tipoPessoa} ---"
        if (pessoas.isEmpty()) {
            println "Nenhum ${tipoPessoa} cadastrado."
        } else {
            pessoas.each {Pessoa it -> println it }
        }
    }

    static int getUserInput() {
        try {
            String input = System.in.newReader().readLine()
            if (input == null || input.trim().isEmpty()) {
                println "Entrada vazia. Por favor, digite um número."
                return -1
            }
            return input.trim().toInteger()
        } catch (NumberFormatException ignored) {
            println "Por favor, digite apenas números."
            return -1
        } catch (Exception e) {
            println "Erro ao ler a opção: ${e.message}"
            return -1
        }
    }

    static void showExitMessage() {
        println "\nEncerrando o sistema..."
    }

    static void showInvalidOption() {
        println "\nOpção inválida. Tente novamente."
    }
    static void showFeedbackInsercao(String feedback){
        println feedback
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

        print "CEP: "
        String cep = System.in.newReader().readLine()

        print "Descrição: "
        String descricao = System.in.newReader().readLine()

        print "Senha de login: "
        String senha = System.in.newReader().readLine()

        return [
                nome: nome,
                email: email,
                cnpj: cnpj,
                pais: pais,
                cep: cep,
                descricao: descricao,
                senha: senha
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

        String dataNascimento = getInputData("Data de Nascimento (dd/mm/aaaa): ")

        print "Cep: "
        String cep = System.in.newReader().readLine()

        print "Descrição: "
        String descricao = System.in.newReader().readLine()

        print "Competências (separadas por vírgula): "
        List<String> competencias = System.in.newReader().readLine().split(",").collect { it.trim() }

        print("Senha de login: ")
        String senha = System.in.newReader().readLine()

        print "País onde reside: "
        String pais = System.in.newReader().readLine()

        List<Map<String, String>> formacoes = new ArrayList<>()

        boolean adicionar = true
        while (adicionar){
            println "Formações :"
            println "1. Adicionar "
            println "2. Pular"
            String opcao = System.in.newReader().readLine()
            switch (opcao){
                case "1":
                    formacoes.add(getFormacaoInput())
                    break
                case "2":
                    adicionar = false
                    break
                default:
                    showInvalidOption()
            }
        }

        return [
                nome: nome,
                email: email,
                competencias: competencias,
                cpf: cpf,
                descricao: descricao,
                cep: cep,
                dataNascimento: dataNascimento,
                formacoes: formacoes
        ]
    }

    static Map<String, ?> getFormacaoInput(){
        print "Nome da formação: "
        String nome = System.in.newReader().readLine()

        print "Nome da instituição: "
        String instituicao = System.in.newReader().readLine()

        Date dadtaInicio = getInputData("Data de início: ")
        Date dataFim = getInputData("Data de fim/previsão: ")

        return [
                nome: nome,
                instituicao: instituicao,
                dataIncio: dadtaInicio,
                dataFim: dataFim
        ]
    }

    static Date getInputData(String label){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy")
        Date data = null

        while (true) {
            print label
            try {
                String dataStr = System.in.newReader().readLine()
                if (dataStr ==~ /\d{2}\/\d{2}\/\d{4}/) {
                    data = dateFormat.parse(dataStr)
                    break
                } else {
                    println "Por favor, insira uma data válida no formato dd/mm/aaaa."
                }
            } catch (Exception e) {
                println "Erro ao ler a data: ${e.message}"
            }
        }
        return data
    }
}
