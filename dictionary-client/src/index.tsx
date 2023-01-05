import ReactDOM from "react-dom/client";
import React from "react";
import {Route, Routes} from "react-router";
import {BrowserRouter} from "react-router-dom";
import App from "./components/app/App";
import reportWebVitals from "./reportWebVitals";
import Home from "./components/home/Home";
import WordsComponent from "./components/words/WordsComponent";
import Login from "./components/auth/login/LoginComponent";
import AuthorizationComponent from "./components/auth/login/AuthorizationComponent";
import ProfileComponent from "./components/profile/ProfileComponent";
import RequireAuth from "./components/auth/require-auth";
import {AuthProvider} from "./components/auth/authentication-helpers";

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);
root.render(
    <React.StrictMode>
        <BrowserRouter>
            <AuthProvider>
                <Routes>
                    <Route path="/" element={<App/>}>
                        <Route path="/" element={<Home/>}/>
                        <Route path="/login" element={<Login/>}/>
                        <Route path="/authorized" element={<AuthorizationComponent/>}/>
                        <Route path="/profile" element={
                            <RequireAuth>
                                <ProfileComponent/>
                            </RequireAuth>
                        }/>
                        <Route path="/words" element={
                            <RequireAuth>
                                <WordsComponent/>
                            </RequireAuth>
                        }/>
                        <Route
                            path="*"
                            element={
                                <main style={{padding: "1rem"}}>
                                    <p>No such page exist</p>
                                </main>
                            }
                        />
                    </Route>
                </Routes>
            </AuthProvider>
        </BrowserRouter>
    </React.StrictMode>
);

reportWebVitals();
