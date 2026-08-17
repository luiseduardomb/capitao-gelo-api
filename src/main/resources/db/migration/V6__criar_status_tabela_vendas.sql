ALTER TABLE vendas
    ADD COLUMN status VARCHAR(20);

UPDATE vendas
SET status = 'PAGA'
WHERE status IS NULL;

ALTER TABLE vendas
    ALTER COLUMN status SET NOT NULL;