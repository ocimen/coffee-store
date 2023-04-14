CREATE TABLE IF NOT EXISTS product
(
    id    IDENTITY,
    name  VARCHAR(50) NOT NULL,
    price DOUBLE      NOT NULL,
    deleted BIT,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS topping
(
    id    IDENTITY,
    name  VARCHAR(50) NOT NULL,
    price DOUBLE      NOT NULL,
    deleted BIT,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS cart_product
(
    id       IDENTITY,
    product_id long,
    deleted BIT,
    PRIMARY KEY (id),
    CONSTRAINT cart_product_fk FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE IF NOT EXISTS cart_product_toppings
(
    id          IDENTITY,
    cart_product_id LONG NOT NULL,
    topping_id  LONG NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT cart_product_toppings_fk1 FOREIGN KEY (cart_product_id) REFERENCES cart_product (id),
    CONSTRAINT cart_product_toppings_fk2 FOREIGN KEY (topping_id) REFERENCES topping (id)
);

CREATE TABLE IF NOT EXISTS cart
(
    id          IDENTITY,
    user_id     LONG   NOT NULL,
    cart_product_id LONG   NOT NULL,
    price       DOUBLE NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT cart_fk FOREIGN KEY (cart_product_id) REFERENCES cart_product (id)
);

CREATE TABLE IF NOT EXISTS orders
(
    id                  IDENTITY,
    user_id             LONG NOT NULL,
    total_price         DOUBLE,
    discounted_price    DOUBLE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS order_item
(
    id          IDENTITY,
    order_id    LONG NOT NULL,
    cart_product_id LONG NOT NULL,
    price       DOUBLE,
    PRIMARY KEY (id),
    CONSTRAINT order_item_fk FOREIGN KEY (cart_product_id) REFERENCES cart_product (id)
);

CREATE TABLE IF NOT EXISTS order_detail
(
    id              IDENTITY,
    order_id        LONG        NOT NULL,
    product_type    VARCHAR(50) NOT NULL,
    product_id   LONG        NOT NULL,
    product_name VARCHAR(50)    NOT NULL,
    PRIMARY KEY (id)
    );




