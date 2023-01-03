import {UnsavedWord} from "./model/unsaved-word"
import {UserWords} from "./model/user-words"
import {Word} from "./model/word";
import {RestClient} from "./rest-client";
import jwtDecode, {JwtPayload} from "jwt-decode";
import {UserDetails} from "./model/user-details";

export function dictionaryClient() {
    return new DictionaryClient(new RestClient())
}

class DictionaryClient {
    private restClient: RestClient

    constructor(restClient: RestClient) {
        this.restClient = restClient
    }

    async addWord(word: UnsavedWord): Promise<Word> {
        const token = this.extractJwtToken();
        const addedWord = await this.restClient.post(`${process.env.REACT_APP_DICTIONARY_SERVICE_API_HOST}/api/words`,
            token, word)
        console.log('Received response on adding the word', addedWord)
        return addedWord
    }

    async getWords(): Promise<UserWords> {
        const token = this.extractJwtToken();
        const url = `${process.env.REACT_APP_DICTIONARY_SERVICE_API_HOST}/api/words`;
        console.debug('Getting user words', url)
        const userWords = await this.restClient.get(url, token)
        console.log('Received response on user words', userWords)
        return userWords
    }

    getUserDetails(): UserDetails {
        const token = this.extractJwtToken();
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
