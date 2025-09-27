Jackpine Benchmark 3D - Complete Setup Guide

# Jackpine Benchmark 3D - Introduction and Goals

## Overview

The Jackpine Benchmark 3D is a spatial database performance evaluation framework designed to compare how different database systems handle complex 3D geospatial data operations. This setup guide provides complete instructions for implementing the benchmark across three major platforms: PostgreSQL with PostGIS, Oracle Spatial, and SpatiaLite, using real-world California county geographic data.

The primary goal is to establish standardized performance testing for 3D spatial operations including building intersections, distance calculations, and geometric analysis queries. The benchmark uses authentic datasets from Riverside county to evaluate each database's capability in processing large-scale 3D building geometries, polyhedral surfaces, and complex spatial relationships under realistic data volumes and query workloads.


datasets : https://zenodo.org/records/17215091?token=eyJhbGciOiJIUzUxMiJ9.eyJpZCI6IjNlZmJhZjM4LTE1NjQtNGM1YS05N2FlLTNmZjBmN2JiNmE0YyIsImRhdGEiOnt9LCJyYW5kb20iOiJhZjA0MTVhMGFlNTMxNzdmNTIyYjQ2M2M4MWY0M2RmZSJ9.96vp_4WIyxdCxWZ50RD19XslMgRSerZ2c_mUTYVElPZ43WpFcYvmG_khzFoVlO8Gl4t1D8hXydbpBWNfCvnhVQ

##### PostgreSQL Database Setup

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

6. Oracle Macro Queries and Analysis Queries
The macro queries and analysis queries for Oracle are provided separately in the datasets folder within the repository. These need to be executed manually and are not run through the Java benchmark code.
Running Oracle Macro Queries
# Navigate to the dataset folder in your repository
cd dataset

# Connect to Oracle database as jackpine user
sqlplus jackpine/jackpine123@//localhost:1521/xepdb1

-- Execute the macro queries file
@macro-queries.sql

-- Or run from command line:
sqlplus jackpine/jackpine123@//localhost:1521/xepdb1 @datasets/macro-queries.sql

-- Exit when done
exit
Running Oracle Analysis Queries
# Navigate to the datasets folder in your repository
cd datasets

# Connect to Oracle database
sqlplus jackpine/jackpine123@//localhost:1521/xepdb1

-- Execute the analysis queries file
@analysis-queries.sql

-- Or run from command line:
sqlplus jackpine/jackpine123@//localhost:1521/xepdb1 @datasets/analysis-queries.sql

-- To save query output to a file:
spool analysis_results.txt
@analysis-queries.sql
spool off

-- Exit when done
exit
# SpatiaLite Installation and Data Import Guide

This guide covers installing SpatiaLite, importing GML and CSV data, and running spatial queries.

## Installation

Install SpatiaLite binary tools:
```bash
sudo apt install spatialite-bin
```

Installation location: `/home/w3kq9/software/spatiallite`

## Database Connection

Connect to your database:
```bash
sqlite3 myspatialdb.sqlite
# or
spatialite /path/to/your/database.sqlite
```

## Data Import

### Important Limitations
⚠️ **SpatiaLite does not accept polyhedral surfaces from GML files**
- Use CSV files instead for complex geometry types
- CSV files provide better compatibility with various geometry formats

### GML Import (Basic Polygons Only)

```bash
# Basic import (may fail with complex geometries)
ogr2ogr -f "SQLite" -dsco SPATIALITE=YES your_database.sqlite California-06065-000.gml -nln cali_data

# Working version with geometry type specification
ogr2ogr -f "SQLite" -dsco SPATIALITE=YES your_database.sqlite California-06065-000.gml -nln cali_data -nlt MULTIPOLYGONZ
```

### Batch GML Import

```bash
# Create initial database with first file
ogr2ogr -f "SQLite" -dsco SPATIALITE=YES riverside.sqlite California-06065-000.gml -nln riverside -nlt MULTIPOLYGONZ

# Append additional files (csh/tcsh syntax)
foreach i ( 001 002 003 004 005 006 007 008 009 010 011 012 013 014 015 016 017 018 019 )
    echo "Importing California-06065-$i.gml..."
    ogr2ogr -append -f "SQLite" riverside.sqlite California-06065-$i.gml -nln riverside -nlt MULTIPOLYGONZ
end
```

### CSV Import (Recommended for Complex Geometries)

```bash
# Import first CSV file
ogr2ogr -f "SQLite" -dsco SPATIALITE=YES riverside.sqlite riverside0.csv \
  -oo GEOM_POSSIBLE_NAMES=WKT \
  -oo KEEP_GEOM_COLUMNS=NO \
  -nln buildings \
  -nlt MULTIPOLYGONZ

# Batch import remaining CSV files (bash syntax)
for i in {1..19}; do
    ogr2ogr -f "SQLite" -update -append \
      -dsco SPATIALITE=YES \
      -oo GEOM_POSSIBLE_NAMES=WKT \
      -oo KEEP_GEOM_COLUMNS=NO \
      -nln buildings \
      -nlt MULTIPOLYGONZ \
      riverside.sqlite riverside${i}.csv
done
```

### Shapefile Import

```bash
# Import landmarks shapefile
spatialite_tool -i -shp tiger_landmarks_3d -d riverside.sqlite -t arealm3dNew -c CP1252

# Import roads shapefile
spatialite_tool -i -shp tiger_roads_3d_all_local2 -d /home/w3kq9/Desktop/Java-Shapefile-Parser-master/JARs/riverside.sqlite -t roads3d -c CP1252
```

## Database Operations

### Essential Setup
**Always load the SpatiaLite extension:**
```sql
SELECT load_extension('mod_spatialite');
```

### Data Inspection
```sql
-- Count records
SELECT COUNT(*) FROM riverside;

-- View sample data
SELECT * FROM riverside LIMIT 5;

-- List all tables
.tables
```

## Important Notes

### File Organization
- **All dataset files must be in the same directory as the SQLite database**
- Ensure consistent directory structure for successful imports

### Main Database Location
```
/home/w3kq9/Desktop/Java-Shapefile-Parser-master/JARs/riverside.sqlite
```

## Sample Query with Timing

```sql
-- Record start time
WITH start AS (SELECT datetime('now') AS start_time)
SELECT start_time FROM start;

-- Main spatial query
SELECT SUM(ST_3DDistance(a.geometry, r.lod1solid)) AS total_distance
FROM arealm3dNew a
JOIN riverside r ON ST_Intersects(a.geometry, r.lod1solid);

-- Record end time
WITH end AS (SELECT datetime('now') AS end_time)
SELECT end_time FROM end;
```

### Example Timing Results
- Start time: 2024-11-12 19:00:13
- End time: 2024-11-13 00:50:15
- Duration: ~5 hours 50 minutes

## Additional Resources

- Additional queries are available in: `dataset/spatiallite-queries.sql`
- Refer to SpatiaLite documentation for advanced spatial operations
