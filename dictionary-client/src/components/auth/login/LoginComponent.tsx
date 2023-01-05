import {useLocation} from "react-router";
import React from "react";

function Login() {
    let location = useLocation();

    let from = location.state?.from?.pathname || "/";

    return (<>
        <h1>Login</h1>
        <p>You must log in to view the page at {from}</p>

        <a href={process.env.REACT_APP_DICTIONARY_SERVICE_API_HOST + "/oauth2/authorization/google"}
           onClick={() => localStorage.setItem("previousLocation", from)}>
            Google
        </a>
    </>);
}

export default Login;
