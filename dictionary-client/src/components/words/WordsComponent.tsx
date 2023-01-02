import {Word} from "../../client/model/word";
import {dictionaryClient} from "../../client/dictionary-client";
import {useEffect, useState} from "react";
import AddWordComponent from "./AddWordComponent";

function WordsComponent() {

    const client = dictionaryClient();

    const [words, setWords] = useState<Word[]>([]);

    useEffect(() => {
        client.getWords()
            .then(userWords => {
                setWords(Array.from(Object.values(userWords.words)));
            });
    }, [])

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
