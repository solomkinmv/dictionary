import {useSettingsDictionaryClient} from "../../client/settings/settings-client";
import {useEffect, useState} from "react";
import {SupportedLanguage} from "../../client/settings/supported-language";
import {UserLanguage} from "../../client/languages/user-language";
import {useLanguagesClient} from "../../client/languages/languages-client";
import {LanguageListComponent} from "./LanguageListComponent";

export function LanguageSelectionComponent() {
    const settingsClient = useSettingsDictionaryClient();
    const languagesClient = useLanguagesClient();

    const [supportedLanguages, setSupportedLanguages] = useState<SupportedLanguage[]>([])
    const [userLanguages, setUserLanguages] = useState<UserLanguage[]>([])
    const [languageSelectorHidden, setLanguageSelectorHidden] = useState(true)

    useEffect(() => {
        settingsClient.getSupportedLanguages()
            .then(supportedLanguages => setSupportedLanguages(supportedLanguages.supportedLanguages))

        languagesClient.getLanguages()
            .then(userLanguages => setUserLanguages(userLanguages.languages))
    }, [settingsClient, languagesClient]);

    return (
        <>
            <h2>Supported Languages:</h2>
            <ul>
                {supportedLanguages.map((supportedLanguage) => (
                    <li key={supportedLanguage.languageCode}>{supportedLanguage.languageName}</li>
                ))}
            </ul>

            <h2>User Languages:</h2>
            <ul>
                {userLanguages.map((userLanguage) => (
                    <li key={userLanguage.languageCode}>{userLanguage.languageName}</li>
                ))}
            </ul>
            {userLanguages.length === 0 && <div>You don't have any languages configured</div>}
            {languageSelectorHidden && <button onClick={() => setLanguageSelectorHidden(!languageSelectorHidden)}>
                Add Language
            </button>}
            {!languageSelectorHidden && <button onClick={() => setLanguageSelectorHidden(!languageSelectorHidden)}>
                Cancel adding new language
            </button>}
            {!languageSelectorHidden &&
                <LanguageListComponent
                    languages={supportedLanguages}
                    onSelected={async (selectedLanguage) => {
                        const aggregatedUserLanguages = await languagesClient.addLanguage(selectedLanguage.languageCode)
                        setUserLanguages(aggregatedUserLanguages.languages)
                        setLanguageSelectorHidden(true)
                    }}/>
            }
        </>
    )
}
