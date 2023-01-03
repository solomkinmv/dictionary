# Dictionary Server

## Required properties

* spring.data.mongodb.uri

## Generate certificates

```bash
cd src/main/resources/certs                                                                                                                                                                                                                                                                                                                                                                                                  [19:36:52]
openssl genrsa -out keypair.pem 2048                                                                                                                                                                                                                                                                                                                                                                                         [19:37:01]
openssl rsa -in keypair.pem -pubout -out public.pem                                                                                                                                                                                                                                                                                                                                                                          [19:37:22]
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem                                                                                                                                                                                                                                                                                                                                      [19:37:31]
rm keypair.pem  
``` 
