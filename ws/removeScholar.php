<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    include_once '../service/ScholarVaultService.php';
    $vaultService = new ScholarVaultService();
    $scholarId = intval($_POST['scholarId']);
    $vaultService->removeById($scholarId);
    header('Content-Type: application/json');
    echo json_encode($vaultService->findAll());
}
?>