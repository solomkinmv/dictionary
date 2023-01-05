import {useSearchParams} from "react-router-dom";
import useAuth from "../authentication-helpers";
import {useNavigate} from "react-router";

function AuthorizationComponent() {
    const [searchParams] = useSearchParams()
    const auth = useAuth()
    const navigate = useNavigate()

    const token = searchParams.get("jwt") || ""
    const previousLocation = localStorage.getItem("previousLocation") || "/"
    auth.signin(token, () => {
        localStorage.removeItem("previousLocation")
        navigate(previousLocation)
    })


    return (
        <>
            <div>Authorized with token: {token}</div>
        </>
    )
}

export default AuthorizationComponent;
