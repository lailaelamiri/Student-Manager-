<?php
include_once '../service/ScholarVaultService.php';
$vaultService = new ScholarVaultService();
header('Content-Type: application/json');
echo json_encode($vaultService->findAll());
?>
