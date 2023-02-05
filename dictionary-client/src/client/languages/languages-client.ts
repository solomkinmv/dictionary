import {RestClient} from "../rest-client";
import {useMemo} from "react";
import {AggregatedUserLanguages} from "./aggregated-user-languages";
import {getToken} from "../../components/auth/authentication-helpers";

export function useLanguagesClient(): LanguagesClient {
    return useMemo(() => new LanguagesClient(new RestClient()), []);
}

class LanguagesClient {
    private readonly restClient: RestClient;

    constructor(restClient: RestClient) {
        this.restClient = restClient;
    }

    async getLanguages(): Promise<AggregatedUserLanguages> {
        const token = getToken();
        const userLanguages = await this.restClient.get(`${process.env.REACT_APP_DICTIONARY_SERVICE_API_HOST}/api/languages`, token);
        console.log('Received response on getting user languages', userLanguages)
        return userLanguages;
    }

    async addLanguage(languageCode: string): Promise<AggregatedUserLanguages> {
        const token = getToken();
        const userLanguages = await this.restClient.put(`${process.env.REACT_APP_DICTIONARY_SERVICE_API_HOST}/api/languages/${languageCode}`, token);
        console.log('Received response on adding user language', userLanguages)
        return userLanguages;
    }
}
