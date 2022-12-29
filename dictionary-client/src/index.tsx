import ReactDOM from "react-dom/client";
import React from "react";
import {Route, Routes} from "react-router";
import {BrowserRouter} from "react-router-dom";
import App from "./components/app/App";
import reportWebVitals from "./reportWebVitals";
import Home from "./components/home/Home";
import WordsComponent from "./components/words/WordsComponent";

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);
root.render(
    <React.StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<App/>}>
                    <Route path="/" element={<Home/>}/>
                    <Route path="/words" element={<WordsComponent/>}/>
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
        </BrowserRouter>
    </React.StrictMode>
);

reportWebVitals();