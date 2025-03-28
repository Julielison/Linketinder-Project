package org.linketinder.model

class Empresa extends Pessoa {
    String cnpj
    String estado
    List<Vaga> vagas

    Empresa(Integer id,
            String nome,
            String email,
            String cnpj,
            String cep,
            String descricao,
            String senhaLogin,
            String paisOndeReside,
            List<Vaga> vagas)
    {
        super(id, nome, email, cep, descricao, senhaLogin, paisOndeReside)
        this.cnpj = cnpj
        this.vagas = vagas
    }

    @Override
    String toString() {
        return super.toString() + """
        CNPJ: ${cnpj}
        Vagas: ${vagas}
        """
    }
}