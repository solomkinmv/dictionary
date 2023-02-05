import {useSettingsDictionaryClient} from "../../client/settings/settings-client";
import {useEffect, useState} from "react";
import {SupportedLanguage} from "../../client/settings/supported-language";
import {UserLanguage} from "../../client/languages/user-language";
import {useLanguagesClient} from "../../client/languages/languages-client";

export function LanguageSelectionComponent() {
    const settingsClient = useSettingsDictionaryClient();
    const languagesClient = useLanguagesClient();

    const [supportedLanguages, setSupportedLanguages] = useState<SupportedLanguage[]>([])
    const [userLanguages, setUserLanguages] = useState<UserLanguage[]>([])

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
        </>
    )
}
