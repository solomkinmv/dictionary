import {Word} from "../../client/model/word";
import {useDictionaryClient} from "../../client/dictionary-client";
import {useContext, useEffect, useState} from "react";
import AddWordComponent from "./AddWordComponent";
import {CurrentLanguageContext} from "../../context/CurrentLanguageContext";

function WordsComponent() {

    const [words, setWords] = useState<Word[]>([]);
    const dictionaryClient = useDictionaryClient();
    const currentLanguageContext = useContext(CurrentLanguageContext);
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
