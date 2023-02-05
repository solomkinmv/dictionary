import {UserLanguage} from "../../client/languages/user-language";

interface CurrentLanguageSelectionComponentProps {
    currentLanguage: UserLanguage;
    allUserLanguages: UserLanguage[];
    onSelected: (language: UserLanguage) => void;
}

export function CurrentLanguageSelectionComponent(props: CurrentLanguageSelectionComponentProps) {
    const userLanguages = [props.currentLanguage, ...props.allUserLanguages.filter(language => language.languageCode !== props.currentLanguage.languageCode)];
    const languagesMapping = Object.assign({},
        ...userLanguages.map((lang) => ({[lang.languageCode]: lang}))) as Record<string, UserLanguage>;
    return (
        <select onChange={event => props.onSelected(languagesMapping[event.target.value])}>
            {userLanguages.map((userLanguage) => (
                <option key={userLanguage.languageCode}
                        value={userLanguage.languageCode}>{userLanguage.languageName}</option>
            ))}
        </select>
    )
}
