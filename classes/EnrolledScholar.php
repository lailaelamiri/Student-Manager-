<?php
class EnrolledScholar {
    private $scholarId, $familyName, $givenName, $homeCity, $genderTag;

    function __construct($scholarId, $familyName, $givenName, $homeCity, $genderTag) {
        $this->scholarId  = $scholarId;
        $this->familyName = $familyName;
        $this->givenName  = $givenName;
        $this->homeCity   = $homeCity;
        $this->genderTag  = $genderTag;
    }

    function getFamilyName() { return $this->familyName; }
    function getGivenName()  { return $this->givenName;  }
    function getHomeCity()   { return $this->homeCity;   }
    function getGenderTag()  { return $this->genderTag;  }
}
?>
