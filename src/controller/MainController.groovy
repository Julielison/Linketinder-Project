package controller

import model.Dados
import view.MenuView

class MainController {
    MenuView view = new MenuView()

    void executar() {
        boolean sair = false
        while (!sair) {
            view.showMenu()
            int opcao = view.getUserInput()
            switch(opcao) {
                case 1:
                    view.showCandidatos(Dados.candidatos)
                    break
                case 2:
                    view.showEmpresas(Dados.empresas)
                    break
                case 0:
                    view.showExitMessage()
                    sair = true
                    break
                default:
                    view.showInvalidOption()
            }
        }
    }
}
