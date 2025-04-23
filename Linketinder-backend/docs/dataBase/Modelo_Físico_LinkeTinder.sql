-- Tabela de países de residência
CREATE TABLE PAIS_DE_RESIDENCIA (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(30) UNIQUE NOT NULL
);

-- Tabela de endereços
CREATE TABLE ENDERECO (
    id SERIAL PRIMARY KEY,
    cep CHAR(8) UNIQUE NOT NULL,           -- CEP sem formatação: 8 dígitos
    pais_id INTEGER NOT NULL,
    CONSTRAINT FK_PAIS_ENDERECO FOREIGN KEY (pais_id)
        REFERENCES PAIS_DE_RESIDENCIA (id)
);

-- Tabela de candidatos
CREATE TABLE CANDIDATO (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    sobrenome VARCHAR(50) NOT NULL,
    data_nascimento DATE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    cpf CHAR(11) UNIQUE NOT NULL,          -- CPF sem formatação: 11 dígitos
    descricao_pessoal VARCHAR(255) NULL,
    senha_de_login VARCHAR(256) NOT NULL,
    id_endereco INTEGER,
    CONSTRAINT FK_ENDERECO_CANDIDATO FOREIGN KEY (id_endereco)
        REFERENCES ENDERECO (id)
);

-- Tabela de empresas
CREATE TABLE EMPRESA (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cnpj CHAR(14) UNIQUE NOT NULL,         -- CNPJ sem formatação: 14 dígitos
    email_corporativo VARCHAR(100) NOT NULL,
    descricao_da_empresa VARCHAR(255) NOT NULL,
    senha_de_login VARCHAR(256) NOT NULL,
    id_endereco INTEGER,
    CONSTRAINT fk_endereco_empresa FOREIGN KEY (id_endereco)
        REFERENCES ENDERECO (id)
);

-- Tabela de vagas
CREATE TABLE VAGA (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    local VARCHAR(100) NOT NULL,
    id_empresa INTEGER NOT NULL,
    CONSTRAINT fk_empresa_vaga FOREIGN KEY (id_empresa)
        REFERENCES EMPRESA (id)
);

-- Tabela de competências
CREATE TABLE COMPETENCIA (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) UNIQUE NOT NULL
);

-- Tabela de formações
CREATE TABLE FORMACAO (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    instituicao VARCHAR(100) NOT NULL
);

-- Tabela associativa: candidato curte vaga
CREATE TABLE CANDIDATO_CURTE_VAGA (
    id_candidato INTEGER,
    id_vaga INTEGER,
    PRIMARY KEY (id_candidato, id_vaga),
    CONSTRAINT fk_candidato_curte_vaga_candidato FOREIGN KEY (id_candidato)
        REFERENCES CANDIDATO (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_candidato_curte_vaga_vaga FOREIGN KEY (id_vaga)
        REFERENCES VAGA (id)
        ON DELETE CASCADE
);


-- Tabela associativa: empresa curte candidato
CREATE TABLE EMPRESA_CURTE_CANDIDATO (
    id_candidato INTEGER,
    id_empresa INTEGER,
    PRIMARY KEY (id_candidato, id_empresa),
    CONSTRAINT FK_EMPRESA_CURTE_CANDIDATO_CANDIDATO FOREIGN KEY (id_candidato)
        REFERENCES CANDIDATO (id)
        ON DELETE CASCADE,
    CONSTRAINT FK_EMPRESA_CURTE_CANDIDATO_EMPRESA FOREIGN KEY (id_empresa)
        REFERENCES EMPRESA (id)
        ON DELETE CASCADE
);

-- Tabela associativa: candidato possui competência
CREATE TABLE CANDIDATO_COMPETENCIA (
    id_candidato INTEGER,
    id_competencia INTEGER,
    PRIMARY KEY (id_candidato, id_competencia),
    CONSTRAINT FK_CANDIDATO_COMPETENCIA_CANDIDATO FOREIGN KEY (id_candidato)
        REFERENCES CANDIDATO (id)
        ON DELETE CASCADE,
    CONSTRAINT FK_CANDIDATO_COMPETENCIA_COMPETENCIA FOREIGN KEY (id_competencia)
        REFERENCES COMPETENCIA (id)
        ON DELETE CASCADE
);

-- Tabela associativa: vaga requer competência
CREATE TABLE VAGA_COMPETENCIA (
    id_vaga INTEGER,
    id_competencia INTEGER,
    PRIMARY KEY (id_vaga, id_competencia),
    CONSTRAINT FK_VAGA_COMPETENCIA_COMPETENCIA FOREIGN KEY (id_competencia)
        REFERENCES COMPETENCIA (id)
        ON DELETE CASCADE,
    CONSTRAINT FK_VAGA_COMPETENCIA_VAGA FOREIGN KEY (id_vaga)
        REFERENCES VAGA (id)
        ON DELETE CASCADE
);

-- Tabela associativa: candidato possui formação
CREATE TABLE FORMACAO_CANDIDATO (
    id_formacao INTEGER,
    id_candidato INTEGER,
    data_inicio DATE NOT NULL,
    data_fim_previsao DATE NOT NULL,
    PRIMARY KEY (id_formacao, id_candidato),
    CONSTRAINT FK_FORMACAO_CANDIDATO_FORMACAO FOREIGN KEY (id_formacao)
        REFERENCES FORMACAO (id)
        ON DELETE CASCADE,
    CONSTRAINT FK_FORMACAO_CANDIDATO_CANDIDATO FOREIGN KEY (id_candidato)
        REFERENCES CANDIDATO (id)
        ON DELETE CASCADE
);



-- Inserções para a tabela PAIS_DE_RESIDENCIA
INSERT INTO PAIS_DE_RESIDENCIA (nome) VALUES ('Brasil');
INSERT INTO PAIS_DE_RESIDENCIA (nome) VALUES ('Estados Unidos');
INSERT INTO PAIS_DE_RESIDENCIA (nome) VALUES ('Portugal');
INSERT INTO PAIS_DE_RESIDENCIA (nome) VALUES ('Canadá');
INSERT INTO PAIS_DE_RESIDENCIA (nome) VALUES ('Alemanha');

-- Inserções para a tabela ENDERECO
INSERT INTO ENDERECO (cep, pais_id) VALUES ('01001000', 1);
INSERT INTO ENDERECO (cep, pais_id) VALUES ('02002000', 1);
INSERT INTO ENDERECO (cep, pais_id) VALUES ('03003000', 1);
INSERT INTO ENDERECO (cep, pais_id) VALUES ('04004000', 1);
INSERT INTO ENDERECO (cep, pais_id) VALUES ('05005000', 1);

-- Inserções para a tabela CANDIDATO
INSERT INTO CANDIDATO (nome, sobrenome, data_nascimento, email, cpf, descricao_pessoal, senha_de_login, id_endereco) 
VALUES ('João', 'Silva', '1990-05-15', 'joao.silva@email.com', '12345678901', 'Desenvolvedor full stack com 5 anos de experiência', 'senha123hash', 1);

INSERT INTO CANDIDATO (nome, sobrenome, data_nascimento, email, cpf, descricao_pessoal, senha_de_login, id_endereco) 
VALUES ('Maria', 'Santos', '1995-08-20', 'maria.santos@email.com', '23456789012', 'Designer UX/UI apaixonada por interfaces intuitivas', 'senha456hash', 2);

INSERT INTO CANDIDATO (nome, sobrenome, data_nascimento, email, cpf, descricao_pessoal, senha_de_login, id_endereco) 
VALUES ('Pedro', 'Oliveira', '1988-12-10', 'pedro.oliveira@email.com', '34567890123', 'Engenheiro de dados especializado em Big Data', 'senha789hash', 3);

INSERT INTO CANDIDATO (nome, sobrenome, data_nascimento, email, cpf, descricao_pessoal, senha_de_login, id_endereco) 
VALUES ('Ana', 'Pereira', '1992-04-25', 'ana.pereira@email.com', '45678901234', 'Analista de sistemas com certificação AWS', 'senha101hash', 4);

INSERT INTO CANDIDATO (nome, sobrenome, data_nascimento, email, cpf, descricao_pessoal, senha_de_login, id_endereco) 
VALUES ('Carlos', 'Ferreira', '1985-09-30', 'carlos.ferreira@email.com', '56789012345', 'Especialista em cibersegurança', 'senha202hash', 5);

-- Inserções para a tabela EMPRESA
INSERT INTO EMPRESA (nome, cnpj, email_corporativo, descricao_da_empresa, senha_de_login, id_endereco) 
VALUES ('Tech Solutions Ltda', '12345678901234', 'contato@techsolutions.com', 'Empresa de desenvolvimento de software', 'empresa123hash', 1);

INSERT INTO EMPRESA (nome, cnpj, email_corporativo, descricao_da_empresa, senha_de_login, id_endereco) 
VALUES ('Inovação Digital S.A.', '23456789012345', 'rh@inovacaodigital.com', 'Startup focada em soluções de IA', 'empresa456hash', 2);

INSERT INTO EMPRESA (nome, cnpj, email_corporativo, descricao_da_empresa, senha_de_login, id_endereco) 
VALUES ('Data Analytics Corp', '34567890123456', 'vagas@dataanalytics.com', 'Consultoria especializada em análise de dados', 'empresa789hash', 3);

INSERT INTO EMPRESA (nome, cnpj, email_corporativo, descricao_da_empresa, senha_de_login, id_endereco) 
VALUES ('Cloud Systems BR', '45678901234567', 'contratacao@cloudsystems.com', 'Líder em soluções de nuvem', 'empresa101hash', 4);

INSERT INTO EMPRESA (nome, cnpj, email_corporativo, descricao_da_empresa, senha_de_login, id_endereco) 
VALUES ('Mobile App Developers', '56789012345678', 'jobs@mobileapp.com', 'Especialistas em desenvolvimento mobile', 'empresa202hash', 5);

-- Inserções para a tabela VAGA
INSERT INTO VAGA (nome, descricao, local, id_empresa) 
VALUES ('Desenvolvedor Back-end', 'Vaga para desenvolvimento com Java e Spring Boot', 'São Paulo', 1);

INSERT INTO VAGA (nome, descricao, local, id_empresa) 
VALUES ('UX Designer Sênior', 'Experiência com Figma e metodologias de design thinking', 'Remoto', 2);

INSERT INTO VAGA (nome, descricao, local, id_empresa) 
VALUES ('Engenheiro de Dados', 'Conhecimento em Hadoop, Spark e AWS', 'Rio de Janeiro', 3);

INSERT INTO VAGA (nome, descricao, local, id_empresa) 
VALUES ('Desenvolvedor Front-end', 'Vaga para desenvolvimento com React e TypeScript', 'Belo Horizonte', 4);

INSERT INTO VAGA (nome, descricao, local, id_empresa) 
VALUES ('Arquiteto de Soluções Cloud', 'Experiência com AWS e Azure', 'Remoto', 5);

-- Inserções para a tabela COMPETENCIA
INSERT INTO COMPETENCIA (nome) VALUES ('Java');
INSERT INTO COMPETENCIA (nome) VALUES ('Python');
INSERT INTO COMPETENCIA (nome) VALUES ('JavaScript');
INSERT INTO COMPETENCIA (nome) VALUES ('SQL');
INSERT INTO COMPETENCIA (nome) VALUES ('AWS');

-- Inserções para a tabela FORMACAO
INSERT INTO FORMACAO (nome, instituicao) VALUES ('Ciência da Computação', 'Universidade de São Paulo');
INSERT INTO FORMACAO (nome, instituicao) VALUES ('Engenharia de Software', 'PUC-Rio');
INSERT INTO FORMACAO (nome, instituicao) VALUES ('Análise e Desenvolvimento de Sistemas', 'FATEC');
INSERT INTO FORMACAO (nome, instituicao) VALUES ('MBA em Gestão de TI', 'FGV');
INSERT INTO FORMACAO (nome, instituicao) VALUES ('Bacharelado em Sistemas de Informação', 'UFRJ');

-- Inserções para a tabela FORMACAO_CANDIDATO
INSERT INTO FORMACAO_CANDIDATO (id_formacao, id_candidato, data_inicio, data_fim_previsao) VALUES (1, 1, '2010-02-01', '2014-12-20');
INSERT INTO FORMACAO_CANDIDATO (id_formacao, id_candidato, data_inicio, data_fim_previsao) VALUES (2, 2, '2013-03-10', '2017-12-15');
INSERT INTO FORMACAO_CANDIDATO (id_formacao, id_candidato, data_inicio, data_fim_previsao) VALUES (3, 3, '2007-02-20', '2010-12-10');
INSERT INTO FORMACAO_CANDIDATO (id_formacao, id_candidato, data_inicio, data_fim_previsao) VALUES (4, 4, '2018-08-05', '2020-07-25');
INSERT INTO FORMACAO_CANDIDATO (id_formacao, id_candidato, data_inicio, data_fim_previsao) VALUES (5, 5, '2004-03-15', '2008-12-18');

-- Inserções para a tabela CANDIDATO_CURTE_VAGA
INSERT INTO CANDIDATO_CURTE_VAGA (id_candidato, id_vaga) VALUES (1, 1);
INSERT INTO CANDIDATO_CURTE_VAGA (id_candidato, id_vaga) VALUES (2, 2);
INSERT INTO CANDIDATO_CURTE_VAGA (id_candidato, id_vaga) VALUES (3, 3);
INSERT INTO CANDIDATO_CURTE_VAGA (id_candidato, id_vaga) VALUES (4, 4);
INSERT INTO CANDIDATO_CURTE_VAGA (id_candidato, id_vaga) VALUES (5, 5);

-- Inserções para a tabela EMPRESA_CURTE_CANDIDATO
INSERT INTO EMPRESA_CURTE_CANDIDATO (id_candidato, id_empresa) VALUES (1, 5);
INSERT INTO EMPRESA_CURTE_CANDIDATO (id_candidato, id_empresa) VALUES (2, 4);
INSERT INTO EMPRESA_CURTE_CANDIDATO (id_candidato, id_empresa) VALUES (3, 3);
INSERT INTO EMPRESA_CURTE_CANDIDATO (id_candidato, id_empresa) VALUES (4, 2);
INSERT INTO EMPRESA_CURTE_CANDIDATO (id_candidato, id_empresa) VALUES (5, 1);

-- Inserções para a tabela CANDIDATO_COMPETENCIA
INSERT INTO CANDIDATO_COMPETENCIA (id_candidato, id_competencia) VALUES (1, 1);
INSERT INTO CANDIDATO_COMPETENCIA (id_candidato, id_competencia) VALUES (2, 2);
INSERT INTO CANDIDATO_COMPETENCIA (id_candidato, id_competencia) VALUES (3, 3);
INSERT INTO CANDIDATO_COMPETENCIA (id_candidato, id_competencia) VALUES (4, 4);
INSERT INTO CANDIDATO_COMPETENCIA (id_candidato, id_competencia) VALUES (5, 5);

-- Inserções para a tabela VAGA_COMPETENCIA
INSERT INTO VAGA_COMPETENCIA (id_vaga, id_competencia) VALUES (1, 1);
INSERT INTO VAGA_COMPETENCIA (id_vaga, id_competencia) VALUES (2, 3);
INSERT INTO VAGA_COMPETENCIA (id_vaga, id_competencia) VALUES (3, 2);
INSERT INTO VAGA_COMPETENCIA (id_vaga, id_competencia) VALUES (4, 3);
INSERT INTO VAGA_COMPETENCIA (id_vaga, id_competencia) VALUES (5, 5);


-- Consulta a tabela PAIS_DE_RESIDENCIA
SELECT * FROM PAIS_DE_RESIDENCIA;

-- Consulta a tabela ENDERECO
SELECT * FROM ENDERECO;

-- Consulta a tabela CANDIDATO
SELECT * FROM CANDIDATO;

-- Consulta a tabela EMPRESA
SELECT * FROM EMPRESA;

-- Consulta a tabela VAGA
SELECT * FROM VAGA;

-- Consulta a tabela COMPETENCIA
SELECT * FROM COMPETENCIA;

-- Consulta a tabela FORMACAO
SELECT * FROM FORMACAO;

-- Consulta a tabela CANDIDATO_CURTE_VAGA
SELECT * FROM CANDIDATO_CURTE_VAGA;

-- Consulta a tabela EMPRESA_CURTE_CANDIDATO
SELECT * FROM EMPRESA_CURTE_CANDIDATO;

-- Consulta a tabela CANDIDATO_COMPETENCIA
SELECT * FROM CANDIDATO_COMPETENCIA;

-- Consulta a tabela VAGA_COMPETENCIA
SELECT * FROM VAGA_COMPETENCIA;

-- Consulta a tabela FORMACAO_CANDIDATO
SELECT * FROM FORMACAO_CANDIDATO;
