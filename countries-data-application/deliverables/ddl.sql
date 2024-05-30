CREATE TABLE IF NOT EXISTS countries(
	iso VARCHAR(10) NOT NULL,
    iso3 VARCHAR(10) NOT NULL UNIQUE,
    isoCode INT PRIMARY KEY,
    fips VARCHAR(255) NOT NULL,
    displayName VARCHAR(255) NOT NULL,
    officialName VARCHAR(255) NOT NULL,
    capital VARCHAR(255) NOT NULL,
    continent VARCHAR(255) NOT NULL,
    currencyCode VARCHAR(255) NOT NULL,
    currencyName VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    regionCode INT NOT NULL,
    regionName VARCHAR(255) NOT NULL,
    subRegionCode INT NOT NULL,
    subRegionName VARCHAR(255) NOT NULL,
    intermediateRegionCode VARCHAR(255),
    intermediateRegionName VARCHAR(255),
    statusC VARCHAR(255) NOT NULL,
    development VARCHAR(255) NOT NULL,
    sids VARCHAR(10),
    lldc VARCHAR(10),
    ldc VARCHAR(10),
    areaSqKm INT NOT NULL,
    population INT NOT NULL
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS temperatureChange(
	id INT AUTO_INCREMENT PRIMARY KEY,
	isoCode INT,
	country VARCHAR(255),
	iso2 VARCHAR(10),
    iso3 VARCHAR(10),
	indicatorC VARCHAR(255),
	unit VARCHAR(255),
	sourceC VARCHAR(255),
	ctsCode VARCHAR(10),
	ctsName VARCHAR(255),
	ctsFullDescriptor VARCHAR(255),
	yearChange VARCHAR(255),
	valueChange FLOAT,
	FOREIGN KEY (isoCode) REFERENCES countries(isoCode)
)ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS climateDisasters(
	id INT AUTO_INCREMENT PRIMARY KEY,
	isoCode INT,
	country VARCHAR(255),
	iso2 VARCHAR(10),
    iso3 VARCHAR(10),
	indicatorC VARCHAR(255),
	unit VARCHAR(255),
	sourceC VARCHAR(255),
	ctsCode VARCHAR(10),
	ctsName VARCHAR(255),
	ctsFullDescriptor VARCHAR(255),
	yearChange VARCHAR(255),
	valueChange INT,
	FOREIGN KEY (isoCode) REFERENCES countries(isoCode)
)ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS landCover(
	id INT AUTO_INCREMENT PRIMARY KEY,
	isoCode INT,
	country VARCHAR(255),
	iso2 VARCHAR(10),
    iso3 VARCHAR(10),
	indicatorC VARCHAR(255),
	unit VARCHAR(255),
	sourceC VARCHAR(255),
	ctsCode VARCHAR(10),
	ctsName VARCHAR(255),
	ctsFullDescriptor VARCHAR(255),
	climateInfluence VARCHAR(255),
	yearChange VARCHAR(255),
	valueChange FLOAT,
	FOREIGN KEY (isoCode) REFERENCES countries(isoCode)
)ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS forestCarbon(
	id INT AUTO_INCREMENT PRIMARY KEY,
	isoCode INT,
	country VARCHAR(255),
	iso2 VARCHAR(10),
    iso3 VARCHAR(10),
	indicatorC VARCHAR(255),
	unit VARCHAR(255),
	sourceC VARCHAR(255),
	ctsCode VARCHAR(10),
	ctsName VARCHAR(255),
	ctsFullDescriptor VARCHAR(255),
	yearChange VARCHAR(255),
	valueChange FLOAT,
	FOREIGN KEY (isoCode) REFERENCES countries(isoCode)
)ENGINE = InnoDB;
