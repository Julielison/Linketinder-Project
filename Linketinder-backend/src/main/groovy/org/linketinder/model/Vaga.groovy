package org.linketinder.model

class Vaga {
    Integer id
    String nome
    String descricao
    String local
    Integer idEmpresa
    List<Competencia> competencias

    Vaga(Integer id,
         String nome,
         String descricao,
         String local,
         Integer idEmpresa,
         List<Competencia> competencias)
    {
        this.id = id
        this.nome = nome
        this.descricao = descricao
        this.local = local
        this.idEmpresa = idEmpresa
        this.competencias = competencias
    }

    @Override
    String toString() {
        return """
            Nome da Vaga: ${nome}
            Descrição: ${descricao}
            Local: ${local}
            Id empresa: ${idEmpresa}
            Competências exigidas: ${competencias}"""
    }

}