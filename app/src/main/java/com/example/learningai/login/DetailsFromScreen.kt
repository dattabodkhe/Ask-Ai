package com.example.learningai.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learningai.model.DetailsFormUiState
import com.example.learningai.model.InstitutionType
import com.example.learningai.model.UserRole

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailsFormScreen(
    uiState: DetailsFormUiState,
    onInstitutionTypeSelected: (InstitutionType) -> Unit,
    onCountryChanged: (String) -> Unit,
    onStateChanged: (String) -> Unit,
    onUniversityChanged: (String) -> Unit,
    onCollegeChanged: (String) -> Unit,
    onCollegeEmailChanged: (String) -> Unit,
    onCollegeIdChanged: (String) -> Unit,
    onPrnChanged: (String) -> Unit,
    onPrivateClassChanged: (String) -> Unit,
    onStudentIdChanged: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val countries = listOf("India", "USA", "UK", "Canada", "Australia")
    val indianStates = listOf("Maharashtra", "Gujarat", "Delhi", "Karnataka", "Tamil Nadu", "Rajasthan", "Punjab")

    var countryExpanded by remember { mutableStateOf(false) }
    var stateExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6FF))
    ) {
        Column {
            /* ================= HEADER ================= */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF2563FF), Color(0xFF9333FF))),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Complete Your Profile 🎓",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "This information will be displayed on your profile.",
                        color = Color.White.copy(0.9f),
                        fontSize = 14.sp
                    )
                }
            }

            /* ================= FORM CONTENT ================= */
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(20.dp))

                /* --- Section 1: Location --- */
                WhiteCard {
                    Label("Country *")
                    DropDownBox(
                        value = uiState.country,
                        expanded = countryExpanded,
                        list = countries,
                        onExpand = { countryExpanded = !countryExpanded; stateExpanded = false },
                        onSelect = { onCountryChanged(it); countryExpanded = false }
                    )

                    Spacer(Modifier.height(16.dp))

                    Label("State *")
                    DropDownBox(
                        value = uiState.state,
                        expanded = stateExpanded,
                        list = indianStates,
                        onExpand = { stateExpanded = !stateExpanded; countryExpanded = false },
                        onSelect = { onStateChanged(it); stateExpanded = false }
                    )
                }

                Spacer(Modifier.height(20.dp))

                /* --- Section 2: Institution Details --- */
                WhiteCard {
                    Label("Institution Type *")
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        InstitutionType.entries.forEach { type ->
                            FigmaChip(
                                text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                                selected = uiState.institutionType == type
                            ) {
                                onInstitutionTypeSelected(type)
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // Dynamic Label based on Institution Type
                    val nameLabel = if (uiState.institutionType == InstitutionType.PRIVATE)
                        "Coaching / Private Class Name *" else "College / School Name *"

                    Label(nameLabel)
                    FigmaTextField(
                        value = uiState.collegeName,
                        hint = "Enter name here",
                        onChange = onCollegeChanged
                    )

                    Spacer(Modifier.height(16.dp))

                    Label("University (Optional)")
                    FigmaTextField(
                        value = uiState.universityName,
                        hint = "e.g. SPPU, Mumbai University",
                        onChange = onUniversityChanged
                    )
                }

                Spacer(Modifier.height(20.dp))

                /* --- Section 3: Identity (For Students) --- */
                if (uiState.role == UserRole.STUDENT) {
                    WhiteCard {
                        Label("PRN or Roll Number *")
                        FigmaTextField(
                            value = uiState.prnNumber,
                            hint = "Enter your ID number",
                            onChange = onPrnChanged
                        )
                    }
                }

                Spacer(Modifier.height(100.dp)) // Padding for button
            }
        }

        /* ================= SUBMIT BUTTON ================= */
        Surface(
            modifier = Modifier.align(Alignment.BottomCenter),
            color = Color.White,
            tonalElevation = 8.dp
        ) {
            Button(
                onClick = onSubmit,
                enabled = uiState.country.isNotEmpty() &&
                        uiState.state.isNotEmpty() &&
                        uiState.collegeName.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6D28FF),
                    disabledContainerColor = Color(0xFF6D28FF).copy(alpha = 0.5f)
                )
            ) {
                Text("Save Profile & Continue", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

/* ================= REUSABLE COMPONENTS ================= */

@Composable
fun WhiteCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), content = content)
    }
}

@Composable
fun Label(text: String) {
    Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4B5563))
    Spacer(Modifier.height(8.dp))
}

@Composable
fun FigmaTextField(value: String, hint: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text(hint, color = Color.Gray, fontSize = 14.sp) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE5E7EB),
            focusedBorderColor = Color(0xFF7C3AED)
        )
    )
}

@Composable
fun FigmaChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (selected) Color(0xFF4F46E5) else Color(0xFFF3F4F6),
        border = if (selected) null else BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color(0xFF374151),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownBox(
    value: String,
    expanded: Boolean,
    list: List<String>,
    onExpand: () -> Unit,
    onSelect: (String) -> Unit
) {
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { onExpand() }) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            placeholder = { Text("Select Option", fontSize = 14.sp) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedBorderColor = Color(0xFF7C3AED)
            )
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = onExpand) {
            list.forEach { item ->
                DropdownMenuItem(text = { Text(item) }, onClick = { onSelect(item) })
            }
        }
    }
}