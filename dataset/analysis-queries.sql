SELECT id, SDO_GEOM.SDO_MBR(geometry) AS bounding_box_3d FROM arealm3d; 

SELECT id, SDO_GEOM.SDO_CONVEXHULL(geometry, 0.005) AS convex_hull FROM arealm3d; 

SELECT DISTINCT t.geometry.SDO_GTYPE/1000 as dimensions FROM arealm3d t; 

SELECT SDO_GEOM.SDO_LENGTH(geometry, 0.005) as length_3d FROM arealm3d;


