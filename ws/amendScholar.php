<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    include_once '../service/ScholarVaultService.php';
    extract($_POST);
    $vaultService = new ScholarVaultService();
    $vaultService->amend(
        intval($scholarId),
        $familyName,
        $givenName,
        $homeCity,
        $genderTag
    );
    header('Content-Type: application/json');
    echo json_encode($vaultService->findAll());
}
?>