function Login() {
    return (<>
        <h1>Login</h1>

        <a href={process.env.REACT_APP_DICTIONARY_SERVICE_API_HOST + "/oauth2/authorization/google"}>Google</a>
    </>);
}

export default Login;
