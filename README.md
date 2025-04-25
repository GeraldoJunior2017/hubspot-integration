# HubSpot Integration API

API REST em Java/Spring Boot para integração com o HubSpot via **OAuth2 (Authorization Code Flow)**, criação de contatos e recebimento de webhooks de contato.

---

## 1. Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- Conta de desenvolvedor no HubSpot

---

## 2. Configuração do HubSpot

1. Acesse o portal de desenvolvedores:  
   https://developers.hubspot.com/
2. Crie um **app** e registre:
   - **Client ID**
   - **Client Secret**
3. Em **Redirect URLs**, adicione:  
   `http://localhost:8080/oauth/callback`
4. Conceda as seguintes scopes ao seu app:
   ```text
   crm.objects.contacts.read
   crm.objects.contacts.write
   oauth
   ```

---

## 3. Clonagem do repositório

```bash
git clone https://github.com/GeraldoJunior2017/hubspot-integration.git
cd hubspot-integration
```

---

## 4. Configuração do `application.properties`

Abra `src/main/resources/application.properties` e substitua os placeholders pelos valores do seu app HubSpot:

```properties
spring.application.name=hubspot-integration

hubspot.clientId=SEU_CLIENT_ID_AQUI
hubspot.clientSecret=SEU_CLIENT_SECRET_AQUI
hubspot.redirectUri=http://localhost:8080/oauth/callback
hubspot.authUrl=https://app.hubspot.com/oauth/authorize
hubspot.tokenUrl=https://api.hubapi.com/oauth/v1/token
hubspot.scopes=crm.objects.contacts.read crm.objects.contacts.write oauth
```

> **Exemplo real** (não use em produção):
> ```properties
> hubspot.clientId=8be144fb-7886-4800-9922-fe3603d1e443
> hubspot.clientSecret=98e7847e-12bd-4457-9776-7d56bbdec6d8
> ```

---

## 5. Execução da aplicação

```bash
mvn clean install
mvn spring-boot:run
```

A aplicação ficará disponível em `http://localhost:8080`.

---

## 6. Endpoints

### 6.1 Gerar URL de autorização

- **Method:** GET
- **Path:** `/oauth/authorize`

**Uso no Postman:**
1. Crie um request **GET** para `{{base_url}}/oauth/authorize`.
2. Clique em **Send**.
3. Copie a URL retornada (começa com `https://app.hubspot.com/oauth/authorize?...`).

---

### 6.2 Callback OAuth

- **Method:** GET
- **Path:** `/oauth/callback`
- **Query param:** `?code=SEU_CODE_AQUI`

**Uso no Postman:**
1. Após autorizar no HubSpot, você será redirecionado para `http://localhost:8080/oauth/callback?code=SEU_CODE`.
2. No Postman, crie um request **GET** com essa URL.
3. Clique em **Send** para trocar o code pelo token.

---

### 6.3 Criar Contato

- **Method:** POST
- **Path:** `/contacts`
- **Auth:** Basic Auth (usuário: `hubspot_user`, senha: `changeMe123`)
- **Headers:**
   - `Authorization: Bearer dummy`
   - `Content-Type: application/json`
- **Body (raw JSON):**
  ```json
  {
    "properties": {
      "firstname": "Ana",
      "lastname":  "Silva",
      "email":     "ana.silva@exemplo.com"
    }
  }
  ```

**Uso no Postman:**
1. Crie um request **POST** para `{{base_url}}/contacts`.
2. Configure **Authorization** para **Basic Auth** e preencha as credenciais.
3. Na aba **Headers**, adicione `Authorization: Bearer dummy` e `Content-Type: application/json`.
4. No **Body**, selecione **raw** e cole o JSON acima.
5. Clique em **Send**.

**Resposta esperada:** `201 Created` com o JSON do contato criado.

---

### 6.4 Receber Webhook de Contato

- **Method:** POST
- **Path:** `/webhooks/contact`
- **Body (raw JSON):**
  ```json
  {
    "subscriptionType": "contact.creation",
    "eventType":        "contact.creation",
    "objectId":         [12345]
  }
  ```
- **Pre-request Script (Postman):**
  ```javascript
  const payload   = pm.request.body.raw;
  const secret    = pm.environment.get("hubspot_client_secret");
  const hash      = CryptoJS.HmacSHA256(payload, secret);
  const signature = CryptoJS.enc.Base64.stringify(hash);
  pm.request.headers.upsert({ key: "X-HubSpot-Signature", value: signature });
  ```

**Uso no Postman:**
1. Crie um request **POST** para `{{base_url}}/webhooks/contact`.
2. No **Body**, cole o JSON de exemplo.
3. Em **Pre-request Script**, insira o código acima para calcular a assinatura HMAC.
4. Clique em **Send**.

**Resposta esperada:** `200 OK` se a assinatura for válida.

---

> **Este README cobre exatamente o escopo obrigatório do desafio técnico.**

