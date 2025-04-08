package org.linketinder.model

class Empresa extends Pessoa {
    String cnpj
    List<Vaga> vagas

    Empresa(Integer id,
            String nome,
            String email,
            String cnpj,
            Address address,
            String descricao,
            String senhaLogin,
            List<Vaga> vagas)
    {
        super(id, nome, email, address, descricao, senhaLogin)
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