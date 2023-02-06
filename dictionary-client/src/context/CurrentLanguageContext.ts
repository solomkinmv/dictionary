import {createContext} from "react";
import {UserLanguage} from "../client/languages/user-language";

interface CurrentLanguageContextType {
    currentLanguage: UserLanguage | undefined, // todo: get rid of undefined
}

export const CurrentLanguageContext = createContext<CurrentLanguageContextType>({
    currentLanguage: undefined
});
