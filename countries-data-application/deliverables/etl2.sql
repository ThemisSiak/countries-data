LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/countries.csv'
INTO TABLE countries
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(iso, iso3, isoCode, fips, displayName, officialName, capital, continent, currencyCode, currencyName, phone, regionCode, regionName, subRegionCode, subRegionName, intermediateRegionCode, intermediateRegionName, statusC, development, sids, lldc, ldc, areaSqKm, population);

CREATE TEMPORARY TABLE temp_temperatureChange (
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
    valueChange VARCHAR(255) -- Use VARCHAR to load all data without errors
) ENGINE = InnoDB;

LOAD DATA LOCAL INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Annual_Surface_Temperature_Change.csv'
INTO TABLE temp_temperatureChange
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(isoCode, country, iso2, iso3, indicatorC, unit, sourceC, ctsCode, ctsName, ctsFullDescriptor, yearChange, valueChange);

INSERT INTO temperatureChange (isoCode, country, iso2, iso3, indicatorC, unit, sourceC, ctsCode, ctsName, ctsFullDescriptor, yearChange, valueChange)
SELECT isoCode, country, iso2, iso3, indicatorC, unit, sourceC, ctsCode, ctsName, ctsFullDescriptor, yearChange,
    CASE
        WHEN valueChange REGEXP '^-?[0-9]+(\.[0-9]+)?$' THEN CAST(valueChange AS DOUBLE)
        ELSE NULL
    END
FROM temp_temperatureChange;

DROP TEMPORARY TABLE temp_temperatureChange;

CREATE TEMPORARY TABLE temp_climateDisasters (
    isoCode INT,
    country VARCHAR(255),
    iso2 VARCHAR(10),
    iso3 VARCHAR(10),
    indicatorC VARCHAR(255),
    unit VARCHAR(255),
    sourceC VARCHAR(255),
    ctsCode VARCHAR(10),
    ctsName VARCHAR(255),
    ctsFullDescriptor VARcHAR(255),
    yearChange VARCHAR(255),
    valueChange VARCHAR(255) -- Use VARCHAR to load all data without errors
) ENGINE = InnoDB;

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Climate-related_Disasters_Frequency.csv'
INTO TABLE temp_climateDisasters
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(isoCode, country, iso2, iso3, indicatorC, unit, source, ctsCode, ctsName, ctsFullDescriptor, yearChange, valueChange);


INSERT INTO climateDisasters (isoCode, country, iso2, iso3, indicatorC, unit, sourceC, ctsCode, ctsName, ctsFullDescriptor, yearChange, valueChange)
SELECT isoCode, country, iso2, iso3, indicatorC, unit, sourceC, ctsCode, ctsName, ctsFullDescriptor, yearChange,
    CASE
        WHEN valueChange REGEXP '^-?[0-9]+$' THEN CAST(valueChange AS SIGNED)
        ELSE NULL
    END
FROM temp_climateDisasters;

DROP TEMPORARY TABLE temp_climateDisasters;

CREATE TEMPORARY TABLE temp_landCover (
    isoCode INT,
    country VARCHAR(255),
    iso2 VARCHAR(10),
    iso3 VARCHAR(10),
    indicatorC VARCHAR(255),
    unit VARCHAR(255),
    sourceC TEXT,
    ctsCode VARCHAR(10),
    ctsName VARCHAR(255),
    ctsFullDescriptor VARCHAR(255),
    climateInfluence VARCHAR(255),
    yearChange VARCHAR(255),
    valueChange VARCHAR(255) -- Use VARCHAR to load all data without errors
) ENGINE = InnoDB;

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Land_Cover_Accounts.csv'
INTO TABLE temp_landCover
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(isoCode, country, iso2, iso3, indicatorC, unit, sourceC, ctsCode, ctsName, ctsFullDescriptor, climateInfluence, yearChange, valueChange);

INSERT INTO landCover (isoCode, country, iso2, iso3, indicatorC, unit, sourceC, ctsCode, ctsName, ctsFullDescriptor, climateInfluence, yearChange, valueChange)
SELECT isoCode, country, iso2, iso3, indicatorC, unit, sourceC, ctsCode, ctsName, ctsFullDescriptor, climateInfluence, yearChange,
    CASE
        WHEN valueChange REGEXP '^-?[0-9]+(\.[0-9]+)?$' THEN CAST(valueChange AS FLOAT)
        ELSE NULL
    END
FROM temp_landCover;

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Forest_and_Carbon.csv'
INTO TABLE forestCarbon
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(isoCode, country, iso2, iso3, indicatorC, unit, sourceC, ctsCode, ctsName, ctsFullDescriptor, yearChange, valueChange);