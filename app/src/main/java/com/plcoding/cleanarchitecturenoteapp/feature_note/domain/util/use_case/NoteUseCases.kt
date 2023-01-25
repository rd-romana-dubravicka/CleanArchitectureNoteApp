package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.use_case

/*
    Gathering class that exists only to collect all usecases that are used for a single feature. We can aad it to the ViewModel instead of 50 separate usecases
 */
data class NoteUseCases(
    val getNotesUseCase: GetNotesUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase
)
