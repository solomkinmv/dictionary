import {UnsavedWord} from "./unsaved-word"

export interface UserWords {
    readonly userId: string
    readonly words: Map<String, UnsavedWord>
}
