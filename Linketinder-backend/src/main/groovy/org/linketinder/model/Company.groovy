package org.linketinder.model

class Company extends Person {
    String cnpj
    List<Job> jobs

    Company(Integer id,
            String name,
            String email,
            String cnpj,
            Address address,
            String description,
            String passwordLogin,
            List<Job> jobs)
    {
        super(id, name, email, address, description, passwordLogin)
        this.cnpj = cnpj
        this.jobs = jobs
    }

    @Override
    String toString() {
        return super.toString() + """
        CNPJ: ${cnpj}
        Vagas: ${jobs}
        """
    }
}