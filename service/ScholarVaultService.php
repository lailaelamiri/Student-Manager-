<?php
include_once '../classes/EnrolledScholar.php';
include_once '../connexion/VaultGateway.php';
include_once '../dao/IVaultDao.php';

class ScholarVaultService implements IVaultDao {
    private $gateway;

    function __construct() {
        $this->gateway = new VaultGateway();
    }

    public function create($scholarObject) {
        $insertQuery = "INSERT INTO enrollees (familyName, givenName, homeCity, genderTag)
                        VALUES (:familyName, :givenName, :homeCity, :genderTag)";
        $preparedStmt = $this->gateway->getVaultLink()->prepare($insertQuery);
        $preparedStmt->execute([
            ':familyName' => $scholarObject->getFamilyName(),
            ':givenName'  => $scholarObject->getGivenName(),
            ':homeCity'   => $scholarObject->getHomeCity(),
            ':genderTag'  => $scholarObject->getGenderTag()
        ]);
    }

    public function delete($scholarObject) {}
    public function update($scholarObject) {}
    public function findById($scholarId)   {}

    public function findAll() {
        $scanQuery = "SELECT * FROM enrollees";
        $resultSet = $this->gateway->getVaultLink()->query($scanQuery);
        return $resultSet->fetchAll(PDO::FETCH_ASSOC);
    }
    public function removeById($scholarId) {
    $purgeQuery = "DELETE FROM enrollees WHERE scholarId = :scholarId";
    $preparedStmt = $this->gateway->getVaultLink()->prepare($purgeQuery);
    $preparedStmt->execute([':scholarId' => $scholarId]);
}

    public function amend($scholarId, $familyName, $givenName, $homeCity, $genderTag) {
    $reviseQuery = "UPDATE enrollees
                    SET familyName = :familyName,
                        givenName  = :givenName,
                        homeCity   = :homeCity,
                        genderTag  = :genderTag
                    WHERE scholarId = :scholarId";
    $preparedStmt = $this->gateway->getVaultLink()->prepare($reviseQuery);
    $preparedStmt->execute([
        ':familyName' => $familyName,
        ':givenName'  => $givenName,
        ':homeCity'   => $homeCity,
        ':genderTag'  => $genderTag,
        ':scholarId'  => $scholarId
    ]);
}
}
?>
