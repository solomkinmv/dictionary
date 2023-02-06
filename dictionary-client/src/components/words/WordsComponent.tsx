import {Word} from "../../client/model/word";
import {useDictionaryClient} from "../../client/dictionary-client";
import {useEffect, useState} from "react";
import AddWordComponent from "./AddWordComponent";
import useCurrentLanguage from "../../context/CurrentLanguageContext";

function WordsComponent() {

    const [words, setWords] = useState<Word[]>([]);
    const dictionaryClient = useDictionaryClient();
    const currentLanguageContext = useCurrentLanguage();
    console.log("Current language on WordsComponent render", currentLanguageContext)
    const currentLanguage = currentLanguageContext.currentLanguage;

    useEffect(() => {
        if (!currentLanguage) return;
        dictionaryClient
            .getWords(currentLanguage)
            .then(userWords => {
                setWords(Array.from(Object.values(userWords.words)));
            });
    }, [currentLanguage, dictionaryClient])

    if (!currentLanguage) {
        return (
            <div>
                <p>Please select a language</p>
            </div>
        )
    }

    return (
        <div>
            <AddWordComponent onWordAdded={word => setWords([...words, word])}/>
            <h1>Words</h1>
            {words.map((word) => (
                <li key={word.id}>{word.wordText} - {word.translation}</li>
            ))}
        </div>
    );
}

export default WordsComponent;
