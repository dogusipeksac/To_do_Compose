package com.example.to_docompose.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.to_docompose.R
import com.example.to_docompose.components.PriorityDropDown
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.ui.theme.LARGE_PADDING
import com.example.to_docompose.ui.theme.MEDIUM_PADDING

@Composable
fun TaskContent(
    title:String,
    onTitleChange:(String)->Unit,
    description:String,
    onDescriptionChange:(String)->Unit,
    priority: Priority,
    onPrioritySelected:(Priority)->Unit

){
    //önce column yapıyoruz
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)
        .padding(all = LARGE_PADDING)

    ) {
        //1. column item
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = { onTitleChange(it) },
            label = { Text(text = stringResource(R.string.title))},
            textStyle = MaterialTheme.typography.body1,
            singleLine = true
        )
        //divider aralara boşluk için
        Divider(
            modifier = Modifier.height(MEDIUM_PADDING),
            color = MaterialTheme.colors.background
        )
        //2. column item drop down
        PriorityDropDown(
            priority = priority,
            onPrioritySelected =  onPrioritySelected
        )
        //divider aralara boşluk için
        Divider(
            modifier = Modifier.height(MEDIUM_PADDING),
            color = MaterialTheme.colors.background
        )
        //3. column item description
        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description,
            label = { Text(text = stringResource(R.string.description))},
            textStyle = MaterialTheme.typography.body1,
            onValueChange = {
                onDescriptionChange(it)
            }
        )

    }
}


@Composable
@Preview
fun TaskContentPreview(){
    TaskContent(
        title = "Deneme",
        onTitleChange = {},
        description ="desc" ,
        onDescriptionChange = {},
        priority = Priority.HIGH,
        onPrioritySelected = {}
    )
}