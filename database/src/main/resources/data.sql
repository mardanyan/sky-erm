
--  password: admin
INSERT INTO user_role (email, password, name) VALUES ('admin@email.com', '{bcrypt}$2a$10$k1bD6SU1vclHR99jmtAAc.79i2uwRqHNw9gCP/ViBHPL3ysQnftOO', 'name') ON CONFLICT DO NOTHING;