<?php
interface IVaultDao {
    function create($scholarObject);
    function delete($scholarObject);
    function update($scholarObject);
    function findAll();
    function findById($scholarId);
}
?>
