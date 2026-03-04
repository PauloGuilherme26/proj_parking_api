insert into usuarios
(id, username, password, role)
values
(100, 'ana@email.com', '$2a$10$VZv60Y8/wkIzF83mUUlDveos/MH1WUr8b..5VmaXyRGMDuH8VQRZe', 'ROLE_ADMIN'),
(101, 'joao@email.com', '$2a$10$VZv60Y8/wkIzF83mUUlDveos/MH1WUr8b..5VmaXyRGMDuH8VQRZe', 'ROLE_ADMIN'),
(102, 'bia@email.com', '$2a$10$VZv60Y8/wkIzF83mUUlDveos/MH1WUr8b..5VmaXyRGMDuH8VQRZe', 'ROLE_CLIENTE'),
(103, 'bob@email.com', '$2a$10$VZv60Y8/wkIzF83mUUlDveos/MH1WUr8b..5VmaXyRGMDuH8VQRZe', 'ROLE_CLIENTE'),
(104, 'toby@email.com', '$2a$10$VZv60Y8/wkIzF83mUUlDveos/MH1WUr8b..5VmaXyRGMDuH8VQRZe', 'ROLE_CLIENTE');

insert into clientes
(id, nome, cpf, id_usuario)
values
(10, 'Bianca Silva', '58381645069', '101'),
(20, 'Roberto Gomes', '89471349028', '102');