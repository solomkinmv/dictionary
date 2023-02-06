import {Link, Outlet} from "react-router-dom";
import {useNavigate} from "react-router";
import useAuth from "../auth/authentication-helpers";
import {CurrentLanguageSelectionComponent} from "../languages/CurrentLanguageSelectionComponent";
import {useLanguagesClient} from "../../client/languages/languages-client";
import {useEffect, useState} from "react";
import {UserLanguage} from "../../client/languages/user-language";
import {CurrentLanguageContext} from "../../context/CurrentLanguageContext";

function App() {
    const auth = useAuth();
    const navigate = useNavigate();
    const languagesClient = useLanguagesClient();

    const [currentLanguage, setCurrentLanguage] = useState<UserLanguage>();
    const [allUserLanguages, setAllUserLanguages] = useState<UserLanguage[]>([]);

    useEffect(() => {
        if (!auth.isAuthenticated()) return;
        languagesClient.getLanguages()
            .then(aggregatedLanguages => {
                setAllUserLanguages(aggregatedLanguages.languages)
                setCurrentLanguage(aggregatedLanguages.languages.at(0))
            })

    }, [auth, languagesClient])

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
                <Link to="/" onClick={() => auth.signout(() => navigate("/"))}>Logout</Link> | {" "}
                {currentLanguage &&
                    <CurrentLanguageSelectionComponent
                        currentLanguage={currentLanguage}
                        allUserLanguages={allUserLanguages}
                        onSelected={selectedLanguage => setCurrentLanguage(selectedLanguage)}
                    />
                }
            </nav>
            <CurrentLanguageContext.Provider value={{currentLanguage: currentLanguage}}>
                <Outlet/>
            </CurrentLanguageContext.Provider>
        </div>
    );
}

export default App;
