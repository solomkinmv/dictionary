import {Word} from "./word";

export interface UserWords {
    readonly userId: string
    readonly words: Record<string, Word>
}
