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
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository
) : ViewModel(){
    private val _signUpState = MutableStateFlow<Response<Any>>(Response.Loading)
    val signUpState: StateFlow<Response<Any>> = _signUpState

    fun signUp (email: String, password: String){
        viewModelScope.launch {
            _signUpState.value = Response.Loading
            authRepository.register(email, password).collect{
                when(it){
                    is Response.Loading -> {
                        _signUpState.value = Response.Loading
                    }
                    is Response.Success -> {
                        _signUpState.value = Response.Success("Sign-up successful")
                    }

                    is Response.Error -> {
                        _signUpState.value = Response.Error(it.message)

                    }                    }
            }
        }
    }
}