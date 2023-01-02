import {RestClient} from "./rest-client";

export function authorizationClient() {
    return new AuthorizationClient(new RestClient())
}

class AuthorizationClient {
    private restClient: RestClient

    constructor(restClient: RestClient) {
        this.restClient = restClient
    }

    async getToken(provider: string, code: string, state: string, scope: string, authuser: string, prompt: string): Promise<string> {
        const url = `${process.env.REACT_APP_DICTIONARY_SERVICE_API_HOST}/login/oauth2/code/${provider}`;
        const params: Record<string, string> = {
            code: code,
            state: state,
            scope: scope,
            authuser: authuser,
            prompt: prompt
        };
        const token = await this.restClient.get(url, params)
        console.log('Received response on token', token)
        return token["jwt"]
    }

}
