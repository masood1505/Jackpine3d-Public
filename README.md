Jackpine Benchmark 3D - Complete Setup Guide

datasets : https://unbcloud-my.sharepoint.com/:f:/g/personal/w3kq9_unb_ca/ElbXLYvhgm9NnIM4iW_rRy8Be4UCZhbGbtQagWhIBf_JCA

1. PostgreSQL Database Setup
Install PostgreSQL and PostGIS

# Update package list
sudo apt update

# Install PostgreSQL
sudo apt install postgresql postgresql-contrib

# Install PostGIS extension
sudo apt install postgis postgresql-14-postgis-3

# Install GDAL tools for data import
sudo apt install gdal-bin

# Start PostgreSQL service
sudo systemctl start postgresql
sudo systemctl enable postgresql

Configure PostgreSQL
# Edit PostgreSQL configuration
sudo nano /home/w3kq9/postgres16/data/postgresql.conf

# Change buffer settings (find and modify):
shared_buffers = 2048MB          # Changed from 128MB

# Save and exit

Database and User Setup
# Switch to postgres user
sudo -i -u postgres

# Create the database
createdb citymodel

# Connect to database and set password
psql -d citymodel
-- Set password for postgres user
ALTER USER postgres WITH PASSWORD '1505';

-- Enable PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis_topology;

-- Verify PostGIS installation
SELECT PostGIS_Full_Version();

-- Exit psql
\q
Service Management
# Start PostgreSQL
sudo service postgresql start

# Stop PostgreSQL
sudo service postgresql stop

# Restart PostgreSQL (after config changes)
sudo service postgresql restart

# Check service status
sudo service postgresql status

Test Connection
# Test database connection
psql -h localhost -U postgres -d citymodel

# If successful, you should see:
# citymodel=#

2. Data Import
# Import Riverside County data (files 0-19)
for i in {0..19}; do 
    FILE="California-06065-$(printf "%03d" $i).gml"
    if [ -f "$FILE" ]; then 
        echo "Processing $FILE into table riversidenew3d..."
        ogr2ogr -f "PostgreSQL" PG:"host=localhost dbname=citymodel user=postgres password=1505" \
            "$FILE" \
            -nln riversidenew3d \
            -append \
            -a_srs EPSG:4326
    else
        echo "File $FILE not found, skipping..."
    fi
done

# Connect to database
psql -h localhost -U postgres -d citymodel

# Check imported tables
\dt

# Check record counts
SELECT COUNT(*) FROM sandiegocounty;
SELECT COUNT(*) FROM riversidenew3d;

# Check spatial reference
SELECT Find_SRID('public', 'sandiegocounty', 'wkb_geometry');
SELECT Find_SRID('public', 'riversidenew3d', 'wkb_geometry');

1. Oracle Database SetupInstall Docker (if not already installed)

2. # Update package list
sudo apt update

# Install Docker
sudo apt install docker.io

# Start and enable Docker
sudo systemctl start docker
sudo systemctl enable docker

# Add user to docker group (optional, to avoid sudo)
sudo usermod -aG docker $USER
# Log out and back in for group changes to take effect
Deploy Oracle Database Container
# Pull and run Oracle Database Express Edition
docker run -d \
  -p 1521:1521 -p 5500:5500 \
  --name oracle-db \
  -e ORACLE_PDB=XE \
  -e ORACLE_PASSWORD=15051505 \
  -e ORACLE_CHARACTERSET=AL32UTF8 \
  container-registry.oracle.com/database/express:latest

# Check container status
docker ps

# Wait for database to be ready (may take several minutes)
docker logs oracle-db
Configure Oracle Database
# Connect to container
docker exec -it oracle-db bash

# Connect as SYSDBA to main database
sqlplus sys/15051505@localhost:1521/XE as sysdba
-- Verify connection
SELECT name FROM v$database;

-- Set SYS password (if needed)
ALTER USER SYS IDENTIFIED BY "15051505";

-- Exit and connect to PDB
exit
# Connect to Pluggable Database (PDB) where tables will be stored
sqlplus sys/15051505@//localhost:1521/xepdb1 as sysdba
Create Benchmark User and Schema
-- Create tablespace
CREATE TABLESPACE jackpine_tbs 
DATAFILE 'jackpine_tbs.dbf' SIZE 1G AUTOEXTEND ON;

-- Create user
CREATE USER jackpine IDENTIFIED BY jackpine123
DEFAULT TABLESPACE jackpine_tbs
QUOTA UNLIMITED ON jackpine_tbs;

-- Grant necessary privileges
GRANT CONNECT, RESOURCE, CREATE VIEW, CREATE SEQUENCE TO jackpine;
GRANT SELECT ANY TABLE TO jackpine;
GRANT INSERT ANY TABLE TO jackpine;
GRANT UPDATE ANY TABLE TO jackpine;
GRANT DELETE ANY TABLE TO jackpine;

-- Enable Oracle Spatial (if not already enabled)
GRANT EXECUTE ON MDSYS.SDO_GEOMETRY TO jackpine;
GRANT EXECUTE ON MDSYS.SDO_GEOM TO jackpine;
GRANT EXECUTE ON MDSYS.SDO_CS TO jackpine;

-- Exit SYSDBA
exit
Test Connection as Jackpine User
# Connect as jackpine user
sqlplus jackpine/jackpine123@//localhost:1521/xepdb1

# Test spatial functionality
SELECT MDSYS.SDO_VERSION FROM DUAL;

-- Exit
exit
2. Data Import Setup
Prepare Java Data Loaders
# Ensure you have the Oracle JDBC driver
# Download ojdbc8.jar or ojdbc11.jar and place in your lib directory

# Compile the data loader classes
javac -cp "lib/*" JDBCDataLoader1.java
javac -cp "lib/*" JDBCDataLoader2.java  
javac -cp "lib/*" JDBCDataLoader3.java
Import Data Using Java Loaders
# Run data loaders in sequence
# Note: Ensure your GML data files are in the correct directory

# Load first dataset
java -cp ".:lib/*" JDBCDataLoader1

# Load second dataset
java -cp ".:lib/*" JDBCDataLoader2

# Load third dataset
java -cp ".:lib/*" JDBCDataLoader3

Verify Data Import
# Connect to Oracle as jackpine user
sqlplus jackpine/jackpine123@//localhost:1521/xepdb1
-- Check tables created by data loaders
SELECT table_name FROM user_tables;

-- Check record counts (adjust table names as per your loaders)
SELECT COUNT(*) FROM your_table_name_1;
SELECT COUNT(*) FROM your_table_name_2;
SELECT COUNT(*) FROM your_table_name_3;

-- Verify spatial data
SELECT COUNT(*) FROM user_sdo_geom_metadata;

-- Check coordinate system
SELECT srid FROM mdsys.cs_srs WHERE cs_name LIKE '%8307%';

-- Exit
exit
3. Update Configuration for Oracle
# Edit configuration file
nano connection_general.properties
##################################################
# Set to 'oracle' for Oracle database
DBMS=oracle
iterations=1
maxdistance=1000

#################################################
# Oracle connection settings
#################################################
url=jdbc:oracle:thin:@localhost:1521/xepdb1
user=jackpine
password=jackpine123
ORACLE_SRID=8307

#################################################
# PostgreSQL connection settings (commented out)
#################################################
#url=jdbc:postgresql://localhost:5432/citymodel
#user=postgres
#password=1505
#POSTGRESQL_SRID=4326

4. Database Management Commands
# Start Oracle container
docker start oracle-db

# Stop Oracle container
docker stop oracle-db

# Restart Oracle container
docker restart oracle-db

# Check container status
docker ps -a

# View container logs
docker logs oracle-db

# Remove container (if needed to start fresh)
docker rm oracle-db
Database Connections
# Connect as SYSDBA to main database
sqlplus sys/15051505@localhost:1521/XE as sysdba

# Connect as SYSDBA to PDB (where tables are stored)
sqlplus sys/15051505@//localhost:1521/xepdb1 as sysdba

# Connect as jackpine user for benchmarks
sqlplus jackpine/jackpine123@//localhost:1521/xepdb1

5. Running Benchmarks with Oracle
// In JackpineBenchmark3DLauncherAllScenarios.java
// Uncomment Oracle-compatible scenarios:
scenarios.add(new Building3DIntersectsLine(properties));
scenarios.add(new Building3DIntersectsArea(properties)); 
scenarios.add(new ConvexHull(properties));
// Add other Oracle-compatible scenarios as needed

Compile and Run

# Ensure Oracle JDBC driver is in classpath
javac -cp "lib/*:ojdbc8.jar" edu/toronto/cs/jackpine/benchmark/JackpineBenchmark3DLauncherAllScenarios.java

# Run benchmarks
java -cp ".:lib/*:ojdbc8.jar" edu.toronto.cs.jackpine.benchmark.JackpineBenchmark3DLauncherAllScenarios

