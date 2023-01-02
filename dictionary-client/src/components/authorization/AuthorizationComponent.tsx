import {useSearchParams} from "react-router-dom";

function AuthorizationComponent() {
    const [searchParams] = useSearchParams()

    const token = searchParams.get("jwt") || ""

    localStorage.setItem("token", token);

    return (
        <>
            <div>Authorized with token: {token}</div>
        </>
    )
}

export default AuthorizationComponent;
