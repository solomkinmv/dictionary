import {Link, Outlet} from "react-router-dom";

function App() {
    return (
        <div className="app">
            <h1>Dictionary</h1>
            <nav style={{
                borderBottom: "solid 1px",
                paddingBottom: "1rem",
            }}>
                <Link to="/">Home</Link> | {" "}
                <Link to="/words">Words</Link> | {" "}
                <Link to="/about">About</Link> | {" "}
                <Link to="/login">Login</Link> | {" "}
                <Link to="/" onClick={() => localStorage.removeItem("token")}>Logout</Link>
            </nav>
            <Outlet/>
        </div>
    );
}

export default App;
