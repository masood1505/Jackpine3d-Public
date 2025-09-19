
-- Record the start time
WITH start AS (SELECT datetime('now') AS start_time)
SELECT start_time FROM start;

-- Run your main query here (the one you want to time)
-- Example of your main query:
SELECT SUM(ST_3DDistance(a.geometry, r.lod1solid)) AS total_distance
FROM arealm3dNew a
JOIN riverside r ON ST_Intersects(a.geometry, r.lod1solid);

-- Record the end time
WITH end AS (SELECT datetime('now') AS end_time)
SELECT end_time FROM end;


-------------------------------------------------------------------------------


-- Record the start time
WITH start AS (SELECT datetime('now') AS start_time)
SELECT start_time FROM start;

-- Run your main query here (the one you want to time)
-- Example of your main query:
SELECT SUM(ST_3DDistance(a.geometry, r.lod1solid)) AS total_distance
FROM arealm3dNew a
JOIN riverside r ON ST_Intersects(a.geometry, r.lod1solid);

-- Record the end time
WITH end AS (SELECT datetime('now') AS end_time)
SELECT end_time FROM end;

-------------------------------------------------------------------
