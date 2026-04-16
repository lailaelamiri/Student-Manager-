package com.example.ivoryledgerii.beans;

public class ScholarRecord {
    private int scholarId;
    private String familyName;
    private String givenName;
    private String homeCity;
    private String genderTag;

    public ScholarRecord() {}

    public ScholarRecord(int scholarId, String familyName, String givenName, String homeCity, String genderTag) {
        this.scholarId  = scholarId;
        this.familyName = familyName;
        this.givenName  = givenName;
        this.homeCity   = homeCity;
        this.genderTag  = genderTag;
    }

    public int getScholarId()      { return scholarId;  }
    public String getFamilyName()  { return familyName; }
    public String getGivenName()   { return givenName;  }
    public String getHomeCity()    { return homeCity;   }
    public String getGenderTag()   { return genderTag;  }

    @Override
    public String toString() {
        return "ScholarRecord{" +
                "scholarId=" + scholarId +
                ", familyName='" + familyName + '\'' +
                ", givenName='" + givenName + '\'' +
                ", homeCity='" + homeCity + '\'' +
                ", genderTag='" + genderTag + '\'' +
                '}';
    }
}