package com.erayerarslan.t_vac_kotlin.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erayerarslan.t_vac_kotlin.core.Response
import com.erayerarslan.t_vac_kotlin.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository
) : ViewModel() {

    private val _signInState = MutableStateFlow<Response<Any>>(Response.Loading)
    val signInState: StateFlow<Response<Any>> = _signInState


    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _signInState.value = Response.Loading
            authRepository.login(email, password).collect {
                when (it) {
                    is Response.Loading -> {
                        _signInState.value = Response.Loading
                    }

                    is Response.Success -> {
                        _signInState.value = Response.Success("Sign-in successful")
                    }

                    is Response.Error -> {
                        _signInState.value = Response.Error(it.message)

                    }
                }
            }
        }

    }
}