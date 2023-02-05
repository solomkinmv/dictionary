import {Link, Outlet} from "react-router-dom";
import {useNavigate} from "react-router";
import useAuth from "../auth/authentication-helpers";

function App() {
    let auth = useAuth();
    let navigate = useNavigate();

    if (!auth.isAuthenticated()) {
        return (
            <div className="app">
                <h1>Dictionary</h1>
                <nav style={{
                    borderBottom: "solid 1px",
                    paddingBottom: "1rem",
                }}>
                    <Link to="/">Home</Link> | {" "}
                    <Link to="/about">About</Link> | {" "}
                    <Link to="/login">Login</Link>
                </nav>
                <Outlet/>
            </div>
        );
    }

    return (
        <div className="app">
            <h1>Dictionary</h1>
            <nav style={{
                borderBottom: "solid 1px",
                paddingBottom: "1rem",
            }}>
                <Link to="/">Home</Link> | {" "}
                <Link to="/words">Words</Link> | {" "}
                <Link to="/languages">Languages</Link> | {" "}
                <Link to="/profile">Profile</Link> | {" "}
                <Link to="/about">About</Link> | {" "}
                <Link to="/" onClick={() => auth.signout(() => navigate("/"))}>Logout</Link>
            </nav>
            <Outlet/>
        </div>
    );
}

export default App;
