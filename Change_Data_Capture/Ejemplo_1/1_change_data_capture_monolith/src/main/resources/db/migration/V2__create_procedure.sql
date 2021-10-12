CREATE OR REPLACE FUNCTION restful_post()
  RETURNS TRIGGER AS $$
  BEGIN
    SELECT content::json->>'form'
    FROM http_post('http://payment.service/loyalty/printer',
                 'myvar=myval&foo=bar',
                 'application/x-www-form-urlencoded');
  END;
  $$ LANGUAGE plpgsql;

CREATE TRIGGER restful_post_trigger AFTER INSERT
    ON loyalty
    FOR EACH ROW
    EXECUTE PROCEDURE  restful_post();

