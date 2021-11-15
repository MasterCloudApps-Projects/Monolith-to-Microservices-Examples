CREATE OR REPLACE FUNCTION restful_post(ajson_text text)
 RETURNS text
 LANGUAGE plperlu
 SECURITY DEFINER
AS $function$
  use REST::Client;  
  use Encode qw(encode);
  my $client = REST::Client->new();    
  $client->getUseragent()->proxy( 'https', 'http://payment.service/' ); # use for proxy authentication
  $client->addHeader('Content-Type', 'application/json');          # headers
  $client->POST('loyalty/printer', encode('UTF-8', $_[0]));        # encoding
  return $client->responseContent();  
$function$  
;

CREATE TRIGGER loyalty_insertion_trigger AFTER INSERT
    ON loyalty
    FOR EACH ROW
    EXECUTE PROCEDURE  restful_post ( 'STRING EXSAMPLE' );

