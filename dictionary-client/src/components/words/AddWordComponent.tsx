import {useContext, useState} from "react";
import {useDictionaryClient} from "../../client/dictionary-client";
import {UnsavedWord} from "../../client/model/unsaved-word";
import {Word} from "../../client/model/word";
import {CurrentLanguageContext} from "../../context/CurrentLanguageContext";

interface AddWordComponentProps {
    onWordAdded: (word: Word) => void
}

function AddWordComponent(props: AddWordComponentProps) {
    const [wordText, setWordText] = useState("");
    const [translation, setTranslation] = useState("");
    const currentLanguageContext = useContext(CurrentLanguageContext);

    const client = useDictionaryClient();

    function onAddWord() {
        console.log("Adding word", currentLanguageContext, wordText, translation);
        client.addWord(currentLanguageContext.currentLanguage!, new UnsavedWord(wordText, translation))
            .then(word => props.onWordAdded(word));
    }

    return (
        <>
            <h1>Add word:</h1>
            <label htmlFor="word">Word:</label>
            <input type="text" value={wordText} onChange={event => setWordText(event.target.value)}/>
            <label htmlFor="translation">Translation:</label>
            <input type="text" value={translation} onChange={event => setTranslation(event.target.value)}/>
            <button onClick={onAddWord}>Add word</button>
        </>
    )
}

export default AddWordComponent;
