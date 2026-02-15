-- Script de criação do banco de dados
-- Catálogo de Filmes API - Grupo 6 (Mariana e Arthur)
-- Sistemas Distribuídos - UFES 2025/2

DROP DATABASE IF EXISTS catalogo_filmes;
CREATE DATABASE catalogo_filmes CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE catalogo_filmes;

-- Tabela de gêneros
CREATE TABLE generos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(500)
) ENGINE=InnoDB;

-- Tabela de filmes
CREATE TABLE filmes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    sinopse TEXT NOT NULL,
    data_lancamento DATE NOT NULL,
    duracao INT NOT NULL,
    diretor VARCHAR(150) NOT NULL,
    avaliacao DECIMAL(3,1),
    poster_url VARCHAR(500),
    genero_id BIGINT NOT NULL,
    criado_em DATE NOT NULL,
    atualizado_em DATE,
    FOREIGN KEY (genero_id) REFERENCES generos(id),
    INDEX idx_titulo (titulo),
    INDEX idx_diretor (diretor),
    INDEX idx_genero (genero_id)
) ENGINE=InnoDB;

-- Tabela de atores
CREATE TABLE atores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    data_nascimento DATE,
    nacionalidade VARCHAR(100),
    biografia TEXT,
    foto_url VARCHAR(500),
    INDEX idx_nome (nome)
) ENGINE=InnoDB;

-- Tabela de relacionamento filmes-atores (muitos para muitos)
CREATE TABLE filme_ator (
    filme_id BIGINT NOT NULL,
    ator_id BIGINT NOT NULL,
    PRIMARY KEY (filme_id, ator_id),
    FOREIGN KEY (filme_id) REFERENCES filmes(id) ON DELETE CASCADE,
    FOREIGN KEY (ator_id) REFERENCES atores(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabela de usuários
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo VARCHAR(20) NOT NULL DEFAULT 'USUARIO',
    criado_em DATETIME NOT NULL,
    ultimo_acesso DATETIME,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB;

-- Inserir dados iniciais de gêneros
INSERT INTO generos (nome, descricao) VALUES
('Ação', 'Filmes com cenas intensas de ação, lutas e aventura'),
('Comédia', 'Filmes destinados a provocar risos e entretenimento leve'),
('Drama', 'Filmes sérios focados em desenvolvimento de personagens e emoções'),
('Ficção Científica', 'Filmes baseados em conceitos científicos futuristas ou especulativos'),
('Terror', 'Filmes projetados para assustar e criar tensão'),
('Romance', 'Filmes centrados em relacionamentos românticos'),
('Suspense', 'Filmes que mantêm o espectador em tensão e expectativa'),
('Animação', 'Filmes criados com técnicas de animação'),
('Documentário', 'Filmes não ficcionais sobre eventos ou pessoas reais'),
('Aventura', 'Filmes sobre jornadas emocionantes e explorações');

-- Inserir dados de exemplo de filmes
INSERT INTO filmes (titulo, sinopse, data_lancamento, duracao, diretor, avaliacao, genero_id, criado_em, atualizado_em) VALUES
('O Poderoso Chefão', 'A saga da família Corleone e seu império do crime em Nova York.', '1972-03-24', 175, 'Francis Ford Coppola', 9.2, 3, CURDATE(), CURDATE()),
('Pulp Fiction', 'Histórias interligadas de criminosos em Los Angeles.', '1994-10-14', 154, 'Quentin Tarantino', 8.9, 3, CURDATE(), CURDATE()),
('Matrix', 'Um hacker descobre a verdadeira natureza da realidade.', '1999-03-31', 136, 'Lana Wachowski, Lilly Wachowski', 8.7, 4, CURDATE(), CURDATE()),
('Interestelar', 'Astronautas viajam através de um buraco de minhoca em busca de um novo lar.', '2014-11-07', 169, 'Christopher Nolan', 8.6, 4, CURDATE(), CURDATE()),
('Cidade de Deus', 'A história do crime organizado nas favelas do Rio de Janeiro.', '2002-08-30', 130, 'Fernando Meirelles', 8.6, 3, CURDATE(), CURDATE());

-- Inserir dados de exemplo de atores
INSERT INTO atores (nome, data_nascimento, nacionalidade, biografia) VALUES
('Marlon Brando', '1924-04-03', 'Americana', 'Um dos maiores atores do cinema americano.'),
('Al Pacino', '1940-04-25', 'Americana', 'Lenda do cinema conhecido por seus papéis icônicos.'),
('John Travolta', '1954-02-18', 'Americana', 'Ator e dançarino de grande sucesso.'),
('Keanu Reeves', '1964-09-02', 'Canadense', 'Ator conhecido por sua versatilidade e carisma.'),
('Matthew McConaughey', '1969-11-04', 'Americana', 'Ator vencedor do Oscar.');

-- Relacionar filmes com atores
INSERT INTO filme_ator (filme_id, ator_id) VALUES
(1, 1), (1, 2),
(2, 3),
(3, 4),
(4, 5);

-- Inserir usuário administrador padrão
-- Senha: admin123 (hash BCrypt)
INSERT INTO usuarios (username, email, senha, tipo, criado_em, ativo) VALUES
('admin', 'admin@catalogo.com', '$2a$12$G4EpLM2jSH34ozVY4rKLFuc.0U.0XJuUZXrmb3jpsJExDHKnUVBRS', 'ADMIN', NOW(), TRUE);

-- Criar índices para melhor performance
CREATE INDEX idx_filme_data ON filmes(data_lancamento);
CREATE INDEX idx_filme_avaliacao ON filmes(avaliacao);
