import {useState} from "react";
import {dictionaryClient} from "../../client/dictionary-client";
import {UnsavedWord} from "../../client/model/unsaved-word";
import {Word} from "../../client/model/word";

interface AddWordComponentProps {
    onWordAdded: (word: Word) => void
}

function AddWordComponent(props: AddWordComponentProps) {
    const [wordText, setWordText] = useState("");
    const [translation, setTranslation] = useState("");

    const client = dictionaryClient();

    function onAddWord() {
        console.log("Adding word", wordText, translation);
        client.addWord(new UnsavedWord(wordText, translation))
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
