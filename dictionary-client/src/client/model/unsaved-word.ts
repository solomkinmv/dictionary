export class UnsavedWord {
    public readonly text: string;
    public readonly translation: string;

    constructor(text: string, translation: string) {
        this.text = text;
        this.translation = translation;
    }
}
