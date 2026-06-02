CREATE DATABASE IF NOT EXISTS db_estoque;
USE db_estoque;

create table Categoria (
	id int not null auto_increment,
    descricao varchar(100) not null,
    
    primary key(id)
);

CREATE TABLE produto(
   id int NOT NULL auto_increment,
   nome varchar(50) not null,
   descricao varchar(200),
   preco decimal(10,2) not null,
   id_categoria int not null ,
   primary key(id)
);

alter table produto add constraint fk_produto_categoria
foreign key(id_categoria) references categoria(id);

create table fornecedor(
	id int not nulL auto_increment,
    nome varchar(50) not null,
    email varchar(100) not null,
    fone varchar(20) not null,
    primary key(id)
);

alter table produto add column id_fornecedor int not null;

alter table produto add constraint fk_produto_fornecedor
foreign key(id_fornecedor) references fornecedor(id);

/*TABELA ESTOQUE COM RELACIONAMENTO 1:1 PARA PRODUTO*/
create table estoque(
	id_produto int not null references produto(id),
    quantidade int not null default 0,
    qtd_minima int default 0,
    qtd_maxima int default 0,
    situacao enum('ATIVO', 'INATIVO', 'BLOQUEADO') not null default 'ATIVO',
    primary key (id_produto)
);

alter table estoque add constraint fk_estoque_produto 
    foreign key (id_produto) 
    references produto(id) on update cascade on delete cascade;
    
/*OPCIONALMENTE EXECUTAR O SCRIPT A SEGUIR PARA INSERIR ALGUNS DADOS NO BANCO */
insert into categoria (descricao) values ('ELETRÔNICO');
insert into categoria (descricao) values ('ELETRODOMÉSTICO');
insert into categoria (descricao) values ('UTENSÍLIOS');
 
insert into fornecedor (nome, email, fone) values ('ELETRO BRASIL', 'CONTATO@ELETROBRASIL.COM', '988229922');
insert into fornecedor (nome, email, fone) values ('UTENSILIOS SC', 'CONTATO@UTENSILIOSSC.COM', '32223434');
insert into fornecedor (nome, email, fone) values ('TEM MAIS', 'CONTATO@TEMMAIS.COM.BR', '32240000');
insert into fornecedor (nome, email, fone) values ('AMERICA', 'CONTATO@AMERICA.COM', '999881234');

insert into produto (nome, descricao, preco, id_categoria, id_fornecedor) values('NOTEBOOK', 'ULTRABOOK', 2100.00, 1, 1);
insert into estoque (id_produto, quantidade, qtd_minima, qtd_maxima, situacao) values(1, 30, 5, 100, 'ATIVO');  
insert into produto (nome, descricao, preco, id_categoria, id_fornecedor) values('GELADEIRA', 'GELADEIRA FROST FREE', 3500.00, 2, 4);
insert into estoque (id_produto, quantidade, qtd_minima, qtd_maxima, situacao) values(2, 15, 3, 200, 'ATIVO');
insert into produto (nome, descricao, preco, id_categoria, id_fornecedor) values('PANELA', 'PANELA DE PRESSÃO', 339.00, 3, 3);
insert into estoque (id_produto, quantidade, qtd_minima, qtd_maxima, situacao) values(3, 40, 10, 400, 'ATIVO');  
