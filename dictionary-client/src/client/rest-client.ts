export class RestClient {

    async get(url: string,
              token?: string,
              params?: Record<string, string>) {
        const urlObject = new URL(url)
        this.fillSearchParams(urlObject, params);

        let headers = this.fillBearerHeader(token);

        const response = await fetch(urlObject, {
            method: 'GET',
            headers: headers
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

    private fillSearchParams(url: URL, params: Record<string, string> | undefined): void {
        if (params) {
            for (let key in params) {
                url.searchParams.append(key, params[key])
            }
        }
    }

    private fillBearerHeader(token: string | undefined): Record<string, string> {
        if (token) {
            return {
                "Authorization": `Bearer ${token}`
            }
        }
        return {};
    }

    async post(url: string, token?: string, body?: any) {
        let headers = this.fillBearerHeader(token);
        headers['Content-Type'] = 'application/json'

        const response = await fetch(url, {
            method: 'POST',
            headers: headers,
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
