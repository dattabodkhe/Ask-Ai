import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningai.MVVM.NotesViewModel
import io.noties.markwon.Markwon

@Composable
fun NotesSCR(
    subjectId: String,
    viewModel: NotesViewModel = viewModel()
) {

    val note by viewModel.note.collectAsState()

    LaunchedEffect(subjectId) {
        viewModel.loadNotes(subjectId)
    }

    if (note == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // 🔹 Title
        Text(
            text = note!!.title,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(16.dp))

        // 🔹 Markdown Content (CLEAR, NO BLUR)
        AndroidView(
            factory = { context ->
                TextView(context).apply {
                    val markwon = Markwon.create(context)
                    markwon.setMarkdown(this, note!!.content)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}