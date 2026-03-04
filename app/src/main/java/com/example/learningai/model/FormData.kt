
package com.example.learningai.model

data class DetailsFormUiState(
    val role: UserRole = UserRole.SELF,
    val country: String = "",
    val state: String = "",
    val institutionType: InstitutionType = InstitutionType.COLLEGE,
    val universityName: String = "",
    val collegeName: String = "",
    val prnNumber: String = "",
    val collegeEmail: String = "",
    val collegeId: String = "",
    val privateClassName: String = "",
    val studentId: String = "",
    val isSubmitEnabled: Boolean = true
)