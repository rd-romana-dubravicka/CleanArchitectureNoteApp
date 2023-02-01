package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.OrderType
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _notesState = mutableStateOf(NotesState())
    val notesState: State<NotesState> = _notesState

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes((NoteOrder.Date(OrderType.Descending)))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNoteEvent      -> {
                viewModelScope.launch {
                    noteUseCases.deleteNoteUseCase(event.note)
                    recentlyDeletedNote = event.note

                }
            }
            is NotesEvent.OrderEvent           -> {
                if (notesState.value.noteOrder::class == event.noteOrder::class &&
                    notesState.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                _notesState.value = notesState.value.copy(
                    noteOrder = event.noteOrder
                )
            }
            NotesEvent.RestoreNoteEvent        -> {
                viewModelScope.launch {
                    noteUseCases.addNoteUseCase(note = recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
            NotesEvent.ToggleOrderSectionEvent -> {
                _notesState.value = notesState.value.copy(
                    isOrderSectionVisible = !notesState.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotesUseCase(noteOrder = noteOrder)
            .onEach { notes ->
                _notesState.value = notesState.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }
}