START TRANSACTION;

INSERT INTO loyalty_accounts(customer_id, loyalty_account)
SELECT
  floor(random() * 10 + 1)::int as customer_id,
  regexp_replace(CAST (random() AS text),'^0\.(\d{3})(\d{3})(\d{4}).*$','\1\2\3') AS loyalty_account
FROM GENERATE_SERIES(1, 10) seq;

COMMIT;
