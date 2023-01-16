import {useSearchParams} from "react-router-dom";
import useAuth from "../authentication-helpers";
import {useNavigate} from "react-router";
import {useEffect} from "react";

function AuthorizationComponent() {
    const [searchParams] = useSearchParams()
    const auth = useAuth()
    const navigate = useNavigate()

    useEffect(() => {
        const token = searchParams.get("jwt") || ""
        console.log("AuthorizationComponent redirected with token " + token);
        const previousLocation = localStorage.getItem("previousLocation") || "/"
        auth.signin(token, () => {
            console.log(`Signing in with token ${token} and previous location ${previousLocation}`);
            localStorage.removeItem("previousLocation")
            navigate(previousLocation)
        })
    }, [auth, navigate, searchParams])


    return (
        <>
            <div>Waiting for token...</div>
        </>
    )
}

export default AuthorizationComponent;
