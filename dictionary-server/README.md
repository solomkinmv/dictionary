# Dictionary Server

## Required properties

* spring.data.mongodb.uri

## Generate certificates

```bash
cd src/main/resources/certs
openssl genrsa -out keypair.pem 2048
openssl rsa -in keypair.pem -pubout -out public.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
rm keypair.pem
``` 
