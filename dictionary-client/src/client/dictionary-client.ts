import {UnsavedWord} from "./model/unsaved-word"
import {UserWords} from "./model/user-words"
import {Word} from "./model/word";
import {RestClient} from "./rest-client";

export function dictionaryClient() {
    return new DictionaryClient(new RestClient())
}

class DictionaryClient {
    private restClient: RestClient

    constructor(restClient: RestClient) {
        this.restClient = restClient
    }

    async addWord(userId: string, word: UnsavedWord): Promise<Word> {
        const addedWord = await this.restClient.post(`${process.env.REACT_APP_DICTIONARY_SERVICE_API_HOST}/api/users/${userId}/words`,
            word)
        console.log('Received response on adding the word', addedWord)
        return addedWord
    }

    async getWords(userId: string): Promise<UserWords> {
        const url = `${process.env.REACT_APP_DICTIONARY_SERVICE_API_HOST}/api/users/${userId}/words`;
        console.debug('Getting user words', url)
        const userWords = await this.restClient.get(url)
        console.log('Received response on user words', userWords)
        return userWords
    }
}
