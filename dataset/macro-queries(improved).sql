BEGIN DBMS_RANDOM.SEED(12345); END;
/
SET TIMING ON

WITH selected_points AS (
  SELECT /*+ SAMPLE(1) */ id, geometry
  FROM buildings3d
  WHERE ROWNUM <= 100
),
subway_analysis AS (
  SELECT 
    s.id,
    COUNT(*) as population_density,
    MIN(SDO_GEOM.SDO_DISTANCE(
      b.geometry, s.geometry, 0.005, 'unit=meter'
    )) as min_distance_meters
  FROM buildings3d b
  JOIN selected_points s ON SDO_WITHIN_DISTANCE(
    b.geometry, s.geometry, 'distance=25 unit=meter'
  ) = 'TRUE'
  WHERE b.id != s.id
  GROUP BY s.id
)
SELECT id, population_density, min_distance_meters
FROM subway_analysis
ORDER BY population_density DESC, min_distance_meters
FETCH FIRST 5 ROWS ONLY;

----------------------------------------------------------------------

BEGIN DBMS_RANDOM.SEED(12345); END;
/

WITH selected_points AS (
  SELECT /*+ SAMPLE(1) */ id, geometry
  FROM buildings3d
  WHERE ROWNUM <= 100
),
expansion_analysis AS (
  SELECT 
    s.id,
    COUNT(*) as building_density,
    MIN(SDO_GEOM.SDO_DISTANCE(
      r.geometry, s.geometry, 0.005, 'unit=meter'
    )) as distance_to_nearest,
    AVG(SDO_GEOM.SDO_DISTANCE(
      r.geometry, s.geometry, 0.005, 'unit=meter'
    )) as avg_distance
  FROM buildings3d r
  JOIN selected_points s ON SDO_WITHIN_DISTANCE(
    r.geometry, s.geometry, 'distance=500 unit=meter'
  ) = 'TRUE'
  WHERE r.id != s.id
  GROUP BY s.id
)
SELECT id, building_density, distance_to_nearest, avg_distance
FROM expansion_analysis
WHERE building_density < 50
ORDER BY building_density ASC, avg_distance DESC
FETCH FIRST 5 ROWS ONLY;

----------------------------------------------------------------

BEGIN DBMS_RANDOM.SEED(12345); END;
/

WITH selected_points AS (
  SELECT /*+ SAMPLE(1) */ id, geometry
  FROM buildings3d
  WHERE ROWNUM <= 50
),
route_analysis AS (
  SELECT 
    s.id,
    COUNT(*) as building_density,
    MAX(SDO_GEOM.SDO_DISTANCE(
      r.geometry, s.geometry, 0.005, 'unit=meter'
    )) as clearance_radius
  FROM buildings3d r
  JOIN selected_points s ON SDO_WITHIN_DISTANCE(
    r.geometry, s.geometry, 'distance=100 unit=meter'
  ) = 'TRUE'
  WHERE r.id != s.id
  GROUP BY s.id
)
SELECT id, building_density, clearance_radius
FROM route_analysis
ORDER BY building_density ASC, clearance_radius DESC
FETCH FIRST 5 ROWS ONLY;

-----------------------------------------------------

SET TIMING ON;

SELECT 
  ROWID, 
  SDO_GEOM.SDO_LENGTH(geometry, 0.005) AS length_3d
FROM synthetic_cells;

-------------------------------------------------------

SET TIMING ON;

SELECT 
  a.ROWID as cell1_id, 
  b.ROWID as cell2_id, 
  CASE 
    WHEN SDO_GEOM.RELATE(a.geometry, 'ANYINTERACT', b.geometry, 0.005) = 'TRUE' 
    THEN 1 
    ELSE 0 
  END AS intersection_3d
FROM synthetic_cells a, synthetic_cells b
WHERE a.ROWID < b.ROWID 
  AND SDO_WITHIN_DISTANCE(a.geometry, b.geometry, 'distance=10') = 'TRUE' 
  AND ROWNUM <= 10000;
  
  ----------------------------------------------------------
  
  SET TIMING ON

BEGIN DBMS_RANDOM.SEED(12345); END;
/

WITH selected_points AS (
  SELECT /*+ SAMPLE(1) */ id, geometry
  FROM buildings3d
  WHERE ROWNUM <= 100
),
bridge_analysis AS (
  SELECT 
    s.id,
    COUNT(*) as connection_points,
    MAX(SDO_GEOM.SDO_DISTANCE(
      SDO_CS.MAKE_3D(b.geometry, 75),
      SDO_CS.MAKE_3D(s.geometry, 75),
      0.005, 'unit=meter'
    )) as bridge_span
  FROM buildings3d b
  JOIN selected_points s ON SDO_WITHIN_DISTANCE(
    b.geometry, s.geometry, 'distance=100 unit=meter'
  ) = 'TRUE'
  WHERE b.id != s.id
  GROUP BY s.id
)
SELECT id, connection_points, bridge_span
FROM bridge_analysis
WHERE connection_points > 5
ORDER BY bridge_span DESC
FETCH FIRST 5 ROWS ONLY;


-------------------------------------------------------------------
SET TIMING ON

BEGIN DBMS_RANDOM.SEED(12345); END;
/

WITH selected_points AS (
  SELECT /*+ SAMPLE(1) */ id, geometry
  FROM buildings3d
  WHERE ROWNUM <= 100
),
garden_analysis AS (
  SELECT 
    s.id,
    COUNT(*) as space_score,
    MIN(SDO_GEOM.SDO_DISTANCE(
      SDO_CS.MAKE_3D(b.geometry, 100),
      SDO_CS.MAKE_3D(s.geometry, 100),
      0.005, 'unit=meter'
    )) as clear_space
  FROM buildings3d b
  JOIN selected_points s ON SDO_WITHIN_DISTANCE(
    b.geometry, s.geometry, 'distance=150 unit=meter'
  ) = 'TRUE'
  WHERE b.id != s.id
  GROUP BY s.id
)
SELECT id, space_score, clear_space
FROM garden_analysis
ORDER BY clear_space DESC
FETCH FIRST 5 ROWS ONLY;



