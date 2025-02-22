package view

class MenuView {
    def showMenu() {
        println "\n=== Menu ==="
        println "1. Listar Candidatos"
        println "2. Listar Empresas"
        println "3. Sair"
        print "Escolha uma opção: "
    }

    def showCandidatos(List candidatos) {
        println "\n--- Lista de Candidatos ---"
        candidatos.each { println it }
    }

    def showEmpresas(List empresas) {
        println "\n--- Lista de Empresas ---"
        empresas.each { println it }
    }

    def getUserInput() {
        return System.in.newReader().readLine() as int
    }

    def showExitMessage() {
        println "\nEncerrando o sistema..."
    }

    def showInvalidOption() {
        println "\nOpção inválida. Tente novamente."
    }
}
