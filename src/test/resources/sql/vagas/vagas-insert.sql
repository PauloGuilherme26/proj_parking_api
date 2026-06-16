insert into usuarios
(id, username, password, role)
values
(100, 'ana@email.com', '$2a$10$VZv60Y8/wkIzF83mUUlDveos/MH1WUr8b..5VmaXyRGMDuH8VQRZe', 'ROLE_ADMIN'),
(101, 'bia@email.com', '$2a$10$VZv60Y8/wkIzF83mUUlDveos/MH1WUr8b..5VmaXyRGMDuH8VQRZe', 'ROLE_CLIENTE'),
(102, 'bob@email.com', '$2a$10$VZv60Y8/wkIzF83mUUlDveos/MH1WUr8b..5VmaXyRGMDuH8VQRZe', 'ROLE_CLIENTE');

insert into vagas
(id, codigo, status)
values
(10, 'A-01', 'LIVRE'),
(20, 'A-02', 'LIVRE'),
(30, 'A-03', 'OCUPADA'),
(40, 'A-04', 'LIVRE');

