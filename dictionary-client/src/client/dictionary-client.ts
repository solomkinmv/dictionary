import {UnsavedWord} from "./model/unsaved-word"
import {UserWords} from "./model/user-words"
import {Word} from "./model/word";

export function dictionaryClient() {
    return new DictionaryClient(new RestClient())
}

class DictionaryClient {
    private restClient: RestClient

    constructor(restClient: RestClient) {
        this.restClient = restClient
    }

    async addWord(userId: string, word: UnsavedWord): Promise<Word> {
        const addedWord = await this.restClient.post(`${process.env.REACT_APP_MEETING_SERVICE_API_HOST}/api/users/${userId}/words`,
            word)
        console.log('Received response on adding the word', addedWord)
        return addedWord
    }

    async getWords(userId: string): Promise<UserWords> {
        const url = `${process.env.REACT_APP_MEETING_SERVICE_API_HOST}/api/users/${userId}/words`;
        console.debug('Getting user words', url)
        const userWords = await this.restClient.get(url)
        console.log('Received response on user words', userWords)
        return userWords
    }
}

class RestClient {

    async get(url: string) {
        const response = await fetch(url, {
            method: 'GET'
        })
        console.log('Received response on get', response)
        if (response.status === 404) {
            throw new NotFoundError("Could not find resource at " + url)
        }
        try {
            return await response.json()
        } catch (e) {
            console.error(`Error on get ${url}`, e)
            throw e;
        }
    }

    async post(url: string, body: any) {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            mode: 'cors',
            body: JSON.stringify(body)
        })
        const data = await response.json()
        console.log('Received response on post', data)
        return data
    }

    async put(url: string, body: any) {
        const response = await fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        })
        const data = await response.json()
        console.log('Received response on put', data)
        return data
    }

    async delete(url: string) {
        const response = await fetch(url, {
            method: 'DELETE',
            mode: 'cors',
        })
        if (response.status === 404) {
            throw new NotFoundError("Could not find resource at " + url)
        }
        console.log('Received response on delete', response)
        try {
            return await response.json()
        } catch (e) {
            console.error(`Error on delete ${url}`, e)
            throw e;
        }
    }
}

class NotFoundError extends Error {
    constructor(message: string) {
        super(message);
        this.name = "NotFound";
    }
}
