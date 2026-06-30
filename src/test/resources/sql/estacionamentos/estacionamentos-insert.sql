insert into usuarios 
(id, username, password, role)
values 
(100, 'ana@email.com.br', '$2a$10$AtWo422MdyRQ1RgPzmJNnuDB7xN0GW38sXT4rnBFBqGnMyVmVEf4O', 'ROLE_ADMIN'),
(101, 'bia@email.com.br', '$2a$10$AtWo422MdyRQ1RgPzmJNnuDB7xN0GW38sXT4rnBFBqGnMyVmVEf4O', 'ROLE_CLIENTE'),
(102, 'bob@email.com.br', '$2a$10$AtWo422MdyRQ1RgPzmJNnuDB7xN0GW38sXT4rnBFBqGnMyVmVEf4O', 'ROLE_CLIENTE');

insert into clientes 
(id, nome, cpf, id_usuario) 
values 
(21, 'Biatriz Rodrigues',   '09191773016', 101),
(22, 'Rodrigo Silva',       '98401203015', 102);

insert into vagas 
(id, codigo, status) 
values 
(100, 'A-01', 'OCUPADA'),
(200, 'A-02', 'OCUPADA'),
(300, 'A-03', 'OCUPADA'),
(400, 'A-04', 'LIVRE'),
(500, 'A-05', 'LIVRE');

insert into clientes_tem_vagas 
(numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
values 
('20230313-101300', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE',   '2023-03-13 10:15:00', 22, 100),
('20230314-101400', 'SIE-1020', 'FIAT', 'SIENA', 'BRANCO',  '2023-03-14 10:15:00', 21, 200),
('20230315-101500', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE',   '2023-03-14 10:15:00', 22, 300);
