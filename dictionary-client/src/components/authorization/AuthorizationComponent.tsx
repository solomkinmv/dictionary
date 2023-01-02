import {useParams} from "react-router";
import {useSearchParams} from "react-router-dom";
import {authorizationClient} from "../../client/authorization-client";
import {useCallback, useEffect, useState} from "react";

function AuthorizationComponent() {
    const params = useParams()

    const authClient = authorizationClient()
    const [searchParams] = useSearchParams()

    const provider = params.provider || ""
    const code = searchParams.get("code") || ""
    const state = searchParams.get("state") || ""
    const scope = searchParams.get("scope") || ""
    const authuser = searchParams.get("authuser") || ""
    const prompt = searchParams.get("prompt") || ""

    const [token, setToken] = useState("")

    const fetchData = useCallback(async () => {
        console.log("Search params", searchParams)
        const token = await authClient.getToken(provider, code, state, scope, authuser, prompt)
        setToken(token)
    }, [provider, code, state, scope, authuser, prompt])


    useEffect(() => {
        fetchData()
            .catch(console.error);
    }, [fetchData])
    return (
        <>
            <div>{token}</div>
        </>
    )
}

export default AuthorizationComponent;
