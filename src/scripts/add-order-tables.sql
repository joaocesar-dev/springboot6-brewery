drop table if exists beer_order_line;
drop table if exists beer_order;

CREATE TABLE `beer_order`
(
    id                 varchar(36) NOT NULL,
    created_date       datetime(6)  DEFAULT NULL,
    customer_ref       varchar(255) DEFAULT NULL,
    last_modified_date datetime(6)  DEFAULT NULL,
    version            bigint       DEFAULT NULL,
    customer_id        varchar(36)  DEFAULT NULL,
    PRIMARY KEY (id),
    INDEX `ix_beer_order_customer` (`customer_id` ASC) VISIBLE,
    CONSTRAINT `fk_beer_order_customer` FOREIGN KEY (customer_id) REFERENCES customer (id)
) ENGINE = InnoDB;

CREATE TABLE `beer_order_line`
(
    id                 varchar(36) NOT NULL,
    beer_id            varchar(36) DEFAULT NULL,
    created_date       datetime(6) DEFAULT NULL,
    last_modified_date datetime(6) DEFAULT NULL,
    order_quantity     int         DEFAULT NULL,
    quantity_allocated int         DEFAULT NULL,
    version            bigint      DEFAULT NULL,
    beer_order_id      varchar(36) DEFAULT NULL,
    PRIMARY KEY (id),
    INDEX `ix_beer_order_line_beer` (`beer_id` ASC) VISIBLE,
    INDEX `ix_beer_order_line_order` (`beer_order_id` ASC) VISIBLE,
    CONSTRAINT `fk_beer_order_line_beer` FOREIGN KEY (beer_order_id) REFERENCES beer_order (id),
    CONSTRAINT `fk_beer_order_line_order` FOREIGN KEY (beer_id) REFERENCES beer (id)
) ENGINE = InnoDB;
