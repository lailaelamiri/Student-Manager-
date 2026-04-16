<?php
class VaultGateway {
    private $vaultLink;

    public function __construct() {
        try {
            $this->vaultLink = new PDO(
                "mysql:host=localhost;dbname=academy_vault;charset=utf8",
                "root",
                ""
            );
            $this->vaultLink->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch (PDOException $gatewayError) {
            die('IvoryLedger Connection Failed: ' . $gatewayError->getMessage());
        }
    }

    public function getVaultLink() {
        return $this->vaultLink;
    }
}
?>
