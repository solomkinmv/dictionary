import {SupportedLanguage} from "../../client/settings/supported-language";
import React from "react";

interface LanguageListComponentProps {
    languages: SupportedLanguage[];
    onSelected: (language: SupportedLanguage) => void;
}

export function LanguageListComponent(props: LanguageListComponentProps) {
    return (
        <div>
            <label htmlFor="language-select">Choose a language:</label>

            <select name="languages" id="language-select" onChange={event => {
                const selectedLanguage = props.languages.find(language => language.languageCode === event.target.value);
                if (selectedLanguage) {
                    props.onSelected(selectedLanguage)
                }
            }
            }>
                <option value="">--Please choose an option--</option>
                {props.languages.map((item) => (
                    <option key={item.languageCode} value={item.languageCode}
                            onClick={() => props.onSelected(item)}>{item.languageName}</option>
                ))}
            </select>

        </div>
    );

}
