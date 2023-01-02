export class UnsavedWord {
    public readonly wordText: string;
    public readonly translation: string;

    constructor(wordText: string, translation: string) {
        this.wordText = wordText;
        this.translation = translation;
    }
}
