import {useSearchParams} from "react-router-dom";

function AuthorizationFinishedComponent() {
    const [searchParams] = useSearchParams()

    const token = searchParams.get("jwt") || ""

    return (
        <>
            <div>Authorized with token: {token}</div>
        </>
    )
}

export default AuthorizationFinishedComponent;
