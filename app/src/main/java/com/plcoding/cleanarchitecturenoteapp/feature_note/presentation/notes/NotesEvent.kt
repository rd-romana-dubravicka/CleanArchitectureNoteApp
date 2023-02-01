package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes

import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder

sealed class NotesEvent {
    data class OrderEvent(val noteOrder: NoteOrder): NotesEvent()
    data class DeleteNoteEvent(val note: Note): NotesEvent()
    object RestoreNoteEvent: NotesEvent()
    object ToggleOrderSectionEvent: NotesEvent()
}
