-- CRIA O BANCO DE DADOS
CREATE DATABASE biblioteca;
GO

USE BIBLIOTECA;
GO

-- TABELA LIVRO
CREATE TABLE livro (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    TITULO VARCHAR(200) NOT NULL,
    AUTOR VARCHAR(200) NOT NULL,
    TOPICOS VARCHAR(200),
    CATEGORIA VARCHAR(200),
    ANO_PUBLICACAO INT,
);

-- INSERÇÃO DE LIVROS
INSERT INTO livro (TITULO, AUTOR, TOPICOS, CATEGORIA, ANO_PUBLICACAO, cod_livro, cod_interno)
VALUES
('A Arte da Guerra', 'Sun Tzu', 'Estratégia', 'História', 2000),
('1984', 'George Orwell', 'Distopia', 'Literatura', 1995),
('O Pequeno Príncipe', 'Antonie de Saint-Exupéry', 'Filosofia', 'Literatura', 1992),
('Sapiens', 'Yuval Harari', 'Evolução Humana', 'História', 2005),
('A Revolução dos Bichos', 'George Orwell', 'Política', 'Literatura', 1998 ),
('Clean Code', 'Robert C. Martin', 'Boas Práticas', 'Tecnologia', 2008),
('O Programador Pragmático', 'Andrew Hunt', 'Desenvolvimento', 'Tecnologia', 2000),
('A Teoria de Tudo', 'Stephen Hawking', 'Física', 'Ciências', 2002),
('O Gene', 'Siddhartha Mukherjee', 'Biologia', 'Ciências', 2010),
('Design Patterns', 'Erich Gamma', 'Padrões de Projeto', 'Tecnologia', 1999);


ALTER TABLE livro
ADD cod_livro INT,
    cod_interno INT;

-- TABELA AUTOR
CREATE TABLE autor (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    NOME VARCHAR(200) NOT NULL,
    SOBRENOME VARCHAR(200) NOT NULL,
    data_criacao DATETIME DEFAULT GETDATE()
);

-- INSERÇÃO DE AUTORES
INSERT INTO autor(NOME, SOBRENOME)
VALUES
('Sun', 'Tzu'),
('George', 'Orwell'),
('Antonie', 'de Saint-Exupéry'),
('Yuval', 'Harari'),
('Robert', 'C. Martin'),
('Andrew', 'Hunt'),
('Stephen', 'Hawking'),
('Siddhartha', 'Mukherjee'),
('Erich', 'Gamma');

-- TABELA RELACIONAL LIVRO_AUTOR
CREATE TABLE livro_autor (
    livro_id INT,
    autor_id INT,
    PRIMARY KEY (livro_id, autor_id),
    FOREIGN KEY (livro_id) REFERENCES livro(ID),
    FOREIGN KEY (autor_id) REFERENCES autor(ID)
);

-- RELACIONAMENTO LIVRO_AUTOR
INSERT INTO livro_autor (livro_id, autor_id)
VALUES
(1,1),
(2,2),
(3,3),
(4,4),
(5,2),
(6,5),
(7,6),
(8,7),
(9,8),
(10,9);

-- TABELA MEMBRO
CREATE TABLE membro (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    Nome VARCHAR(200) NOT NULL,
    Sobrenome VARCHAR(200) NOT NULL,
    joined_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    data_criacao DATETIME DEFAULT GETDATE()
);

-- INSERÇÃO DE MEMBROS
INSERT INTO membro (Nome, Sobrenome, joined_date, status)
VALUES
('Ana', 'Silva', '2023-01-15', 'ativo'),
('Carlos', 'Oliveira', '2022-11-03', 'inativo'),
('Fernanda', 'Souza', '2024-06-21', 'ativo'),
('João', 'Pereira', '2021-09-10', 'pendente'),
('Mariana', 'Almeida', '2025-04-01', 'ativo');

-- TABELA LOAN
CREATE TABLE loan (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    livro_id INT,
    membro_id INT,
    loan_date DATE NOT NULL DEFAULT GETDATE(),
    return_date DATE NULL,
    FOREIGN KEY (livro_id) REFERENCES livro(ID),
    FOREIGN KEY (membro_id) REFERENCES membro(ID)
);

-- Índice para garantir apenas um empréstimo ativo por livro
CREATE UNIQUE INDEX idx_loan_livro_id_active
ON loan(livro_id)
WHERE return_date IS NULL;

-- TABELA USUARIO
CREATE TABLE usuario (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    Login VARCHAR(200) NOT NULL,
    Senha VARCHAR(20) NOT NULL,
    data_criacao DATETIME DEFAULT GETDATE()
);

-- INSERÇÃO DE USUÁRIOS
INSERT INTO usuario ([Login], [Senha])
VALUES 
  ('admin', 'admin'),
  ('user1', '1234'),
  ('user2', 'abcd'),
  ('banco', 'banco*123');




-- TABELA EMPRESTIMO
CREATE TABLE emprestimo (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    nome_cliente VARCHAR(100) NOT NULL,
    data_validade DATE NOT NULL,
    multa DECIMAL(10,2) DEFAULT 0,
    observacoes TEXT,
    livro_id INT,
    usuario_id INT,
    data_emprestimo DATETIME DEFAULT GETDATE(),
    return_date DATE NULL,
    status VARCHAR(20) DEFAULT 'ativo',
    FOREIGN KEY (livro_id) REFERENCES livro(ID),
    FOREIGN KEY (usuario_id) REFERENCES usuario(ID)
);

-- INSERÇÃO DE EXEMPLO EM EMPRÉSTIMO
INSERT INTO emprestimo (
    nome_cliente,
    data_validade,
    multa,
    observacoes,
    livro_id,
    usuario_id
)
VALUES (
    'Carlos Andrade',
    '2025-06-30',
    5.00,
    'Devolução com pequeno atraso.',
    10,
    1
);

-- TABELA ADMIN
CREATE TABLE administrador (
    ID_Admin INT PRIMARY KEY,
    Nome_Usuario VARCHAR(50) NOT NULL,
    Senha VARCHAR(255) NOT NULL,
    Permissoes VARCHAR(100),
    data_criacao DATETIME DEFAULT GETDATE()
);

-- INSERÇÃO DE ADMIN
INSERT INTO administrador (ID_Admin, Nome_Usuario, Senha, Permissoes)
VALUES (1, 'admin1', 'senha123', 'Total'),
       (2, 'cesar', 'senha456', 'Total');

ALTER TABLE usuario ADD perfil VARCHAR(20);

UPDATE usuario SET perfil = 'admin' WHERE login = 'admin';
UPDATE usuario SET perfil = 'user' WHERE login IN ('user1', 'user2', 'banco');

-- TABELA CLIENTE
CREATE TABLE cliente (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    Nome VARCHAR(200) NOT NULL,
    Sobrenome VARCHAR(200) NOT NULL,
    Email VARCHAR(200),
    Telefone VARCHAR(20),
    Endereco VARCHAR(255),
    Data_Registro DATE DEFAULT GETDATE(),
    data_criacao DATETIME DEFAULT GETDATE()
);

-- INSERÇÃO DE CLIENTES
INSERT INTO cliente (Nome, Sobrenome, Email, Telefone, Endereco)
VALUES
('Carlos', 'Andrade', 'carlos.andrade@email.com', '11999990000', 'Rua A, 123'),
('Fernanda', 'Lima', 'fernanda.lima@email.com', '11988880000', 'Av. B, 456'),
('João', 'Silva', 'joao.silva@email.com', '11977770000', 'Rua C, 789');

create table funcionario(
	ID INT IDENTITY,
	nome varchar(200)not null,
	tipo_usuario varchar(200),
	cargo varchar(200),
	departamento varchar(200)
);




-- ATUALIZAÇÃO DE CLIENTE
UPDATE cliente
SET Telefone = '11955554444',
    Endereco = 'Rua Nova, 999'
WHERE ID = 2;

-- CRIA VIEW PARA RELATÓRIO DE LIVROS EMPRESTADOS
CREATE VIEW vw_livros_emprestados AS
SELECT 
    e.ID AS emprestimo_id,
    l.TITULO,
    c.nome AS nome_cliente,
    e.data_emprestimo,
    e.data_validade,
    e.return_date,
    e.status
FROM emprestimo e
JOIN livro l ON e.livro_id = l.ID
JOIN cliente c ON e.cliente_id = c.ID;

USE biblioteca;
GRANT SELECT ON dbo.livro TO [banco];

drop table admin;
