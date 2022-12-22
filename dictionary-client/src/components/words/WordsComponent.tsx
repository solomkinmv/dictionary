import {Word} from "../../client/model/word";

function WordsComponent() {
    const words: Word[] = [
        {
            id: "1",
            text: "hello",
            translation: "привіт",
        },
        {
            id: "2",
            text: "world",
            translation: "світ",
        }
    ]
    return (
        <div>
            {words.map((word) => (
                <div key={word.id}>
                    <p>{word.text} - {word.translation}</p>
                </div>
            ))}
        </div>
    );
}

export default WordsComponent;
