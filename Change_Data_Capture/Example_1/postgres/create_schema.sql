START TRANSACTION;

CREATE EXTENSION if not exists "uuid-ossp";

CREATE TABLE loyalty_accounts (
  id  TEXT PRIMARY KEY DEFAULT uuid_generate_v4(),
  customer_id INT NOT NULL UNIQUE,
  loyalty_account TEXT NOT NULL ,
  created_at TIMESTAMP DEFAULT now()
);

CREATE OR REPLACE FUNCTION random_string(length integer) RETURNS TEXT AS
$$
  DECLARE
    chars text[] := '{A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z}';
    result text := '';
    i INTEGER := 0;
  BEGIN
    IF length < 0 THEN
      raise exception 'Given length cannot be less than 0';
    END IF;
    FOR i IN 1..length LOOP
      result := result || chars[1+random()*(array_length(chars, 1)-1)];
    END LOOP;
    RETURN result;
  END;
$$ language plpgsql;

COMMIT;