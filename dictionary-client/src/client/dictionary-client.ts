import {UnsavedWord} from "./model/unsaved-word"
import {UserWords} from "./model/user-words"
import {Word} from "./model/word";
import {RestClient} from "./rest-client";
import jwtDecode, {JwtPayload} from "jwt-decode";
import {UserDetails} from "./model/user-details";
import {getToken} from "../components/auth/authentication-helpers";
import {UserLanguage} from "./languages/user-language";
import {useMemo} from "react";

export function useDictionaryClient() {
    return useMemo(() => new DictionaryClient(new RestClient()), [])
}

class DictionaryClient {
    private restClient: RestClient
    private readonly _dictionaryHost: string;

    constructor(restClient: RestClient) {
        this.restClient = restClient
        this._dictionaryHost = process.env.REACT_APP_DICTIONARY_SERVICE_API_HOST!;
    }


    async addWord(userLanguage: UserLanguage, word: UnsavedWord): Promise<Word> {
        const token = this.extractJwtToken();
        const addedWord = await this.restClient.post(`${this._dictionaryHost}/api/languages/${userLanguage.languageCode}/words`,
            token, word)
        console.log('Received response on adding the word', addedWord)
        return addedWord
    }

    async getWords(userLanguage: UserLanguage): Promise<UserWords> {
        const token = getToken();
        const url = `${this._dictionaryHost}/api/languages/${userLanguage.languageCode}/words`
        console.debug('Getting user words', url)
        const userWords = await this.restClient.get(url, token)
        console.log('Received response on user words', userWords)
        return userWords
    }

    async deleteWord(userLanguage: UserLanguage, word: Word): Promise<UserWords> {
        const token = this.extractJwtToken();
        const url = `${this._dictionaryHost}/api/languages/${userLanguage.languageCode}/words/${word.id}`
        console.debug('Deleting word', url)
        const userWords = await this.restClient.delete(url, token)
        console.log('Received response on deleting the word', userWords)
        return userWords
    }

    getUserDetails(): UserDetails {
        const token = getToken();
        const decodedToken = jwtDecode<JwtPayload>(token)
        const userId = decodedToken.sub
        if (!userId) {
            throw new Error('No user id found in token')
        }
        return {userId: userId}
    }

    private extractJwtToken(): string {
        const token = localStorage.getItem('token')
        if (!token) {
            throw new Error('No token found')
        }
        return token
    }
}
