Things yall need to download :
- APACHE Netbeans, regular netbeans trash

- Mysql Workbench :
  https://dev.mysql.com/get/Downloads/MySQLInstaller/mysql-installer-community-8.0.46.0.msi
  Copy on ur browser url nd pick a path
  
- Mysql Connector Jar:
  https://dev.mysql.com/downloads/connector/j/
  Select Operating System - > PICK PLATFORM INDEPENTENT

After allat js open up ur Apache netbeans, click FILE -> New Project -> Java with Ant -> Java Application -> Hit Next

Name yo shi and click Finish

And then Right Click on yall project name (Js right click the coffee icon) -> Properties -> Source -> and then COPY your mfs 'Project Folder Path'
Next Open up cmd, type :
cd 'path to yo project path shi'
hit enter
then type:
git clone 'this repo link'

owh yeah one more thingy, Right Click on 'Libraries' Folder -> ADD JAR/Folder -> Find yo mysql Jar connector path, hit that mfs and done;

DATABASE SETUP

Open ur Mysql, go to Query tab (search how) and Copy Paste the SQL Commands under, CTRL + A, and hit the thunder bolt icon:

CREATE DATABASE electronic_shop;
USE electronic_shop;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(30) NOT NULL
);

INSERT INTO users(username,password)
VALUES
('admin','12345');

CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    brand VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,
    stock INT NOT NULL
);

INSERT INTO products(name,brand,category,price,stock)
VALUES
('Mouse M100','Logitech','Mouse',120000,25),
('Keyboard K120','Logitech','Keyboard',175000,20),
('SSD 512GB','Kingston','Storage',650000,10),
('Flashdisk 64GB','Sandisk','Storage',95000,40);

DROP TABLE transactions;

CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total DOUBLE NOT NULL,
    payment DOUBLE NOT NULL,
    change_money DOUBLE NOT NULL
);

CREATE TABLE transaction_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_id INT NOT NULL,
    product_id INT NOT NULL,
    price DOUBLE NOT NULL,
    qty INT NOT NULL,
    subtotal DOUBLE NOT NULL,
    FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

ALTER TABLE transactions 
DROP COLUMN invoice;


DESCRIBE transactions;

DESCRIBE transaction_details;


