package edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario.edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario.threed;

import java.util.Random;

public class Address3DPicker {

    int roadNumber = 0;
    String roadName = "";
    String zipcode = "";
    double elevation = 0.0;
    double buildingHeight = 0.0;
    int numFloors = 0;
    String buildingType = "";
    Random rnd = null;
    
    static final int TOTAL_ADDRESSES = 50;
    static final Address3DPicker[] addresses = {
        new Address3DPicker(1743,"SOUTH","19146", 10.0, 30.0, 3, "Residential"),
        new Address3DPicker(1743,"SOUTH","19146", 10.0, 30.0, 3, "Residential"),
        new Address3DPicker(8128, "Rowland", "19136", 12.5, 40.0, 4, "Residential"),
        new Address3DPicker(773, "N Pallas", "19104", 15.2, 25.0, 2, "Residential"),
        new Address3DPicker(262, "E Cliveden", "19119", 18.0, 35.0, 3, "Residential"),
        new Address3DPicker(7010, "Keystone", "19135", 9.5, 28.0, 2, "Residential"),
        new Address3DPicker(1300, "Lindley", "19141", 11.0, 32.0, 3, "Residential"),
        new Address3DPicker(6009, "Bingham", "19111", 13.5, 38.0, 3, "Residential"),
        new Address3DPicker(2131, "Christian", "19146", 10.8, 29.0, 2, "Residential"),
        new Address3DPicker(1311, "Arrott", "19124", 12.0, 31.0, 3, "Residential"),
        new Address3DPicker(5008, "Chestnut", "19139", 14.5, 45.0, 4, "Commercial"),
        new Address3DPicker(5338, "Darrah", "19124", 11.5, 33.0, 3, "Residential"),
        new Address3DPicker(532, "Turner", "19122", 13.0, 36.0, 3, "Residential"),
        new Address3DPicker(5457, "Wayne", "19144", 16.5, 42.0, 4, "Residential"),
        new Address3DPicker(5912, "Washington", "19143", 12.8, 34.0, 3, "Residential"),
        
        new Address3DPicker(7010, "Keystone", "19135", 9.5, 28.0, 2, "Residential"),
        new Address3DPicker(1300, "Lindley", "19141", 11.0, 32.0, 3, "Residential"),
        new Address3DPicker(6009, "Bingham", "19111", 13.5, 38.0, 3, "Residential"),
        new Address3DPicker(2131, "Christian", "19146", 10.8, 29.0, 2, "Residential"),
        new Address3DPicker(1743,"SOUTH","19146", 10.0, 30.0, 3, "Residential"),
        new Address3DPicker(1743,"SOUTH","19146", 10.0, 30.0, 3, "Residential"),
        new Address3DPicker(5008, "Chestnut", "19139", 14.5, 45.0, 4, "Commercial"),
        new Address3DPicker(5338, "Darrah", "19124", 11.5, 33.0, 3, "Residential"),
        new Address3DPicker(532, "Turner", "19122", 13.0, 36.0, 3, "Residential"),
        new Address3DPicker(5457, "Wayne", "19144", 16.5, 42.0, 4, "Residential"),
        new Address3DPicker(5912, "Washington", "19143", 12.8, 34.0, 3, "Residential"),
        new Address3DPicker(8128, "Rowland", "19136", 12.5, 40.0, 4, "Residential"),
        new Address3DPicker(773, "N Pallas", "19104", 15.2, 25.0, 2, "Residential"),
        new Address3DPicker(262, "E Cliveden", "19119", 18.0, 35.0, 3, "Residential"),
        new Address3DPicker(1311, "Arrott", "19124", 12.0, 31.0, 3, "Residential"),
        
        new Address3DPicker(7010, "Keystone", "19135", 9.5, 28.0, 2, "Residential"),
        new Address3DPicker(1300, "Lindley", "19141", 11.0, 32.0, 3, "Residential"),
        new Address3DPicker(6009, "Bingham", "19111", 13.5, 38.0, 3, "Residential"),
        new Address3DPicker(2131, "Christian", "19146", 10.8, 29.0, 2, "Residential"),
        new Address3DPicker(1743,"SOUTH","19146", 10.0, 30.0, 3, "Residential"),
        new Address3DPicker(5912, "Washington", "19143", 12.8, 34.0, 3, "Residential"),
        new Address3DPicker(8128, "Rowland", "19136", 12.5, 40.0, 4, "Residential"),
        new Address3DPicker(773, "N Pallas", "19104", 15.2, 25.0, 2, "Residential"),
        new Address3DPicker(262, "E Cliveden", "19119", 18.0, 35.0, 3, "Residential"),
        new Address3DPicker(1311, "Arrott", "19124", 12.0, 31.0, 3, "Residential"),
        new Address3DPicker(1743,"SOUTH","19146", 10.0, 30.0, 3, "Residential"),
        new Address3DPicker(5008, "Chestnut", "19139", 14.5, 45.0, 4, "Commercial"),
        new Address3DPicker(5338, "Darrah", "19124", 11.5, 33.0, 3, "Residential"),
        new Address3DPicker(532, "Turner", "19122", 13.0, 36.0, 3, "Residential"),
        new Address3DPicker(5457, "Wayne", "19144", 16.5, 42.0, 4, "Residential"),
        
        new Address3DPicker(773, "N Pallas", "19104", 15.2, 25.0, 2, "Residential"),
        new Address3DPicker(262, "E Cliveden", "19119", 18.0, 35.0, 3, "Residential"),
        new Address3DPicker(1311, "Arrott", "19124", 12.0, 31.0, 3, "Residential"),
        new Address3DPicker(1743,"SOUTH","19146", 10.0, 30.0, 3, "Residential"),
        new Address3DPicker(5008, "Chestnut", "19139", 14.5, 45.0, 4, "Commercial"),
    };
    
    Address3DPicker(int roadNumber, String roadName, String zipcode, 
                    double elevation, double buildingHeight, int numFloors, String buildingType) {
        this.roadNumber = roadNumber;
        this.roadName = roadName;
        this.zipcode = zipcode;
        this.elevation = elevation;
        this.buildingHeight = buildingHeight;
        this.numFloors = numFloors;
        this.buildingType = buildingType;
    }
    
    public Address3DPicker() {
        rnd = new Random(System.currentTimeMillis());
    }
    
    public int getTotalAddresses() {
        return TOTAL_ADDRESSES;
    }
    
    public Address3DPicker[] getAllAddresses() {
        return addresses;
    }
    
    public Address3DPicker nextAddress() {
        return addresses[rnd.nextInt(TOTAL_ADDRESSES)];
    }
    
    public int getRoadNumber() {
        return roadNumber;
    }
    
    public String getRoadName() {
        return roadName.toUpperCase();
    }
    
    public String getZipcode() {
        return zipcode;
    }
    
    public double getElevation() {
        return elevation;
    }
    
    public double getBuildingHeight() {
        return buildingHeight;
    }
    
    public int getNumFloors() {
        return numFloors;
    }
    
    public String getBuildingType() {
        return buildingType;
    }
}