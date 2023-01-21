# Dictionary Server

## Required env variables

* SPRING_DATA_MONGODB_URI
* RSA_PRIVATEKEYCONTENT
* RSA_PUBLICKEYCONTENT
* SECURITY_USER_NAME
* SECURITY_USER_PASSWORD

## Run the application

```bash

 docker run --rm \
  -e FACEBOOK_OAUTH2_CLIENT_ID={value} \
  -e FACEBOOK_OAUTH2_CLIENT_SECRET=={value} \
  -e GOOGLE_OAUTH2_CLIENT_ID=={value} \
  -e GOOGLE_OAUTH2_CLIENT_SECRET=={value} \
  -e OKTA_OAUTH2_CLIENT_ID=={value} \
  -e OKTA_OAUTH2_CLIENT_SECRET=={value} \
  -e OKTA_OAUTH2_DOMAIN=={value} \
  -e RSA_PRIVATEKEYCONTENT=={value} \
  -e RSA_PUBLICKEYCONTENT={value} \
  -e SPRING_DATA_MONGODB_URI={value} \
  -e SECURITY_USER_NAME={value} \
  -e SECURITY_USER_PASSWORD={value} \
  solomkinmv/dictionary:latest

````

## Generate certificates

```bash
cd src/main/resources/certs
openssl genrsa -out keypair.pem 2048
openssl rsa -in keypair.pem -pubout -out public.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
rm keypair.pem
``` 
