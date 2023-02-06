import React, {createContext, useContext, useEffect, useState} from "react";
import {UserLanguage} from "../client/languages/user-language";
import {useLanguagesClient} from "../client/languages/languages-client";

interface CurrentLanguageContextType {
    currentLanguage: UserLanguage | undefined,

    updateCurrentLanguage: (language: UserLanguage | undefined) => void,
    allUserLanguages: UserLanguage[]
    updateAllUserLanguages: (languages: UserLanguage[]) => void,
}

const CurrentLanguageContext = createContext<CurrentLanguageContextType>(null!);

export function CurrentLanguageProvider({children}: { children: React.ReactNode }) {
    const currentLanguageItemName = "currentLanguage"

    const [currentLanguage, setCurrentLanguage] = useState<UserLanguage>()
    const [allUserLanguages, setAllUserLanguages] = useState<UserLanguage[]>([])
    const languagesClient = useLanguagesClient()

    useEffect(() => {
        languagesClient.getLanguages()
            .then(aggregatedLanguages => {
                setAllUserLanguages(aggregatedLanguages.languages)
            })
    }, [languagesClient])

    useEffect(() => {
        if (allUserLanguages.length === 0) {
            updateCurrentLanguage(undefined)
        } else {
            const currentLanguageCode = sessionStorage.getItem(currentLanguageItemName || "")
            const currentLanguage = allUserLanguages
                .find(language => language.languageCode === currentLanguageCode) || allUserLanguages[0]
            updateCurrentLanguage(currentLanguage)
        }
    }, [allUserLanguages])

    const updateCurrentLanguage = (language: UserLanguage | undefined) => {
        if (language) {
            sessionStorage.setItem(currentLanguageItemName, language.languageCode)
        } else {
            sessionStorage.removeItem(currentLanguageItemName)
        }
        setCurrentLanguage(language)
    }

    const updateAllUserLanguages = (languages: UserLanguage[]) => {
        setAllUserLanguages(languages)
    }

    const value = {currentLanguage, updateCurrentLanguage, allUserLanguages, updateAllUserLanguages};
    return (
        <CurrentLanguageContext.Provider value={value}>{children}</CurrentLanguageContext.Provider>
    )
}

export default function useCurrentLanguage() {
    return useContext(CurrentLanguageContext);
}
