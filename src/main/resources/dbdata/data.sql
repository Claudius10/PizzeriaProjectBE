INSERT INTO pizzeria.address (door, floor, gate, staircase, street, street_nr, id)
VALUES (NULL, NULL, NULL, NULL, 'Calle Alustre', 15, 1),
       (NULL, NULL, NULL, NULL, 'Avenida Viciosa', 221, 2);

INSERT INTO pizzeria.store (name, phone_number, schedule, address_id, id)
VALUES ('Alustre', 666555666, 'L-D 12:00h-00:00h', 1, 1),
       ('Viciosa', 555666555, 'L-D 12:00h-00:00h', 2, 2);

INSERT INTO pizzeria.offer (caveat, description, image, name, id)
VALUES ('Cualquiera especialidad o hasta 4 ingredientes', 'Válida todos los días en Medianas y Familiares',
        'https://i.imgur.com/bfQpnP2.jpg', 'El 3x2 en Pizzas', 1),
       ('Cualquiera especialidad o hasta 4 ingredientes', 'Válida todos los días en Medianas y Familiares',
        'https://i.imgur.com/bfQpnP2.jpg', 'La 2ª Pizza al 50%', 2);

INSERT INTO pizzeria.product (description, format, image, name, price, product_type, id)
VALUES ('Salsa de Tomate, Mozzarella 100%, Parmesano, Emmental, Queso Azul', 'Mediana',
        'https://i.imgur.com/loYe1jf.jpg', 'Cuatro Quesos', 13.3, 'pizza', 1),
       ('Salsa de Tomate, Mozzarella 100%, Parmesano, Emmental, Queso Azul', 'Familiar',
        'https://i.imgur.com/loYe1jf.jpg', 'Cuatro Quesos', 18.3, 'pizza', 2),
       ('Salsa de Tomate, Mozzarella 100%, Calabacín, Tomate Natural, Parmesano', 'Mediana',
        'https://i.imgur.com/loYe1jf.jpg', 'Natura', 13.3, 'pizza', 3),
       ('Salsa de Tomate, Mozzarella 100%, Calabacín, Tomate Natural, Parmesano', 'Familiar',
        'https://i.imgur.com/loYe1jf.jpg', 'Natura', 18.3, 'pizza', 4),
       ('Salsa de Tomate, Mozzarella 100%, Cebolla, Bacon, Queso de Cabra', 'Mediana',
        'https://i.imgur.com/loYe1jf.jpg', 'Cabra Loca', 13.3, 'pizza', 5),
       ('Salsa de Tomate, Mozzarella 100%, Cebolla, Bacon, Queso de Cabra', 'Familiar',
        'https://i.imgur.com/loYe1jf.jpg', 'Cabra Loca', 18.3, 'pizza', 6),
       ('Salsa de Tomate, Doble de Mozzarella 100%, Doble de Peperoni', 'Mediana', 'https://i.imgur.com/loYe1jf.jpg',
        'Roni Pepperoni', 13.3, 'pizza', 7),
       ('Salsa de Tomate, Doble de Mozzarella 100%, Doble de Peperoni', 'Familiar', 'https://i.imgur.com/loYe1jf.jpg',
        'Roni Pepperoni', 18.3, 'pizza', 8),
       ('Salsa de Tomate, Mozarella 100%, Tomate Natural, Doble de Jamón Serrano', 'Mediana',
        'https://i.imgur.com/loYe1jf.jpg', 'Pata Negra', 13.3, 'pizza', 9),
       ('Salsa de Tomate, Mozarella 100%, Tomate Natural, Doble de Jamón Serrano', 'Familiar',
        'https://i.imgur.com/loYe1jf.jpg', 'Pata Negra', 18.3, 'pizza', 10);
INSERT INTO pizzeria.product (description, format, image, name, price, product_type, id)
VALUES ('Cremosa salsa de nata, Mozzarella 100%, Cebolla, Champiñones frescos, Parmesano', 'Mediana',
        'https://i.imgur.com/loYe1jf.jpg', 'Allo Bianca', 13.3, 'pizza', 11),
       ('Cremosa salsa de nata, Mozzarella 100%, Cebolla, Champiñones frescos, Parmesano', 'Familiar',
        'https://i.imgur.com/loYe1jf.jpg', 'Allo Bianca', 18.3, 'pizza', 12),
       ('Salsa de Nata, Mozzarella 100%, Cebolla, Champiñon Fresco, Doble de Bacon', 'Mediana',
        'https://i.imgur.com/loYe1jf.jpg', 'Carbonara', 14.75, 'pizza', 13),
       ('Salsa de Nata, Mozzarella 100%, Cebolla, Champiñon Fresco, Doble de Bacon', 'Familiar',
        'https://i.imgur.com/loYe1jf.jpg', 'Carbonara', 20.25, 'pizza', 14),
       ('Salsa de Tomate, Mozzarella 100%, Calabacín, Champiñon Fresco, Pollo, Aceite de Trufa', 'Mediana',
        'https://i.imgur.com/loYe1jf.jpg', 'Trufa Gourmet', 14.75, 'pizza', 15),
       ('Salsa de Tomate, Mozzarella 100%, Calabacín, Champiñon Fresco, Pollo, Aceite de Trufa', 'Familiar',
        'https://i.imgur.com/loYe1jf.jpg', 'Trufa Gourmet', 20.25, 'pizza', 16),
       ('Salsa de Tomate, Mozzarella 100%, Berenjena, Calabacín, Tomate Natural, Aceitunas Negras', 'Mediana',
        'https://i.imgur.com/loYe1jf.jpg', 'Mediterránea', 14.75, 'pizza', 17),
       ('Salsa de Tomate, Mozzarella 100%, Berenjena, Calabacín, Tomate Natural, Aceitunas Negras', 'Familiar',
        'https://i.imgur.com/loYe1jf.jpg', 'Mediterránea', 20.25, 'pizza', 18),
       ('Salsa de Tomate, Mozzarella 100%, Jamón York, Bacon, Pepperoni, Ternera', 'Mediana',
        'https://i.imgur.com/loYe1jf.jpg', 'Caníbal', 14.75, 'pizza', 19),
       ('Salsa de Tomate, Mozzarella 100%, Jamón York, Bacon, Pepperoni, Ternera', 'Familiar',
        'https://i.imgur.com/loYe1jf.jpg', 'Caníbal', 20.25, 'pizza', 20);
INSERT INTO pizzeria.product (description, format, image, name, price, product_type, id)
VALUES ('Salsa de Tomate, Mozzarella 100%, Atún, Jamón York, Alcachofa, Pimiento Verde', 'Mediana',
        'https://i.imgur.com/loYe1jf.jpg', '4 Estaciones', 14.75, 'pizza', 21),
       ('Salsa de Tomate, Mozzarella 100%, Atún, Jamón York, Alcachofa, Pimiento Verde', 'Familiar',
        'https://i.imgur.com/loYe1jf.jpg', '4 Estaciones', 20.25, 'pizza', 22),
       ('Rellenos de Salsa Tomate, Mozzarella 100% fundida, Jamón York y Bacon', NULL,
        'https://i.imgur.com/L0XNTnT.jpg', 'Mini Calzones Famosos', 6.95, 'appetizer', 23),
       ('Rellenos de Salsa Tomate y nuestro fundido especial 4 quesos', NULL, 'https://i.imgur.com/L0XNTnT.jpg',
        'Mini Calzones 4 Quesos', 6.95, 'appetizer', 24),
       ('Pequeñas delicias, ¡Buenísimos! (10 Uds.)', NULL, 'https://i.imgur.com/L0XNTnT.jpg', 'Panes de Ajo Con Queso',
        5.95, 'appetizer', 25),
       ('Braseadas a la barbacoa (6 Uds.)', NULL, 'https://i.imgur.com/L0XNTnT.jpg', 'Alitas de Pollo BBQ', 4.95,
        'appetizer', 26),
       ('Con queso cheddar y jalapeño  (6 Uds.)', NULL, 'https://i.imgur.com/L0XNTnT.jpg', 'Delicias de Queso', 4.95,
        'appetizer', 27),
       ('6 Minicalzones (3 Famosos/3 Cuatro Quesos) + 6 Delicias de Queso + 6 Alitas de Pollo BBQ', NULL,
        'https://i.imgur.com/L0XNTnT.jpg', 'Combo Deluxe', 12.95, 'appetizer', 28),
       ('Tallarines a la carbonara', NULL, 'https://i.imgur.com/rq8y9At.jpg', 'Pasta Carbonara', 6.95, 'pasta', 29),
       ('Spaguetti con salsa bolognesa', NULL, 'https://i.imgur.com/rq8y9At.jpg', 'Pasta Bolognese', 6.95, 'pasta', 30);
INSERT INTO pizzeria.product (description, format, image, name, price, product_type, id)
VALUES ('Tagliatelle, boletus y salsa de champiñones frescos', NULL, 'https://i.imgur.com/rq8y9At.jpg', 'Pasta Funghi',
        6.95, 'pasta', 31),
       ('Spaguetti pesto con piñones y albahaca', NULL, 'https://i.imgur.com/rq8y9At.jpg', 'Pasta Pesto', 6.95, 'pasta',
        32),
       ('Tortellini al huevo con ricotta y espinacas', NULL, 'https://i.imgur.com/rq8y9At.jpg', 'Pasta Spinaci', 6.95,
        'pasta', 33),
       ('¡Una delicia!', NULL, 'https://i.imgur.com/rq8y9At.jpg', 'Lasaña de Ternera', 6.95, 'pasta', 34),
       ('Ligera y muy sabrosa', NULL, 'https://i.imgur.com/rq8y9At.jpg', 'Lasaña Vegetal', 6.95, 'pasta', 35),
       ('Recién hecho ¡tremendo!', NULL, 'https://i.imgur.com/wK7TCCX.jpg', 'Tiramisú de la Casa', 4.45, 'dessert', 36),
       ('Auténtica tarta de queso', NULL, 'https://i.imgur.com/y46hOxE.jpg', 'Tarta de Queso', 4.45, 'dessert', 37),
       ('Explosión de sabor', NULL, 'https://i.imgur.com/GjN2sOg.jpg', 'Bomba de Chocolate', 4.45, 'dessert', 38),
       ('¡Nuestro helado favorito!', '500ml', 'https://i.imgur.com/fOu7AGK.jpg', 'Häagen-Dazs', 7.95, 'dessert', 39),
       ('¡Nuestro helado favorito!', '100ml', 'https://i.imgur.com/fOu7AGK.jpg', 'Häagen-Dazs', 3.45, 'dessert', 40);
INSERT INTO pizzeria.product (description, format, image, name, price, product_type, id)
VALUES (NULL, '330ml', 'https://i.imgur.com/1BzVHZT.png', 'Coca Cola Clásica', 1.95, 'beverage', 41),
       (NULL, '1L', 'https://i.imgur.com/1BzVHZT.png', 'Coca Cola Clásica', 2.95, 'beverage', 42),
       (NULL, '330ml', 'https://i.imgur.com/Nfvl5TU.png', 'Coca Cola Zero', 1.95, 'beverage', 43),
       (NULL, '1L', 'https://i.imgur.com/Nfvl5TU.png', 'Coca Cola Zero', 2.95, 'beverage', 44),
       (NULL, '330ml', 'https://i.imgur.com/cmW3aYq.png', 'Fanta Naranja', 1.95, 'beverage', 45),
       (NULL, '1L', 'https://i.imgur.com/cmW3aYq.png', 'Fanta Naranja', 2.95, 'beverage', 46),
       (NULL, '330ml', 'https://i.imgur.com/X0TvZQh.jpg', 'Fanta Limón', 1.95, 'beverage', 47),
       (NULL, '1L', 'https://i.imgur.com/X0TvZQh.jpg', 'Fanta Limón', 2.95, 'beverage', 48),
       (NULL, '330ml', 'https://i.imgur.com/obYpRq6.jpg', 'Nestea Limón', 1.95, 'beverage', 49),
       (NULL, '330ml', 'https://i.imgur.com/uCmMiKI.jpg', 'Sprite', 1.95, 'beverage', 50);
INSERT INTO pizzeria.product (description, format, image, name, price, product_type, id)
VALUES (NULL, '330ml', 'https://i.imgur.com/ejlEnRd.jpg', 'Aquarius Limón', 1.95, 'beverage', 51),
       (NULL, '330ml', 'https://i.imgur.com/vCIvzF8.jpg', 'Mahou Clásica', 1.95, 'beverage', 52),
       (NULL, '330ml', 'https://i.imgur.com/C6ZW6h8.png', 'Mahou Sin', 1.95, 'beverage', 53);