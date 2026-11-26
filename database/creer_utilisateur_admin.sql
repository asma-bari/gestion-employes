CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'Asmabari.200';

GRANT ALL PRIVILEGES ON gestion_employes.* TO 'admin'@'localhost';
FLUSH PRIVILEGES;
