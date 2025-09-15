package edu.toronto.cs.jackpine.benchmark.db.threed;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect.SupportedSqlDialect;

public interface SpatialSqlDialect3D {
	
	public static  enum SupportedSqlDialect {PostgreSQL, SQLServer, Oracle, SpatiaLite };
	
	public SupportedSqlDialect getSqlDialectType();
	
    String getSelectAllFeaturesWithinADistanceFromPoint();
    String getSelectBoundingBoxSearch();
    String getSelectBuffer3D();
    String getSelectClosestPoint3D();
    String getSelectContains3D();
    String getSelectConvexHull3D();
    String getSelectCrosses3D();
    String getSelectDistance3D();
    String getSelectDistanceWithin3D();
    String getSelectDWithin3D();
    String getSelectFullyWithin3D();
    String getSelectIntersects3D();
    String getSelectIs3D();
    String getSelectLength3D();
    String getSelectLongestLine3D();
    String getSelectMaxDistance3D();
    String getSelectShortestLine3D();
    String getSelectReadSpatialBufferPolygon3D();
    String getSelectReadSpatialLongestLine3D();
    String getSelectAreaContainsArea();
    String getLandUseQueries(); // Adding the missing method
    
    

    public String getSelectVolumeContainsLine3D();
    public String getSelectVolumeContainsRegion3D();
    public String getSelectRegionInsideVolume3D();
    public String getSelectRegionCrossesVolume3D();
    public String getSelectRegionIntersectsVolume3D();
    public String getSelectLineIntersectsVolume3D();
    public String getSelectLineWithinVolume3D();
    public String getSelectLineContainsVolume3D();
    public String getSelect3DRegionContainsRegion();
    public String getSelect3DRegionEqualsRegion();
    public String getSelect3DRegionContainsLine();
    public String getSelect3DLineCrossesRegion();
    public String getSelect3DLineWithinRegion();
    public String getSelect3DPointWithinRegion();
    public String getSelect3DLineEqualsLine();
    public String getSelect3DLineCrossesLine();
    public String getSelect3DPointEqualsPoint();
    public String getSelect3DPointIntersectsLine();
    public String getSelectLineOverlapsLine3D();
    public String getSelectRegionContainsPoint3D();
    public String getSelectRegionCoversPoint3D();
    public String getSelectRegionCoversLine3D();
    public String getSelectRegionCoversRegion3D();
    public String getSelectVolumeCoversLine3D();
    public String getSelectVolumeCoversRegion3D();
    public String getSelectPointCoveredByRegion3D();
    public String getSelectRegionCoveredByRegion3D();
    public String getSelectLineCoveredByVolume3D();
    public String getSelectRegionCoveredByVolume3D();
    
    public String getGeocodingQuery3D();
    
        boolean supportsJdbcUrl(String url);

		public String getCityStateForReverseGeocoding3D();

		public String getStreetAddressForReverseGeocoding3D();

		String getSelectBodyEqualsBody();

		String getSelectBodyOverlapsBody();

		public	String getSelectArealmIntersectsBuildings3Dvs3D();

		public String getSelect3DLineEqualsLinevs3D();

		public String getSelectArea3D();

		public String getSelectPerimeter3D();

		public String getSelect3DDistance3D();

		String getClosestPointAreaQuery();

		String getShortestLineAreaQuery();

		String getLongestLineAreaQuery();

		String getLineInterpolatePointAreaQuery();

		String getSelectBuildingsDifferenceArea3Dvs3D();

		String get3DConvexHullAreaQuery();

		String get3DUnionSAreaQuery();

		public String getSelectBuildingIntersectionArea3Dvs3D();

		String getSelectBuildingFullyWithinArea3d();

		String getSelectLineOverlapsLine3Dvs3DLine();

		String getSelectBuildingFullyWithinArea3DLine();

		String getSelectBuildingIntersectionArea3Dvs3DLine();

		String getSelectBuildingsDifferenceArea3Dvs3DLine();

		String getClosestPointAreaQueryLine();

		String getBuilding3DIntersectsLineQuery();
		
		String getBuilding3DIntersectsAreaQuery();

		String getLongestLineAreaQueryLine();

		String getLineInterpolatePointAreaQueryLine();

		String getSelectAreaContainsBuildings3D();

		String getSelectLineIsContainedBuilding3D();

		String getSelectLineOverlapsBuildings3D();

		String getSelectArealmOverlapsBuildings3D();

		public String getBuilding3DDistanceLineQuery();

		public String getBuilding3DDistanceAreaQuery();

		public String getBuilding3DDistanceWithinBuildingQuery();

		public String getBoundingBox3DQuery();

		public String getConvexHullQuery();

		public String getDimensionsQuery();

		public String getLength3DQuery();

		public String getPerimeter3DQuery();

		public String getBridgeAnalysisQuery();

		public String getCancerousAnalysisIntersectionQuery();

		public String getEmergencyRoutesQuery();

		public String getFutureExpansionQuery();

		public String getGardenAnalysisQuery();

		public String getLength3DMedicalAnalysisQuery();

		public String getPerimeter3DMedicalAnalysisQuery();

		public String getSubwayStationLocationQuery();

//		String getSelectLineLongestLine3D();
    
}
