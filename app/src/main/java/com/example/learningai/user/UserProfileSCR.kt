package com.example.learningai.user

import android.R.attr.title
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
@Preview(showSystemUi = true)
fun UserProfileSCR (){

    Column (modifier = Modifier.fillMaxSize().
    padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally){

        Box (modifier = Modifier.size(120.dp)
            .background(color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ),
            contentAlignment = Alignment.Center){
            Text(text = "DB", color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold)

        }
        Column(modifier = Modifier.fillMaxWidth()
            .padding(16.dp),Arrangement.SpaceAround) {
            Text(
                text = "Datta Bodkhe", fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            HorizontalDivider(
                modifier = Modifier.padding(top = 6.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Android Developer",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "learning kotlin and compose",
                modifier = Modifier.padding(horizontal = 12.dp), lineHeight = 17.sp
            )
            HorizontalDivider(
                modifier = Modifier.padding(top = 6.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )

            Spacer(modifier = Modifier.height(4.dp))
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Edit Profile")
            }
        }


    }

}

